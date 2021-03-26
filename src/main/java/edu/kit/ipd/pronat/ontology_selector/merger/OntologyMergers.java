package edu.kit.ipd.pronat.ontology_selector.merger;

/**
 * Enum representing the different {@link OntologyMerger}s. Use {@link #get()}
 * to get an instance of the {@link OntologyMerger}.
 *
 * @author Jan Keim
 * @author Sebastian Weigelt
 *
 */
public enum OntologyMergers {
	SimpleOntologyMerger;

	/**
	 * Returns an instance of the corresponding {@link OntologyMerger}
	 * 
	 * @return corresponding ontology merger
	 */
	public OntologyMerger get() {
		switch (this) {
		case SimpleOntologyMerger:
			return new SimpleOntologyMerger();
		default:
			return new SimpleOntologyMerger();
		}
	}
}
