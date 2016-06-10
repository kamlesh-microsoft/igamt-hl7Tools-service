package gov.nist.healthcare.hl7tools.service;

import gov.nist.healthcare.hl7tools.domain.CodeTable;
import gov.nist.healthcare.hl7tools.domain.Datatype;
import gov.nist.healthcare.hl7tools.domain.IGLibrary;
import gov.nist.healthcare.hl7tools.domain.Segment;

import java.util.List;

/**
 * 
 * @author Salifou Sidi M. Malick <salifou.sidi@gmail.com>
 *
 */
public interface HL7DBService {
	
	/**
	 * Return the list of supported HL7 versions.
	 * 
	 * @return the list of supported HL7 versions.
	 */
	public List<String> getSupportedHL7Versions();
	
	/**
	 * Return a list of arrays representing the messages for the specified HL7 version.
	 * The first element of the array is the ID, the second the description and the third
	 * the section of the HL7 message.
	 * 
	 * @param hl7Version - the HL7 version
	 * @return the list of messages included in the specified HL7 version
	 * @throws HL7DBServiceException
	 */
	public List<String[]> getMessageListByVersion(String hl7Version) throws HL7DBServiceException;
	
	/**
	 * Build and return the implementation guide object (including all the library)
	 * from the specified list of messages.
	 * 
	 * @param hl7Version - the HL7 version
	 * @param messageList - the list of messages.
	 * @return Build and return the implementation guide object (including all the library)
	 * from the specified list of messages.
	 * @throws HL7DBServiceException
	 */
	public IGLibrary buildIGFromMessageList(String hl7Version, List<String> messageList)
		throws HL7DBServiceException;
	
	/**
	 * Build and return the implementation guide object (including all the library)
	 * from the specified list of events.
	 * 
	 * @param eventList - the list of events.
	 * @param hl7Version - the HL7 version
	 * @return Build and return the implementation guide object (including all the library)
	 * from the specified list of events.
	 * @throws HL7DBServiceException
	 */
	public IGLibrary buildIGFromEventList(String hl7Version, List<String> eventList)
		throws HL7DBServiceException;
	
	/*
	 * Methods needed for global libraries
	 */
	
	/**
	 * Return the list of segments included in the specified HL7 version
	 * 
	 * @param hl7Version
	 * @return the list of segments included in the specified HL7 version
	 * @throws HL7DBServiceException
	 */
	public List<Segment> getSegmentListByVersion(String hl7Version)
		throws HL7DBServiceException;

	/**
	 * Return the list of datatypes included in the specified HL7 version.
	 * 
	 * @param hl7Version
	 * @return the list of datatypes included in the specified HL7 version.
	 * @throws HL7DBServiceException
	 */
	public List<Datatype> getDatatypeListByVersion(String hl7Version)
		throws HL7DBServiceException;

	
	 /**
	 * Return the list of code tables included in the specified HL7 version
	 * 
	 * @param hl7Version
	 * @return the list of code tables included in the specified HL7 version
	 * @throws HL7DBServiceException
	 */
	public List<CodeTable> getCodeTableListByVersion(String hl7Version)
		throws HL7DBServiceException;
	
	/**
	 * Return the message with the specified id
	 * @param hl7Version 
	 * @param messageId
	 * @return the message with the specified id
	 * @throws HL7DBServiceException
	 */
	public gov.nist.healthcare.hl7tools.domain.Message getMessage(
		String hl7Version, String messageId) throws HL7DBServiceException;


	/**
	 *  Return the list of events included in the specified HL7 version
	 *  
	 * @param hl7Version - the HL7 version
	 * @return the list of events included in the specified HL7 version
	 * @throws HL7DBServiceException
	 */
	//public List<Event> getEventListByVersion(String hl7Version) throws HL7DBServiceException;
	
}
