package edu.kit.ipd.parse.ontologySelector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.text.WordUtils;
import org.kohsuke.MetaInfServices;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.kit.ipd.parse.luna.agent.AbstractAgent;
import edu.kit.ipd.parse.luna.data.MissingDataException;
import edu.kit.ipd.parse.luna.data.PrePipelineData;
import edu.kit.ipd.parse.luna.graph.IGraph;
import edu.kit.ipd.parse.luna.graph.INode;
import edu.kit.ipd.parse.luna.graph.INodeType;
import edu.kit.ipd.parse.luna.graph.Pair;
import edu.kit.ipd.parse.luna.tools.ConfigManager;
import edu.kit.ipd.parse.ontologySelector.extractors.WikiConceptExtractor;
import edu.kit.ipd.parse.ontologySelector.merger.OntologyMerger;
import edu.kit.ipd.parse.ontologySelector.merger.SimpleOntologyMerger;
import edu.kit.ipd.parse.topic_extraction_common.Topic;
import edu.kit.ipd.parse.topic_extraction_common.TopicExtractionCommon;
import edu.kit.ipd.parse.topic_extraction_common.TopicSelectionMethod;
import edu.kit.ipd.parse.topic_extraction_common.graph.TopicGraph;

/**
 * @author Jan Keim
 *
 */
@MetaInfServices(AbstractAgent.class)
public class OntologySelector extends AbstractAgent {
	private static final Logger	logger				= LoggerFactory.getLogger(OntologySelector.class);
	private static final String	ID					= "OntologySelector";
	public static final String	ONTOLOGY_ATTRIBUTE	= "ontology";
	public static final String	ONTOLOGY_NODE_TYPE	= "ontology";

	private TopicExtractionCommon		topicExtraction;
	private List<TopicOntology>	actorOntologies;
	private List<TopicOntology>	environmentOntologies;
	private SelectionMethod		selectionMethod			= SelectionMethod.BEST_AND_SIMILAR;
	private boolean				useSavedTopicOntologies	= false;

	private double	threshold		= 0.1;
	private int		amountOfTopics	= 5;	// TODO

	private String outputFolder;

	@Override
	public void init() {
		setId(ID);
		topicExtraction = new TopicExtractionCommon();
		topicExtraction.init();
		// save old method to be able to restore later
		TopicSelectionMethod oldSM = topicExtraction.getTopicSelectionMethod();
		// change to maxConnectivity method
		topicExtraction.setTopicSelectionMethod(TopicSelectionMethod.MaxConnectivity);

		actorOntologies = new ArrayList<>();
		environmentOntologies = new ArrayList<>();

		// load ontology-paths from configuration file
		Properties props = ConfigManager.getConfiguration(this.getClass());
		loadOntologiesFromConfig(props);
		outputFolder = props.getProperty("OUTPUT", "/");

		// restore topic selection method
		topicExtraction.setTopicSelectionMethod(oldSM);
	}

	/**
	 * Sets if saved topic ontologies should be used. Needs to be called BEFORE {@link #init()}!
	 *
	 * @param val
	 *            true of saved ontologies should be used
	 */
	public void useSavedTopicOntologies(boolean val) {
		useSavedTopicOntologies = val;
	}

	/**
	 * Sets the {@link TopicExtractionCommon} instance that should be used for getting the topics for an ontology. Needs to be
	 * called BEFORE {@link #init()}!
	 *
	 * @param te
	 *            TopicExtraction instance that should be used
	 */
	public void setTopicExtraction(TopicExtractionCommon te) {
		topicExtraction = te;
	}

	/**
	 * Sets the threshold (ratio) for selecting ontologies. Default is 0.1
	 *
	 * @param threshold
	 *            threshold for selecting ontologies
	 */
	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	/**
	 * Sets the threshold factor for selecting ontologies. Default is (1-threshold)=0.9
	 *
	 * {@link Deprecated} because it is basically the same as {@link #setThreshold(double)}, only inverted
	 *
	 * @param thresholdFactor
	 *            thresholdFactor for setting the threshold for selecting ontologies
	 */
	@Deprecated
	public void setThresholdFactor(double thresholdFactor) {
		threshold = 1 - thresholdFactor;
	}

	private void loadOntologiesFromConfig(Properties props) {

		boolean isActor = false;
		ExecutorService executor = Executors.newWorkStealingPool();
		do {
			String ontologiesPaths;
			if (isActor) {
				ontologiesPaths = props.getProperty("ACTOR_ONTOLOGIES", "");
			} else {
				ontologiesPaths = props.getProperty("ENVIRONMENT_ONTOLOGIES", "");
			}

			if (!ontologiesPaths.isEmpty()) {
				for (String ontologyPath : ontologiesPaths.split(",")) {
					String path = ontologyPath;
					// check if the path is correct the way it is provided by checking existence
					if (!new File(path).exists()) {
						// not existing, then try with getting as resource
						URL pathURL = OntologySelector.class.getResource(path);
						if (pathURL != null) {
							path = pathURL.getFile();
							if (!new File(path).exists()) {
								// still not found, don't load the ontology in
								logger.warn("Could not load ontology and will skip it: " + ontologyPath);
								continue;
							}
						} else {
							logger.warn("Could not load ontology and will skip it: " + ontologyPath);
							continue;
						}
					}
					final String finalPath = path;
					if (isActor) {
						logger.info("Starting to add actor ontology at " + finalPath);
						executor.execute(() -> registerActorOntology(finalPath));
					} else {
						logger.info("Starting to add environment ontology at " + finalPath);
						executor.execute(() -> registerEnvironmentOntology(finalPath));
					}
				}
			}
			isActor = !isActor;
		} while (isActor);

		executor.shutdown();
		try {
			executor.awaitTermination(1, TimeUnit.HOURS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param selectionMethod
	 *            the selectionMethod to set
	 */
	public void setSelectionMethod(SelectionMethod selectionMethod) {
		this.selectionMethod = selectionMethod;
	}

	public boolean registerActorOntology(String path) {
		return registerOntology(path, true);
	}

	public boolean registerEnvironmentOntology(String path) {
		return registerOntology(path, false);
	}

	private boolean registerOntology(String path, boolean isActor) {
		if ((path == null) || path.isEmpty()) {
			return false;
		}
		if (!new File(path).exists()) {
			return false;
		}
		TopicOntology topicOntology = null;
		String savePath = path + ".ossf";
		if (useSavedTopicOntologies && new File(savePath).isFile()) {
			// TODO what happens at ontology change?
			Optional<TopicOntology> optTO = TopicOntology.loadFromPath(savePath);
			if (optTO.isPresent()) {
				topicOntology = optTO.get();
			}
		}

		if (topicOntology == null) {
			// load ontology
			Optional<OWLOntology> optOnto = loadOntology(path);
			if (!optOnto.isPresent()) {
				logger.warn("Problem when trying to load Ontology at " + path);
				return false;
			}
			OWLOntology onto = optOnto.get();

			// get wiki-concepts out of ontology
			List<String> concepts = getConceptsFromOntology(onto);

			// do topic extraction
			TopicGraph tg;
			List<Topic> ontologyTopics;
			synchronized (topicExtraction) {
				if (logger.isDebugEnabled()) {
					LocalDateTime currentTime = LocalDateTime.now();
					logger.debug(currentTime.toLocalTime().toString() + "\tGetting Topics for " + path);
				}
				tg = topicExtraction.getTopicGraphForSenses(concepts);
				ontologyTopics = topicExtraction.getTopicsForTopicGraph(tg, amountOfTopics);
			}
			topicOntology = new TopicOntology(onto, path, ontologyTopics, tg);
			if (useSavedTopicOntologies) {
				topicOntology.saveToPath(savePath);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug(topicOntology.toDetailedString());
		}

		if (isActor) {
			return actorOntologies.add(topicOntology);
		} else {
			return environmentOntologies.add(topicOntology);
		}
	}

	private Optional<OWLOntology> loadOntology(String path) {
		OWLOntology onto = null;
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

		try {
			onto = manager.loadOntologyFromOntologyDocument(new FileInputStream(path));
		} catch (OWLOntologyCreationException | FileNotFoundException e) {
			e.printStackTrace();
		}
		return Optional.ofNullable(onto);
	}

	private void prepareGraph() {
		// add graph attribute
		INodeType tokenType;
		if (graph.hasNodeType(ONTOLOGY_NODE_TYPE)) {
			tokenType = graph.getNodeType(ONTOLOGY_NODE_TYPE);
		} else {
			tokenType = graph.createNodeType(ONTOLOGY_NODE_TYPE);
		}
		if (!tokenType.containsAttribute(ONTOLOGY_ATTRIBUTE, "org.semanticweb.owlapi.model.OWLOntology")) {
			tokenType.addAttributeToType("org.semanticweb.owlapi.model.OWLOntology", ONTOLOGY_ATTRIBUTE);
		}
	}

	@Override
	protected void exec() {
		prepareGraph();
		// get topics out of graph
		List<Topic> topics = TopicExtractionCommon.getTopicsFromIGraph(graph);
		if (topics.isEmpty()) {
			logger.warn("No annotated topics found. Aborting...");
			return;
		}
		Pair<OWLOntology, String> mergedOntologyAndPath = exec(topics);
		if (logger.isInfoEnabled()) {
			logger.info("Merged ontology saved to " + mergedOntologyAndPath.getRight());
		}
		// annotate Onto to graph
		annotateOntologyToGraph(mergedOntologyAndPath.getLeft());
	}

	/**
	 * Returns the {@link OWLOntology} that was annotated to the graph. If no Ontology was annotated, it returns an
	 * empty {@link Optional}.
	 *
	 * @param inputGraph
	 *            Graph the Ontology shall be extracted from
	 * @return {@link Optional} with an {@link OWLOntology} if one was annotated or an empty {@link Optional} otherwise
	 */
	public static Optional<OWLOntology> getOntologyFromIGraph(IGraph inputGraph) {
		Optional<OWLOntology> retVal = Optional.empty();
		if (!inputGraph.hasNodeType(ONTOLOGY_NODE_TYPE)) {
			return retVal;
		}
		List<INode> nodesList = inputGraph.getNodesOfType(inputGraph.getNodeType(ONTOLOGY_NODE_TYPE));
		if ((nodesList == null) || nodesList.isEmpty()) {
			return retVal;
		}
		INode node = nodesList.get(0);
		if (node == null) {
			return retVal;
		}
		Object o = node.getAttributeValue(ONTOLOGY_ATTRIBUTE);
		if (o == null) {
			return retVal;
		}
		if (o instanceof OWLOntology) {
			return Optional.of((OWLOntology) o);
		} else {
			return retVal;
		}
	}

	private void annotateOntologyToGraph(OWLOntology ontology) {
		INode node = graph.createNode(graph.getNodeType(ONTOLOGY_NODE_TYPE));
		node.setAttributeValue(ONTOLOGY_ATTRIBUTE, ontology);
	}

	protected Pair<OWLOntology, String> exec(List<Topic> topics) {
		// compare topics in command with topics from ontologies
		Map<TopicOntology, Double> actorAgreements = calculateTopicOntologyConformities(topics, actorOntologies);
		Map<TopicOntology, Double> envAgreements = calculateTopicOntologyConformities(topics, environmentOntologies);

		// select ontology/ontologies
		List<TopicOntology> ontologies = selectOntologies(selectionMethod, actorAgreements, envAgreements);

		// maybe save the selected (and merged) ontology
		OWLOntologyManager owlManager = OWLManager.createOWLOntologyManager();
		OWLOntology merged = mergeOntologies(owlManager, ontologies);
		String mergeId = getMergedOntoIdentificator(ontologies);
		String filename = outputFolder + "merged_" + mergeId + ".owl";
		File file = new File(filename);
		saveOntologyToFile(owlManager, merged, file);

		Pair<OWLOntology, String> retPair = new Pair<>(merged, filename);
		return retPair;
	}

	private List<TopicOntology> selectOntologies(SelectionMethod selectionMethod,
			Map<TopicOntology, Double> actorAgreements, Map<TopicOntology, Double> envAgreements) {

		List<TopicOntology> ontologies = new ArrayList<>();
		// select and add environment ontologies
		switch (selectionMethod) {
			case BEST:
				ontologies.add(selectBestOntology(envAgreements));
				break;
			case THRESHOLD:
				ontologies.addAll(selectOntologiesByThreshold(envAgreements, threshold));
				break;
			case BEST_AND_SIMILAR:
				ontologies.addAll(selectBestAndSimilarOntologies(envAgreements, 1 - threshold));
				break;
			default:
				logger.warn("Invalid selection method used!");
				break;
		}
		// add actor ontologies (only the best actor will be selected and therefore added)
		ontologies.addAll(selectActorOntologies(actorAgreements, ontologies));

		// print info about selection
		if (logger.isDebugEnabled()) {
			for (java.util.Map.Entry<TopicOntology, Double> entry : envAgreements.entrySet()) {
				logger.debug("Env: " + entry.getKey().getDescription() + " with " + entry.getValue());
			}
			for (java.util.Map.Entry<TopicOntology, Double> entry : actorAgreements.entrySet()) {
				logger.debug("Act: " + entry.getKey().getDescription() + " with " + entry.getValue());
			}
		}
		if (logger.isInfoEnabled()) {
			StringBuilder strBuilder = new StringBuilder("Selected the following ontologies:");
			for (TopicOntology to : ontologies) {
				strBuilder.append("\n\t");
				strBuilder.append(to.toString());
			}
			logger.info(strBuilder.toString());
		}
		return ontologies;
	}

	public void exec(PrePipelineData ppd) {
		try {
			graph = ppd.getGraph();
		} catch (MissingDataException e) {
			e.printStackTrace();
		}
		this.exec();
	}

	private String getMergedOntoIdentificator(List<TopicOntology> ontologies) {
		List<String> names = new ArrayList<>();
		for (TopicOntology to : ontologies) {
			String desc = to.getDescription();
			desc = desc.replaceAll("http://www.semanticweb.org/", "")
					.replaceAll("environment_", "")
					.replaceAll("_", "")
					.replaceAll("actor_", "");
			names.add(WordUtils.capitalize(desc));
		}
		return names.stream().sorted().collect(Collectors.joining());
	}

	private OWLOntology mergeOntologies(OWLOntologyManager owlManager, List<TopicOntology> ontologies) {
		OntologyMerger merger = new SimpleOntologyMerger();
		OWLOntology merged = merger.merge(owlManager, ontologies,
				IRI.create("http://www.semanticweb.com/mergedOntology_" + getMergedOntoIdentificator(ontologies)));
		return merged;
	}

	private void saveOntologyToFile(OWLOntologyManager owlManager, OWLOntology onto, File file) {
		if ((onto == null) || (onto == null) || (file == null)) {
			throw new IllegalArgumentException("A provided Argument is null!");
		}

		try {
			owlManager.saveOntology(onto, new FileOutputStream(file));
		} catch (OWLOntologyStorageException | FileNotFoundException e) {
			e.printStackTrace();
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Saving merged ontology to " + file.getAbsolutePath() + "\n");
		}
	}

	protected static Map<TopicOntology, Double> calculateTopicOntologyConformities(List<Topic> topics,
			List<TopicOntology> ontologies) {
		Map<TopicOntology, Double> retMap = new HashMap<>();
		for (TopicOntology topicOnto : ontologies) {
			double conformity = topicOnto.calculateTopicConformity(topics);
			retMap.put(topicOnto, conformity);
		}
		return retMap;
	}

	private List<TopicOntology> selectActorOntologies(Map<TopicOntology, Double> conformities,
			List<TopicOntology> ontologies) {
		Set<String> objectClasses = getObjectClasses(ontologies);
		for (Map.Entry<TopicOntology, Double> entry : conformities.entrySet()) {
			double counter = 0;
			List<String> dataTypes = entry.getKey().getDataTypes();
			for (String obj : objectClasses) {
				if (dataTypes.contains(obj)) {
					counter++;
				}
			}
			// new conformity is (2*previous + percentage of object classes that can be used) / 2
			double newConformity = ((entry.getValue() * 2) + (counter / objectClasses.size())) / 3;
			entry.setValue(newConformity);
		}

		List<TopicOntology> retList = new ArrayList<>();
		retList.add(selectBestOntology(conformities));
		return retList;
	}

	private Set<String> getObjectClasses(List<TopicOntology> ontologies) {
		Set<String> retList = new HashSet<>();

		for (TopicOntology to : ontologies) {
			retList.addAll(to.getObjectClasses());
		}
		return retList;
	}

	private TopicOntology selectBestOntology(Map<TopicOntology, Double> conformities) {
		TopicOntology best = null;
		double bestScore = -1.0;

		for (Entry<TopicOntology, Double> entry : conformities.entrySet()) {
			if (entry.getValue() > bestScore) {
				best = entry.getKey();
				bestScore = entry.getValue();
			}
		}

		return best;
	}

	private List<TopicOntology> selectOntologiesByThreshold(Map<TopicOntology, Double> conformities, double threshold) {
		List<TopicOntology> ontologies = new ArrayList<>();

		for (Entry<TopicOntology, Double> entry : conformities.entrySet()) {
			if (entry.getValue() >= threshold) {
				ontologies.add(entry.getKey());
			}
		}
		return ontologies;
	}

	private List<TopicOntology> selectBestAndSimilarOntologies(Map<TopicOntology, Double> conformities,
			double thresholdFactor) {

		// get best ontology score
		double bestScore = -1.0;
		bestScore = conformities.values().stream().max((d1, d2) -> Double.compare(d1, d2)).orElse(-1.0);
		if (bestScore < 0.0) {
			return new ArrayList<>();
		}
		double threshold = bestScore * (thresholdFactor);
		return selectOntologiesByThreshold(conformities, threshold);
	}

	private List<String> getConceptsFromOntology(OWLOntology onto) {
		return WikiConceptExtractor.getConceptsFromOntology(onto);
	}

}
