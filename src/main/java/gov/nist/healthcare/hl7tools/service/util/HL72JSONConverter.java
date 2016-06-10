package gov.nist.healthcare.hl7tools.service.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Code;
import gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Component;
import gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.DataElement;
import gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Datatype;
import gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Element;
import gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Field;
import gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Group;
import gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Message;
import gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.MessageType;
import gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Segment;
import gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Table;
import gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Usage;

// gcr

@org.springframework.stereotype.Component
public class HL72JSONConverter implements Runnable {

	Logger log = LoggerFactory.getLogger(HL72JSONConverter.class);

	public final static File OUTPUT_DIR = new File("src/main/resources/hl7db");

	static JdbcTemplate jdbcTemplate;

	String hl7Version;

	static File versionDir;

	ObjectMapper mapper = new ObjectMapper();

	int groupIncr;
	int componentIncr;
	int fieldIncr;
	int elementIncr;

	Map<String, Group> grpByMsg = new HashMap<String, Group>();
	Map<String, Group> grpByName = new HashMap<String, Group>();

	public HL72JSONConverter(String hl7Version) {
		try {
			SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
			dataSource.setDriver(new com.mysql.jdbc.Driver());
			dataSource.setUrl("jdbc:mysql://localhost/mdb");
			dataSource.setUsername("root");

			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			this.jdbcTemplate = jdbcTemplate;
			this.hl7Version = hl7Version;
			mapper.setSerializationInclusion(Include.NON_NULL);
			mapper.addMixInAnnotations(Group.class, GroupMixIn.class);
			mapper.addMixInAnnotations(Component.class, ComponentMixIn.class);
			mapper.addMixInAnnotations(Field.class, FieldMixIn.class);
			mapper.addMixInAnnotations(Code.class, CodeMixIn.class);
			mapper.addMixInAnnotations(DataElement.class, DataElementMixIn.class);
		} catch (SQLException e) {
			log.error("", e);
		}
	}

	public void run() {
		if (!OUTPUT_DIR.exists()) {
			OUTPUT_DIR.mkdir();
		}
		versionDir = new File(OUTPUT_DIR, hl7Version);
		if (!versionDir.exists()) {
			versionDir.mkdir();
		}

		log.info("Doing MessageType...");
		Helper<MessageType> hlpMessageType = new Helper<MessageType>();
		List<MessageType> messageTypes = (hlpMessageType.fetch(SQL4_MESSAGETYPE(), RM4_MESSAGETYPE));
		hlpMessageType.write(messageTypes, "message_types");

		log.info("Doing Message...");
		Helper<Message> hlpMessage = new Helper<Message>();
		List<Message> messages = (hlpMessage.fetch(SQL4_MESSAGE(), RM4_MESSAGE));
		hlpMessage.write(messages, "messages");

		log.info("Doing Group...");
		Helper<Group> hlpGroup = new Helper<Group>();
		List<Group> groups = (hlpGroup.fetch(SQL4_GROUP(), RM4_GROUP));
		List<Group> groups1 = assembleGroups(groups);
		hlpGroup.write(groups1, "groups");

		log.info("Doing Segment...");
		Helper<Segment> hlpSegment = new Helper<Segment>();
		List<Segment> segments = (hlpSegment.fetch(SQL4_SEGMENT(), RM4_SEGMENT));
		hlpSegment.write(segments, "segments");

		log.info("Doing Datatype...");
		Helper<Datatype> hlpDatatype = new Helper<Datatype>();
		List<Datatype> datatypes = (hlpDatatype.fetch(SQL4_DATATYPE(), RM4_DATATYPE));
		hlpDatatype.write(datatypes, "datatypes");

		log.info("Doing Component...");
		Helper<Component> hlpComponent = new Helper<Component>();
		List<Component> components = (hlpComponent.fetch(SQL4_COMPONENT(), RM4_COMPONENT));
		hlpComponent.write(components, "components");

		log.info("Doing Field...");
		Helper<Field> hlpField = new Helper<Field>();
		List<Field> fields = (hlpField.fetch(SQL4_FIELD(), RM4_FIELD));
		hlpField.write(fields, "fields");

		log.info("Doing Code...");
		Helper<Code> hlpCode = new Helper<Code>();
		List<Code> codes = (hlpCode.fetch(SQL4_CODE(), RM4_CODE));
		hlpCode.write(codes, "codes");

		log.info("Doing DataElement...");
		Helper<DataElement> hlpDataElement = new Helper<DataElement>();
		List<DataElement> dataelements = (hlpDataElement.fetch(SQL4_DATAELEMENT(), RM4_DATAELEMENT));
		hlpDataElement.write(dataelements, "data_elements");

		log.info("Doing Table...");
		Helper<Table> hlpTable = new Helper<Table>();
		List<Table> tables = (hlpTable.fetch(SQL4_TABLE(), RM4_TABLE));
		hlpTable.write(tables, "tables");

		log.info("Doing Element...");
		Helper<Element> hlpElement = new Helper<Element>();
		Helper<InterimElement> hlpInterimElement = new Helper<InterimElement>();
		List<InterimElement> interimelements = (hlpInterimElement.fetch(SQL4_ELEMENT(), RM4_ELEMENT));
		List<Element> elements = postProcessElements(interimelements);
		hlpElement.write(elements, "elements");

		log.info("...Done");

	}
	
	List<Element> postProcessElements(List<InterimElement> ies) {
		List<Element> rval = new ArrayList<Element>();
		GroupStacker stk = new GroupStacker();
		for(InterimElement ie : ies) {
			switch (stk.waw(grpByName.get(ie.getGroupName()))) {
				case 1: {
					ie.setParentId(grpByName.get(ie.getGroupName()).getId());
					rval.add((Element)ie);
					break;
				}
				case 0: {
					ie.setGroupId(grpByMsg.get(ie.messageStucture).getId());
					rval.add((Element)ie);
					break;
				}
				case -1: {
					break;
				}
				default: {
					break;
				}
			}
		}
		return rval;
	}

	List<Group> assembleGroups(List<Group> groups) {
		String currentGroup = null;
		List<Group> rval = new ArrayList<Group>();
		for (Group group : groups) {
			if (!group.getName().equals(currentGroup)) {
				if (group.isRoot()) {
					currentGroup = null;
				} else {
					currentGroup = group.getName();
				}
				rval.add(group);
				this.grpByMsg.put(group.getMessageId(), group);
				this.grpByName.put(group.getName(), group);
			}
		}
		return rval;
	}

	public static void main(String[] args) {
		HL72JSONConverter app = new HL72JSONConverter(args[0]);
		app.run();
	}

	public class Helper<T> {

		void write(List<T> list, String fileName) {
			File outfile = new File(versionDir, fileName + ".json");
			try {
				Writer writer = new FileWriter(outfile);
				mapper.writerWithDefaultPrettyPrinter().writeValue(writer, list);
			} catch (IOException e) {
				e.printStackTrace();
				log.error("", e);
			}
		}

		List<T> fetch(String sqlSelect, RowMapper<T> rMapper) {
			List<T> list = jdbcTemplate.query(sqlSelect, rMapper);
			return list;
		}
	}
	
	class InterimElement extends Element {
		String groupName;
		String groupState;
		String messageStucture;
		
		public String getGroupName() {
			return groupName;
		}
		public void setGroupName(String groupName) {
			this.groupName = groupName;
		}
		public String getGroupState() {
			return groupState;
		}
		public void setGroupState(String groupState) {
			this.groupState = groupState;
		}
		public String getMessageStucture() {
			return messageStucture;
		}
		public void setMessageStucture(String messageStucture) {
			this.messageStucture = messageStucture;
		}
	}
	
	class GroupStacker {
		
		Stack<Group> stack = new Stack<Group>();
		
		int waw(Group group) {
			if (group.getName() == null || group.getName().trim().length() == 0) {
				return 0;
			}
			if(group.getName().equals(stack.peek().getName())) {
				stack.pop();
				return -1;
			} else {
				stack.push(group);
				return 1;
			}
		}
	}

	String SQL4_MESSAGETYPE() {
		StringBuilder bld = new StringBuilder();
		bld.append("SELECT m.message_type, m.description, m.section");
		bld.append(" FROM hl7messagetypes m, hl7versions v");
		bld.append(" WHERE m.version_id = v.version_id");
		bld.append(" AND v.hl7_version = ");
		bld.append("'");
		bld.append(hl7Version);
		bld.append("'");
		bld.append(";");
		return bld.toString();
	}

	final RowMapper<MessageType> RM4_MESSAGETYPE = new RowMapper<MessageType>() {

		public MessageType mapRow(ResultSet rs, int rowNum) throws SQLException {
			MessageType messageType = new MessageType();
			messageType.setId(rs.getString("message_type"));
			messageType.setDescription(rs.getString("description"));
			messageType.setSection(rs.getString("section"));

			return messageType;
		}
	};

	String SQL4_MESSAGE() {
		StringBuilder bld = new StringBuilder();
		bld.append("SELECT m.message_structure");
		bld.append(" FROM hl7msgstructids m, hl7versions v");
		bld.append(" WHERE m.version_id = v.version_id");
		bld.append(" AND v.hl7_version = ");
		bld.append("'");
		bld.append(hl7Version);
		bld.append("'");
		bld.append(";");
		return bld.toString();
	}

	final RowMapper<Message> RM4_MESSAGE = new RowMapper<Message>() {

		public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
			Message message = new Message();
			message.setId(rs.getString("message_structure"));
			return message;
		}
	};

	String SQL4_GROUP() {
		StringBuilder bld = new StringBuilder();
		bld.append("SELECT m.`message_structure`, m.`seq_no`, m.`groupname`, m.`seg_code`");
		// bld.append(" FROM hl7msgstructidsegments m, hl7versions v");
		bld.append(" FROM hl7versions v INNER JOIN hl7msgstructidsegments m ON v.version_id = m.version_id");
		bld.append(" WHERE v.`hl7_version` = ");
		bld.append("'");
		bld.append(hl7Version);
		bld.append("'");
		bld.append(" AND m.`version_id` = v.`version_id`");
		bld.append(" AND (m.`seq_no` = 1 || length(m.`groupname`) > 0)");
		bld.append(" ORDER BY m.`message_structure`, m.`seq_no`");
		bld.append(";");
		String rval = bld.toString();
		return rval;
	}

	final RowMapper<Group> RM4_GROUP = new RowMapper<Group>() {

		public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
			Group grp = new Group();
			grp.setId(++groupIncr);
			grp.setRoot((rs.getInt("seq_no") == 1) ? true : false);
			grp.setChoice((rs.getString("seg_code") == "<") ? true : false);
			grp.setMessageId(rs.getString("message_structure"));
			grp.setName((grp.isRoot() ? (rs.getString("message_structure") + ".ROOT") : rs.getString("groupname")));
			return grp;
		}
	};

	String SQL4_SEGMENT() {
		StringBuilder bld = new StringBuilder();
		bld.append("SELECT s.seg_code, s.description, s.section");
		bld.append(" FROM hl7versions v INNER JOIN hl7segments s ON v.version_id = s.version_id");
		bld.append(" WHERE v.`hl7_version` = ");
		bld.append("'");
		bld.append(hl7Version);
		bld.append("'");
		bld.append(" AND s.visible = 'TRUE'");
		bld.append(" ORDER BY s.seg_code");
		bld.append(";");
		String rval = bld.toString();
		return rval;
	}

	final RowMapper<Segment> RM4_SEGMENT = new RowMapper<Segment>() {

		public Segment mapRow(ResultSet rs, int rowNum) throws SQLException {
			Segment seg = new Segment();
			seg.setId(rs.getString("seg_code"));
			seg.setDescription(rs.getString("description"));
			seg.setSection(rs.getString("section"));
			return seg;
		}
	};

	String SQL4_FIELD() {
		StringBuilder bld = new StringBuilder();
		bld.append("SELECT s.`seg_code`, s.`data_item`, s.`req_opt`, s.`seq_no`");
		bld.append(" FROM hl7segmentdataelements s INNER JOIN hl7versions v ON v.version_id = s.version_id");
		bld.append(" WHERE v.`hl7_version` = ");
		bld.append("'");
		bld.append(hl7Version);
		bld.append("'");
		bld.append(" ORDER BY s.`seg_code`");
		bld.append(";");
		String rval = bld.toString();
		return rval;
	}

	final RowMapper<Field> RM4_FIELD = new RowMapper<Field>() {

		public Field mapRow(ResultSet rs, int rowNum) throws SQLException {
			Field fld = new Field();
			fld.setId(++fieldIncr);
			fld.setSegmentId(rs.getString("seg_code"));
			String s = "0" + Integer.toString(rs.getInt("data_item"));
			fld.setDataElementId(s);
			fld.setPosition(rs.getInt("seq_no"));
			String s1 = rs.getString("req_opt").substring(0, 1);
			fld.setUsage(Usage.valueOf(s1));
			fld.setMin(0);
			fld.setMax("1");
			return fld;
		}
	};

	String SQL4_DATATYPE() {
		StringBuilder bld = new StringBuilder();
		bld.append("SELECT d.data_structure, d.`description`, d.`section`, d.`elementary`");
		bld.append(" FROM hl7datastructures d, hl7versions v");
		bld.append(" WHERE v.`hl7_version` = ");
		bld.append("'");
		bld.append(hl7Version);
		bld.append("'");
		bld.append(" AND d.`version_id` = v.`version_id`");
		bld.append(" ORDER BY d.data_structure");
		bld.append(";");
		String rval = bld.toString();
		return rval;
	}

	final RowMapper<Datatype> RM4_DATATYPE = new RowMapper<Datatype>() {

		public Datatype mapRow(ResultSet rs, int rowNum) throws SQLException {
			Datatype dt = new Datatype();
			dt.setId(rs.getString("data_structure"));
			dt.setDescription(rs.getString("description"));
			dt.setPrimitive(rs.getBoolean("elementary"));
			String s = rs.getString("section");
			dt.setSection(s.length() > 0 ? s : null);
			return dt;
		}
	};

	String SQL4_COMPONENT() {
		StringBuilder bld = new StringBuilder();
		// bld.append("SET @rownum = 0;");
		bld.append(
				"SELECT d.`data_structure`, c.`data_type_code`, c.`description`, d.`modification`, d.`min_length`, d.`max_length`, d.`conf_length`, c.`table_id`");
		bld.append(" FROM hl7datastructurecomponents d, hl7components c, hl7versions v");
		bld.append(" WHERE v.`hl7_version` = ");
		bld.append("'");
		bld.append(hl7Version);
		bld.append("'");
		bld.append(" AND d.`version_id` = v.`version_id`");
		bld.append(" AND d.comp_no = c.comp_no");
		bld.append(" ORDER BY d.data_structure");
		bld.append(";");
		String rval = bld.toString();
		return rval;
	}

	final RowMapper<Component> RM4_COMPONENT = new RowMapper<Component>() {

		public Component mapRow(ResultSet rs, int rowNum) throws SQLException {
			Component cmp = new Component();
			cmp.setId(++componentIncr);
			cmp.setParentDatatypeId(rs.getString("data_structure"));
			cmp.setDatatypeId(rs.getString("data_type_code"));
			cmp.setPosition(componentIncr);
			cmp.setDescription(rs.getString("description"));
			cmp.setUsage(Usage.valueOf(rs.getString("modification")));
			cmp.setMinLength(rs.getInt("min_length"));
			cmp.setMaxLength(rs.getInt("max_length"));
			String s = rs.getString("conf_length").replaceAll("[#=]", "");
			int confLength = (s.length() > 0) ? new Integer(s) : 0;
			cmp.setConfLength(confLength);
			cmp.setTableId(rs.getString("table_id"));
			return cmp;
		}
	};

	String SQL4_CODE() {
		StringBuilder bld = new StringBuilder();
		bld.append(" SELECT distinct tv.`table_id`, tv.`table_value`, tv.`description_as_pub`");
		bld.append(" FROM hl7versions v");
		bld.append(" INNER JOIN hl7tablevalues tv ON v.`version_id` = tv.`version_id`");
		bld.append(" WHERE v.`hl7_version` = ");
		bld.append("'");
		bld.append(hl7Version);
		bld.append("'");
		bld.append(" ORDER BY tv.`table_id`");
		bld.append(";");
		String rval = bld.toString();
		return rval;
	}

	final RowMapper<Code> RM4_CODE = new RowMapper<gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Code>() {

		public Code mapRow(ResultSet rs, int rowNum) throws SQLException {
			Code cd = new Code();
			String s = String.format("%04d", rs.getInt("table_id"));
			cd.setTableId(s);
			cd.setName(rs.getString("table_value"));
			cd.setDescription(rs.getString("description_as_pub"));
			cd.setUsage(Usage.valueOf("F"));
			return cd;
		}
	};

	String SQL4_DATAELEMENT() {
		StringBuilder bld = new StringBuilder();
		bld.append(
				"SELECT de.`data_item`, de.`description` , de.`min_length`, de.`max_length`, de.`conf_length`, de.`table_id`, de.`section`");
		bld.append(" FROM hl7versions v");
		bld.append(" INNER JOIN hl7dataelements de ON v.`version_id` = de.`version_id`");
		bld.append(" WHERE v.`hl7_version` = ");
		bld.append("'");
		bld.append(hl7Version);
		bld.append("'");
		bld.append(" ORDER BY de.`data_item`");
		bld.append(";");
		String rval = bld.toString();
		return rval;
	}

	final RowMapper<DataElement> RM4_DATAELEMENT = new RowMapper<gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.DataElement>() {

		public DataElement mapRow(ResultSet rs, int rowNum) throws SQLException {
			DataElement de = new DataElement();
			String s = String.format("%04d", rs.getInt("data_item"));
			de.setId(s);
			de.setDescription(rs.getString("description"));
			String min = rs.getString("min_length");
			de.setMinLength(min != null && min.length() > 0 ? new Integer(min) : 0);
			String max = rs.getString("max_length");
			de.setMaxLength(max != null && max.length() > 0 ? new Integer(max) : 0);
			String conf = rs.getString("conf_length").replaceAll("[=#]", "");
			String conf1 = conf.contains("..") ? conf.substring(conf.lastIndexOf(".") + 1) : conf;
			de.setConfLength(conf1 != null && conf1.length() > 0 ? new Integer(conf1) : 0);
			String s1 = rs.getString("table_id");
			String s2 = s1 == null ? s1 : String.format("%04d", new Integer(s1));
			de.setTableId(s2);
			de.setSection(rs.getString("section"));
			return de;
		}
	};

	String SQL4_TABLE() {
		StringBuilder bld = new StringBuilder();
		bld.append("SELECT t.`table_id`, t.`description_as_pub`, t.`table_type`, t.`oid_codesystem`, t.`section`");
		bld.append(" FROM hl7versions v");
		bld.append(" INNER JOIN hl7tables t ON v.`version_id` = t.`version_id`");
		bld.append(" WHERE v.`hl7_version` = ");
		bld.append("'");
		bld.append(hl7Version);
		bld.append("'");
		bld.append(" AND t.`table_id` > 0");
		bld.append(" ORDER BY t.`table_id`");
		bld.append(";");
		String rval = bld.toString();
		return rval;
	}

	final RowMapper<Table> RM4_TABLE = new RowMapper<gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Table>() {

		public Table mapRow(ResultSet rs, int rowNum) throws SQLException {
			Table tab = new Table();
			String id = rs.getString("table_id");
			String id1 = id == null ? id : String.format("%04d", new Integer(id));
			tab.setId(id1);
			tab.setDescription(rs.getString("description_as_pub"));
			tab.setType(rs.getString("table_type"));
			tab.setOid("UNSPECIFIED");
			tab.setSection(rs.getString("section"));
			return tab;
		}
	};

	String SQL4_ELEMENT() {
		StringBuilder bld = new StringBuilder();
		bld.append("SELECT m.`message_structure`, m.`seq_no`, m.`groupname`, m.`seg_code`, m.`modification`, m.`optional`, m.`repetitional`"); 
		bld.append(" FROM hl7versions v INNER JOIN hl7msgstructidsegments m ON v.version_id = m.version_id");
		bld.append(" WHERE v.`hl7_version` = ");
		bld.append("'");
		bld.append(hl7Version);
		bld.append("'");		
		bld.append(";");
		String rval = bld.toString();
		return rval;
	}

	final RowMapper<InterimElement> RM4_ELEMENT = new RowMapper<InterimElement>() {

		public InterimElement mapRow(ResultSet rs, int rowNum) throws SQLException {
			InterimElement el = new InterimElement();
			el.setId(++elementIncr);
			el.setMessageStucture(rs.getString("message_structure"));
			el.setGroupName(rs.getString("groupname"));
			el.setGroupState(rs.getString("seg_code"));
			el.setMin(rs.getString("optional") == "TRUE" ? 0 : 1);
			el.setMax(rs.getString("repetitional") == "TRUE" ? "*" : "1");
			el.setUsage(Usage.valueOf(rs.getString("modification")));
			return el;
		}
	};
}
