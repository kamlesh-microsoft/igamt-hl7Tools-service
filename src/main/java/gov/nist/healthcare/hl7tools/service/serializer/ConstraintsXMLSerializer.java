package gov.nist.healthcare.hl7tools.service.serializer;

import gov.nist.healthcare.hl7tools.domain.Component;
import gov.nist.healthcare.hl7tools.domain.ConformanceStatement;
import gov.nist.healthcare.hl7tools.domain.Datatype;
import gov.nist.healthcare.hl7tools.domain.Field;
import gov.nist.healthcare.hl7tools.domain.Message;
import gov.nist.healthcare.hl7tools.domain.Predicate;
import gov.nist.healthcare.hl7tools.domain.Segment;
import gov.nist.healthcare.hl7tools.domain.StatementDetails;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import nu.xom.Attribute;
import nu.xom.Element;

/**
 * Generates the constraints supported by the new validation engine.
 * 
 * @author Jungyub Woo <jungyub.woo@nist.gov>
 */

public class ConstraintsXMLSerializer {
	
	/**
	 * Creates a single profile from the message model
	 * @param m - The message model
	 * @return A single profile containing all artifacts from the library
	 * @throws Exception 
	 */
	public Element serialize(Message m) throws Exception {
		if( m == null ) throw new Exception("The message is null");
		
		Element e = new Element("ConformanceContext");
		//FIXME: The ID need to be fixed in IGAMT, it has to be something 
		// predictable since it will be used while writing constrains
		e.addAttribute( new Attribute("UUID", UUID.randomUUID().toString()) );
		e.addNamespaceDeclaration("xsi", "http://www.w3.org/2001/XMLSchema-instance");
		e.addAttribute( new Attribute("xsi:noNamespaceSchemaLocation", "http://www.w3.org/2001/XMLSchema-instance", "../ConformanceContext.xsd"));
		
		Element metaData_Elm = new Element("MetaData");
		Element metaData_Description_Elm = new Element("Description");
		metaData_Description_Elm.appendChild("Conformance context for " + m.getName());
		metaData_Elm.appendChild(metaData_Description_Elm);
		e.appendChild(metaData_Elm);
		
		
		Element predicates_Elm = new Element("Predicates");
		Element pDataType_Elm = new Element("Datatype");
		
		
		Map<String, Datatype> datatypeMap = new HashMap<String, Datatype>();
		for(Datatype d: m.getDatatypeSet()){
			datatypeMap.put(d.getName(), d);
		}
		
		for (Map.Entry<String, Datatype> entry : datatypeMap.entrySet()) {
			Element dataTypeConstaint = serialize(entry.getValue(), "cp");
			if(dataTypeConstaint != null) pDataType_Elm.appendChild(dataTypeConstaint);
		}
		
		predicates_Elm.appendChild(pDataType_Elm);
		
		Element pSegment_Elm = new Element("Segment");
		for(Segment s: m.getSegmentSet()){
			Element segmentConstaint = serialize(s, "cp");
			if(segmentConstaint != null) pSegment_Elm.appendChild(segmentConstaint);
		}
		predicates_Elm.appendChild(pSegment_Elm);
		
		Element pGroup_Elm = new Element("Group");
		predicates_Elm.appendChild(pGroup_Elm);
		
		e.appendChild(predicates_Elm);
		
		Element constraints_Elm = new Element("Constraints");
		Element cDataType_Elm = new Element("Datatype");
		
		for (Map.Entry<String, Datatype> entry : datatypeMap.entrySet()) {
			Element dataTypeConstaint = serialize(entry.getValue(), "cs");
			if(dataTypeConstaint != null) cDataType_Elm.appendChild(dataTypeConstaint);
		}	
		constraints_Elm.appendChild(cDataType_Elm);
		
		Element cSegment_Elm = new Element("Segment");
		for(Segment s: m.getSegmentSet()){
			Element segmentConstaint = serialize(s, "cs");
			if(segmentConstaint != null) cSegment_Elm.appendChild(segmentConstaint);
		}
		constraints_Elm.appendChild(cSegment_Elm);
		
		Element cGroup_Elm = new Element("Group");
		constraints_Elm.appendChild(cGroup_Elm);
		
		e.appendChild(constraints_Elm);
		
		return e;
	}
	
	/**
	 * Serializes the segment to XML (nu.xom.Element)
	 * @param s - The segment to be serialized
	 * @return nu.xom.Element representing the component
	 */
	private Element serialize(Segment s, String type) {
		
		if(type.equals("cs")){
			Element elmByID = new Element("ByID");
			elmByID.addAttribute( new Attribute("ID", s.getName()) );
			
			boolean isCS = false;
			
			
			List<Field> fields = s.getFields();
			if(fields != null){
				for(Field f:fields){
					String fieldPath = f.getPosition() + "[1]";
					List<ConformanceStatement> css = f.getConformanceStatementList();
					if(css !=null){
						for(ConformanceStatement cs:css){
							Element elmConstraint = new Element("Constraint");
							elmConstraint.addAttribute(new Attribute("ID", cs.getName()));
							elmConstraint.addAttribute(new Attribute("Target", fieldPath));

							
							StatementDetails sd = (StatementDetails)cs.getStatementDetails();
							sd.generateStatementText(true, true);
							cs.setDescription(sd.getStatementText());
							
							Element elmDescription = new Element("Description");
							elmDescription.appendChild(cs.getDescription());
							elmConstraint.appendChild(elmDescription);
							
							
							Element elmAssertion = ((StatementDetails)cs.getStatementDetails()).serializeConstraints(type);
							if(elmAssertion != null) {
								elmConstraint.appendChild(elmAssertion);
								elmByID.appendChild(elmConstraint);
								isCS = true;
							}else {
								System.out.println("Segment ID: " + s.getName());
								System.out.println("Skipped " + type + " ID: " + cs.getName());
								System.out.println("Skipped " + type + " Desc: " + cs.getDescription());
								System.out.println("-------------------------");
							}
						}
					}
					
				}
			}
			
			if(isCS) return elmByID;
		} else if(type.equals("cp")){
			Element elmByID = new Element("ByID");
			elmByID.addAttribute( new Attribute("ID", s.getName()) );
			
			boolean isCS = false;
			
			
			List<Field> fields = s.getFields();
			if(fields != null){
				for(Field f:fields){
					String fieldPath = f.getPosition() + "[1]";
					Predicate cp = f.getPredicate();
					if(cp !=null){
						StatementDetails sd = (StatementDetails)cp.getPredicateDetails();
						sd.generateStatementText(true, true);
						cp.setDescription(sd.getStatementText());
						
						Element elmConstraint = new Element("Predicate");
						elmConstraint.addAttribute(new Attribute("ID", "[" + s.getName()+ "]" + fieldPath));
						elmConstraint.addAttribute(new Attribute("Target", fieldPath));
						elmConstraint.addAttribute(new Attribute("TrueUsage", f.getUsage().getTrueUsage()));
						elmConstraint.addAttribute(new Attribute("FalseUsage", f.getUsage().getFalseUsage()));
						Element elmDescription = new Element("Description");
						elmDescription.appendChild(cp.getDescription());
						elmConstraint.appendChild(elmDescription);
						
						Element elmAssertion = ((StatementDetails)cp.getPredicateDetails()).serializeConstraints(type);
						if(elmAssertion != null) {
							elmConstraint.appendChild(elmAssertion);
							elmByID.appendChild(elmConstraint);
							isCS = true;
						}else {
							System.out.println("Segment ID: " + s.getName());
							System.out.println("Skipped " + type + " ID: " + "[" + s.getName()+ "]" + fieldPath);
							System.out.println("Skipped " + type + " Desc: " + cp.getDescription());
							System.out.println("-------------------------");
						}
					}
					
				}
			}
			
			if(isCS) return elmByID;
		}
		
		return null;
	}
	
	/**
	 * Serializes the data type to XML (nu.xom.Element)
	 * @param d - The data type to be serialized
	 * @return nu.xom.Element representing the component
	 */
	private Element serialize(Datatype d, String type) {
		if(type.equals("cs")){
			Element elmByID = new Element("ByID");
			elmByID.addAttribute( new Attribute("ID", d.getName()) );
			
			boolean isCS = false;
			
			
			List<Component> components = d.getComponents();
			if(components != null){
				for(Component c:d.getComponents()){
					String componentpath = c.getPosition() + "[1]";
					List<ConformanceStatement> css = c.getConformanceStatementList();
					if(css !=null){
						for(ConformanceStatement cs:css){
							Element elmConstraint = new Element("Constraint");
							elmConstraint.addAttribute(new Attribute("ID", cs.getName()));
							elmConstraint.addAttribute(new Attribute("Target", componentpath));
							
							StatementDetails sd = (StatementDetails)cs.getStatementDetails();
							sd.generateStatementText(true, true);
							cs.setDescription(sd.getStatementText());
							
							
							Element elmDescription = new Element("Description");	
							elmDescription.appendChild(cs.getDescription());
							elmConstraint.appendChild(elmDescription);
							
							Element elmAssertion = ((StatementDetails)cs.getStatementDetails()).serializeConstraints(type);
							if(elmAssertion != null) {
								elmConstraint.appendChild(elmAssertion);
								elmByID.appendChild(elmConstraint);
								isCS = true;
							} else {
								System.out.println("Segment ID: " + d.getName());
								System.out.println("Skipped " + type + " ID: " + cs.getName());
								System.out.println("Skipped " + type + " Desc: " + cs.getDescription());
								System.out.println("-------------------------");
							}
						}
					}
					
				}
			}
			
			if(isCS) return elmByID;
		}else if(type.equals("cp")){
			Element elmByID = new Element("ByID");
			elmByID.addAttribute( new Attribute("ID", d.getName()) );
			
			boolean isCS = false;
			
			
			List<Component> components = d.getComponents();
			if(components != null){
				for(Component c:d.getComponents()){
					
					String componentpath = c.getPosition() + "[1]";
					Predicate cp = c.getPredicate();
					if(cp !=null){
						StatementDetails sd = (StatementDetails)cp.getPredicateDetails();
						sd.generateStatementText(true, true);
						cp.setDescription(sd.getStatementText());
						
						Element elmConstraint = new Element("Predicate");
						elmConstraint.addAttribute(new Attribute("ID", "[" + d.getName()+ "]" + componentpath));
						elmConstraint.addAttribute(new Attribute("Target", componentpath));
						elmConstraint.addAttribute(new Attribute("TrueUsage", c.getUsage().getTrueUsage()));
						elmConstraint.addAttribute(new Attribute("FalseUsage", c.getUsage().getFalseUsage()));
						Element elmDescription = new Element("Description");
						elmDescription.appendChild(cp.getDescription());
						elmConstraint.appendChild(elmDescription);
						
						Element elmAssertion = ((StatementDetails)cp.getPredicateDetails()).serializeConstraints(type);
						if(elmAssertion != null) {
							elmConstraint.appendChild(elmAssertion);
							elmByID.appendChild(elmConstraint);
							isCS = true;
						} else {
							System.out.println("Segment ID: " + d.getName());
							System.out.println("Skipped " + type + " ID: " + "[" + d.getName()+ "]" + componentpath);
							System.out.println("Skipped " + type + " Desc: " + cp.getDescription());
							System.out.println("-------------------------");
						}
					}
					
				}
			}
			
			if(isCS) return elmByID;
		}
		
		return null;
	}

}
