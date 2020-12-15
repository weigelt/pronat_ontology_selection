package edu.kit.ipd.parse.ontology_selector;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.kit.ipd.parse.topic_extraction_common.Topic;
import edu.kit.ipd.parse.topic_extraction_common.TopicExtractionCore;

/**
 * @author Jan Keim
 *
 */
@Ignore
public class SimpleOntologySelectorTest {
	private static final Logger logger = LoggerFactory.getLogger(SimpleOntologySelectorTest.class);

	private static OntologySelector ontoSelector;
	private static TopicExtractionCore topicExtraction;

	@BeforeClass
	public static void beforeClass() {
		topicExtraction = new TopicExtractionCore();

		ontoSelector = new OntologySelector();
		ontoSelector.init();
	}

	private void test(List<String> wordSenses, String id) {
		logger.info("Starting test " + id);
		final List<Topic> topics = topicExtraction.getTopicsForSenses(wordSenses);
		ontoSelector.exec(topics);
	}

	@Test
	public void testHeatingOneOne() {
		final List<String> wordSenses = new ArrayList<>();
		wordSenses.add("radiator");
		this.test(wordSenses, "heating1.1");
	}

	@Test
	public void testHeatingOneTwo() {
		final List<String> wordSenses = new ArrayList<>();
		wordSenses.add("temperature");
		wordSenses.add("air conditioning"); // TODO
		wordSenses.add("degree");
		this.test(wordSenses, "heating1.2");
	}

	@Test
	public void testHeatingTwoOne() {
		final List<String> wordSenses = new ArrayList<>();
		wordSenses.add("radiator");
		wordSenses.add("thermostat");
		wordSenses.add("temperature");
		this.test(wordSenses, "heating2.1");
	}

	@Test
	public void testBedroomOneOne() {
		final List<String> wordSenses = new ArrayList<>();
		wordSenses.add("closet");
		wordSenses.add("sweater");
		wordSenses.add("trousers");
		this.test(wordSenses, "bedroom1.1");
	}

	@Test
	public void testBedroomOneTwo() {
		final List<String> wordSenses = new ArrayList<>();
		wordSenses.add("desk");
		wordSenses.add("nightstand");
		wordSenses.add("task (project management)");
		wordSenses.add("book");
		wordSenses.add("shelf (storage)");
		this.test(wordSenses, "bedroom1.2");
	}

	@Test
	public void testBedroomOneThree() {
		final List<String> wordSenses = new ArrayList<>();
		wordSenses.add("robot");
		wordSenses.add("bed");
		wordSenses.add("pillow");
		wordSenses.add("bed");
		wordSenses.add("bed sheet");
		this.test(wordSenses, "bedroom1.3");
	}

	@Test
	public void testMusicOneOne() {
		final List<String> wordSenses = new ArrayList<>();
		wordSenses.add("heavy metal music");
		wordSenses.add("playlist");
		wordSenses.add("sequence"); // for "order"...
		this.test(wordSenses, "music1.1");
	}

	@Test
	public void testMusicOneTwo() {
		final List<String> wordSenses = new ArrayList<>();
		wordSenses.add("song");
		wordSenses.add("composer");
		this.test(wordSenses, "music1.2");
	}

	@Test
	public void testMusicOneThree() {
		final List<String> wordSenses = new ArrayList<>();
		wordSenses.add("songwriter");
		wordSenses.add("piano");
		this.test(wordSenses, "music1.3");
	}

	@Test
	public void testChildrensRoomOneOne() {
		final List<String> wordSenses = new ArrayList<>();
		wordSenses.add("doll");
		wordSenses.add("action figure");
		wordSenses.add("cabinetry");
		this.test(wordSenses, "childrensroom1.1");
	}

	@Test
	public void testChildrensRoomOneTwo() {
		final List<String> wordSenses = new ArrayList<>();
		wordSenses.add("line (geometry)");
		wordSenses.add("carpet");
		wordSenses.add("carpet");
		wordSenses.add("rattle");
		wordSenses.add("rattle");
		this.test(wordSenses, "childrensroom1.2");
	}

	@Test
	public void testChildrensRoomOneThree() {
		final List<String> wordSenses = new ArrayList<>();
		wordSenses.add("lego");
		wordSenses.add("brick");
		wordSenses.add("brick");
		wordSenses.add("brick");
		wordSenses.add("structure");
		wordSenses.add("dollhouse");
		this.test(wordSenses, "childrensroom1.3");
	}

	@Test
	public void testGardenOneOne() {
		final List<String> wordSenses = new ArrayList<>();
		wordSenses.add("lawn");
		wordSenses.add("mower");
		wordSenses.add("grass");
		this.test(wordSenses, "garden1.1");
	}

	@Test
	public void testGardenOneTwo() {
		final List<String> wordSenses = new ArrayList<>();
		wordSenses.add("shed");
		wordSenses.add("mower");
		wordSenses.add("lawn");
		this.test(wordSenses, "garden1.2");
	}

	@Test
	public void testGardenOneThree() {
		final List<String> wordSenses = new ArrayList<>();
		wordSenses.add("tree");
		wordSenses.add("hedge");
		this.test(wordSenses, "garden1.3");
	}

	@Test
	public void testGardenOneFour() {
		final List<String> wordSenses = new ArrayList<>();
		wordSenses.add("saw");
		wordSenses.add("table (furniture)");
		wordSenses.add("table (furniture)");
		this.test(wordSenses, "garden1.4");
	}

	@Test
	public void testGardenOneFive() {
		final List<String> wordSenses = new ArrayList<>();
		wordSenses.add("tank");
		wordSenses.add("rake");
		wordSenses.add("shed");
		this.test(wordSenses, "garden1.5");
	}

	@Test
	public void testBarOneOne() {
		final List<String> wordSenses = new ArrayList<>();
		wordSenses.add("refrigerator");
		wordSenses.add("tonic water");
		wordSenses.add("glass (drinkware)");
		wordSenses.add("gin");
		this.test(wordSenses, "bar1.1");
	}

	@Test
	public void testBarOneTwo() {
		final List<String> wordSenses = new ArrayList<>();
		wordSenses.add("cuba");
		wordSenses.add("cola");
		wordSenses.add("rum");
		wordSenses.add("lime (fruit)");
		wordSenses.add("juice");
		wordSenses.add("glass (drinkware)");
		this.test(wordSenses, "bar1.2");
	}

	@Test
	public void testBarOneThree() {
		final List<String> wordSenses = new ArrayList<>();
		wordSenses.add("champagne");
		wordSenses.add("champagne");
		wordSenses.add("glass");
		wordSenses.add("counter (furniture)");
		this.test(wordSenses, "bar1.3");
	}

	@Test
	public void testBarOneFour() {
		final List<String> wordSenses = new ArrayList<>();
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
		this.test(wordSenses, "bar1.4");
	}

	@Test
	public void testBarOneFive() {
		final List<String> wordSenses = new ArrayList<>();
		wordSenses.add("table (furniture)");
		wordSenses.add("beer");
		wordSenses.add("table (furniture)");
		this.test(wordSenses, "bar1.5");
	}

	@Test
	public void testOneOne() {
		final List<String> wordSenses = new ArrayList<>();
		wordSenses.add("table (furniture)");
		wordSenses.add("popcorn");
		wordSenses.add("popcorn");
		wordSenses.add("hand");
		this.test(wordSenses, "1.1");
	}

	@Test
	public void testOneTwo() {
		final List<String> wordSenses = new ArrayList<>();
		wordSenses.add("table (furniture)");
		wordSenses.add("cup");
		wordSenses.add("dishwasher");
		wordSenses.add("cup");
		wordSenses.add("dishwasher");
		this.test(wordSenses, "1.2");
	}

	@Test
	public void testOneThree() {
		final List<String> wordSenses = new ArrayList<>();
		wordSenses.add("fridge");
		wordSenses.add("orange (fruit)");
		wordSenses.add("juice");
		wordSenses.add("refrigerator");
		wordSenses.add("juice");
		this.test(wordSenses, "1.3");
	}

	@Test
	public void testTwoOne() {
		final List<String> wordSenses = new ArrayList<>();
		wordSenses.add("popcorn");
		wordSenses.add("bag");
		this.test(wordSenses, "2.1");
	}

	@Test
	public void testTwoTwo() {
		final List<String> wordSenses = new ArrayList<>();
		wordSenses.add("cup");
		wordSenses.add("dishwasher");
		this.test(wordSenses, "2.2");
	}

	@Test
	public void testTwoThree() {
		final List<String> wordSenses = new ArrayList<>();
		wordSenses.add("orange (fruit)");
		wordSenses.add("juice");
		wordSenses.add("refrigerator");
		this.test(wordSenses, "2.3");
	}

	@Test
	public void testThreeOne() {
		final List<String> wordSenses = new ArrayList<>();
		wordSenses.add("popcorn");
		wordSenses.add("kitchen");
		wordSenses.add("table (furniture)");
		this.test(wordSenses, "3.1");
	}

	@Test
	public void testThreeTwo() {
		final List<String> wordSenses = new ArrayList<>();
		wordSenses.add("cup");
		wordSenses.add("kitchen");
		wordSenses.add("table (furniture)");
		wordSenses.add("dishwasher");
		wordSenses.add("cup");
		wordSenses.add("dishwasher");
		this.test(wordSenses, "3.2");
	}

	@Test
	public void testThreeThree() {
		final List<String> wordSenses = new ArrayList<>();
		wordSenses.add("refrigerator");
		wordSenses.add("orange (fruit)");
		wordSenses.add("juice");
		this.test(wordSenses, "3.3");
	}

	@Test
	public void testFourOne() {
		final List<String> wordSenses = new ArrayList<>();
		wordSenses.add("table (furniture)");
		wordSenses.add("popcorn");
		wordSenses.add("bag");
		this.test(wordSenses, "4.1");
	}

	@Test
	public void testFourTwo() {
		final List<String> wordSenses = new ArrayList<>();
		wordSenses.add("table (furniture)");
		wordSenses.add("cup");
		wordSenses.add("dishwasher");
		wordSenses.add("dishwasher");
		wordSenses.add("dishwasher");
		this.test(wordSenses, "4.2");
	}

	@Test
	public void testFourThree() {
		final List<String> wordSenses = new ArrayList<>();
		wordSenses.add("refrigerator");
		wordSenses.add("orange (fruit)");
		wordSenses.add("juice");
		wordSenses.add("refrigerator");
		wordSenses.add("orange (fruit)");
		wordSenses.add("juice");
		this.test(wordSenses, "4.3");
	}

	@Test
	public void testIfFourOne() {
		final List<String> wordSenses = new ArrayList<>();
		wordSenses.add("dishware");
		wordSenses.add("dishwasher");
		wordSenses.add("cupboard");
		this.test(wordSenses, "If.4.1");
	}

	@Test
	public void testIfFourTwo() {
		final List<String> wordSenses = new ArrayList<>();
		wordSenses.add("table (furniture)");
		wordSenses.add("dishware");
		wordSenses.add("dishwasher");
		wordSenses.add("cupboard");
		this.test(wordSenses, "If.4.2");
	}

	@Test
	public void testIfFourThree() {
		final List<String> wordSenses = new ArrayList<>();
		wordSenses.add("table (furniture)");
		wordSenses.add("dishware");
		wordSenses.add("dishware");
		wordSenses.add("dishwasher");
		wordSenses.add("dishwasher");
		wordSenses.add("cupboard");
		this.test(wordSenses, "If.4.3");
	}

	@Test
	public void testIfFiveOne() {
		final List<String> wordSenses = new ArrayList<>();
		wordSenses.add("refrigerator");
		wordSenses.add("orange (fruit)");
		wordSenses.add("vodka");
		wordSenses.add("orange (fruit)");
		wordSenses.add("juice");
		wordSenses.add("vodka");
		this.test(wordSenses, "If.5.1");
	}

	@Test
	public void testIfFiveTwo() {
		final List<String> wordSenses = new ArrayList<>();
		wordSenses.add("refrigerator");
		wordSenses.add("orange (fruit)");
		wordSenses.add("vodka");
		wordSenses.add("orange (fruit)");
		wordSenses.add("juice");
		this.test(wordSenses, "If.5.2");
	}

	@Test
	public void testIfFiveThree() {
		final List<String> wordSenses = new ArrayList<>();
		wordSenses.add("orange (fruit)");
		wordSenses.add("refrigerator");
		wordSenses.add("vodka");
		wordSenses.add("orange (fruit)");
		wordSenses.add("orange (fruit)");
		wordSenses.add("juice");
		this.test(wordSenses, "If.5.3");
	}

	@Test
	public void testSSevenPEight() {
		final List<String> wordSenses = new ArrayList<>();
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
		this.test(wordSenses, "s7p08");
	}

	@Test
	public void testSSevenPNine() {
		final List<String> wordSenses = new ArrayList<>();
		wordSenses.add("plate (dishware)");
		wordSenses.add("dishwasher");
		wordSenses.add("meal");
		wordSenses.add("refrigerator");
		wordSenses.add("plate (dishware)");
		wordSenses.add("microwave");
		wordSenses.add("table (furniture)");
		this.test(wordSenses, "s7p09");
	}

	@Test
	public void testSSevenPTen() {
		final List<String> wordSenses = new ArrayList<>();
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
		this.test(wordSenses, "s7p10");
	}

}
