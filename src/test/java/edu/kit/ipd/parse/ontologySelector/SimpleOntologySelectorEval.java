package edu.kit.ipd.parse.ontologySelector;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.kit.ipd.parse.ontologySelector.OntologySelector;
import edu.kit.ipd.parse.topic_extraction_common.Topic;
import edu.kit.ipd.parse.topic_extraction_common.TopicExtractionCommon;

/**
 * @author Jan Keim
 *
 */
public class SimpleOntologySelectorEval {
	private static final Logger logger = LoggerFactory.getLogger(SimpleOntologySelectorEval.class);

	private static OntologySelector	ontoSelector;
	private static TopicExtractionCommon	topicExtraction;

	@BeforeClass
	public static void beforeClass() {
		topicExtraction = new TopicExtractionCommon();
		topicExtraction.init();
		topicExtraction.setNumTopics(5);

		ontoSelector = new OntologySelector();
		ontoSelector.useSavedTopicOntologies(true);
		ontoSelector.setTopicExtraction(topicExtraction);
		ontoSelector.init();
		// ontoSelector.setThreshold(0.15);
	}

	private void test(List<String> wordSenses, String id) {
		logger.info("Starting test " + id);
		List<Topic> topics = topicExtraction.getTopicsForSenses(wordSenses);
		ontoSelector.exec(topics);
	}

	// 1
	@Test
	public void testOneOne() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("table (furniture)");
		wordSenses.add("popcorn");
		wordSenses.add("popcorn");
		wordSenses.add("hand");
		test(wordSenses, "1.1");
	}

	@Test
	public void testTwoOne() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("popcorn");
		wordSenses.add("bag");
		test(wordSenses, "2.1");
	}

	// 2
	@Test
	public void testEighteenTwo() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("table (furniture)");
		wordSenses.add("cup");
		wordSenses.add("dishwasher");
		test(wordSenses, "18.2");
	}

	@Test
	public void testThirtyOneTwo() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("cup");
		wordSenses.add("kitchen");
		wordSenses.add("table (furniture)");
		wordSenses.add("dishwasher");
		test(wordSenses, "31.2");
	}

	// 3
	@Test
	public void testThreeThree() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("refrigerator");
		wordSenses.add("orange (fruit)");
		wordSenses.add("juice");
		test(wordSenses, "3.3");
	}

	@Test
	public void testTwentyfiveThree() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("refrigerator");
		wordSenses.add("orange (fruit)");
		wordSenses.add("juice");
		wordSenses.add("refrigerator");
		wordSenses.add("orange (fruit)");
		wordSenses.add("juice");
		test(wordSenses, "25.3");
	}

	// 4
	@Test
	public void testIfFourOne() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("dishware");
		wordSenses.add("dishwasher");
		wordSenses.add("cupboard");
		test(wordSenses, "If.4.1");
	}

	@Test
	public void testIfFourTen() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("table (furniture)");
		wordSenses.add("dishware");
		wordSenses.add("dishware");
		wordSenses.add("dishwasher");
		wordSenses.add("dishwasher");
		wordSenses.add("dishware");
		wordSenses.add("dishwasher");
		wordSenses.add("dishwasher");
		wordSenses.add("table (furniture)");
		wordSenses.add("dishware");
		wordSenses.add("dishware");
		wordSenses.add("cupboard");
		wordSenses.add("cupboard");
		wordSenses.add("dishware");
		wordSenses.add("cupboard");
		test(wordSenses, "If.4.10");
	}

	// 5
	@Test
	public void testIfFiveFive() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("drink");
		wordSenses.add("refrigerator");
		wordSenses.add("orange (fruit)");
		wordSenses.add("orange (fruit)");
		wordSenses.add("vodka");
		wordSenses.add("orange (fruit)");
		wordSenses.add("juice");
		wordSenses.add("vodka");
		test(wordSenses, "If.5.5");
	}

	@Test
	public void testIfFiveTwelve() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("vodka");
		wordSenses.add("orange (fruit)");
		wordSenses.add("fridge");
		wordSenses.add("orange (fruit)");
		wordSenses.add("vodka");
		wordSenses.add("orange (fruit)");
		wordSenses.add("juice");
		test(wordSenses, "If.5.12");
	}

	// 6
	@Test
	public void testSSixPThree() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("table (furniture)");
		wordSenses.add("cup");
		wordSenses.add("table (furniture)");
		wordSenses.add("refrigerator");
		wordSenses.add("refrigerator");
		wordSenses.add("water");
		wordSenses.add("bottle");
		wordSenses.add("bottle");
		wordSenses.add("water");
		wordSenses.add("cup");
		wordSenses.add("bottle");
		wordSenses.add("fridge");
		wordSenses.add("fridge");
		wordSenses.add("cup");
		wordSenses.add("dishwasher");
		wordSenses.add("dishwasher");
		wordSenses.add("cup");
		wordSenses.add("cupboard");
		wordSenses.add("cupboard");
		wordSenses.add("cup");
		wordSenses.add("shelf (storage)");
		wordSenses.add("cupboard");
		test(wordSenses, "s6p03");
	}

	@Test
	public void testSSixPTen() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("fridge");
		wordSenses.add("refrigerator");
		wordSenses.add("door");
		wordSenses.add("water");
		wordSenses.add("bottle");
		wordSenses.add("refrigerator");
		wordSenses.add("door");
		wordSenses.add("table (furniture)");
		wordSenses.add("water");
		wordSenses.add("bottle");
		wordSenses.add("cup");
		wordSenses.add("water");
		wordSenses.add("cup");
		wordSenses.add("dishwasher");
		wordSenses.add("cupboard");
		test(wordSenses, "s6p10");
	}

	// 7
	@Test
	public void testSSevenPEight() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("plate (dishware)");
		wordSenses.add("dishwasher");
		wordSenses.add("water");
		wordSenses.add("sink");
		wordSenses.add("fridge");
		wordSenses.add("fridge");
		wordSenses.add("food");
		wordSenses.add("plate (dishware)");
		wordSenses.add("fridge");
		wordSenses.add("plate (dishware)");
		wordSenses.add("microwave");
		wordSenses.add("door");
		wordSenses.add("table (furniture)");
		test(wordSenses, "s7p08");
	}

	@Test
	public void testSSevenPTen() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("dishwasher");
		wordSenses.add("plate (dishware)");
		wordSenses.add("plate (dishware)");
		wordSenses.add("fridge");
		wordSenses.add("meal");
		wordSenses.add("plate (dishware)");
		wordSenses.add("microwave");
		wordSenses.add("microwave");
		wordSenses.add("meal");
		wordSenses.add("plate (dishware)");
		wordSenses.add("plate (dishware)");
		wordSenses.add("table (furniture)");
		test(wordSenses, "s7p10");
	}

	// 8
	@Test
	public void testSEightPOne() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("machine");
		wordSenses.add("laundry");
		wordSenses.add("laundry");
		wordSenses.add("clothes dryer");
		wordSenses.add("clothes dryer");
		test(wordSenses, "s8p01");
	}

	@Test
	public void testSEightPSix() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("clothes dryer");
		wordSenses.add("clothes dryer");
		wordSenses.add("machine");
		wordSenses.add("machine");
		wordSenses.add("window");
		wordSenses.add("machine");
		wordSenses.add("arm");
		wordSenses.add("machine");
		wordSenses.add("laundry");
		wordSenses.add("laundry");
		wordSenses.add("machine");
		wordSenses.add("clothes dryer");
		wordSenses.add("clothes dryer");
		wordSenses.add("push-button");
		wordSenses.add("clothes dryer");
		test(wordSenses, "s8p06");
	}

	// 9
	@Test
	public void testDroneOneOne() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("gate");
		wordSenses.add("degree (angle)");
		wordSenses.add("table (furniture)");
		wordSenses.add("greenhouse");
		wordSenses.add("pond");
		wordSenses.add("pond");
		wordSenses.add("lawn");
		test(wordSenses, "drone1.1");
	}

	@Test
	public void testDroneOneTwo() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("gate");
		wordSenses.add("greenhouse");
		wordSenses.add("table (furniture)");
		wordSenses.add("greenhouse");
		wordSenses.add("pond");
		wordSenses.add("lawn");
		test(wordSenses, "drone1.2");
	}

	// 10
	@Test
	public void testMindstormOneOne() {
		// "Follow the line on the carpet. At the end of the carpet turn until you see the doll. Grab the doll and bring
		// it to me."
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("line (geometry)");
		wordSenses.add("carpet");
		wordSenses.add("carpet");
		wordSenses.add("doll");
		wordSenses.add("doll");
		test(wordSenses, "mindstorm1.1");
	}

	@Test
	public void testMindstormOneTwo() {
		// "Move along the line until you are at the end of the carpet. Turn right until you see the doll, grab it, come
		// to me and then release it again."
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("line (geometry)");
		wordSenses.add("carpet");
		wordSenses.add("doll");
		test(wordSenses, "mindstorm1.2");
	}

	// 11
	@Test
	public void testAlexaOneOne() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("temperature");
		wordSenses.add("radiator");
		wordSenses.add("degree (temperature)");
		wordSenses.add("playlist");
		test(wordSenses, "alexa1.1");
	}

	@Test
	public void testAlexaOneTwo() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("playlist");
		wordSenses.add("radiator");
		test(wordSenses, "alexa1.2");
	}

	// extra
	@Test
	public void testGardenOneOne() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("lawn");
		wordSenses.add("mower");
		wordSenses.add("grass");
		test(wordSenses, "Garden1.1");
	}

	@Test
	public void testGardenOneTwo() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("shed");
		wordSenses.add("mower");
		wordSenses.add("lawn");
		test(wordSenses, "Garden1.2");
	}

	@Test
	public void testBarOneOne() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("refrigerator");
		wordSenses.add("tonic water");
		wordSenses.add("glass (drinkware)");
		wordSenses.add("gin");
		test(wordSenses, "Bar1.1");
	}

	@Test
	public void testBarOneTwo() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("cuba");
		wordSenses.add("cola");
		wordSenses.add("rum");
		wordSenses.add("lime (fruit)");
		wordSenses.add("juice");
		wordSenses.add("glass (drinkware)");
		test(wordSenses, "Bar1.2");
	}

	@Test
	public void testBedroomOneOne() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("closet");
		wordSenses.add("sweater");
		wordSenses.add("trousers");
		test(wordSenses, "Bedroom1.1");
	}

	@Test
	public void testBedroomOneTwo() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("desk");
		wordSenses.add("nightstand");
		wordSenses.add("task (project management)");
		wordSenses.add("book");
		wordSenses.add("shelf (storage)");
		test(wordSenses, "Bedroom1.2");
	}

	@Test
	public void testMusicOneOne() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("metal (music)");
		wordSenses.add("playlist");
		test(wordSenses, "Music1.1");
	}

	@Test
	public void testMusicOneTwo() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("songwriter");
		wordSenses.add("piano");
		test(wordSenses, "Music1.3");
	}

	@Test
	public void testJazzBarOneOne() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("artist");
		wordSenses.add("trumpet");
		wordSenses.add("loudness");
		wordSenses.add("refrigerator");
		wordSenses.add("beer");
		wordSenses.add("whiskey");
		test(wordSenses, "JazzBar1.1");
	}

	@Test
	public void testJazzBarOneTwo() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("jazz");
		wordSenses.add("glass (drinkware)");
		wordSenses.add("whisky");
		wordSenses.add("drink");
		test(wordSenses, "JazzBar1.2");
	}

	@Test
	public void testKitchenToGardenOneOne() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("fridge");
		wordSenses.add("ketchup");
		wordSenses.add("drinking water");
		wordSenses.add("patio table");
		wordSenses.add("pond");
		wordSenses.add("lawn");
		test(wordSenses, "KitchenToGarden1.1");
	}

	@Ignore
	@Test
	public void testGBMOneOne() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("bench (furniture)");
		wordSenses.add("pond");
		wordSenses.add("radio broadcasting");
		wordSenses.add("nightstand");
		wordSenses.add("book");
		wordSenses.add("bench (furniture)");
		test(wordSenses, "GBMTest1.1");
	}
}
