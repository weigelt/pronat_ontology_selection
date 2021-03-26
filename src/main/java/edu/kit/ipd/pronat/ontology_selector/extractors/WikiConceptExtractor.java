package edu.kit.ipd.pronat.ontology_selector.extractors;

import java.util.List;
import java.util.stream.Collectors;

import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * @author Jan Keim
 * @author Sebastian Weigelt
 *
 */
public class WikiConceptExtractor {

	public static List<String> getConceptsFromOntology(OWLOntology onto) {
		// TODO check
		// @formatter:off
		List<String> concepts = onto.axioms()
					.filter(ax -> ax.toString().contains("@wiki"))
					.map(ax -> {
						return ax.components()
								.filter(c -> c instanceof OWLLiteral)
								.map(c -> (OWLLiteral) c)
								.findFirst()
								.orElse(null);
					})
					.filter(ax -> ax != null)
					.map(lit -> lit.getLiteral())
					.collect(Collectors.toList());
		// @formatter:on
		return concepts;
	}
}
