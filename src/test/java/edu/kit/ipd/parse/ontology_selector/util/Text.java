/**
 *
 */
package edu.kit.ipd.parse.ontology_selector.util;

import java.util.List;

/**
 * This is an adapted version of the Text class in the CorefAnalyzer by Tobias
 * Hey
 *
 * @author Jan Keim
 *
 */
public class Text {
	private String text;
	private List<String[]> annotations;

	Text(String text, List<String[]> annotations) {
		this.setText(text);
		this.setRefs(annotations);
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return this.text;
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
		return this.annotations;
	}

	/**
	 * @param refs
	 *            the refs to set
	 */
	private void setRefs(List<String[]> annotations) {
		this.annotations = annotations;
	}

}
