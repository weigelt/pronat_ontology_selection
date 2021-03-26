package edu.kit.ipd.pronat.ontology_selector;

import java.util.HashMap;
import java.util.Optional;

import edu.kit.ipd.pronat.graph_builder.GraphBuilder;
import edu.kit.ipd.pronat.ner.NERTagger;
import edu.kit.ipd.pronat.ontology_selector.util.TestHelper;
import edu.kit.ipd.pronat.ontology_selector.util.Text;
import edu.kit.ipd.pronat.prepipedatamodel.PrePipelineData;
import edu.kit.ipd.pronat.prepipedatamodel.tools.StringToHypothesis;
import edu.kit.ipd.pronat.shallow_nlp.ShallowNLP;
import edu.kit.ipd.pronat.topic_extraction.TopicExtraction;
import edu.kit.ipd.pronat.topic_extraction_common.TopicExtractionCore;
import edu.kit.ipd.pronat.wiki_wsd.WordSenseDisambiguation;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLOntology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.kit.ipd.parse.luna.data.MissingDataException;
import edu.kit.ipd.parse.luna.graph.IGraph;
import edu.kit.ipd.parse.luna.pipeline.PipelineStageException;

/**
 * @author Jan Keim
 * @author Sebastian Weigelt
 */
@Ignore
public class OntologySelectorTest {
	private static final Logger logger = LoggerFactory.getLogger(OntologySelectorTest.class);

	private static OntologySelector ontoSelector;
	private static MyTopicExtraction topicExtraction;
	private static HashMap<String, Text> texts;
	private static MyWikiWSD wsd;
	private static ShallowNLP snlp;
	private static GraphBuilder graphBuilder;
	private static NERTagger ner;
	private static TopicExtractionCore topicExtractionCommon;
	private PrePipelineData ppd;

	@BeforeClass
	public static void beforeClass() {
		graphBuilder = new GraphBuilder();
		graphBuilder.init();
		snlp = new ShallowNLP();
		snlp.init();
		ner = new NERTagger();
		ner.init();
		wsd = new MyWikiWSD();
		wsd.init();
		topicExtraction = new MyTopicExtraction();
		topicExtraction.init();

		texts = TestHelper.texts;

		topicExtractionCommon = new TopicExtractionCore(); // TODO

		ontoSelector = new OntologySelector();
		ontoSelector.setTopicExtraction(topicExtractionCommon);
		ontoSelector.useSavedTopicOntologies(true);
		ontoSelector.init();
	}

	private void executePrepipeline(PrePipelineData ppd) {
		try {
			snlp.exec(ppd);
			ner.exec(ppd);
			graphBuilder.exec(ppd);
			wsd.setGraph(ppd.getGraph());
			wsd.exec();
			topicExtraction.setGraph(wsd.getGraph());
			topicExtraction.exec();
		} catch (final PipelineStageException | MissingDataException e) {
			e.printStackTrace();
		}
	}

	private void testOneText(String id) {
		logger.debug(id);
		ppd = new PrePipelineData();
		final Text text = texts.get(id);
		logger.debug(text.getText());
		final String input = text.getText().replace("\n", " ");
		ppd.setMainHypothesis(StringToHypothesis.stringToMainHypothesis(input));
		executePrepipeline(ppd);
		try {
			final IGraph graph = ppd.getGraph();
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
		testOneText("3.1");
	}

	@Ignore
	@Test
	public void testThreeTwo() {
		testOneText("3.2");
	}

	@Test
	public void testThreeThree() {
		testOneText("3.3");
	}

	@Ignore
	@Test
	public void testGardenOneOne() {
		testOneText("garden1.1");
	}

	@Ignore
	@Test
	public void testHeatingOneOne() {
		testOneText("heating1.1");
	}

	@Ignore
	@Test
	public void testHeatingOneTwo() {
		testOneText("heating1.2");
	}

	@Ignore
	@Test
	public void testHeatingTwoOne() {
		testOneText("heating2.1");
	}

	@Test
	public void testBedroomOneOne() {
		testOneText("bedroom1.1");
	}

	@Test
	public void testBedroomOneTwo() {
		testOneText("bedroom1.2");
	}

	@Ignore
	@Test
	public void testBedroomOneThree() {
		testOneText("bedroom1.3");
	}

	@Test
	public void testMusicOneOne() {
		testOneText("music1.1");
	}

	@Test
	public void testMusicOneTwo() {
		testOneText("music1.2");
	}

	@Ignore
	@Test
	public void testChildrensRoomOneOne() {
		testOneText("childrensroom1.1");
	}

	@Ignore
	@Test
	public void testChildrensRoomOneTwo() {
		testOneText("childrensroom1.2");
	}

	@Ignore
	@Test
	public void testChildrensRoomOneThree() {
		testOneText("childrensroom1.1");
	}

	@Ignore
	@Test
	public void testMusicOneThree() {
		testOneText("music1.3");
	}

	@Ignore
	@Test
	public void testGardenOneTwo() {
		testOneText("garden1.2");
	}

	@Ignore
	@Test
	public void testGardenOneThree() {
		testOneText("garden1.3");
	}

	@Ignore
	@Test
	public void testGardenOneFour() {
		testOneText("garden1.4");
	}

	@Ignore
	@Test
	public void testGardenOneFive() {
		testOneText("garden1.5");
	}

	@Test
	public void testBarOneOne() {
		testOneText("bar1.1");
	}

	@Test
	public void testBarOneTwo() {
		testOneText("bar1.2");
	}

	@Ignore
	@Test
	public void testBarOneThree() {
		testOneText("bar1.3");
	}

	@Ignore
	@Test
	public void testBarOneFour() {
		testOneText("bar1.4");
	}

	@Ignore
	@Test
	public void testBarOneFive() {
		testOneText("bar1.5");
	}

	@Test
	public void testOneOne() {
		testOneText("1.1");
	}

	@Ignore
	@Test
	public void testOneTwo() {
		testOneText("1.2");
	}

	@Ignore
	@Test
	public void testOneThree() {
		testOneText("1.3");
	}

	@Test
	public void testTwoOne() {
		testOneText("2.1");
	}

	@Ignore
	@Test
	public void testTwoTwo() {
		testOneText("2.2");
	}

	@Ignore
	@Test
	public void testTwoThree() {
		testOneText("2.3");
	}

	@Ignore
	@Test
	public void testFourOne() {
		testOneText("4.1");
	}

	@Ignore
	@Test
	public void testFourTwo() {
		testOneText("4.2");
	}

	@Ignore
	@Test
	public void testFourThree() {
		testOneText("4.3");
	}

	@Ignore
	@Test
	public void testFiveOne() {
		testOneText("5.1");
	}

	@Ignore
	@Test
	public void testFiveTwo() {
		testOneText("5.2");
	}

	@Ignore
	@Test
	public void testFiveThree() {
		testOneText("5.3");
	}

	@Ignore
	@Test
	public void testSixOne() {
		testOneText("6.1");
	}

	@Ignore
	@Test
	public void testSixTwo() {
		testOneText("6.2");
	}

	@Ignore
	@Test
	public void testSixThree() {
		testOneText("6.3");
	}

	@Ignore
	@Test
	public void testSevenOne() {
		testOneText("7.1");
	}

	@Ignore
	@Test
	public void testSevenTwo() {
		testOneText("7.2");
	}

	@Ignore
	@Test
	public void testSevenThree() {
		testOneText("7.3");
	}

	@Ignore
	@Test
	public void testEightOne() {
		testOneText("8.1");
	}

	@Ignore
	@Test
	public void testEightTwo() {
		testOneText("8.2");
	}

	@Ignore
	@Test
	public void testEightThree() {
		testOneText("8.3");
	}

	@Ignore
	@Test
	public void testNineOne() {
		testOneText("9.1");
	}

	@Ignore
	@Test
	public void testNineTwo() {
		testOneText("9.2");
	}

	@Ignore
	@Test
	public void testNineThree() {
		testOneText("9.3");
	}

	@Ignore
	@Test
	public void testTenOne() {
		testOneText("10.1");
	}

	@Ignore
	@Test
	public void testTenTwo() {
		testOneText("10.2");
	}

	@Ignore
	@Test
	public void testTenThree() {
		testOneText("10.3");
	}

	@Ignore
	@Test
	public void testElevenOne() {
		testOneText("11.1");
	}

	@Ignore
	@Test
	public void testElevenTwo() {
		testOneText("11.2");
	}

	@Ignore
	@Test
	public void testElevenThree() {
		testOneText("11.3");
	}

	@Ignore
	@Test
	public void testTwelveOne() {
		testOneText("12.1");
	}

	@Ignore
	@Test
	public void testTwelveTwo() {
		testOneText("12.2");
	}

	@Ignore
	@Test
	public void testTwelveThree() {
		testOneText("12.3");
	}

	@Ignore
	@Test
	public void testThirteenOne() {
		testOneText("13.1");
	}

	@Ignore
	@Test
	public void testThirteenTwo() {
		testOneText("13.2");
	}

	@Ignore
	@Test
	public void testThirteenThree() {
		testOneText("13.3");
	}

	@Ignore
	@Test
	public void testFourteenOne() {
		testOneText("14.1");
	}

	@Ignore
	@Test
	public void testFourteenTwo() {
		testOneText("14.2");
	}

	@Ignore
	@Test
	public void testFourteenThree() {
		testOneText("14.3");
	}

	@Ignore
	@Test
	public void testFifteenOne() {
		testOneText("15.1");
	}

	@Ignore
	@Test
	public void testFifteenTwo() {
		testOneText("15.2");
	}

	@Ignore
	@Test
	public void testFifteenThree() {
		testOneText("15.3");
	}

	@Ignore
	@Test
	public void testSixteenOne() {
		testOneText("16.1");
	}

	@Ignore
	@Test
	public void testSixteenTwo() {
		testOneText("16.2");
	}

	@Ignore
	@Test
	public void testSixteenThree() {
		testOneText("16.3");
	}

	@Ignore
	@Test
	public void testSeventeenOne() {
		testOneText("17.1");
		// TODO check why no topics annotated
	}

	@Ignore
	@Test
	public void testSeventeenTwo() {
		testOneText("17.2");
	}

	@Ignore
	@Test
	public void testSeventeenThree() {
		testOneText("17.3");
	}

	@Ignore
	@Test
	public void testEighteenOne() {
		testOneText("18.1");
	}

	@Test
	public void testEighteenTwo() {
		testOneText("18.2");
	}

	@Ignore
	@Test
	public void testEighteenThree() {
		testOneText("18.3");
	}

	@Ignore
	@Test
	public void testNineteenOne() {
		testOneText("19.1");
	}

	@Ignore
	@Test
	public void testNineteenTwo() {
		testOneText("19.2");
	}

	@Ignore
	@Test
	public void testNineteenThree() {
		testOneText("19.3");
	}

	@Ignore
	@Test
	public void testTwentyOne() {
		testOneText("20.1");
	}

	@Ignore
	@Test
	public void testTwentyTwo() {
		testOneText("20.2");
	}

	@Ignore
	@Test
	public void testTwentyThree() {
		testOneText("20.3");
	}

	@Ignore
	@Test
	public void testTwentyoneOne() {
		testOneText("21.1");
	}

	@Ignore
	@Test
	public void testTwentyoneTwo() {
		testOneText("21.2");
	}

	@Ignore
	@Test
	public void testTwentyoneThreeA() {
		testOneText("21.3a");
	}

	@Ignore
	@Test
	public void testTwentyoneThreeB() {
		testOneText("21.3b");
	}

	@Ignore
	@Test
	public void testTwentytwoOne() {
		testOneText("22.1");
	}

	@Ignore
	@Test
	public void testTwentytwoTwo() {
		testOneText("22.2");
	}

	@Ignore
	@Test
	public void testTwentytwoThree() {
		testOneText("22.3");
	}

	@Test
	public void testIfFourOne() {
		testOneText("if.4.1");
	}

	@Ignore
	@Test
	public void testIfFourTwo() {
		testOneText("if.4.2");
	}

	@Ignore
	@Test
	public void testIfFourThree() {
		testOneText("if.4.3");
	}

	@Ignore
	@Test
	public void testIfFourFour() {
		testOneText("if.4.4");
	}

	@Ignore
	@Test
	public void testIfFourFive() {
		testOneText("if.4.5");
	}

	@Ignore
	@Test
	public void testIfFourSix() {
		testOneText("if.4.6");
	}

	@Ignore
	@Test
	public void testIfFourSeven() {
		testOneText("if.4.7");
	}

	@Ignore
	@Test
	public void testIfFourEight() {
		testOneText("if.4.8");
	}

	@Ignore
	@Test
	public void testIfFourNine() {
		testOneText("if.4.9");
	}

	@Test
	public void testIfFourTen() {
		testOneText("if.4.10");
	}

	@Ignore
	@Test
	public void testIfFourEleven() {
		testOneText("if.4.11");
	}

	@Ignore
	@Test
	public void testIfFourTwelve() {
		testOneText("if.4.12");
	}

	@Ignore
	@Test
	public void testIfFourThirteen() {
		testOneText("if.4.13");
	}

	@Ignore
	@Test
	public void testIfFourFourteen() {
		testOneText("if.4.14");
	}

	@Ignore
	@Test
	public void testIfFourFifteen() {
		testOneText("if.4.15");
	}

	@Ignore
	@Test
	public void testIfFourSixteen() {
		testOneText("if.4.16");
	}

	@Ignore
	@Test
	public void testIfFourSeventeen() {
		testOneText("if.4.17");
	}

	@Ignore
	@Test
	public void testIfFourEighteen() {
		testOneText("if.4.18");
	}

	@Ignore
	@Test
	public void testIfFourNineteen() {
		testOneText("if.4.19");
	}

	@Ignore
	@Test
	public void testIfFiveOne() {
		testOneText("if.5.1");
	}

	@Ignore
	@Test
	public void testIfFiveTwo() {
		testOneText("if.5.2");
	}

	@Ignore
	@Test
	public void testIfFiveThree() {
		testOneText("if.5.3");
	}

	@Ignore
	@Test
	public void testIfFiveFour() {
		testOneText("if.5.4");
	}

	@Test
	public void testIfFiveFive() {
		testOneText("if.5.5");
	}

	@Ignore
	@Test
	public void testIfFiveSix() {
		testOneText("if.5.6");
	}

	@Ignore
	@Test
	public void testIfFiveSeven() {
		testOneText("if.5.7");
	}

	@Ignore
	@Test
	public void testIfFiveEight() {
		testOneText("if.5.8");
	}

	@Ignore
	@Test
	public void testIfFiveNine() {
		testOneText("if.5.9");
	}

	@Ignore
	@Test
	public void testIfFiveTen() {
		testOneText("if.5.10");
	}

	@Ignore
	@Test
	public void testIfFiveEleven() {
		testOneText("if.5.11");
	}

	@Test
	public void testIfFiveTwelve() {
		testOneText("if.5.12");
	}

	@Ignore
	@Test
	public void testIfFiveThirteen() {
		testOneText("if.5.13");
	}

	@Ignore
	@Test
	public void testIfFiveFourteen() {
		testOneText("if.5.14");
	}

	@Ignore
	@Test
	public void testIfFiveFifteen() {
		testOneText("if.5.15");
	}

	@Ignore
	@Test
	public void testIfFiveSixteen() {
		testOneText("if.5.16");
	}

	@Ignore
	@Test
	public void testIfFiveSeventeen() {
		testOneText("if.5.17");
	}

	@Ignore
	@Test
	public void testIfFiveEighteen() {
		testOneText("if.5.18");
	}

	@Ignore
	@Test
	public void testIfFiveNineteen() {
		testOneText("if.5.19");
	}

	@Ignore
	@Test
	public void testTwentythreeOne() {
		testOneText("23.1");
		// TODO check why no topics annotated
	}

	@Ignore
	@Test
	public void testTwentythreeTwo() {
		testOneText("23.2");
	}

	@Ignore
	@Test
	public void testTwentythreeThree() {
		testOneText("23.3");
	}

	@Ignore
	@Test
	public void testTwentyfourOne() {
		testOneText("24.1");
	}

	@Ignore
	@Test
	public void testTwentyfourTwo() {
		testOneText("24.2");
	}

	@Ignore
	@Test
	public void testTwentyfourThree() {
		testOneText("24.3");
	}

	@Ignore
	@Test
	public void testTwentyfiveOne() {
		testOneText("25.1");
	}

	@Ignore
	@Test
	public void testTwentyfiveTwo() {
		testOneText("25.2");
	}

	@Test
	public void testTwentyfiveThree() {
		testOneText("25.3");
	}

	@Ignore
	@Test
	public void testTwentysixOne() {
		testOneText("26.1");
	}

	@Ignore
	@Test
	public void testTwentysixTwo() {
		testOneText("26.2");
	}

	@Ignore
	@Test
	public void testTwentysixThree() {
		testOneText("26.3");
	}

	@Ignore
	@Test
	public void testTwentysevenOne() {
		testOneText("27.1");
	}

	@Ignore
	@Test
	public void testTwentysevenTwo() {
		testOneText("27.2");
	}

	@Ignore
	@Test
	public void testTwentysevenThree() {
		testOneText("27.3");
	}

	@Ignore
	@Test
	public void testTwentyeightOne() {
		testOneText("28.1");
	}

	@Ignore
	@Test
	public void testTwentyeightTwo() {
		testOneText("28.2");
	}

	@Ignore
	@Test
	public void testTwentyeightThree() {
		testOneText("28.3");
	}

	@Ignore
	@Test
	public void testTwentynineOne() {
		testOneText("29.1");
	}

	@Ignore
	@Test
	public void testTwentynineTwo() {
		testOneText("29.2");
	}

	@Ignore
	@Test
	public void testTwentynineThree() {
		testOneText("29.3");
	}

	@Ignore
	@Test
	public void testThirtyOne() {
		testOneText("30.1");
	}

	@Ignore
	@Test
	public void testThirtyTwo() {
		testOneText("30.2");
	}

	@Ignore
	@Test
	public void testThirtyThree() {
		testOneText("30.3");
	}

	@Ignore
	@Test
	public void testThirtyoneOne() {
		testOneText("31.1");
	}

	@Test
	public void testThirtyoneTwo() {
		testOneText("31.2");
	}

	@Ignore
	@Test
	public void testThirtyoneThree() {
		testOneText("31.3");
	}

	@Ignore
	@Test
	public void testThirtytwoOne() {
		testOneText("32.1");
	}

	@Ignore
	@Test
	public void testThirtytwoTwo() {
		testOneText("32.2");
	}

	@Ignore
	@Test
	public void testThirtytwoThree() {
		testOneText("32.3");
	}

	@Ignore
	@Test
	public void testThirtythreeOne() {
		testOneText("33.1");
	}

	@Ignore
	@Test
	public void testThirtythreeTwo() {
		testOneText("33.2");
	}

	@Ignore
	@Test
	public void testThirtythreeThree() {
		testOneText("33.3");
	}

	@Ignore
	@Test
	public void testThirtyfourOne() {
		testOneText("34.1");
	}

	@Ignore
	@Test
	public void testThirtyfourTwo() {
		testOneText("34.2");
	}

	@Ignore
	@Test
	public void testThirtyfourThree() {
		testOneText("34.3");
	}

	@Ignore
	@Test
	public void testThirtyfiveOne() {
		testOneText("35.1");
	}

	@Ignore
	@Test
	public void testThirtyfiveTwo() {
		testOneText("35.2");
	}

	@Ignore
	@Test
	public void testThirtyfiveThree() {
		testOneText("35.3");
	}

	@Ignore
	@Test
	public void testThirtysixOne() {
		testOneText("36.1");
	}

	@Ignore
	@Test
	public void testThirtysixTwo() {
		testOneText("36.2");
	}

	@Ignore
	@Test
	public void testThirtysixThree() {
		testOneText("36.3");
	}

	@Ignore
	@Test
	public void testSSixPZeroOne() {
		testOneText("s6p01");
	}

	@Ignore
	@Test
	public void testSSixPZeroTwo() {
		testOneText("s6p02");
	}

	@Test
	public void testSSixPZeroThree() {
		testOneText("s6p03");
	}

	@Ignore
	@Test
	public void testSSixPZeroFour() {
		testOneText("s6p04");
	}

	@Ignore
	@Test
	public void testSSixPZeroFive() {
		testOneText("s6p05");
	}

	@Ignore
	@Test
	public void testSSixPZeroSix() {
		testOneText("s6p06");
	}

	@Ignore
	@Test
	public void testSSixPZeroSeven() {
		testOneText("s6p07");
	}

	@Ignore
	@Test
	public void testSSixPZeroEight() {
		testOneText("s6p08");
	}

	@Ignore
	@Test
	public void testSSixPZeroNine() {
		testOneText("s6p09");
	}

	@Test
	public void testSSixPTen() {
		testOneText("s6p10");
	}

	@Ignore
	@Test
	public void testSSevenPZeroOne() {
		testOneText("s7p01");
	}

	@Ignore
	@Test
	public void testSSevenPZeroTwo() {
		testOneText("s7p02");
	}

	@Ignore
	@Test
	public void testSSevenPZeroThree() {
		testOneText("s7p03");
	}

	@Ignore
	@Test
	public void testSSevenPZeroFour() {
		testOneText("s7p04");
	}

	@Ignore
	@Test
	public void testSSevenPZeroFiveA() {
		testOneText("s7p05a");
	}

	@Ignore
	@Test
	public void testSSevenPZeroFiveB() {
		testOneText("s7p05b");
	}

	@Ignore
	@Test
	public void testSSevenPZeroSix() {
		testOneText("s7p06");
	}

	@Ignore
	@Test
	public void testSSevenPZeroSeven() {
		testOneText("s7p07");
	}

	@Test
	public void testSSevenPZeroEight() {
		testOneText("s7p08");
	}

	@Ignore
	@Test
	public void testSSevenPZeroNine() {
		testOneText("s7p09");
	}

	@Test
	public void testSSevenPTen() {
		testOneText("s7p10");
	}

	@Test
	public void testSEightPOne() {
		testOneText("s8p01");
	}

	@Test
	public void testSEightPSix() {
		testOneText("s8p06");
	}

	@Test
	public void testDroneOneOne() {
		testOneText("drone1.1");
	}

	@Test
	public void testDroneOneTwo() {
		testOneText("drone1.2");
	}

	@Test
	public void testMindstormOneOne() {
		testOneText("mindstorm1.1");
	}

	@Test
	public void testMindstormOneTwo() {
		testOneText("mindstorm1.2");
	}

	@Test
	public void testAlexaOneOne() {
		testOneText("alexa1.1");
	}

	@Test
	public void testAlexaOneTwo() {
		testOneText("alexa1.2");
	}

	static class MyWikiWSD extends WordSenseDisambiguation {

		@Override
		public void exec() {
			super.exec();
		}
	}

	static class MyTopicExtraction extends TopicExtraction {
		@Override
		public void exec() {
			super.exec();
		}
	}

}
