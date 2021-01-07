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
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.kit.ipd.parse.topic_extraction_common.Topic;
import edu.kit.ipd.parse.topic_extraction_common.graph.TopicGraph;
import edu.kit.ipd.parse.topic_extraction_common.graph.WikiVertex;

/**
 * @author Jan Keim
 *
 */
public class TopicOntology implements Serializable {
	private static final Logger logger = LoggerFactory.getLogger(TopicOntology.class);

	private static final long serialVersionUID = -2315954009336287861L;

	private final String ontologyPath;
	private final OWLOntology ontology;
	private OWLReasoner reasoner;
	private OWLDataFactory factory;

	private final List<Topic> topics;
	private final TopicGraph topicGraph;

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
		final Optional<IRI> iri = ontology.getOntologyID().getOntologyIRI();
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
		final StringBuilder sb = new StringBuilder(getDescription());
		sb.append("\n\tTopics: ");
		sb.append(topics.stream().map(Topic::toString).collect(Collectors.joining(", ")));
		return sb.toString();
	}

	public double calculateTopicConformity(List<Topic> topicList) {
		double sum = 0.0;
		for (final Topic topic : topicList) {
			sum += calculateSimilarityOf(topic);
		}
		return sum / (topicList.size());
	}

	private double calculateSimilarityOf(Topic topic) {
		return calculateDistanceScore(topic);
	}

	private double calculateDistanceScore(Topic topic) {
		final Optional<WikiVertex> optV = topicGraph.getVertex(topic.getLabel());
		if (optV.isPresent()) {
			final WikiVertex topicVertex = optV.get();
			double minDist = Integer.MAX_VALUE;
			for (final Topic ontoTopic : topics) {
				final WikiVertex ov = topicGraph.getVertex(ontoTopic.getLabel()).get();
				final double dist = topicGraph.shortestPathLength(topicVertex, ov);
				minDist = Math.min(minDist, dist);
			}
			final double avgWeight = topicGraph.getAvgVertexWeight(topicVertex);
			return Math.min(avgWeight / (minDist + 1d + avgWeight), 1d);
		} else {
			return 0.0;
		}

	}

	private OWLReasoner getReasoner() {
		if (reasoner == null) {
			final OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
			final OWLReasoner newReasoner = reasonerFactory.createReasoner(ontology);
			newReasoner.precomputeInferences();
			reasoner = newReasoner;
		}

		return reasoner;
	}

	public List<String> getObjectClasses() {
		final List<String> retList = new ArrayList<>();
		final Optional<OWLClass> clazzO = ontology.classesInSignature().filter(c -> c.getIRI().getShortForm().equalsIgnoreCase("Object"))
				.findAny();
		if (!clazzO.isPresent()) {
			return retList;
		}
		final OWLClass clazz = clazzO.get();
		retList.add(clazz.getIRI().getShortForm());
		final NodeSet<OWLClass> nodeset = getReasoner().getSubClasses(clazz, false);
		nodeset.forEach(node -> {
			final String name = node.getRepresentativeElement().getIRI().getShortForm();
			retList.add(name);
		});
		return retList;
	}

	public List<String> getDataTypes() {
		List<String> retList = new ArrayList<>();
		final Optional<OWLClass> clazzO = ontology.classesInSignature().filter(c -> c.getIRI().getShortForm().equalsIgnoreCase("DataType"))
				.findAny();
		if (!clazzO.isPresent()) {
			return retList;
		}
		final OWLClass clazz = clazzO.get();

		retList = ontology.classAssertionAxioms(clazz).flatMap(OWLClassAssertionAxiom::individualsInSignature)
				.map(c -> c.getIRI().getShortForm()).collect(Collectors.toList());

		return retList;
	}

	private OWLDataFactory getDataFactory() {
		if (factory == null) {
			final OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			try {
				manager.loadOntologyFromOntologyDocument(new File(ontologyPath));
			} catch (final OWLOntologyCreationException e) {
				logger.warn(e.getMessage(), e.getCause());
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
		final TopicOntology other = (TopicOntology) obj;
		if (topics == null && other.topics != null) {
			return false;
		}
		for (final Topic topic : topics) {
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
		try (final FileInputStream fin = new FileInputStream(path); final ObjectInputStream ois = new ObjectInputStream(fin)) {
			final Object o = ois.readObject();
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
		try (final FileOutputStream fout = new FileOutputStream(path); final ObjectOutputStream oos = new ObjectOutputStream(fout)) {
			oos.writeObject(to);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

}
