/**
 *
 */
package edu.kit.ipd.parse.ontologySelector.merger;

import java.util.List;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import edu.kit.ipd.parse.ontologySelector.TopicOntology;

/**
 * @author Jan Keim
 *
 */
public abstract class OntologyMerger {
	public abstract OWLOntology merge(OWLOntologyManager owlManager, List<TopicOntology> ontologies, IRI iri);
}
