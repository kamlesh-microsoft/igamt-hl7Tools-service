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

import org.apache.commons.lang3.StringUtils;
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
import gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Event;
import gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Field;
import gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Group;
import gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Interaction;
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

  static final String ROOT_GROUP_NAME = "ROOT";
  static final String MISSING_GROUP_NAME = "NA";

  JdbcTemplate jdbcTemplate;

  String hl7Version;

  static File versionDir;

  ObjectMapper mapper = new ObjectMapper();

  int groupIncr = 1707;
  int componentIncr;
  int fieldIncr;
  int elementIncr = 6943;

  Map<String, Group> grpByMsg = new HashMap<String, Group>();
  Map<String, Group> grpByName = new HashMap<String, Group>();
  List<String> segIds = new ArrayList<String>();

  public HL72JSONConverter(String hl7Version) {
    try {
      SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
      dataSource.setDriver(new com.mysql.jdbc.Driver());
      dataSource.setUrl("jdbc:mysql://localhost/mdb");
      dataSource.setUsername("root");

      this.jdbcTemplate = new JdbcTemplate(dataSource);
      this.hl7Version = hl7Version;
      mapper.setSerializationInclusion(Include.NON_NULL);
      mapper.addMixInAnnotations(Group.class, GroupMixIn.class);
      mapper.addMixInAnnotations(Component.class, ComponentMixIn.class);
      mapper.addMixInAnnotations(Field.class, FieldMixIn.class);
      mapper.addMixInAnnotations(Code.class, CodeMixIn.class);
      mapper.addMixInAnnotations(DataElement.class, DataElementMixIn.class);
      mapper.addMixInAnnotations(Element.class, ElementMixIn.class);
      mapper.addMixInAnnotations(Interaction.class, InteractionMixIn.class);
    } catch (SQLException e) {
      log.error("", e);
    }
  }

  @Override
  public void run() {
    if (!OUTPUT_DIR.exists()) {
      OUTPUT_DIR.mkdir();
    }
    versionDir = new File(OUTPUT_DIR, hl7Version);
    if (!versionDir.exists()) {
      versionDir.mkdir();
    }

    log.info("For version=" + hl7Version);
    log.info("Doing MessageType...");
    Helper<MessageType> hlpMessageType = new Helper<MessageType>();
    List<MessageType> messageTypes = (hlpMessageType.fetch(SQL4_MESSAGETYPE(), RM4_MESSAGETYPE));
    hlpMessageType.write(messageTypes, "message_types");

    log.info("Doing Message...");
    Helper<Message> hlpMessage = new Helper<Message>();
    List<Message> messages1 = (hlpMessage.fetch(SQL4_MESSAGE1(), RM4_MESSAGE));
    List<Message> messages2 = (hlpMessage.fetch(SQL4_MESSAGE2(), RM4_MESSAGE));
    messages1.addAll(messages2);
    log.info(" writing messages=" + messages1.size());
    hlpMessage.write(messages1, "messages");

    log.info("Doing Group...");
    Helper<Group> hlpGroup = new Helper<Group>();
    List<Group> groups = (hlpGroup.fetch(SQL4_GROUP(), RM4_GROUP));
    List<Group> groups1 = assembleGroups(groups);
    log.info(" writing groups1=" + groups1.size());
    hlpGroup.write(groups1, "groups");

    log.info("Doing Segment...");
    Helper<Segment> hlpSegment = new Helper<Segment>();
    List<Segment> segments = (hlpSegment.fetch(SQL4_SEGMENT(), RM4_SEGMENT));
    for (Segment seg : segments) {
      segIds.add(seg.getId());
    }
    log.info(" writing segments=" + segments.size());
    hlpSegment.write(segments, "segments");

    log.info("Doing Datatype...");
    Helper<Datatype> hlpDatatype = new Helper<Datatype>();
    List<Datatype> datatypes = (hlpDatatype.fetch(SQL4_DATATYPE(), RM4_DATATYPE));
    log.info(" writing datatypes=" + datatypes.size());
    hlpDatatype.write(datatypes, "datatypes");

    log.info("Doing Component...");
    Helper<Component> hlpComponent = new Helper<Component>();
    List<Component> components = (hlpComponent.fetch(SQL4_COMPONENT(), RM4_COMPONENT));
    log.info(" writing components=" + components.size());
    hlpComponent.write(components, "components");

    log.info("Doing Field...");
    Helper<Field> hlpField = new Helper<Field>();
    List<Field> fields = (hlpField.fetch(SQL4_FIELD(), RM4_FIELD));
    log.info(" writing fields=" + fields.size());
    hlpField.write(fields, "fields");

    log.info("Doing Code...");
    Helper<Code> hlpCode = new Helper<Code>();
    List<Code> codes = (hlpCode.fetch(SQL4_CODE(), RM4_CODE));
    hlpCode.write(codes, "codes");

    log.info("Doing DataElement...");
    Helper<DataElement> hlpDataElement = new Helper<DataElement>();
    List<DataElement> dataelements = (hlpDataElement.fetch(SQL4_DATAELEMENT(), RM4_DATAELEMENT));
    log.info(" writing dataelements=" + dataelements.size());
    hlpDataElement.write(dataelements, "data_elements");

    log.info("Doing Table...");
    Helper<Table> hlpTable = new Helper<Table>();
    List<Table> tables = (hlpTable.fetch(SQL4_TABLE(), RM4_TABLE));
    log.info(" writing tables=" + tables.size());
    hlpTable.write(tables, "tables");

    log.info("Doing Element...");
    Helper<Element> hlpElement = new Helper<Element>();
    Helper<InterimElement> hlpInterimElement = new Helper<InterimElement>();
    List<InterimElement> interimelements = (hlpInterimElement.fetch(SQL4_ELEMENT(), RM4_ELEMENT));
    List<Element> elements = postProcessElements(interimelements);
    log.info(" writing elements=" + elements.size());
    hlpElement.write(elements, "elements");

    log.info("Doing Event...");
    Helper<Event> hlpEvent = new Helper<Event>();
    List<Event> events = (hlpEvent.fetch(SQL4_EVENT(), RM4_EVENT));
    hlpEvent.write(events, "events");

    log.info("Doing Interaction...");
    Helper<Interaction> hlpInteraction = new Helper<Interaction>();
    List<Interaction> interactions = (hlpInteraction.fetch(SQL4_INTERACTION(), RM4_INTERACTION));
    hlpInteraction.write(interactions, "interactions");

    log.info("...Done");

  }

  List<Element> postProcessElements(List<InterimElement> ies) {

    List<Element> rval = new ArrayList<Element>();
    GroupStacker stk = new GroupStacker();
    int position = 1;
    int state = 1;
    int nullconts = 0;
    int openconts = 0;
    int closeconts = 0;
    int incdecconts = 0;

    for (InterimElement ie : ies) {
      if (ie.getGroupName() == null && ie.getSegmentId() == null) {
        nullconts++;
        continue;
      }
      if ("|".equals(ie.getSegmentId())) {
        closeconts++;
        continue;
      }
      String key = assembleGroupKey(ie);
      Group group = grpByName.get(key);
      if (group == null) {
        log.info("null4=" + key);
        for (String key1 : grpByName.keySet()) {
          log.info("key1=" + key1);
        }
        continue;
      }

      int incdec = stk.waw(group, ie.getSegmentId(), ie.getSeqNo());
      state += incdec;
      if (incdec < 0) {
        incdecconts++;
        continue;
      }
      if (state > 1) {
        if (group.isRoot()) {
          Integer parentId = stk.getCurrGroupId();
          ie.setParentId(parentId);
        } else {
          Integer parentId = stk.getPrevGroupId();
          ie.setParentId(parentId);
          Integer groupId = stk.getCurrGroupId();
          ie.setGroupId(groupId);
        }
      } else {
        if (group.isRoot()) {
          Integer parentId = stk.getCurrGroupId();
          ie.setParentId(parentId);
        } else {
          int idx = ies.indexOf(ie);
          InterimElement next = ies.get(idx + 1);
          String nextKey = assembleGroupKey(next);
          Group nextGroup = grpByName.get(nextKey);
          if (nextGroup != null) {
            Integer parentId = nextGroup.getId();
            ie.setParentId(parentId);
          }
        }
        position = 1;
      }
      ie.setPosition(position++);
      if (ie.getParentId() == null) {
        throw new NullPointerException();
      }
      Element el = ie;
      if (isEmbraced(el.getSegmentId())) {
        el.setSegmentId(null);
      }
      rval.add(el);
      log.info(String.format("%" + state + 1 * 2 + "d", state) + ie.toString());
    }
    log.info("ies=" + ies.size());
    log.info("nullconts=" + nullconts);
    log.info("openconts=" + openconts);
    log.info("closeconts=" + closeconts);
    log.info("incdecconts=" + incdecconts);
    return rval;

  }

  boolean isEmbraced(String segId) {
    return segId.contains("{") || segId.contains("[") || segId.contains("<") || segId.contains("}")
        || segId.contains("]") || segId.contains(">");
  }

  List<Group> assembleGroups(List<Group> groups) {
    GroupStacker stk = new GroupStacker();
    List<Group> rval = new ArrayList<Group>();
    boolean b = false;
    for (Group group : groups) {
      String key = assembleGroupKey(group);
      if (stk.waw1(group) >= 0) {
        rval.add(group);
      }
      grpByName.put(key, group);
    }
    return rval;
  }

  String assembleGroupKey(Group group) {
    return group.getName()
        + (group.getName().contains(ROOT_GROUP_NAME) ? "" : "." + group.getSeq());
  }

  String assembleGroupKey(InterimElement ie) {
    String groupName = ie.getGroupName();
    String key = ie.messageStucture;
    if ((groupName == null || groupName.trim().length() == 0)) {
      if (isEmbraced(ie.getSegmentId())) {
        groupName = MISSING_GROUP_NAME;
      } else {
        groupName = ROOT_GROUP_NAME;
      }
      key = ie.messageStucture + "." + groupName;
    } else {
      key = ie.messageStucture + "." + groupName + "." + ie.getSeqNo();
    }
    return key;
  }

  public static void main(String[] args) {
    for (String arg : args) {
      HL72JSONConverter app = new HL72JSONConverter(arg);
      app.run();
    }
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

  public class GroupStacker {

    Stack<Group> stack = new Stack<Group>();

    int waw(Group group, String segId, Integer seq) {
      if (seq == 1) {
        stack.clear();
        stack.push(group);
      }
      if (group.isRoot()) {
        return 0;
      }
      if (!stack.empty() && (segId.contains("}") || segId.contains("]") || segId.contains(">"))) {
        stack.pop();
        return -1;
      } else {
        stack.push(group);
        return 1;
      }
    }

    int waw1(Group group) {
      if (group.isRoot()) {
        return 0;
      }
      if (!stack.empty() && stack.peek().getName().equals(group.getName())) {
        stack.pop();
        return -1;
      } else {
        stack.push(group);
        return 1;
      }
    }

    Integer getCurrGroupId() {
      if (stack.empty()) {
        return 1;
      }
      return stack.peek().getId();
    }

    Integer getPrevGroupId() {
      if (stack.empty()) {
        return 1;
      }
      Group group = stack.peek();
      if (stack.size() == 1) {
        return group.getId();
      }
      int idx = stack.indexOf(group);
      return stack.get(idx - 1).getId();
    }

    Group getCurrGroup() {
      if (stack.empty()) {
        return null;
      }
      return grpByName.get(stack.peek());
    }

  }

  public String SQL4_MESSAGETYPE() {
    StringBuilder bld = new StringBuilder();
    bld.append("SELECT m.message_type, m.description, m.section");
    bld.append(System.lineSeparator());
    bld.append(" FROM hl7messagetypes m, hl7versions v");
    bld.append(System.lineSeparator());
    bld.append(" WHERE m.version_id = v.version_id");
    bld.append(System.lineSeparator());
    bld.append(" AND v.hl7_version = ");
    bld.append("'");
    bld.append(hl7Version);
    bld.append("'");
    bld.append(System.lineSeparator());
    bld.append(";");
    return bld.toString();
  }

  final RowMapper<MessageType> RM4_MESSAGETYPE = new RowMapper<MessageType>() {

    @Override
    public MessageType mapRow(ResultSet rs, int rowNum) throws SQLException {
      MessageType messageType = new MessageType();
      messageType.setId(rs.getString("message_type"));
      messageType.setDescription(rs.getString("description"));
      messageType.setSection(rs.getString("section"));

      return messageType;
    }
  };

  public String SQL4_MESSAGE1() {
    StringBuilder bld = new StringBuilder();
    bld.append("SELECT m.`message_type`, '' as event_code, i.`message_structure`, m.`section`");
    bld.append(System.lineSeparator());
    bld.append(" FROM hl7versions v ");
    bld.append(System.lineSeparator());
    bld.append(" INNER JOIN hl7messagetypes m ON v.`version_id` = m.`version_id`");
    bld.append(System.lineSeparator());
    bld.append(" INNER JOIN hl7msgstructids i ON v.`version_id` = i.`version_id`");
    bld.append(System.lineSeparator());
    bld.append(" INNER JOIN hl7events e ON v.`version_id` = e.`version_id`");
    bld.append(System.lineSeparator());
    bld.append(" WHERE v.`hl7_version` = ");
    bld.append("'");
    bld.append(hl7Version);
    bld.append("'");
    bld.append(System.lineSeparator());
    bld.append(" AND m.`message_type` = i.`message_structure`");
    bld.append(System.lineSeparator());
    bld.append(" LIMIT 1");
    bld.append(System.lineSeparator());
    bld.append(";");

    return bld.toString();
  }

  public String SQL4_MESSAGE2() {
    StringBuilder bld = new StringBuilder();
    bld.append("SELECT m.`message_type`, e.`event_code`, i.`message_structure`, m.`section`");
    bld.append(System.lineSeparator());
    bld.append(" FROM hl7versions v ");
    bld.append(System.lineSeparator());
    bld.append(" INNER JOIN hl7messagetypes m ON v.`version_id` = m.`version_id`");
    bld.append(System.lineSeparator());
    bld.append(" INNER JOIN hl7msgstructids i ON v.`version_id` = i.`version_id`");
    bld.append(System.lineSeparator());
    bld.append(" INNER JOIN hl7events e ON v.`version_id` = e.`version_id`");
    bld.append(System.lineSeparator());
    bld.append(" WHERE v.`hl7_version` = ");
    bld.append("'");
    bld.append(hl7Version);
    bld.append("'");
    bld.append(System.lineSeparator());
    bld.append(" AND concat(m.`message_type`, '_', e.`event_code`) = i.`message_structure`");
    bld.append(System.lineSeparator());
    bld.append(" ORDER BY m.message_type");
    bld.append(System.lineSeparator());
    bld.append(";");
    return bld.toString();
  }



  final RowMapper<Message> RM4_MESSAGE = new RowMapper<Message>() {

    @Override
    public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
      Message message = new Message();
      message.setId(rs.getString("message_structure"));
      message.setSection(rs.getString("section"));
      message.setEvent_id(rs.getString("event_code"));
      message.setMsg_type_id(rs.getString("message_type"));
      return message;
    }
  };

  public String SQL4_GROUP() {
    StringBuilder bld = new StringBuilder();
    bld.append("SELECT m.`message_structure`, m.`seq_no`, m.`groupname`, m.`seg_code`");
    bld.append(System.lineSeparator());
    bld.append(" FROM hl7versions v");
    bld.append(System.lineSeparator());
    bld.append(" INNER JOIN hl7msgstructidsegments m ON v.version_id = m.version_id");
    bld.append(System.lineSeparator());
    bld.append(" WHERE v.`hl7_version` = ");
    bld.append("'");
    bld.append(hl7Version);
    bld.append("'");
    bld.append(System.lineSeparator());
    bld.append(" AND (m.`seg_code` = 'MSH' || length(m.`groupname`) > 0)");
    bld.append(System.lineSeparator());
    bld.append(" ORDER BY m.`message_structure`, m.`seq_no`");
    bld.append(System.lineSeparator());
    bld.append(";");
    String rval = bld.toString();
    return rval;
  }

  final RowMapper<Group> RM4_GROUP = new RowMapper<Group>() {

    @Override
    public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
      Group grp = new Group();
      grp.setId(++groupIncr);
      grp.setRoot(("MSH".equals(rs.getString("seg_code"))) ? true : false);
      String segId = rs.getString("seg_code");
      grp.setChoice("<".equals(segId) ? true : false);
      grp.setMessageId(rs.getString("message_structure"));
      grp.setSeq(rs.getInt("seq_no"));
      grp.setName((grp.isRoot() ? (rs.getString("message_structure") + ".ROOT")
          : rs.getString("message_structure") + "." + rs.getString("groupname")));
      return grp;
    }
  };

  public String SQL4_SEGMENT() {
    StringBuilder bld = new StringBuilder();
    bld.append("SELECT s.seg_code, s.description, s.section");
    bld.append(System.lineSeparator());
    bld.append(" FROM hl7versions v");
    bld.append(System.lineSeparator());
    bld.append(" INNER JOIN hl7segments s ON v.version_id = s.version_id");
    bld.append(System.lineSeparator());
    bld.append(" WHERE v.`hl7_version` = ");
    bld.append("'");
    bld.append(hl7Version);
    bld.append("'");
    bld.append(System.lineSeparator());
    // bld.append(" AND s.visible = 'TRUE'");
    bld.append(" AND s.visible = '1'");
    bld.append(System.lineSeparator());
    bld.append(" ORDER BY s.seg_code");
    bld.append(System.lineSeparator());
    bld.append(";");
    String rval = bld.toString();
    return rval;
  }

  final RowMapper<Segment> RM4_SEGMENT = new RowMapper<Segment>() {

    @Override
    public Segment mapRow(ResultSet rs, int rowNum) throws SQLException {
      Segment seg = new Segment();
      seg.setId(rs.getString("seg_code"));
      seg.setDescription(rs.getString("description"));
      seg.setSection(rs.getString("section"));
      return seg;
    }
  };

  public String SQL4_FIELD() {
    StringBuilder bld = new StringBuilder();
    bld.append(
        "SELECT s.`seg_code`, s.`data_item`, s.`req_opt`, s.`seq_no`, s.`repetitional`, s.`repetitions`");
    bld.append(System.lineSeparator());
    bld.append(" FROM hl7segmentdataelements s");
    bld.append(" INNER JOIN hl7versions v ON v.version_id = s.version_id");
    bld.append(System.lineSeparator());
    bld.append(" WHERE v.`hl7_version` = ");
    bld.append("'");
    bld.append(hl7Version);
    bld.append("'");
    bld.append(System.lineSeparator());
    bld.append(" ORDER BY s.`seg_code`");
    bld.append(System.lineSeparator());
    bld.append(";");
    String rval = bld.toString();
    return rval;
  }

  final RowMapper<Field> RM4_FIELD = new RowMapper<Field>() {

    @Override
    public Field mapRow(ResultSet rs, int rowNum) throws SQLException {
      Field fld = new Field();
      fld.setId(++fieldIncr);
      fld.setSegmentId(rs.getString("seg_code"));
      String s = String.format("%05d", rs.getInt("data_item"));
      fld.setDataElementId(s);
      fld.setPosition(rs.getInt("seq_no"));
      String usage1 = rs.getString("req_opt");
      String usage2 = stripParens(usage1);
      Usage usage3 = Usage.fromValue(usage2);
      fld.setUsage(usage3);

      // We use the fld.getUsage().name() here because Usage.fromValue(usage2) has already corrected
      // for bad values.
      Object[] cardinality = calcCardinality(fld.getUsage().name(), rs.getString("repetitional"),
          rs.getInt("repetitions"));
      fld.setMin((Integer) cardinality[0]);
      fld.setMax((String) cardinality[1]);
      return fld;
    }
  };

  Object[] calcCardinality(String usage, String repetitional, Integer repetitions) {
    repetitional = repetitional == null ? "" : repetitional.trim();
    Object[] rval = new Object[2];

    if ("R".equals(usage)) {
      rval[0] = new Integer(1);
    } else if ("B".equals(usage) || "W".equals(usage) || "X".equals(usage)) {
      rval[0] = new Integer(0);
    } else if ("C".equals(usage)) {
      rval[0] = new Integer(0);
    } else {
      rval[0] = new Integer(0);
    }

    if ("Y".equals(repetitional)) {
      if (repetitions == 0) {
        rval[1] = "*";
      } else {
        rval[1] = new Integer(repetitions).toString();
      }
    } else {
      if ("B".equals(usage) || "W".equals(usage) || "X".equals(usage)) {
        rval[1] = "1";
      } else if ("R".equals(usage)) {
        rval[1] = "1";
      } else if ("C".equals(usage)) {
        rval[1] = "1";
      } else {
        rval[1] = "1";
      }
    }

    // if ("Y".equals(repetitional)) {
    // if (repetitions == 0) {
    // rval[0] = new Integer(0);
    // rval[1] = "*";
    // } else {
    // rval[0] = new Integer(0);
    // rval[1] = new Integer(repetitions).toString();
    // }
    // } else {
    // if ("B".equals(usage) || "W".equals(usage) || "X".equals(usage)) {
    // rval[0] = new Integer(0);
    // rval[1] = "1";
    // } else if ("R".equals(usage)) {
    // rval[0] = new Integer(1);
    // rval[1] = "1";
    // } else if ("C".equals(usage)) {
    // rval[0] = new Integer(0);
    // rval[1] = "1";
    // } else {
    // rval[0] = new Integer(0);
    // rval[1] = "1";
    // }
    // }
    return rval;
  }

  String stripParens(String usage) {
    // We're dealing with a very specific situation here.
    // There are times when usage is set to something like (B) R in
    // which case we strip all but the last character.
    String rval = null;
    if (StringUtils.isNotEmpty(usage) && usage.contains(")")) {
      int pos = usage.indexOf(")");
      rval = usage.substring(pos + 1).trim();
      return rval;
    }
    return usage;
  }

  public String SQL4_DATATYPE() {
    StringBuilder bld = new StringBuilder();
    bld.append("SELECT d.data_structure, d.`description`, d.`section`, d.`elementary`");
    bld.append(System.lineSeparator());
    bld.append(" FROM hl7datastructures d, hl7versions v");
    bld.append(System.lineSeparator());
    bld.append(" WHERE v.`hl7_version` = ");
    bld.append("'");
    bld.append(hl7Version);
    bld.append("'");
    bld.append(System.lineSeparator());
    bld.append(" AND d.`version_id` = v.`version_id`");
    bld.append(System.lineSeparator());
    bld.append(" ORDER BY d.data_structure");
    bld.append(System.lineSeparator());
    bld.append(";");
    String rval = bld.toString();
    return rval;
  }

  final RowMapper<Datatype> RM4_DATATYPE = new RowMapper<Datatype>() {

    @Override
    public Datatype mapRow(ResultSet rs, int rowNum) throws SQLException {
      Datatype dt = new Datatype();
      dt.setId(rs.getString("data_structure").toUpperCase());
      dt.setDescription(rs.getString("description"));
      dt.setPrimitive(rs.getBoolean("elementary"));
      String s = rs.getString("section");
      dt.setSection(s != null && s.length() > 0 ? s : null);
      return dt;
    }
  };

  public String SQL4_COMPONENT() {
    StringBuilder bld = new StringBuilder();
    bld.append(
        "SELECT dc.`data_structure`, c.`data_type_code`, c.`description`, dc.`req_opt`, dc.`min_length`, dc.`max_length`, dc.`conf_length`, c.`table_id`");
    bld.append(System.lineSeparator());
    bld.append(" FROM hl7versions v");
    bld.append(System.lineSeparator());
    bld.append(" INNER JOIN hl7datastructures d ON v.`version_id` = d.`version_id`");
    bld.append(System.lineSeparator());
    bld.append(" INNER JOIN hl7datastructurecomponents dc ON v.`version_id` = dc.`version_id`");
    bld.append(System.lineSeparator());
    bld.append(" INNER JOIN hl7components c ON v.`version_id` = c.`version_id`");
    bld.append(System.lineSeparator());
    bld.append(" WHERE v.`hl7_version` = ");
    bld.append("'");
    bld.append(hl7Version);
    bld.append("'");
    bld.append(System.lineSeparator());
    bld.append(" AND d.`data_structure` = dc.`data_structure`");
    bld.append(System.lineSeparator());
    bld.append(" AND dc.comp_no = c.comp_no");
    bld.append(System.lineSeparator());
    bld.append(" AND d.`elementary` = '0'");
    bld.append(System.lineSeparator());
    bld.append(";");
    String rval = bld.toString();
    return rval;
  }

  final RowMapper<Component> RM4_COMPONENT = new RowMapper<Component>() {

    @Override
    public Component mapRow(ResultSet rs, int rowNum) throws SQLException {
      Component cmp = new Component();
      cmp.setId(++componentIncr);
      cmp.setParentDatatypeId(rs.getString("data_structure"));
      cmp.setDatatypeId(rs.getString("data_type_code"));
      cmp.setPosition(componentIncr);
      cmp.setDescription(rs.getString("description"));
      cmp.setUsage(Usage.fromValue(rs.getString("req_opt")));
      cmp.setMinLength(rs.getInt("min_length"));
      cmp.setMaxLength(rs.getInt("max_length"));
      // String s = rs.getString("conf_length").replaceAll("[#=]", "");
      String s = rs.getString("conf_length");
      // int confLength = (s.length() > 0) ? new Integer(s) : 0;
      cmp.setConfLength(s);
      String tableId = rs.getString("table_id");
      String id = "0".equals(tableId) ? null : tableId;
      String id1 = id == null ? id : String.format("%04d", new Integer(tableId));
      cmp.setTableId(id1);
      return cmp;
    }
  };

  public String SQL4_CODE() {
    StringBuilder bld = new StringBuilder();
    bld.append(" SELECT distinct tv.`table_id`, tv.`table_value`, tv.`description_as_pub`");
    bld.append(System.lineSeparator());
    bld.append(" FROM hl7versions v");
    bld.append(System.lineSeparator());
    bld.append(" INNER JOIN hl7tablevalues tv ON v.`version_id` = tv.`version_id`");
    bld.append(System.lineSeparator());
    bld.append(" WHERE v.`hl7_version` = ");
    bld.append("'");
    bld.append(hl7Version);
    bld.append("'");
    bld.append(System.lineSeparator());
    bld.append(" ORDER BY tv.`table_id`");
    bld.append(System.lineSeparator());
    bld.append(";");
    String rval = bld.toString();
    return rval;
  }

  final RowMapper<Code> RM4_CODE =
      new RowMapper<gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Code>() {

        @Override
        public Code mapRow(ResultSet rs, int rowNum) throws SQLException {
          Code cd = new Code();
          String s = String.format("%04d", rs.getInt("table_id"));
          cd.setTableId(s);
          cd.setName(rs.getString("table_value"));
          cd.setDescription(rs.getString("description_as_pub"));
          cd.setUsage(Usage.fromValue("F"));
          return cd;
        }
      };

  public String SQL4_DATAELEMENT() {
    StringBuilder bld = new StringBuilder();
    bld.append(
        "SELECT de.`data_item`, de.`data_structure`, de.`description` , de.`min_length`, de.`max_length`, de.`conf_length`, de.`table_id`, de.`section`");
    bld.append(System.lineSeparator());
    bld.append(" FROM hl7versions v");
    bld.append(System.lineSeparator());
    bld.append(" INNER JOIN hl7dataelements de ON v.`version_id` = de.`version_id`");
    bld.append(System.lineSeparator());
    bld.append(" WHERE v.`hl7_version` = ");
    bld.append("'");
    bld.append(hl7Version);
    bld.append("'");
    bld.append(System.lineSeparator());
    bld.append(" ORDER BY de.`data_item`");
    bld.append(System.lineSeparator());
    bld.append(";");
    String rval = bld.toString();
    return rval;
  }

  final RowMapper<DataElement> RM4_DATAELEMENT =
      new RowMapper<gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.DataElement>() {

        @Override
        public DataElement mapRow(ResultSet rs, int rowNum) throws SQLException {
          DataElement de = new DataElement();
          String s = String.format("%05d", rs.getInt("data_item"));
          de.setId(s);
          de.setDatatypeId(rs.getString("data_structure").toUpperCase());
          de.setDescription(rs.getString("description"));
          String min = rs.getString("min_length");
          de.setMinLength(min != null && min.length() > 0 ? new Integer(min) : 0);
          String max = rs.getString("max_length");
          de.setMaxLength(max != null && max.length() > 0 ? new Integer(max) : 0);
          // String conf = rs.getString("conf_length").replaceAll("[=#]", "");
          String conf = rs.getString("conf_length");
          String conf1 = conf != null && conf.contains("..")
              ? conf.substring(conf.lastIndexOf(".") + 1) : conf;
          de.setConfLength(conf1 != null && conf1.length() > 0 ? conf1 : null);
          String s1 = rs.getString("table_id");
          String s2 = "0".equals(s1) ? null : String.format("%04d", new Integer(s1));
          de.setTableId(s2);
          de.setSection(rs.getString("section"));
          return de;
        }
      };

  public String SQL4_TABLE() {
    StringBuilder bld = new StringBuilder();
    bld.append(
        "SELECT t.`table_id`, t.`description_as_pub`, t.`table_type`, t.`oid_codesystem`, t.`section`");
    bld.append(System.lineSeparator());
    bld.append(" FROM hl7versions v");
    bld.append(System.lineSeparator());
    bld.append(" INNER JOIN hl7tables t ON v.`version_id` = t.`version_id`");
    bld.append(System.lineSeparator());
    bld.append(" WHERE v.`hl7_version` = ");
    bld.append("'");
    bld.append(hl7Version);
    bld.append("'");
    bld.append(System.lineSeparator());
    bld.append(" AND t.`table_id` > 0");
    bld.append(System.lineSeparator());
    bld.append(" ORDER BY t.`table_id`");
    bld.append(System.lineSeparator());
    bld.append(";");
    String rval = bld.toString();
    return rval;
  }

  final RowMapper<Table> RM4_TABLE =
      new RowMapper<gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Table>() {

        @Override
        public Table mapRow(ResultSet rs, int rowNum) throws SQLException {
          Table tab = new Table();
          String id = rs.getString("table_id");
          // The following two lines were originally intended to have the id of table data set be
          // congruent
          // with the table ids from versions 2.1 - 2.7, which had a leading zero prepended.
          // It didn't work with version 2.7.1 - 2.8.2. Can't say why.
          String id1 = id == null ? id : String.format("%04d", new Integer(id));
          tab.setId(id1);
          tab.setDescription(rs.getString("description_as_pub"));
          tab.setType(rs.getString("table_type"));
          tab.setOid("UNSPECIFIED");
          tab.setSection(rs.getString("section"));
          return tab;
        }
      };

  public String SQL4_ELEMENT() {
    StringBuilder bld = new StringBuilder();
    bld.append(
        "SELECT m.`message_structure`, m.`seq_no`, m.`groupname`, m.`seg_code`, m.`modification`, m.`optional`, m.`repetitional`");
    bld.append(System.lineSeparator());
    bld.append(" FROM hl7versions v ");
    bld.append(System.lineSeparator());
    bld.append(" INNER JOIN hl7msgstructidsegments m ON v.version_id = m.version_id");
    bld.append(System.lineSeparator());
    bld.append(" WHERE v.`hl7_version` = ");
    bld.append("'");
    bld.append(hl7Version);
    bld.append("'");
    bld.append(System.lineSeparator());
    bld.append(";");
    String rval = bld.toString();
    return rval;
  }

  final RowMapper<InterimElement> RM4_ELEMENT = new RowMapper<InterimElement>() {

    @Override
    public InterimElement mapRow(ResultSet rs, int rowNum) throws SQLException {
      InterimElement el = new InterimElement();
      el.setId(++elementIncr);
      el.setMessageStucture(rs.getString("message_structure"));
      String groupname = rs.getString("groupname");
      el.setGroupName(
          groupname != null && groupname.trim().length() > 0 ? groupname.toUpperCase() : null);
      String segCode = rs.getString("seg_code");
      el.setSegmentId(segCode);
      el.setMin(rs.getBoolean("optional") == true ? 0 : 1);
      el.setMax(rs.getBoolean("repetitional") == true ? "*" : "1");
      if (el.getMin() == 1 && "1".equals(el.getMax())) {
        el.setUsage(Usage.R);
      } else if (el.getMin() == 0 && "*".equals(el.getMax())) {
        el.setUsage(Usage.O);
      } else {
        el.setUsage(Usage.O);
      }
      el.setSeqNo(rs.getInt("seq_no"));
      log.info("el=" + el.toString());
      return el;
    }
  };

  public String SQL4_EVENT() {
    StringBuilder bld = new StringBuilder();
    bld.append("SELECT e.`event_code`, e.`description`, e.`section`");
    bld.append(System.lineSeparator());
    bld.append(" FROM hl7versions v ");
    bld.append(System.lineSeparator());
    bld.append(" INNER JOIN hl7events e ON v.`version_id` = e.`version_id`");
    bld.append(System.lineSeparator());
    bld.append(" WHERE v.`hl7_version` = ");
    bld.append("'");
    bld.append(hl7Version);
    bld.append("'");
    bld.append(System.lineSeparator());
    bld.append(" ORDER BY e.`event_code`");
    bld.append(System.lineSeparator());
    bld.append(";");
    String rval = bld.toString();
    return rval;
  }

  final RowMapper<Event> RM4_EVENT = new RowMapper<Event>() {

    @Override
    public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
      Event evt = new Event();
      evt.setId(rs.getString("event_code"));
      evt.setDescription(rs.getString("description"));
      evt.setSection(rs.getString("section"));
      return evt;
    }
  };

  public String SQL4_INTERACTION() {
    StringBuilder bld = new StringBuilder();
    bld.append("SELECT e.`event_code`, e.`message_structure_snd`, e.`message_structure_return`");
    bld.append(System.lineSeparator());
    bld.append(" FROM hl7versions v ");
    bld.append(System.lineSeparator());
    bld.append(" INNER JOIN hl7eventmessagetypes e ON v.`version_id` = e.`version_id`");
    bld.append(System.lineSeparator());
    bld.append(" WHERE v.`hl7_version` = ");
    bld.append("'");
    bld.append(hl7Version);
    bld.append("'");
    bld.append(System.lineSeparator());
    bld.append(" AND e.`message_structure_snd` IS NOT NULL");
    bld.append(System.lineSeparator());
    bld.append(" ORDER BY e.`event_code`");
    bld.append(System.lineSeparator());
    bld.append(";");

    String rval = bld.toString();
    return rval;
  }

  final RowMapper<Interaction> RM4_INTERACTION = new RowMapper<Interaction>() {

    @Override
    public Interaction mapRow(ResultSet rs, int rowNum) throws SQLException {
      Interaction it = new Interaction();
      it.setEventId(rs.getString("event_code"));
      it.setNumber(1);
      it.setSenderMsg(rs.getString("message_structure_snd"));
      it.setReceiverMsg(rs.getString("message_structure_return"));
      return it;
    }
  };
}
