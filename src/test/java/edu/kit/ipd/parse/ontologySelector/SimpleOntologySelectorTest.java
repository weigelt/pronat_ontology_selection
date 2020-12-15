package edu.kit.ipd.parse.ontologySelector;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
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
public class SimpleOntologySelectorTest {
	private static final Logger logger = LoggerFactory.getLogger(SimpleOntologySelectorTest.class);

	private static OntologySelector	ontoSelector;
	private static TopicExtractionCommon	topicExtraction;

	@BeforeClass
	public static void beforeClass() {
		topicExtraction = new TopicExtractionCommon();
		topicExtraction.init();

		ontoSelector = new OntologySelector();
		ontoSelector.init();
	}

	private void test(List<String> wordSenses, String id) {
		logger.info("Starting test " + id);
		List<Topic> topics = topicExtraction.getTopicsForSenses(wordSenses);
		ontoSelector.exec(topics);
	}

	@Test
	public void testHeatingOneOne() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("radiator");
		test(wordSenses, "heating1.1");
	}

	@Test
	public void testHeatingOneTwo() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("temperature");
		wordSenses.add("air conditioning"); // TODO
		wordSenses.add("degree");
		test(wordSenses, "heating1.2");
	}

	@Test
	public void testHeatingTwoOne() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("radiator");
		wordSenses.add("thermostat");
		wordSenses.add("temperature");
		test(wordSenses, "heating2.1");
	}

	@Test
	public void testBedroomOneOne() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("closet");
		wordSenses.add("sweater");
		wordSenses.add("trousers");
		test(wordSenses, "bedroom1.1");
	}

	@Test
	public void testBedroomOneTwo() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("desk");
		wordSenses.add("nightstand");
		wordSenses.add("task (project management)");
		wordSenses.add("book");
		wordSenses.add("shelf (storage)");
		test(wordSenses, "bedroom1.2");
	}

	@Test
	public void testBedroomOneThree() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("robot");
		wordSenses.add("bed");
		wordSenses.add("pillow");
		wordSenses.add("bed");
		wordSenses.add("bed sheet");
		test(wordSenses, "bedroom1.3");
	}

	@Test
	public void testMusicOneOne() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("heavy metal music");
		wordSenses.add("playlist");
		wordSenses.add("sequence"); // for "order"...
		test(wordSenses, "music1.1");
	}

	@Test
	public void testMusicOneTwo() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("song");
		wordSenses.add("composer");
		test(wordSenses, "music1.2");
	}

	@Test
	public void testMusicOneThree() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("songwriter");
		wordSenses.add("piano");
		test(wordSenses, "music1.3");
	}

	@Test
	public void testChildrensRoomOneOne() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("doll");
		wordSenses.add("action figure");
		wordSenses.add("cabinetry");
		test(wordSenses, "childrensroom1.1");
	}

	@Test
	public void testChildrensRoomOneTwo() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("line (geometry)");
		wordSenses.add("carpet");
		wordSenses.add("carpet");
		wordSenses.add("rattle");
		wordSenses.add("rattle");
		test(wordSenses, "childrensroom1.2");
	}

	@Test
	public void testChildrensRoomOneThree() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("lego");
		wordSenses.add("brick");
		wordSenses.add("brick");
		wordSenses.add("brick");
		wordSenses.add("structure");
		wordSenses.add("dollhouse");
		test(wordSenses, "childrensroom1.3");
	}

	@Test
	public void testGardenOneOne() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("lawn");
		wordSenses.add("mower");
		wordSenses.add("grass");
		test(wordSenses, "garden1.1");
	}

	@Test
	public void testGardenOneTwo() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("shed");
		wordSenses.add("mower");
		wordSenses.add("lawn");
		test(wordSenses, "garden1.2");
	}

	@Test
	public void testGardenOneThree() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("tree");
		wordSenses.add("hedge");
		test(wordSenses, "garden1.3");
	}

	@Test
	public void testGardenOneFour() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("saw");
		wordSenses.add("table (furniture)");
		wordSenses.add("table (furniture)");
		test(wordSenses, "garden1.4");
	}

	@Test
	public void testGardenOneFive() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("tank");
		wordSenses.add("rake");
		wordSenses.add("shed");
		test(wordSenses, "garden1.5");
	}

	@Test
	public void testBarOneOne() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("refrigerator");
		wordSenses.add("tonic water");
		wordSenses.add("glass (drinkware)");
		wordSenses.add("gin");
		test(wordSenses, "bar1.1");
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
		test(wordSenses, "bar1.2");
	}

	@Test
	public void testBarOneThree() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("champagne");
		wordSenses.add("champagne");
		wordSenses.add("glass");
		wordSenses.add("counter (furniture)");
		test(wordSenses, "bar1.3");
	}

	@Test
	public void testBarOneFour() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("refrigerator");
		wordSenses.add("lime (fruit)");
		wordSenses.add("juice");
		wordSenses.add("cocktail");
		wordSenses.add("cocktail shaker");
		wordSenses.add("basil");
		wordSenses.add("syrup");
		wordSenses.add("gin");
		wordSenses.add("cocktail");
		wordSenses.add("glass (drinkware)");
		wordSenses.add("counter (furniture)");
		test(wordSenses, "bar1.4");
	}

	@Test
	public void testBarOneFive() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("table (furniture)");
		wordSenses.add("beer");
		wordSenses.add("table (furniture)");
		test(wordSenses, "bar1.5");
	}

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
	public void testOneTwo() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("table (furniture)");
		wordSenses.add("cup");
		wordSenses.add("dishwasher");
		wordSenses.add("cup");
		wordSenses.add("dishwasher");
		test(wordSenses, "1.2");
	}

	@Test
	public void testOneThree() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("fridge");
		wordSenses.add("orange (fruit)");
		wordSenses.add("juice");
		wordSenses.add("refrigerator");
		wordSenses.add("juice");
		test(wordSenses, "1.3");
	}

	@Test
	public void testTwoOne() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("popcorn");
		wordSenses.add("bag");
		test(wordSenses, "2.1");
	}

	@Test
	public void testTwoTwo() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("cup");
		wordSenses.add("dishwasher");
		test(wordSenses, "2.2");
	}

	@Test
	public void testTwoThree() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("orange (fruit)");
		wordSenses.add("juice");
		wordSenses.add("refrigerator");
		test(wordSenses, "2.3");
	}

	@Test
	public void testThreeOne() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("popcorn");
		wordSenses.add("kitchen");
		wordSenses.add("table (furniture)");
		test(wordSenses, "3.1");
	}

	@Test
	public void testThreeTwo() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("cup");
		wordSenses.add("kitchen");
		wordSenses.add("table (furniture)");
		wordSenses.add("dishwasher");
		wordSenses.add("cup");
		wordSenses.add("dishwasher");
		test(wordSenses, "3.2");
	}

	@Test
	public void testThreeThree() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("refrigerator");
		wordSenses.add("orange (fruit)");
		wordSenses.add("juice");
		test(wordSenses, "3.3");
	}

	@Test
	public void testFourOne() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("table (furniture)");
		wordSenses.add("popcorn");
		wordSenses.add("bag");
		test(wordSenses, "4.1");
	}

	@Test
	public void testFourTwo() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("table (furniture)");
		wordSenses.add("cup");
		wordSenses.add("dishwasher");
		wordSenses.add("dishwasher");
		wordSenses.add("dishwasher");
		test(wordSenses, "4.2");
	}

	@Test
	public void testFourThree() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("refrigerator");
		wordSenses.add("orange (fruit)");
		wordSenses.add("juice");
		wordSenses.add("refrigerator");
		wordSenses.add("orange (fruit)");
		wordSenses.add("juice");
		test(wordSenses, "4.3");
	}

	@Test
	public void testIfFourOne() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("dishware");
		wordSenses.add("dishwasher");
		wordSenses.add("cupboard");
		test(wordSenses, "If.4.1");
	}

	@Test
	public void testIfFourTwo() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("table (furniture)");
		wordSenses.add("dishware");
		wordSenses.add("dishwasher");
		wordSenses.add("cupboard");
		test(wordSenses, "If.4.2");
	}

	@Test
	public void testIfFourThree() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("table (furniture)");
		wordSenses.add("dishware");
		wordSenses.add("dishware");
		wordSenses.add("dishwasher");
		wordSenses.add("dishwasher");
		wordSenses.add("cupboard");
		test(wordSenses, "If.4.3");
	}

	@Test
	public void testIfFiveOne() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("refrigerator");
		wordSenses.add("orange (fruit)");
		wordSenses.add("vodka");
		wordSenses.add("orange (fruit)");
		wordSenses.add("juice");
		wordSenses.add("vodka");
		test(wordSenses, "If.5.1");
	}

	@Test
	public void testIfFiveTwo() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("refrigerator");
		wordSenses.add("orange (fruit)");
		wordSenses.add("vodka");
		wordSenses.add("orange (fruit)");
		wordSenses.add("juice");
		test(wordSenses, "If.5.2");
	}

	@Test
	public void testIfFiveThree() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("orange (fruit)");
		wordSenses.add("refrigerator");
		wordSenses.add("vodka");
		wordSenses.add("orange (fruit)");
		wordSenses.add("orange (fruit)");
		wordSenses.add("juice");
		test(wordSenses, "If.5.3");
	}

	@Test
	public void testSSevenPEight() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("plate (dishware)");
		wordSenses.add("dishwasher");
		wordSenses.add("water");
		wordSenses.add("sink");
		wordSenses.add("refrigerator");
		wordSenses.add("refrigerator");
		wordSenses.add("food");
		wordSenses.add("plate (dishware)");
		wordSenses.add("refrigerator");
		wordSenses.add("plate (dishware)");
		wordSenses.add("microwave");
		wordSenses.add("door");
		wordSenses.add("table (furniture)");
		test(wordSenses, "s7p08");
	}

	@Test
	public void testSSevenPNine() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("plate (dishware)");
		wordSenses.add("dishwasher");
		wordSenses.add("meal");
		wordSenses.add("refrigerator");
		wordSenses.add("plate (dishware)");
		wordSenses.add("microwave");
		wordSenses.add("table (furniture)");
		test(wordSenses, "s7p09");
	}

	@Test
	public void testSSevenPTen() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("dishwasher");
		wordSenses.add("plate (dishware)");
		wordSenses.add("plate (dishware)");
		wordSenses.add("refrigerator");
		wordSenses.add("meal");
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

}
