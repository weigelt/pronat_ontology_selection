package edu.kit.ipd.parse.ontology_selector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;

import edu.kit.ipd.parse.topic_extraction_common.Topic;
import edu.kit.ipd.parse.topic_extraction_common.graph.TopicGraph;
import edu.kit.ipd.parse.topic_extraction_common.graph.WikiVertex;

/**
 * @author Jan Keim
 *
 */
public class TopicOntology implements Serializable {
	// private static final Logger logger = LoggerFactory.getLogger(TopicOntology.class);

	private static final long serialVersionUID = -2315954009336287861L;

	private String			ontologyPath;
	private OWLOntology		ontology;
	private OWLReasoner		reasoner;
	private OWLDataFactory	factory;

	private List<Topic>	topics;
	private TopicGraph	topicGraph;

	public TopicOntology(OWLOntology ontology, String path, List<Topic> topics, TopicGraph topicGraph) {
		this.ontology = ontology;
		this.topics = topics;
		this.topicGraph = topicGraph;
		ontologyPath = path;
	}

	/**
	 * @return the ontology
	 */
	public OWLOntology getOntology() {
		return ontology;
	}

	/**
	 * @return the path
	 */
	public String getOntologyPath() {
		return ontologyPath;
	}

	/**
	 * @return a description about this ontology consisting of the ID
	 */
	public String getDescription() {
		Optional<IRI> iri = ontology.getOntologyID().getOntologyIRI();
		if (iri.isPresent()) {
			return iri.get().toString();
		} else {
			return ontology.getOntologyID().toString();
		}
	}

	@Override
	public String toString() {
		return getDescription();
	}

	public String toDetailedString() {
		StringBuilder sb = new StringBuilder(getDescription());
		sb.append("\n\tTopics: ");
		sb.append(topics.stream().map(topic -> topic.toString()).collect(Collectors.joining(", ")));
		return sb.toString();
	}

	public double calculateTopicConformity(List<Topic> topicList) {
		double sum = 0.0;
		for (Topic topic : topicList) {
			sum += calculateSimilarityOf(topic);
		}
		double conformityProbability = sum / (topicList.size());
		return conformityProbability;
	}

	private double calculateSimilarityOf(Topic topic) {
		return calculateDistanceScore(topic);
	}

	private double calculateDistanceScore(Topic topic) {
		Optional<WikiVertex> optV = topicGraph.getVertex(topic.getLabel());
		if (optV.isPresent()) {
			WikiVertex topicVertex = optV.get();
			double minDist = Integer.MAX_VALUE;
			for (Topic ontoTopic : topics) {
				WikiVertex ov = topicGraph.getVertex(ontoTopic.getLabel()).get();
				double dist = topicGraph.shortestPathLength(topicVertex, ov);
				minDist = Math.min(minDist, dist);
			}
			double avgWeight = topicGraph.getAvgVertexWeight(topicVertex);
			return Math.min(avgWeight / (minDist + 1d + avgWeight), 1d); // TODO check with avgWeight
		} else {
			return 0.0;
		}

	}

	private OWLReasoner getReasoner() {
		if (reasoner == null) {
			OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
			OWLReasoner new_reasoner = reasonerFactory.createReasoner(ontology);
			new_reasoner.precomputeInferences();
			reasoner = new_reasoner;
		}

		return reasoner;
	}

	public List<String> getObjectClasses() {
		List<String> retList = new ArrayList<>();
		Optional<OWLClass> clazzO = ontology.classesInSignature()
				.filter(c -> c.getIRI().getShortForm().equalsIgnoreCase("Object"))
				.findAny();
		if (!clazzO.isPresent()) {
			return retList;
		}
		OWLClass clazz = clazzO.get();
		retList.add(clazz.getIRI().getShortForm());
		NodeSet<OWLClass> nodeset = getReasoner().getSubClasses(clazz, false);
		nodeset.forEach(node ->
			{
				String name = node.getRepresentativeElement().getIRI().getShortForm();
				retList.add(name);
			});
		return retList;
	}

	public List<String> getDataTypes() {
		List<String> retList = new ArrayList<>();
		Optional<OWLClass> clazzO = ontology.classesInSignature()
				.filter(c -> c.getIRI().getShortForm().equalsIgnoreCase("DataType"))
				.findAny();
		if (!clazzO.isPresent()) {
			return retList;
		}
		OWLClass clazz = clazzO.get();

		retList = ontology.classAssertionAxioms(clazz)
				.flatMap(a -> a.individualsInSignature())
				.map(c -> c.getIRI().getShortForm())
				.collect(Collectors.toList());

		return retList;
	}

	@SuppressWarnings("unused")
	private OWLDataFactory getDataFactory() {
		if (factory == null) {
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			try {
				manager.loadOntologyFromOntologyDocument(new File(ontologyPath));
			} catch (OWLOntologyCreationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			factory = manager.getOWLDataFactory();
		}

		return factory;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((topics == null) ? 0 : topics.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof TopicOntology)) {
			return false;
		}
		TopicOntology other = (TopicOntology) obj;
		if (topics == null) {
			if (other.topics != null) {
				return false;
			}
		}
		for (Topic topic : topics) {
			if (!other.topics.contains(topic)) {
				return false;
			}
		}
		return true;
	}

	public void saveToPath(String path) {
		saveTopicOntologyToPath(this, path);
	}

	public static Optional<TopicOntology> loadFromPath(String path) {
		TopicOntology to = null;
		try {
			FileInputStream fin = new FileInputStream(path);
			ObjectInputStream ois = new ObjectInputStream(fin);
			Object o = ois.readObject();
			ois.close();
			if (o instanceof TopicOntology) {
				to = (TopicOntology) o;
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			to = null;
		}
		return Optional.ofNullable(to);
	}

	public static void saveTopicOntologyToPath(TopicOntology to, String path) {
		try {
			FileOutputStream fout = new FileOutputStream(path);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(to);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
