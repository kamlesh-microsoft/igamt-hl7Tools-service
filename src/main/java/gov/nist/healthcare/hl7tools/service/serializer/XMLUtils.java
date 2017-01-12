package gov.nist.healthcare.hl7tools.service.serializer;

import gov.nist.healthcare.hl7tools.domain.EBoolean;
import gov.nist.healthcare.hl7tools.domain.ElementType;

import java.io.InputStream;
import nu.xom.Element;

public class XMLUtils {
	
	public static boolean isSchemaPriorToV29(ProfileSchemaVersion version) {
		return version == ProfileSchemaVersion.V25 || /*version == ProfileSchemaVersion.V27 ||*/ 
				version == ProfileSchemaVersion.V271 || version == ProfileSchemaVersion.V28;
	}
	
	public static InputStream getSchema(ProfileSchemaVersion version) {
		switch (version) {
			case V29: return XMLUtils.class.getResourceAsStream("/profile/xsd/v29.xsd");
			case V28: return null;
			//case V27: //	return null;
			case V25: return null;
			default: throw new IllegalArgumentException("[Error] Conformance Profile Schema for the version '"+version+"' doesn't exist");
		}
	}
	
	public static String maxLength(Element e, ProfileSchemaVersion schemaVersion) {
		return schemaVersion == ProfileSchemaVersion.V25 ? e.getAttributeValue("Length"): e.getAttributeValue("MaxLength");
	}
	
	public static int minLength(Element e, ProfileSchemaVersion schemaVersion) {
		String minLen = e.getAttributeValue("MinLength");
		return minLen == null || schemaVersion == ProfileSchemaVersion.V25 ? -1 : new Integer( minLen );
	}
	
	public static String confLength(Element e, ProfileSchemaVersion schemaVersion) {
		String confLength = e.getAttributeValue("ConformanceLength");
		return confLength == null || schemaVersion == ProfileSchemaVersion.V25 ? "" :confLength;
	}
	
	public static EBoolean truncation(Element e, ProfileSchemaVersion schemaVersion) {
		String trunc = e.getAttributeValue("Truncation");
		return trunc == null || schemaVersion == ProfileSchemaVersion.V25 ? EBoolean.NA : EBoolean.valueOf( "true".equalsIgnoreCase(trunc) ? "YES" : "no".equalsIgnoreCase(trunc) ? "NO" : "NA");
	}
	
	public static String tagName( gov.nist.healthcare.hl7tools.domain.Element e, ProfileSchemaVersion schemaVersion ) {
		if(e.getType() == ElementType.SEGEMENT)
			return "Segment";
		if( e.getType() == ElementType.GROUP ) 
			return schemaVersion == ProfileSchemaVersion.V29 ? "Group" : "SegGroup";
		if( isSchemaPriorToV29(schemaVersion) )
			throw new IllegalStateException("Choice is not supported the version '"+schemaVersion+"' of the conformance profile schema.");
		return "Choice";
	}
	
	/*
	public static void documentToStream( OutputStream out, Document document , boolean format) throws IOException {
		if( format ) {
			Serializer serializer = new Serializer(out, "UTF-8");
	        serializer.setIndent(4);
	        serializer.setMaxLength(400);
	        serializer.write( document );
		} else {
			Canonicalizer canonicalizer = new Canonicalizer(out);
			canonicalizer.write( document );
		}
	}*/
	

}
