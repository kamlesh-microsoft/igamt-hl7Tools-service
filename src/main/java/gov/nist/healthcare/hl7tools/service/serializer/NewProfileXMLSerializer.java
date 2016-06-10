package gov.nist.healthcare.hl7tools.service.serializer;

import gov.nist.healthcare.hl7tools.domain.CodeTable;
import gov.nist.healthcare.hl7tools.domain.Component;
import gov.nist.healthcare.hl7tools.domain.Datatype;
import gov.nist.healthcare.hl7tools.domain.DatatypeLibrary;
import gov.nist.healthcare.hl7tools.domain.EBoolean;
import gov.nist.healthcare.hl7tools.domain.ElementType;
import gov.nist.healthcare.hl7tools.domain.Field;
import gov.nist.healthcare.hl7tools.domain.IGLibrary;
import gov.nist.healthcare.hl7tools.domain.Message;
import gov.nist.healthcare.hl7tools.domain.MessageLibrary;
import gov.nist.healthcare.hl7tools.domain.Segment;
import gov.nist.healthcare.hl7tools.domain.SegmentLibrary;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import nu.xom.Attribute;
import nu.xom.Element;

/**
 * Generates the profile supported by the new validation engine.
 * 
 * @author Salifou Sidi M. Malick <salifou.sidi@gmail.com>
 */

public class NewProfileXMLSerializer {
	private static String defaultBSBL = "#R#1";
	private static String defaultBS = "#R";
	
	/**
	 * Creates a single profile containing all artifacts from the library
	 * @param l - The library
	 * @return A single profile containing all artifacts from the library
	 * @throws Exception 
	 */
	public Element serialize(IGLibrary l) throws Exception {
		if( l == null )
			throw new Exception("The library is null");
		
		MessageLibrary  ml = l.getMessageLibrary();
		if( ml == null )
			throw new Exception("The message library is null");
		
		SegmentLibrary  sl = l.getSegmentLibrary();
		if( sl == null )
			throw new Exception("The segment library is null");
		
		DatatypeLibrary dl = l.getDatatypeLibrary();
		if( dl == null )
			throw new Exception("The data type library is null");
		
		Element e = new Element("ConformanceProfile");
		//FIXME: The ID need to be fixed in IGAMT, People usually use OIDs
		e.addAttribute( new Attribute("ID", UUID.randomUUID().toString()) );
		
		Element ms = new Element("Messages");
		for(Message m: ml.values())
			ms.appendChild( _serialize(m) );
		e.appendChild(ms);
		
		Element ss = new Element("Segments");
		for(Segment s: sl.values())
			ss.appendChild( serialize(s) );
		e.appendChild(ss);
		
		Element ds = new Element("Datatypes");
		for(Datatype d: dl.values())
			ds.appendChild( serialize(d) );
		e.appendChild(ds);
		
		//The profile do not contain the table library but this may change in the future
		return e;
	}
	
	/**
	 * Creates a single profile from the message model
	 * @param m - The message model
	 * @return A single profile containing all artifacts from the library
	 * @throws Exception 
	 */
	public Element serialize(Message m) throws Exception {
		if( m == null ) throw new Exception("The message is null");
		
		Element e = new Element("ConformanceProfile");
		//FIXME: The ID need to be fixed in IGAMT, it has to be something 
		// predictable since it will be used while writing constrains
		e.addAttribute( new Attribute("ID", UUID.randomUUID().toString()) );
		
		Element ms = new Element("Messages");
		ms.appendChild( _serialize(m) );
		e.appendChild(ms);
		
		Element ss = new Element("Segments");
		for(Segment s: m.getSegmentSet())
			ss.appendChild( serialize(s) );
		e.appendChild(ss);
		
		Element ds = new Element("Datatypes");
		
		Map<String, Datatype> datatypeMap = new HashMap<String, Datatype>();
		for(Datatype d: m.getDatatypeSet()){
			datatypeMap.put(d.getName(), d);
		}
		
		for (Map.Entry<String, Datatype> entry : datatypeMap.entrySet()) {
			ds.appendChild( serialize(entry.getValue()) );
		}
		e.appendChild(ds);
		
		return e;
	}
	
	/**
	 * Serializes the message to XML (nu.xom.Element)
	 * @param m - The message to be serialized
	 * @return nu.xom.Element representing the component
	 * @throws Exception 
	 */
	private Element _serialize(Message m) throws Exception {
		Element e = new Element("Message");
		//FIXME: [Salifou] I will use UUID as the ID in the profile for now ...
		//FIXME getId() will return null if the artifact is from the DB
		e.addAttribute( new Attribute("ID", UUID.randomUUID().toString()) ); 
		e.addAttribute( new Attribute("Type", m.getMessageType()) );
		e.addAttribute( new Attribute("Event", m.getEvent() == null  || "".equals(m.getEvent())? m.getMessageType() : m.getEvent()) );
		e.addAttribute( new Attribute("StructID", m.getName()) );
		e.addAttribute( new Attribute("Description", m.getDescription()) );
		if( m.getChildren() != null )
			for( gov.nist.healthcare.hl7tools.domain.Element c: m.getChildren() )
				e.appendChild( serialize(c) );
		return e;
	}
	
	/**
	 * Returns the expected tag name of the element
	 */
	private String getElementTagName(gov.nist.healthcare.hl7tools.domain.Element e) throws Exception {
		if( e.getType() == ElementType.SEGEMENT )
			return "Segment";
		else if( e.getType() == ElementType.GROUP )
			return "Group";
		else throw new Exception("Choice is not allowed in the profile");
	}
	
	/**
	 * Serializes the element to XML (nu.xom.Element)
	 * @param e - The element to be serialized
	 * @return nu.xom.Element representing the component
	 * @throws Exception 
	 */
	private Element serialize(gov.nist.healthcare.hl7tools.domain.Element e) throws Exception {
		String tagName = getElementTagName(e);
		Element t = new Element(tagName);
		if( e.getSegment() != null )
			t.addAttribute( new Attribute("Ref", e.getSegment().getName()) );
		else
			t.addAttribute( new Attribute("Name", e.getName()) );
		t.addAttribute( new Attribute("Usage", e.getUsage().getValue().startsWith("C") ? "C": e.getUsage().getValue()) );
		t.addAttribute( new Attribute("Min", e.getMin()+"") );
		t.addAttribute( new Attribute("Max", e.getMax()+"") );
		if( e.getChildren() != null )
			for( gov.nist.healthcare.hl7tools.domain.Element ee: e.getChildren() )
				t.appendChild( serialize(ee) );
		return t;
	}
	
	/**
	 * Serializes the segment to XML (nu.xom.Element)
	 * @param s - The segment to be serialized
	 * @return nu.xom.Element representing the component
	 */
	private Element serialize(Segment s) {
		Element e = new Element("Segment");
		e.addAttribute( new Attribute("ID", s.getName()) );            //FIXME: This is little bit confusing ...
		String name = s.getRoot() != null ? s.getRoot() : s.getName(); //FIXME: This is little bit confusing ...
		e.addAttribute( new Attribute("Name", name) ); 
		e.addAttribute( new Attribute("Description", s.getDescription()) );
		//FIXME: Handle Dynamic mapping. This needs to be implemented in IGAMT
		if( s.getFields() != null )
			for(Field f: s.getFields())
				e.appendChild( serialize(f) );
		return e;
	}
	
	/**
	 * Serializes the field to XML (nu.xom.Element)
	 * @param f - The field to be serialized
	 * @return nu.xom.Element representing the component
	 */
	private Element serialize(Field f) {
		Element e = new Element("Field");
		e.addAttribute( new Attribute("Name", f.getDescription()) );
		e.addAttribute( new Attribute("Usage", f.getUsage().getValue().startsWith("C") ? "C": f.getUsage().getValue()) );
		e.addAttribute( new Attribute("Min", f.getMin()+"") );
		e.addAttribute( new Attribute("Max", f.getMax()+"") );
		e.addAttribute( new Attribute("Datatype", f.getDatatype().getName()) );
		int minLength = f.getMinLength() == -1 ? 1 : f.getMinLength() ;
		e.addAttribute( new Attribute("MinLength", minLength+ "") );
		String maxLen = f.getMaxLength();
		e.addAttribute( new Attribute("MaxLength", maxLen == null || maxLen.equals("") ? "*" : maxLen) );
		String cLen = genConfLen(f.getConfLength(), f.getTruncationAllowed()) ;
		if( !"".equals(cLen) )
			e.addAttribute( new Attribute("ConfLength", cLen) );
		CodeTable table = f.getCodeTable();
		if( table != null ){
			if(f.getDatatype().getComponents() != null && f.getDatatype().getComponents().size() > 1){
				e.addAttribute( new Attribute("Table", table.getName() + NewProfileXMLSerializer.defaultBSBL) );
			}else {
				e.addAttribute( new Attribute("Table", table.getName() + NewProfileXMLSerializer.defaultBS) );
			}
		}
		String itemNo = f.getItemNo();
		if(itemNo != null)
			e.addAttribute( new Attribute("ItemNo", itemNo) );
		return e;
	}
	
	/**
	 * Serializes the data type to XML (nu.xom.Element)
	 * @param d - The data type to be serialized
	 * @return nu.xom.Element representing the component
	 */
	private Element serialize(Datatype d) {
		Element e = new Element("Datatype");
		e.addAttribute( new Attribute("ID", d.getName()) );            //FIXME: This is little bit confusing ...
		String name = d.getRoot() != null ? d.getRoot() : d.getName(); //FIXME: This is little bit confusing ...
		e.addAttribute( new Attribute("Name", name) ); 
		e.addAttribute( new Attribute("Description", d.getDescription()) );
		if( d.getComponents() != null )
			for( Component c : d.getComponents())
				e.appendChild( serialize(c) );
		return e;
	}
	
	/**
	 * Serializes the component to XML (nu.xom.Element)
	 * @param c - The component to be serialized
	 * @return nu.xom.Element representing the component
	 */
	private Element serialize(Component c) {
		Element e = new Element("Component");
		e.addAttribute( new Attribute("Name", c.getDescription()) );
		e.addAttribute( new Attribute("Usage", c.getUsage().getValue().startsWith("C") ? "C": c.getUsage().getValue()) );
		e.addAttribute( new Attribute("Datatype", c.getDatatype().getName()) );
		int minLength = c.getMinLength() == -1 ? 1  : c.getMinLength() ;
		e.addAttribute( new Attribute("MinLength", minLength+ "") );
		String maxLen = c.getMaxLength();
		e.addAttribute( new Attribute("MaxLength", maxLen == null  || "".equals(maxLen)? "*" : maxLen) );
		String cLen = genConfLen(c.getConfLength(), c.getTruncationAllowed()) ;
		if( !"".equals(cLen) )
			e.addAttribute( new Attribute("ConfLength", cLen) );
		CodeTable table = c.getCodeTable();
		if( table != null )
			if(c.getDatatype().getComponents() != null && c.getDatatype().getComponents().size() > 1){
				e.addAttribute( new Attribute("Table", table.getName() + NewProfileXMLSerializer.defaultBSBL) );
			}else {
				e.addAttribute( new Attribute("Table", table.getName() + NewProfileXMLSerializer.defaultBS) );
			}
		return e;
	}
	
	private String genConfLen(int clen, EBoolean b) {
		String t = clen != -1 ? clen + "" : "";
		if( b == EBoolean.YES && clen != -1 )
			return t + "#";
		else 	if( b == EBoolean.YES && clen != -1 )
			return t + "=";
		return t;
	}

}
