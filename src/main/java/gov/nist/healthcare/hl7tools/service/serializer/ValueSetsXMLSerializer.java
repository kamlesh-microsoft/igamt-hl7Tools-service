package gov.nist.healthcare.hl7tools.service.serializer;

import gov.nist.healthcare.hl7tools.domain.Code;
import gov.nist.healthcare.hl7tools.domain.CodeTable;
import gov.nist.healthcare.hl7tools.domain.Message;

import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Generates the constraints supported by the new validation engine.
 * 
 * @author Jungyub Woo <jungyub.woo@nist.gov>
 */

public class ValueSetsXMLSerializer {
	
	/**
	 * Creates a single value Set from the message model
	 */
	public String serialize(Message m, String type) throws Exception {
		if( m == null ) throw new Exception("The message is null");
		
		HashMap<String,CodeTable> codeTableMap = new HashMap<String,CodeTable>();
		
		for(CodeTable ct: m.getCodeTableSet()){
			
			if(type.equals("HL7")){
				if(ct.getId() == null) codeTableMap.put(ct.getName(), ct);
			}else if(type.equals("Restricted")){
				if(ct.getId() != null) codeTableMap.put(ct.getName(), ct);
			}else{
				codeTableMap.put(ct.getName(), ct);
			}
		}
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		
		docBuilder = docFactory.newDocumentBuilder();

		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("TableLibrary");
		doc.appendChild(rootElement);
		rootElement.setAttribute("xmlns", "http://www.nist.gov/healthcare/data");
		rootElement.setAttribute("TableLibraryIdentifier", "");
		rootElement.setAttribute("Status", "Active");
		rootElement.setAttribute("TableLibraryVersion", "1.0");
		rootElement.setAttribute("OrganizationName", "HL7 NIST");
		rootElement.setAttribute("Name",  "Vocab for " + m.getName());
		rootElement.setAttribute("Description", "Automatically generated vacab for " + m.getName());
		
		for(String key: codeTableMap.keySet()){
			CodeTable ct = codeTableMap.get(key);
			
			Element tableDefinition = doc.createElement("TableDefinition");
			tableDefinition.setAttribute("AlternateId", (ct.getName()==null)?"":ct.getName());
			tableDefinition.setAttribute("Id", (ct.getName()==null)?"":ct.getName());
			tableDefinition.setAttribute("Name", (ct.getDescription()==null)?"":ct.getDescription());
			tableDefinition.setAttribute("Version", (ct.getVersion()==null)?"":ct.getVersion());
			tableDefinition.setAttribute("Codesys", "");
			tableDefinition.setAttribute("Oid", "");
			tableDefinition.setAttribute("Type", "HL7");
			rootElement.appendChild(tableDefinition);
			
			if(ct.getCodes() != null){
				for (Code c: ct.getCodes()){
					Element tableElement = doc.createElement("TableElement");
					tableElement.setAttribute("Code", (c.getValue()==null)?"":c.getValue());
					tableElement.setAttribute("DisplayName", (c.getDescription()==null)?"":c.getDescription());
					tableElement.setAttribute("Codesys", (c.getCodeSystem()==null)?"":c.getCodeSystem().getName());
					tableElement.setAttribute("Source", "Local");
					tableDefinition.appendChild(tableElement);
				}
			}
			
		}

		return XMLManager.docToString(doc);
	}
	
}
