package edu.kit.ipd.parse.ontology_selector;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import edu.kit.ipd.parse.ontology_selector.OntologySelector;
import edu.kit.ipd.parse.topic_extraction_common.Topic;
import edu.kit.ipd.parse.topic_extraction_common.TopicExtractionCore;

/**
 * @author Jan Keim
 *
 */
public class SimpleSingleOntologySelectorTest {
	// private static final Logger logger = LoggerFactory.getLogger(SimpleSingleOntologySelectorTest.class);

	private static OntologySelector ontoSelector;
	private static TopicExtractionCore topicExtraction;

	@BeforeClass
	public static void beforeClass() {
		topicExtraction = new TopicExtractionCore();

		ontoSelector = new OntologySelector();
		ontoSelector.init();
	}

	@Test
	public void test() {
		final List<String> wordSenses = new ArrayList<>();
		// add disambiguated meanings of words
		wordSenses.add("radiator");

		// start selection
		final List<Topic> topics = topicExtraction.getTopicsForSenses(wordSenses);
		ontoSelector.exec(topics);
	}

}
