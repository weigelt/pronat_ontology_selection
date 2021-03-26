/**
 *
 */
package edu.kit.ipd.pronat.ontology_selector.merger;

import java.util.List;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import edu.kit.ipd.pronat.ontology_selector.TopicOntology;

/**
 * @author Jan Keim
 * @author Sebastian Weigelt
 *
 */
public abstract class OntologyMerger {
	public abstract OWLOntology merge(OWLOntologyManager owlManager, List<TopicOntology> ontologies, IRI iri);
}
