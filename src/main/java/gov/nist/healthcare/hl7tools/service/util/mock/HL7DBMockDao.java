package gov.nist.healthcare.hl7tools.service.util.mock;

import gov.nist.healthcare.hl7tools.domain.CodeTable;
import gov.nist.healthcare.hl7tools.domain.CodeTableLibrary;
import gov.nist.healthcare.hl7tools.domain.Datatype;
import gov.nist.healthcare.hl7tools.domain.DatatypeLibrary;
import gov.nist.healthcare.hl7tools.domain.IGLibrary;
import gov.nist.healthcare.hl7tools.domain.MessageLibrary;
import gov.nist.healthcare.hl7tools.domain.Segment;
import gov.nist.healthcare.hl7tools.domain.SegmentLibrary;
import gov.nist.healthcare.hl7tools.service.HL7DBServiceException;
import gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Event;
import gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Group;
import gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Interaction;
import gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Message;
import gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.MessageType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HL7DBMockDao {

	/**
	 * Return a list of arrays representing the list of messages for the specified HL7 version
	 * The first element of the array is the ID, the second the description and the third the 
	 * section of the HL7 message.
	 * @param version - the HL7 version
	 * @return the list of messages for the specified HL7 version
	 * @throws Exception
	 */
	public static List<String[]> getMessageByVersion(String version) throws Exception {
		List<Message> msgList = HL7DBMockServiceHelper.getEntityList(version+"/messages.json", Message.class, null);
		Map<String, MessageType> msgTypeMap = HL7DBMockServiceHelper.getEntityMap(version, MessageType.class, null);
		MessageType mt;
		List<String[]> result = new ArrayList<String[]>();
		for(Message m : msgList) {
			mt = msgTypeMap.get(m.getId().substring(0, 3));
			String[] msg = { m.getId(), mt != null ? mt.getDescription() : "", m.getSection() };
			result.add(msg);
		}
		return result;
	}
	
	/**
	 * Return the list of events for the specified HL7 version
	 * @param version - the HL7 version
	 * @return the list of events for the specified HL7 version
	 * @throws Exception
	 */
	public static List<Event> getEventByVersion(String version) throws Exception {
		List<Event> eventList = HL7DBMockServiceHelper.getEntityList(version+"/events.json", Event.class, null);
		Map<String, List<Interaction>> interactionMap = getInteractionMap(version);
		// Add the interactions
		for(Event e : eventList) 
			e.setInteractions(interactionMap.get(e.getId()));
		return eventList;
	}
	
	public static IGLibrary buildIGFromMessageList(String version, List<String> messageList) throws Exception {
		return createIG(version, messageList);
	}
	
	 /**
	 * Return the list of code tables included in the specified HL7 version
	 * @param hl7Version
	 * @return the list of code tables included in the specified HL7 version
	 * @throws Exception
	 */
	public static List<CodeTable> getCodeTableByVersion(String hl7Version) throws Exception {
		CodeTableLibrary codeTableLibrary = createCodeTableLibrary(hl7Version, null);
		return codeTableLibrary == null ? null : new ArrayList<CodeTable>(codeTableLibrary.values());
	}
	
	/**
	 * Return the list of datatypes included in the specified HL7 version.
	 * @param hl7Version
	 * @return the list of datatypes included in the specified HL7 version.
	 * @throws Exception
	 */
	public static List<Datatype> getDatatypeByVersion(String hl7Version) throws Exception {
		CodeTableLibrary codeTableLibrary = createCodeTableLibrary(hl7Version, null);
		DatatypeLibrary  datatypeLibrary  = createDatatypeLibrary(hl7Version, null, codeTableLibrary);
		return datatypeLibrary == null ? null : new ArrayList<Datatype>(datatypeLibrary.values());
	}
	
	/**
	 * Return the list of segments included in the specified HL7 version
	 * @param hl7Version
	 * @return the list of segments included in the specified HL7 version
	 * @throws HL7DBServiceException
	 */
	public static List<Segment> getSegmentByVersion(String hl7Version) throws Exception {
		CodeTableLibrary codeTableLibrary = createCodeTableLibrary(hl7Version, null);
		DatatypeLibrary  datatypeLibrary  = createDatatypeLibrary(hl7Version, null, codeTableLibrary);
		SegmentLibrary   segmentLibrary   = createSegmentLibrary(hl7Version, null, codeTableLibrary, datatypeLibrary);
		return segmentLibrary == null ? null : new ArrayList<Segment>(segmentLibrary.values());
	}
	
	public static gov.nist.healthcare.hl7tools.domain.Message getMessageByIdAndVersion(String id, String version) throws Exception {
		List<String> messageList = Arrays.asList(id);
		IGLibrary ig = createIG(version, messageList);
		gov.nist.healthcare.hl7tools.domain.Message m = ig.getMessageLibrary().get(id);
		ig = null;
		return m;
	}

	private static Map<String, List<Interaction>> getInteractionMap(String version) throws Exception {		
		List<Interaction> iList = HL7DBMockServiceHelper.getEntityList(version+"/interactions.json", Interaction.class, null);
		Map<String, List<Interaction>> result = new HashMap<String, List<Interaction>>();
		for(Interaction i: iList){
			if(!result.containsKey(i.getEventId()))
				result.put(i.getEventId(), new ArrayList<Interaction>());
			result.get(i.getEventId()).add(i);
		}
		return result;
	}
	
	private static CodeTableLibrary createCodeTableLibrary(String version, List<String> filter) throws Exception{
		Map<String, gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Table> tableMap = 
				HL7DBMockServiceHelper.getEntityMap(version, gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Table.class, filter);
		return CodeTableLibraryBuilder.build(tableMap);
	}
	
	private static DatatypeLibrary createDatatypeLibrary(String version, List<String> filter, 
			CodeTableLibrary codeTableLibrary) throws Exception{
		Map<String, gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Datatype> tableMap = 
				HL7DBMockServiceHelper.getEntityMap(version, gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Datatype.class, filter);
		return DatatypeLibraryBuilder.build(codeTableLibrary, tableMap);
	}
	
	private static SegmentLibrary createSegmentLibrary(String version, Collection<String> filter, CodeTableLibrary codeTableLibrary, 
			DatatypeLibrary datatypeLibrary ) throws Exception{
		Map<String, gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Segment> segMap = 
				HL7DBMockServiceHelper.getEntityMap(version, gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Segment.class, filter);
		return SegmentLibraryBuilder.build(codeTableLibrary, datatypeLibrary, segMap);
	}
	
	@SuppressWarnings("unchecked")
	private static IGLibrary createIG(String version, List<String> messageList) throws Exception{
		Object [] groupMapWithSegmentFilter = HL7DBMockServiceHelper.getGroupMapWithSegmentFilter(version, messageList);
		Map<Integer, gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Group> groupMap = (Map<Integer, Group>) groupMapWithSegmentFilter[0];
		// Segments of interest
		Set<String> segmentFilter = (Set<String>) groupMapWithSegmentFilter[1];
		
		//TODO: include filters
		CodeTableLibrary codeTableLibrary = createCodeTableLibrary(version, null);
		DatatypeLibrary  datatypeLibrary  = createDatatypeLibrary(version, null, codeTableLibrary);
		SegmentLibrary   segmentLibrary   = createSegmentLibrary(version, segmentFilter, codeTableLibrary, datatypeLibrary);
		MessageLibrary   messageLibrary   =  createMessageLibrary(groupMap, segmentLibrary);
		updateMessageDescription(version, messageList, messageLibrary);
		updateMessageEventDescription(version, messageList, messageLibrary);
		return new IGLibrary(codeTableLibrary, datatypeLibrary, segmentLibrary, messageLibrary);
		
	}
	
	private static MessageLibrary createMessageLibrary(Map<Integer, gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Group> map, SegmentLibrary segmentLibrary) throws Exception {
		return MessageLibraryBuilder.build(segmentLibrary, map);
	}
	
	private static void updateMessageDescription(String version, List<String> messageList, MessageLibrary library) throws Exception{
		List<String> msgTypefilter = createMessageTypeFilter(messageList);
		Map<String, MessageType> msgTypeMap = HL7DBMockServiceHelper.getEntityMap(version, MessageType.class, msgTypefilter);
		for(gov.nist.healthcare.hl7tools.domain.Message m: library.values()) {
			m.setDescription(msgTypeMap.get( m.getMessageType() /*m.getName().substring(0, 3)*/).getDescription());
		}	
	}
	
	private static void updateMessageEventDescription(String version, List<String> messageList, MessageLibrary library) throws Exception{
		List<String> eventFilter = createEventFilter(messageList);
		Map<String, Event> eventMap = HL7DBMockServiceHelper.getEntityMap(version, Event.class, eventFilter);
		for(gov.nist.healthcare.hl7tools.domain.Message m: library.values()) {
			String event = m.getEvent();//  m.getName().length() > 4 ? m.getName().substring(4) : "";
			if(eventMap.containsKey(event))
				m.setEventDescription(eventMap.get(event).getDescription());
		}	
	}
	
	private static List<String> createMessageTypeFilter(List<String> messageList) {
		List<String> filter = new ArrayList<String>(messageList.size());
		for(String s: messageList)
			filter.add(s.substring(0, 3));
		return filter;
	}
	
	private static List<String> createEventFilter(List<String> messageList) {
		List<String> filter = new ArrayList<String>(messageList.size());
		for(String s: messageList)
			if(s.length() > 4)
				filter.add(s.substring(4));
		return filter;
	}
}
