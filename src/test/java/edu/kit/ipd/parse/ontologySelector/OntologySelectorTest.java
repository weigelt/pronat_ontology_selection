package edu.kit.ipd.parse.ontologySelector;

import java.util.HashMap;
import java.util.Optional;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLOntology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.kit.ipd.parse.graphBuilder.GraphBuilder;
import edu.kit.ipd.parse.luna.data.MissingDataException;
import edu.kit.ipd.parse.luna.data.PrePipelineData;
import edu.kit.ipd.parse.luna.graph.IGraph;
import edu.kit.ipd.parse.luna.pipeline.PipelineStageException;
import edu.kit.ipd.parse.luna.tools.StringToHypothesis;
import edu.kit.ipd.parse.ner.NERTagger;
import edu.kit.ipd.parse.ontologySelector.util.TestHelper;
import edu.kit.ipd.parse.ontologySelector.util.Text;
import edu.kit.ipd.parse.shallownlp.ShallowNLP;
import edu.kit.ipd.parse.topic_extraction_common.TopicExtractionCommon;
import edu.kit.ipd.parse.wikiWSD.WordSenseDisambiguation;

/**
 * @author Jan Keim
 *
 */
// @Ignore
public class OntologySelectorTest {
	private static final Logger logger = LoggerFactory.getLogger(OntologySelectorTest.class);

	private static OntologySelector ontoSelector;
	private static TopicExtractionCommon topicExtraction;
	private static HashMap<String, Text> texts;
	private static WordSenseDisambiguation wsd;
	private static ShallowNLP snlp;
	private static GraphBuilder graphBuilder;
	private static NERTagger ner;
	private PrePipelineData ppd;

	@BeforeClass
	public static void beforeClass() {
		graphBuilder = new GraphBuilder();
		graphBuilder.init();
		snlp = new ShallowNLP();
		snlp.init();
		ner = new NERTagger();
		ner.init();
		wsd = new WordSenseDisambiguation();
		wsd.init();
		topicExtraction = new TopicExtractionCommon();
		topicExtraction.init();

		texts = TestHelper.texts;

		ontoSelector = new OntologySelector();
		ontoSelector.setTopicExtraction(topicExtraction);
		ontoSelector.useSavedTopicOntologies(true);
		ontoSelector.init();
	}

	private void executePrepipeline(PrePipelineData ppd) {
		try {
			snlp.exec(ppd);
			ner.exec(ppd);
			graphBuilder.exec(ppd);
			wsd.exec(ppd);
			topicExtraction.exec(ppd);
		} catch (final PipelineStageException e) {
			e.printStackTrace();
		}
	}

	private void testOneText(String id) {
		logger.debug(id);
		this.ppd = new PrePipelineData();
		final Text text = texts.get(id);
		logger.debug(text.getText());
		final String input = text.getText().replace("\n", " ");
		this.ppd.setMainHypothesis(StringToHypothesis.stringToMainHypothesis(input));
		this.executePrepipeline(this.ppd);
		try {
			final IGraph graph = this.ppd.getGraph();
			ontoSelector.setGraph(graph);
			ontoSelector.exec();

			// check if an OWLOntology was annotated
			final Optional<OWLOntology> ontoOpt = OntologySelector.getOntologyFromIGraph(graph);
			OWLOntology owlOnto = null;
			if (ontoOpt.isPresent()) {
				owlOnto = ontoOpt.get();
			} else {
				logger.warn("No Ontology annotated!");
			}
			Assert.assertNotNull(owlOnto);
		} catch (final MissingDataException e) {
			e.printStackTrace();
		}
	}

	@Ignore
	@Test
	public void testThreeOne() {
		this.testOneText("3.1");
	}

	@Ignore
	@Test
	public void testThreeTwo() {
		this.testOneText("3.2");
	}

	@Test
	public void testThreeThree() {
		this.testOneText("3.3");
	}

	@Ignore
	@Test
	public void testGardenOneOne() {
		this.testOneText("garden1.1");
	}

	@Ignore
	@Test
	public void testHeatingOneOne() {
		this.testOneText("heating1.1");
	}

	@Ignore
	@Test
	public void testHeatingOneTwo() {
		this.testOneText("heating1.2");
	}

	@Ignore
	@Test
	public void testHeatingTwoOne() {
		this.testOneText("heating2.1");
	}

	@Test
	public void testBedroomOneOne() {
		this.testOneText("bedroom1.1");
	}

	@Test
	public void testBedroomOneTwo() {
		this.testOneText("bedroom1.2");
	}

	@Ignore
	@Test
	public void testBedroomOneThree() {
		this.testOneText("bedroom1.3");
	}

	@Test
	public void testMusicOneOne() {
		this.testOneText("music1.1");
	}

	@Test
	public void testMusicOneTwo() {
		this.testOneText("music1.2");
	}

	@Ignore
	@Test
	public void testChildrensRoomOneOne() {
		this.testOneText("childrensroom1.1");
	}

	@Ignore
	@Test
	public void testChildrensRoomOneTwo() {
		this.testOneText("childrensroom1.2");
	}

	@Ignore
	@Test
	public void testChildrensRoomOneThree() {
		this.testOneText("childrensroom1.1");
	}

	@Ignore
	@Test
	public void testMusicOneThree() {
		this.testOneText("music1.3");
	}

	@Ignore
	@Test
	public void testGardenOneTwo() {
		this.testOneText("garden1.2");
	}

	@Ignore
	@Test
	public void testGardenOneThree() {
		this.testOneText("garden1.3");
	}

	@Ignore
	@Test
	public void testGardenOneFour() {
		this.testOneText("garden1.4");
	}

	@Ignore
	@Test
	public void testGardenOneFive() {
		this.testOneText("garden1.5");
	}

	@Test
	public void testBarOneOne() {
		this.testOneText("bar1.1");
	}

	@Test
	public void testBarOneTwo() {
		this.testOneText("bar1.2");
	}

	@Ignore
	@Test
	public void testBarOneThree() {
		this.testOneText("bar1.3");
	}

	@Ignore
	@Test
	public void testBarOneFour() {
		this.testOneText("bar1.4");
	}

	@Ignore
	@Test
	public void testBarOneFive() {
		this.testOneText("bar1.5");
	}

	@Test
	public void testOneOne() {
		this.testOneText("1.1");
	}

	@Ignore
	@Test
	public void testOneTwo() {
		this.testOneText("1.2");
	}

	@Ignore
	@Test
	public void testOneThree() {
		this.testOneText("1.3");
	}

	@Test
	public void testTwoOne() {
		this.testOneText("2.1");
	}

	@Ignore
	@Test
	public void testTwoTwo() {
		this.testOneText("2.2");
	}

	@Ignore
	@Test
	public void testTwoThree() {
		this.testOneText("2.3");
	}

	@Ignore
	@Test
	public void testFourOne() {
		this.testOneText("4.1");
	}

	@Ignore
	@Test
	public void testFourTwo() {
		this.testOneText("4.2");
	}

	@Ignore
	@Test
	public void testFourThree() {
		this.testOneText("4.3");
	}

	@Ignore
	@Test
	public void testFiveOne() {
		this.testOneText("5.1");
	}

	@Ignore
	@Test
	public void testFiveTwo() {
		this.testOneText("5.2");
	}

	@Ignore
	@Test
	public void testFiveThree() {
		this.testOneText("5.3");
	}

	@Ignore
	@Test
	public void testSixOne() {
		this.testOneText("6.1");
	}

	@Ignore
	@Test
	public void testSixTwo() {
		this.testOneText("6.2");
	}

	@Ignore
	@Test
	public void testSixThree() {
		this.testOneText("6.3");
	}

	@Ignore
	@Test
	public void testSevenOne() {
		this.testOneText("7.1");
	}

	@Ignore
	@Test
	public void testSevenTwo() {
		this.testOneText("7.2");
	}

	@Ignore
	@Test
	public void testSevenThree() {
		this.testOneText("7.3");
	}

	@Ignore
	@Test
	public void testEightOne() {
		this.testOneText("8.1");
	}

	@Ignore
	@Test
	public void testEightTwo() {
		this.testOneText("8.2");
	}

	@Ignore
	@Test
	public void testEightThree() {
		this.testOneText("8.3");
	}

	@Ignore
	@Test
	public void testNineOne() {
		this.testOneText("9.1");
	}

	@Ignore
	@Test
	public void testNineTwo() {
		this.testOneText("9.2");
	}

	@Ignore
	@Test
	public void testNineThree() {
		this.testOneText("9.3");
	}

	@Ignore
	@Test
	public void testTenOne() {
		this.testOneText("10.1");
	}

	@Ignore
	@Test
	public void testTenTwo() {
		this.testOneText("10.2");
	}

	@Ignore
	@Test
	public void testTenThree() {
		this.testOneText("10.3");
	}

	@Ignore
	@Test
	public void testElevenOne() {
		this.testOneText("11.1");
	}

	@Ignore
	@Test
	public void testElevenTwo() {
		this.testOneText("11.2");
	}

	@Ignore
	@Test
	public void testElevenThree() {
		this.testOneText("11.3");
	}

	@Ignore
	@Test
	public void testTwelveOne() {
		this.testOneText("12.1");
	}

	@Ignore
	@Test
	public void testTwelveTwo() {
		this.testOneText("12.2");
	}

	@Ignore
	@Test
	public void testTwelveThree() {
		this.testOneText("12.3");
	}

	@Ignore
	@Test
	public void testThirteenOne() {
		this.testOneText("13.1");
	}

	@Ignore
	@Test
	public void testThirteenTwo() {
		this.testOneText("13.2");
	}

	@Ignore
	@Test
	public void testThirteenThree() {
		this.testOneText("13.3");
	}

	@Ignore
	@Test
	public void testFourteenOne() {
		this.testOneText("14.1");
	}

	@Ignore
	@Test
	public void testFourteenTwo() {
		this.testOneText("14.2");
	}

	@Ignore
	@Test
	public void testFourteenThree() {
		this.testOneText("14.3");
	}

	@Ignore
	@Test
	public void testFifteenOne() {
		this.testOneText("15.1");
	}

	@Ignore
	@Test
	public void testFifteenTwo() {
		this.testOneText("15.2");
	}

	@Ignore
	@Test
	public void testFifteenThree() {
		this.testOneText("15.3");
	}

	@Ignore
	@Test
	public void testSixteenOne() {
		this.testOneText("16.1");
	}

	@Ignore
	@Test
	public void testSixteenTwo() {
		this.testOneText("16.2");
	}

	@Ignore
	@Test
	public void testSixteenThree() {
		this.testOneText("16.3");
	}

	@Ignore
	@Test
	public void testSeventeenOne() {
		this.testOneText("17.1");
		// TODO check why no topics annotated
	}

	@Ignore
	@Test
	public void testSeventeenTwo() {
		this.testOneText("17.2");
	}

	@Ignore
	@Test
	public void testSeventeenThree() {
		this.testOneText("17.3");
	}

	@Ignore
	@Test
	public void testEighteenOne() {
		this.testOneText("18.1");
	}

	@Test
	public void testEighteenTwo() {
		this.testOneText("18.2");
	}

	@Ignore
	@Test
	public void testEighteenThree() {
		this.testOneText("18.3");
	}

	@Ignore
	@Test
	public void testNineteenOne() {
		this.testOneText("19.1");
	}

	@Ignore
	@Test
	public void testNineteenTwo() {
		this.testOneText("19.2");
	}

	@Ignore
	@Test
	public void testNineteenThree() {
		this.testOneText("19.3");
	}

	@Ignore
	@Test
	public void testTwentyOne() {
		this.testOneText("20.1");
	}

	@Ignore
	@Test
	public void testTwentyTwo() {
		this.testOneText("20.2");
	}

	@Ignore
	@Test
	public void testTwentyThree() {
		this.testOneText("20.3");
	}

	@Ignore
	@Test
	public void testTwentyoneOne() {
		this.testOneText("21.1");
	}

	@Ignore
	@Test
	public void testTwentyoneTwo() {
		this.testOneText("21.2");
	}

	@Ignore
	@Test
	public void testTwentyoneThreeA() {
		this.testOneText("21.3a");
	}

	@Ignore
	@Test
	public void testTwentyoneThreeB() {
		this.testOneText("21.3b");
	}

	@Ignore
	@Test
	public void testTwentytwoOne() {
		this.testOneText("22.1");
	}

	@Ignore
	@Test
	public void testTwentytwoTwo() {
		this.testOneText("22.2");
	}

	@Ignore
	@Test
	public void testTwentytwoThree() {
		this.testOneText("22.3");
	}

	@Test
	public void testIfFourOne() {
		this.testOneText("if.4.1");
	}

	@Ignore
	@Test
	public void testIfFourTwo() {
		this.testOneText("if.4.2");
	}

	@Ignore
	@Test
	public void testIfFourThree() {
		this.testOneText("if.4.3");
	}

	@Ignore
	@Test
	public void testIfFourFour() {
		this.testOneText("if.4.4");
	}

	@Ignore
	@Test
	public void testIfFourFive() {
		this.testOneText("if.4.5");
	}

	@Ignore
	@Test
	public void testIfFourSix() {
		this.testOneText("if.4.6");
	}

	@Ignore
	@Test
	public void testIfFourSeven() {
		this.testOneText("if.4.7");
	}

	@Ignore
	@Test
	public void testIfFourEight() {
		this.testOneText("if.4.8");
	}

	@Ignore
	@Test
	public void testIfFourNine() {
		this.testOneText("if.4.9");
	}

	@Test
	public void testIfFourTen() {
		this.testOneText("if.4.10");
	}

	@Ignore
	@Test
	public void testIfFourEleven() {
		this.testOneText("if.4.11");
	}

	@Ignore
	@Test
	public void testIfFourTwelve() {
		this.testOneText("if.4.12");
	}

	@Ignore
	@Test
	public void testIfFourThirteen() {
		this.testOneText("if.4.13");
	}

	@Ignore
	@Test
	public void testIfFourFourteen() {
		this.testOneText("if.4.14");
	}

	@Ignore
	@Test
	public void testIfFourFifteen() {
		this.testOneText("if.4.15");
	}

	@Ignore
	@Test
	public void testIfFourSixteen() {
		this.testOneText("if.4.16");
	}

	@Ignore
	@Test
	public void testIfFourSeventeen() {
		this.testOneText("if.4.17");
	}

	@Ignore
	@Test
	public void testIfFourEighteen() {
		this.testOneText("if.4.18");
	}

	@Ignore
	@Test
	public void testIfFourNineteen() {
		this.testOneText("if.4.19");
	}

	@Ignore
	@Test
	public void testIfFiveOne() {
		this.testOneText("if.5.1");
	}

	@Ignore
	@Test
	public void testIfFiveTwo() {
		this.testOneText("if.5.2");
	}

	@Ignore
	@Test
	public void testIfFiveThree() {
		this.testOneText("if.5.3");
	}

	@Ignore
	@Test
	public void testIfFiveFour() {
		this.testOneText("if.5.4");
	}

	@Test
	public void testIfFiveFive() {
		this.testOneText("if.5.5");
	}

	@Ignore
	@Test
	public void testIfFiveSix() {
		this.testOneText("if.5.6");
	}

	@Ignore
	@Test
	public void testIfFiveSeven() {
		this.testOneText("if.5.7");
	}

	@Ignore
	@Test
	public void testIfFiveEight() {
		this.testOneText("if.5.8");
	}

	@Ignore
	@Test
	public void testIfFiveNine() {
		this.testOneText("if.5.9");
	}

	@Ignore
	@Test
	public void testIfFiveTen() {
		this.testOneText("if.5.10");
	}

	@Ignore
	@Test
	public void testIfFiveEleven() {
		this.testOneText("if.5.11");
	}

	@Test
	public void testIfFiveTwelve() {
		this.testOneText("if.5.12");
	}

	@Ignore
	@Test
	public void testIfFiveThirteen() {
		this.testOneText("if.5.13");
	}

	@Ignore
	@Test
	public void testIfFiveFourteen() {
		this.testOneText("if.5.14");
	}

	@Ignore
	@Test
	public void testIfFiveFifteen() {
		this.testOneText("if.5.15");
	}

	@Ignore
	@Test
	public void testIfFiveSixteen() {
		this.testOneText("if.5.16");
	}

	@Ignore
	@Test
	public void testIfFiveSeventeen() {
		this.testOneText("if.5.17");
	}

	@Ignore
	@Test
	public void testIfFiveEighteen() {
		this.testOneText("if.5.18");
	}

	@Ignore
	@Test
	public void testIfFiveNineteen() {
		this.testOneText("if.5.19");
	}

	@Ignore
	@Test
	public void testTwentythreeOne() {
		this.testOneText("23.1");
		// TODO check why no topics annotated
	}

	@Ignore
	@Test
	public void testTwentythreeTwo() {
		this.testOneText("23.2");
	}

	@Ignore
	@Test
	public void testTwentythreeThree() {
		this.testOneText("23.3");
	}

	@Ignore
	@Test
	public void testTwentyfourOne() {
		this.testOneText("24.1");
	}

	@Ignore
	@Test
	public void testTwentyfourTwo() {
		this.testOneText("24.2");
	}

	@Ignore
	@Test
	public void testTwentyfourThree() {
		this.testOneText("24.3");
	}

	@Ignore
	@Test
	public void testTwentyfiveOne() {
		this.testOneText("25.1");
	}

	@Ignore
	@Test
	public void testTwentyfiveTwo() {
		this.testOneText("25.2");
	}

	@Test
	public void testTwentyfiveThree() {
		this.testOneText("25.3");
	}

	@Ignore
	@Test
	public void testTwentysixOne() {
		this.testOneText("26.1");
	}

	@Ignore
	@Test
	public void testTwentysixTwo() {
		this.testOneText("26.2");
	}

	@Ignore
	@Test
	public void testTwentysixThree() {
		this.testOneText("26.3");
	}

	@Ignore
	@Test
	public void testTwentysevenOne() {
		this.testOneText("27.1");
	}

	@Ignore
	@Test
	public void testTwentysevenTwo() {
		this.testOneText("27.2");
	}

	@Ignore
	@Test
	public void testTwentysevenThree() {
		this.testOneText("27.3");
	}

	@Ignore
	@Test
	public void testTwentyeightOne() {
		this.testOneText("28.1");
	}

	@Ignore
	@Test
	public void testTwentyeightTwo() {
		this.testOneText("28.2");
	}

	@Ignore
	@Test
	public void testTwentyeightThree() {
		this.testOneText("28.3");
	}

	@Ignore
	@Test
	public void testTwentynineOne() {
		this.testOneText("29.1");
	}

	@Ignore
	@Test
	public void testTwentynineTwo() {
		this.testOneText("29.2");
	}

	@Ignore
	@Test
	public void testTwentynineThree() {
		this.testOneText("29.3");
	}

	@Ignore
	@Test
	public void testThirtyOne() {
		this.testOneText("30.1");
	}

	@Ignore
	@Test
	public void testThirtyTwo() {
		this.testOneText("30.2");
	}

	@Ignore
	@Test
	public void testThirtyThree() {
		this.testOneText("30.3");
	}

	@Ignore
	@Test
	public void testThirtyoneOne() {
		this.testOneText("31.1");
	}

	@Test
	public void testThirtyoneTwo() {
		this.testOneText("31.2");
	}

	@Ignore
	@Test
	public void testThirtyoneThree() {
		this.testOneText("31.3");
	}

	@Ignore
	@Test
	public void testThirtytwoOne() {
		this.testOneText("32.1");
	}

	@Ignore
	@Test
	public void testThirtytwoTwo() {
		this.testOneText("32.2");
	}

	@Ignore
	@Test
	public void testThirtytwoThree() {
		this.testOneText("32.3");
	}

	@Ignore
	@Test
	public void testThirtythreeOne() {
		this.testOneText("33.1");
	}

	@Ignore
	@Test
	public void testThirtythreeTwo() {
		this.testOneText("33.2");
	}

	@Ignore
	@Test
	public void testThirtythreeThree() {
		this.testOneText("33.3");
	}

	@Ignore
	@Test
	public void testThirtyfourOne() {
		this.testOneText("34.1");
	}

	@Ignore
	@Test
	public void testThirtyfourTwo() {
		this.testOneText("34.2");
	}

	@Ignore
	@Test
	public void testThirtyfourThree() {
		this.testOneText("34.3");
	}

	@Ignore
	@Test
	public void testThirtyfiveOne() {
		this.testOneText("35.1");
	}

	@Ignore
	@Test
	public void testThirtyfiveTwo() {
		this.testOneText("35.2");
	}

	@Ignore
	@Test
	public void testThirtyfiveThree() {
		this.testOneText("35.3");
	}

	@Ignore
	@Test
	public void testThirtysixOne() {
		this.testOneText("36.1");
	}

	@Ignore
	@Test
	public void testThirtysixTwo() {
		this.testOneText("36.2");
	}

	@Ignore
	@Test
	public void testThirtysixThree() {
		this.testOneText("36.3");
	}

	@Ignore
	@Test
	public void testSSixPZeroOne() {
		this.testOneText("s6p01");
	}

	@Ignore
	@Test
	public void testSSixPZeroTwo() {
		this.testOneText("s6p02");
	}

	@Test
	public void testSSixPZeroThree() {
		this.testOneText("s6p03");
	}

	@Ignore
	@Test
	public void testSSixPZeroFour() {
		this.testOneText("s6p04");
	}

	@Ignore
	@Test
	public void testSSixPZeroFive() {
		this.testOneText("s6p05");
	}

	@Ignore
	@Test
	public void testSSixPZeroSix() {
		this.testOneText("s6p06");
	}

	@Ignore
	@Test
	public void testSSixPZeroSeven() {
		this.testOneText("s6p07");
	}

	@Ignore
	@Test
	public void testSSixPZeroEight() {
		this.testOneText("s6p08");
	}

	@Ignore
	@Test
	public void testSSixPZeroNine() {
		this.testOneText("s6p09");
	}

	@Test
	public void testSSixPTen() {
		this.testOneText("s6p10");
	}

	@Ignore
	@Test
	public void testSSevenPZeroOne() {
		this.testOneText("s7p01");
	}

	@Ignore
	@Test
	public void testSSevenPZeroTwo() {
		this.testOneText("s7p02");
	}

	@Ignore
	@Test
	public void testSSevenPZeroThree() {
		this.testOneText("s7p03");
	}

	@Ignore
	@Test
	public void testSSevenPZeroFour() {
		this.testOneText("s7p04");
	}

	@Ignore
	@Test
	public void testSSevenPZeroFiveA() {
		this.testOneText("s7p05a");
	}

	@Ignore
	@Test
	public void testSSevenPZeroFiveB() {
		this.testOneText("s7p05b");
	}

	@Ignore
	@Test
	public void testSSevenPZeroSix() {
		this.testOneText("s7p06");
	}

	@Ignore
	@Test
	public void testSSevenPZeroSeven() {
		this.testOneText("s7p07");
	}

	@Test
	public void testSSevenPZeroEight() {
		this.testOneText("s7p08");
	}

	@Ignore
	@Test
	public void testSSevenPZeroNine() {
		this.testOneText("s7p09");
	}

	@Test
	public void testSSevenPTen() {
		this.testOneText("s7p10");
	}

	@Test
	public void testSEightPOne() {
		this.testOneText("s8p01");
	}

	@Test
	public void testSEightPSix() {
		this.testOneText("s8p06");
	}

	@Test
	public void testDroneOneOne() {
		this.testOneText("drone1.1");
	}

	@Test
	public void testDroneOneTwo() {
		this.testOneText("drone1.2");
	}

	@Test
	public void testMindstormOneOne() {
		this.testOneText("mindstorm1.1");
	}

	@Test
	public void testMindstormOneTwo() {
		this.testOneText("mindstorm1.2");
	}

	@Test
	public void testAlexaOneOne() {
		this.testOneText("alexa1.1");
	}

	@Test
	public void testAlexaOneTwo() {
		this.testOneText("alexa1.2");
	}

}
