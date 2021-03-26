/**
 *
 */
package edu.kit.ipd.pronat.ontology_selector.util;

import java.util.List;

/**
 * This is an adapted version of the Text class in the CorefAnalyzer by Tobias
 * Hey
 *
 * @author Jan Keim
 * @author Sebastian Weigelt
 *
 */
public class Text {
	private String text;
	private List<String[]> annotations;

	Text(String text, List<String[]> annotations) {
		setText(text);
		setRefs(annotations);
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	private void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the refs
	 */
	public List<String[]> getAnnotations() {
		return annotations;
	}

	/**
	 * @param annotations
	 *            the annotations to set
	 */
	private void setRefs(List<String[]> annotations) {
		this.annotations = annotations;
	}

}
