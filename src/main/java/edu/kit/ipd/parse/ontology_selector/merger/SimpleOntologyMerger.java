package edu.kit.ipd.parse.ontology_selector.merger;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.semanticweb.owlapi.model.AddImport;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLNamedObject;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.OWLEntityRenamer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.kit.ipd.parse.ontology_selector.TopicOntology;

/**
 * Simple {@link OntologyMerger} that throws all axioms and ImportsDeclarations
 * into a new Ontology and renames the IRI's. Works fine, if equal concepts have
 * equal names. Otherwise it won't be great.
 *
 * @author Jan Keim
 *
 */
public class SimpleOntologyMerger extends OntologyMerger {
	private static final Logger logger = LoggerFactory.getLogger(SimpleOntologyMerger.class);

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * im.janke.ontologySelector.merger.OntologyMerger#merge(org.semanticweb.owlapi.
	 * model.OWLOntologyManager, java.util.List, org.semanticweb.owlapi.model.IRI)
	 */
	@Override
	public OWLOntology merge(OWLOntologyManager owlManager, List<TopicOntology> ontologies, IRI iri) {
		Set<OWLAxiom> axioms = new HashSet<>();
		Set<OWLImportsDeclaration> imports = new HashSet<>();
		OWLOntology merged = null;

		try {
			merged = owlManager.createOntology(iri);
			for (TopicOntology ontology : ontologies) {
				IRI t_iri = IRI.create(new File(ontology.getOntologyPath()).toURI());
				OWLOntology onto = owlManager.loadOntologyFromOntologyDocument(t_iri);
				onto.axioms().forEach(axiom -> {
					axioms.add(axiom);
				});
				onto.importsDeclarations().forEach(impDecl -> {
					imports.add(impDecl);
				});
				owlManager.removeOntology(onto);
			}

			owlManager.addAxioms(merged, axioms.stream());
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}

		// Adding the import declarations
		for (OWLImportsDeclaration decl : imports) {
			owlManager.applyChange(new AddImport(merged, decl));
		}

		// rename everything to use the merged ontology's IRI
		renameAll(owlManager, ontologies, iri);
		// add the properties to connect dataTypes to Objects
		connectTypesToObjects(owlManager, merged, iri);
		return merged;
	}

	private void connectTypesToObjects(OWLOntologyManager owlManager, OWLOntology merged, IRI iri) {
		List<OWLClass> classes = merged.classesInSignature().collect(Collectors.toList());
		List<OWLNamedIndividual> individuals = merged.individualsInSignature().collect(Collectors.toList());

		// create axiom (Domain: DataType, Range: Object)
		// TODO: check if needed
		for (OWLClass clazz : classes) {
			String clazzName = clazz.getIRI().getShortForm();
			if (clazzName.equalsIgnoreCase("Object")) {
				merged.add(createObjectPropertyRangeAxiom(owlManager, iri, clazz));
			} else if (clazzName.equalsIgnoreCase("DataType")) {
				merged.add(createObjectPropertyDomainAxiom(owlManager, iri, clazz));
			}
		}

		// add assertion (individuals to classes)
		for (OWLNamedIndividual individual : individuals) {
			String individualName = individual.getIRI().getShortForm().toLowerCase();
			for (OWLClass clazz : classes) {
				String clazzName = clazz.getIRI().getShortForm().toLowerCase();
				if (clazzName.equalsIgnoreCase(individualName)) {
					// case 1: direct match of names
					merged.classAssertionAxioms(clazz).flatMap(a -> a.individualsInSignature())
							.forEach(other -> merged.add(createObjectPropertyAssertionAxiom(owlManager, iri, individual, other)));
				} else if (getHammingDistance(clazzName, individualName) < (Math.min(clazzName.length(), individualName.length()) * 0.20)) {
					// case 2: hamming distance is smaller than 20% of length of shorter String. This is dumb, but
					// better than nothing
					logger.debug("Connecting " + individualName + " with " + clazzName);
					merged.classAssertionAxioms(clazz).flatMap(a -> a.individualsInSignature())
							.forEach(other -> merged.add(createObjectPropertyAssertionAxiom(owlManager, iri, individual, other)));
				}
			}
		}
	}

	private static int getHammingDistance(String sequence1, String sequence2) {
		// see https://stackoverflow.com/a/16260973
		char[] s1 = sequence1.toCharArray();
		char[] s2 = sequence2.toCharArray();

		int shorter = Math.min(s1.length, s2.length);
		int longest = Math.max(s1.length, s2.length);

		int result = 0;
		for (int i = 0; i < shorter; i++) {
			if (s1[i] != s2[i]) {
				result++;
			}
		}

		result += longest - shorter;

		return result;
	}

	private OWLObjectPropertyAssertionAxiom createObjectPropertyAssertionAxiom(OWLOntologyManager owlManager, IRI iri,
			OWLIndividual individual, OWLIndividual otherIndividual) {
		OWLObjectPropertyAssertionAxiom axiom = null;
		OWLDataFactory df = owlManager.getOWLDataFactory();
		OWLObjectProperty objectProperty = df.getOWLObjectProperty(IRI.create(iri.getIRIString(), "#typeToObject"));
		axiom = df.getOWLObjectPropertyAssertionAxiom(objectProperty, individual, otherIndividual);
		return axiom;
	}

	private OWLObjectPropertyRangeAxiom createObjectPropertyRangeAxiom(OWLOntologyManager owlManager, IRI iri, OWLClass clazz) {
		OWLObjectPropertyRangeAxiom axiom = null;
		OWLDataFactory df = owlManager.getOWLDataFactory();
		OWLObjectProperty objectProperty = df.getOWLObjectProperty(IRI.create(iri.getIRIString(), "#typeToObject"));
		axiom = df.getOWLObjectPropertyRangeAxiom(objectProperty, clazz);
		return axiom;
	}

	private OWLObjectPropertyDomainAxiom createObjectPropertyDomainAxiom(OWLOntologyManager owlManager, IRI iri, OWLClass clazz) {
		OWLObjectPropertyDomainAxiom axiom = null;
		OWLDataFactory df = owlManager.getOWLDataFactory();
		OWLObjectProperty objectProperty = df.getOWLObjectProperty(IRI.create(iri.getIRIString(), "#typeToObject"));
		axiom = df.getOWLObjectPropertyDomainAxiom(objectProperty, clazz);
		return axiom;
	}

	private void renameIndividuals(OWLOntologyManager owlManager, List<TopicOntology> ontologies, IRI iri) {
		OWLEntityRenamer renamer = new OWLEntityRenamer(owlManager, owlManager.ontologies().collect(Collectors.toList()));
		for (TopicOntology ontology : ontologies) {
			ontology.getOntology().individualsInSignature().forEach(individual -> {
				IRI individualName = IRI.create(individual.getIRI().toString().replaceFirst("[^*]+(?=#|;)", iri.toString()));
				owlManager.applyChanges(renamer.changeIRI(individual.getIRI(), individualName));
			});
		}
	}

	private void renameAll(OWLOntologyManager owlManager, List<TopicOntology> ontologies, IRI iri) {
		OWLEntityRenamer renamer = new OWLEntityRenamer(owlManager, owlManager.ontologies().collect(Collectors.toList()));
		for (TopicOntology ontology : ontologies) {
			ontology.getOntology().axioms().flatMap(a -> a.components()).filter(c -> c instanceof OWLNamedObject)
					.map(c -> (OWLNamedObject) c)
					.filter(no -> !no.getIRI().toString().contains("w3.org") && !no.getIRI().toString().contains("Thing")).forEach(obj -> {
						IRI individualName = IRI.create(obj.getIRI().toString().replaceFirst("[^*]+(?=#|;)", iri.toString()));
						owlManager.applyChanges(renamer.changeIRI(obj.getIRI(), individualName));
					});
		}
	}
}
