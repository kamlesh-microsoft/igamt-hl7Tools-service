package gov.nist.healthcare.hl7tools.service;

import gov.nist.healthcare.hl7tools.domain.CodeTable;
import gov.nist.healthcare.hl7tools.domain.Datatype;
import gov.nist.healthcare.hl7tools.domain.Message;
import gov.nist.healthcare.hl7tools.domain.Segment;

public interface TransformerService {
	
	/**
	 * Generates DITA from the message
	 * @param message - the message
	 * @return the DITA
	 */
	public String toDita( Message message );
	
	/**
	 * Generates DITA from the segment
	 * @param segment - the segment
	 * @return the DITA
	 */
	public String toDita( Segment segment );
	
	
	/**
	 * Generates DITA from the datatype
	 * @param datatype the datatype
	 * @return the DITA
	 */
	public String toDita( Datatype datatype );
	
	
	/**
	 * Generates DITA from the code table
	 * @param codeTable - the code table
	 * @return the DITA
	 */
	public String toDita( CodeTable codeTable );
	
	/**
	 * Generates HTML from the message
	 * @param message - the message
	 * @return the HTML
	 */
	public String toHtml( Message message );
	
	/**
	 * Generates HTML from the segment
	 * @param segment - the segment
	 * @return the HTML
	 */
	public String toHtml( Segment segment );
	
	
	/**
	 * Generates HTML from the datatype
	 * @param datatype the datatype
	 * @return the HTML
	 */
	public String toHtml( Datatype datatype );
	
	
	/**
	 * Generates HTML from the code table
	 * @param codeTable - the code table
	 * @return the HTML
	 */
	public String toHtml( CodeTable codeTable );

}
