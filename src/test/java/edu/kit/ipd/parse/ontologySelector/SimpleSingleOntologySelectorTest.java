package edu.kit.ipd.parse.ontologySelector;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import edu.kit.ipd.parse.ontologySelector.OntologySelector;
import edu.kit.ipd.parse.topicExtraction.Topic;
import edu.kit.ipd.parse.topicExtraction.TopicExtraction;

/**
 * @author Jan Keim
 *
 */
public class SimpleSingleOntologySelectorTest {
	// private static final Logger logger = LoggerFactory.getLogger(SimpleSingleOntologySelectorTest.class);

	private static OntologySelector	ontoSelector;
	private static TopicExtraction	topicExtraction;

	@BeforeClass
	public static void beforeClass() {
		topicExtraction = new TopicExtraction();
		topicExtraction.init();

		ontoSelector = new OntologySelector();
		ontoSelector.init();
	}

	@Test
	public void test() {
		List<String> wordSenses = new ArrayList<>();
		// add disambiguated meanings of words
		wordSenses.add("radiator");

		// start selection
		List<Topic> topics = topicExtraction.getTopicsForSenses(wordSenses);
		ontoSelector.exec(topics);
	}

}
