package gov.nist.healthcare.hl7tools.service.util.mock;

import gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Code;
import gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Component;
import gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.DataElement;
import gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Datatype;
import gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Element;
import gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Event;
import gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Field;
import gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Group;
import gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.MessageType;
import gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Segment;
import gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

public class HL7DBMockServiceHelper {

	private final static String TABLE_CLASS = Table.class.getName();
	private final static String DATATYPE_CLASS = Datatype.class.getName();
	private final static String DATA_ELEMENT_CLASS = DataElement.class.getName();
	private final static String SEGMENT_CLASS = Segment.class.getName();
	private final static String MESSAGE_TYPE_CLASS = MessageType.class.getName();
	private final static String GROUP_CLASS = Group.class.getName();
	private final static String ELEMENT_CLASS = Element.class.getName();
	private final static String EVENT_CLASS = Event.class.getName();
	// private final static String INTERACTION_CLASS =
	// "gov.nist.healthcare.igamt.service.util.mock.hl7.domain.Interaction";

	private static JsonFactory factory = new JsonFactory();
	private static ObjectMapper mapper = new ObjectMapper();

	static {
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
	}

	public static <T> List<T> getEntityList(String jsonFile, Class<T> clazz,
			Collection<String> filter) throws Exception {
		JsonParser jp = null;
		try {
			jp = factory.createJsonParser(HL7DBMockDao.class
					.getResourceAsStream("/hl7db/" + jsonFile));
			List<T> result = new ArrayList<T>();
			jp.nextToken();
			String id;
			while (jp.nextToken() == JsonToken.START_OBJECT) {
				T t = mapper.readValue(jp, clazz);
				id = getId(t);
				// gcr added check for filter size.
				if (filter == null || filter.size() == 0 || filter.contains(id))
					result.add(t);
			}
			return result;
		} finally {
			if (jp != null)
				jp.close();
		}
	}

	public static <T> Map<String, T> getEntityMap(String version,
			Class<T> clazz, Collection<String> filter) throws Exception {
		List<T> fullList = getEntityList(version + "/" + getFileName(clazz),
				clazz, filter);
		Map<String, T> result = new HashMap<String, T>();
		String id;
		for (T t : fullList) {
			id = getId(t);
			// gcr added check for filter size.
			if (filter == null || filter.size() == 0 || filter.contains(id))
				result.put(id, t);
		}
		updateChildren(result, version, clazz);
		return result;
	}

	@SuppressWarnings("unchecked")
	private static <T> void updateChildren(Map<String, T> parentMap,
			String version, Class<T> clazz) throws Exception {
		if (TABLE_CLASS.equals(clazz.getName()))
			updateTableCodes(version, (Map<String, Table>) parentMap);
		if (DATATYPE_CLASS.equals(clazz.getName()))
			updateDatatypeComponents(version, (Map<String, Datatype>) parentMap);
		if (SEGMENT_CLASS.equals(clazz.getName()))
			updateSegmentFields(version, (Map<String, Segment>) parentMap);
	}

	private static void updateDatatypeComponents(String version,
			Map<String, Datatype> datatypeMap) throws Exception {
		List<Component> componentList = getEntityList(version
				+ "/components.json", Component.class, null);
		Datatype dt;
		String componentParentDatatype;
		for (Component c : componentList) {
			componentParentDatatype = c.getParentDatatypeId();
			if (datatypeMap.containsKey(componentParentDatatype)) {
				dt = datatypeMap.get(componentParentDatatype);
				if (dt.getComponents() == null)
					dt.setComponents(new ArrayList<Component>());
				dt.getComponents().add(c);
			}
		}
	}

	private static void updateTableCodes(String version,
			Map<String, Table> tableMap) throws Exception {
		List<Code> codeList = getEntityList(version + "/codes.json",
				Code.class, null);
		Table t;
		String tableId;
		for (Code c : codeList) {
			tableId = c.getTableId();
			if (tableMap.containsKey(tableId)) {
				t = tableMap.get(tableId);
				if (t.getCodes() == null)
					t.setCodes(new ArrayList<Code>());
				t.getCodes().add(c);
			}
		}
	}

	private static void updateSegmentFields(String version,
			Map<String, Segment> segmentMap) throws Exception {
		Map<String, DataElement> dataElementMap = getEntityMap(version,
				DataElement.class, null);
		List<Field> fieldList = getEntityList(version + "/fields.json",
				Field.class, null);
		Segment s = null;
		String segmentId;
		for (Field f : fieldList) {
			segmentId = f.getSegmentId();
			if (segmentMap.containsKey(segmentId)) {
				s = segmentMap.get(segmentId);
				if (s.getFields() == null)
					s.setFields(new ArrayList<Field>());
				s.getFields().add(f);
				f.setDataElement(dataElementMap.get(f.getDataElementId()));
			}
		}
	}

	/**
	 * Return a map containing the group defined in the message and the segment
	 * filter
	 * 
	 * @param version
	 * @param messageList
	 * @return a map containing the group defined in the message and the segment
	 *         filter
	 * @throws Exception
	 */
	public static Object[] getGroupMapWithSegmentFilter(String version,
			List<String> messageList) throws Exception {
		List<Group> groupList = getEntityList(version + "/groups.json",
				Group.class, messageList);
		List<String> groupFilter = new ArrayList<String>();
		Map<Integer, Group> groupMap = new HashMap<Integer, Group>();
		for (Group g : groupList) {
			groupFilter.add(g.getId().toString());
			groupMap.put(g.getId(), g);
		}
		Object[] result = { groupMap,
				updateGroupElements(version, groupMap, groupFilter) };
		return result;
	}

	private static Set<String> updateGroupElements(String version,
			Map<Integer, Group> groupMap, List<String> filter) throws Exception {
		List<Element> elementList = getEntityList(version + "/elements.json",
				Element.class, filter);
		Group g;
		Set<String> segmentFilter = new HashSet<String>();
		for (Element e : elementList) {
			g = groupMap.get(e.getParentId());
			if (g.getChildren() == null)
				g.setChildren(new ArrayList<Element>());
			g.getChildren().add(e);
			if (e.getSegmentId() != null)
				segmentFilter.add(e.getSegmentId());
		}
		return segmentFilter;
	}

	private static <T> String getId(T t) {
		if (TABLE_CLASS.equals(t.getClass().getName()))
			return ((Table) t).getId();
		if (DATATYPE_CLASS.equals(t.getClass().getName()))
			return ((Datatype) t).getId();
		if (DATA_ELEMENT_CLASS.equals(t.getClass().getName()))
			return ((DataElement) t).getId();
		if (SEGMENT_CLASS.equals(t.getClass().getName()))
			return ((Segment) t).getId();
		if (MESSAGE_TYPE_CLASS.equals(t.getClass().getName()))
			return ((MessageType) t).getId();
		if (GROUP_CLASS.equals(t.getClass().getName()))
			return ((Group) t).getMessageId();
		if (ELEMENT_CLASS.equals(t.getClass().getName()))
			return ((Element) t).getParentId().toString();
		if (EVENT_CLASS.equals(t.getClass().getName()))
			return ((Event) t).getId().toString();
		return null;
	}

	private static <T> String getFileName(Class<T> clazz) {
		if (TABLE_CLASS.equals(clazz.getName()))
			return "tables.json";
		if (DATATYPE_CLASS.equals(clazz.getName()))
			return "datatypes.json";
		if (DATA_ELEMENT_CLASS.equals(clazz.getName()))
			return "data_elements.json";
		if (SEGMENT_CLASS.equals(clazz.getName()))
			return "segments.json";
		if (MESSAGE_TYPE_CLASS.equals(clazz.getName()))
			return "message_types.json";
		if (EVENT_CLASS.equals(clazz.getName()))
			return "events.json";
		// if(GROUP_CLASS.equals(clazz.getName()))
		// return "groups.json";
		// if(ELEMENT_CLASS.equals(clazz.getName()))
		// return "elements.json";
		return null;
	}
}
