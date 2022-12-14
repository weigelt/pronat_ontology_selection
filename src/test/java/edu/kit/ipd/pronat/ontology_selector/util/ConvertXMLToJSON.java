package edu.kit.ipd.pronat.ontology_selector.util;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Converts the XML Korpus to JSON Korpus
 *
 * @author Dominik Fuchss
 * @author Sebastian Weigelt
 *
 */
@Ignore
public class ConvertXMLToJSON {
	@Test
	public void convert() throws JsonGenerationException, JsonMappingException, IOException {
		new TestHelper();
		final File of = new File("src/test/resources/korpus.json");
		final Map<String, Text> data = TestHelper.texts;
		System.out.println(data);
		ConvertXMLToJSON.getObjectMapper().writeValue(of, data);
	}

	/**
	 * Get a new default object mapper for the project.
	 *
	 * @return a new default object mapper
	 */
	private static ObjectMapper getObjectMapper() {
		final ObjectMapper objectMapper = new ObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true);
		objectMapper.setVisibility(objectMapper.getSerializationConfig().getDefaultVisibilityChecker() //
				.withFieldVisibility(Visibility.ANY)//
				.withGetterVisibility(Visibility.NONE)//
				.withSetterVisibility(Visibility.NONE)//
				.withIsGetterVisibility(Visibility.NONE));
		return objectMapper;
	}

}
