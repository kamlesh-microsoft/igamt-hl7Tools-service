package gov.nist.healthcare.hl7tools.service.transformer;

import java.text.SimpleDateFormat;

public class ConformanceProfileTemplate {

	//tag definition for XML
	public static final String TAG_DOCUMENT_ROOT = "HL7v2xConformanceProfile";
	public static final String TAG_META_DATA = "MetaData";
	public static final String TAG_ENCODINGS = "Encodings";
	public static final String TAG_ENCODING = "Encoding";
	public static final String TAG_DYNAMIC_DEF = "DynamicDef";
	public static final String TAG_HL7_V2_STATIC_DEF = "HL7v2xStaticDef";
	public static final String TAG_SEGMENT = "Segment";
	public static final String TAG_SEGMENT_GRP = "SegGroup";
	public static final String TAG_FIELD = "Field";
	public static final String TAG_CONFORMANCE_STAT = "ConformanceStatement";
	public static final String TAG_ENG_DESC = "EnglishDescription";
	public static final String TAG_ASSERTION = "Assertion";
	public static final String TAG_PLAIN_TEXT = "PlainText";
	public static final String TAG_COMPONENT = "Component";
	public static final String TAG_SUB_COMPONENT = "SubComponent";
	public static final String TAG_PREDICATE = "Predicate";
	public static final String TAG_CONDITION = "Condition";
	public static final String TAG_NOT = "Not";
	public static final String TAG_AND = "AND";
	public static final String TAG_OR = "OR";
	public static final String TAG_IF = "IF";
	public static final String TAG_VALUED = "Valued";
	public static final String TAG_REGEX = "Regex";
	public static final String TAG_LIST = "List";
	
	
	//tag attributes
	public static final String ATTR_HL7VERSION = "HL7Version";
	public static final String ATTR_PROFILE_TYPE = "ProfileType";
	public static final String ATTR_NAME = "Name";
	public static final String ATTR_ORG_NAME = "OrgName";
	public static final String ATTR_STATUS = "Status";
	public static final String ATTR_TOPICS = "Topics";
	public static final String ATTR_VERSION = "Version";
	public static final String ATTR_DATE = "Date";
	public static final String ATTR_ACC_ACK = "AccAck";
	public static final String ATTR_APP_ACK = "AppAck";
	public static final String ATTR_MSG_ACK_MODE = "MsgAckMode";
	public static final String ATTR_EVENT_DESC = "EventDesc";
	public static final String ATTR_EVENT_TYPE = "EventType";
	public static final String ATTR_MSG_STRUCT_ID = "MsgStructID";
	public static final String ATTR_MSG_TYPE = "MsgType";
	public static final String ATTR_ORDER_CONTROL = "OrderControl";
	public static final String ATTR_ROLE = "Role";
	public static final String ATTR_LONG_NAME = "LongName";
	public static final String ATTR_MAX = "Max";
	public static final String ATTR_MIN = "Min";
	public static final String ATTR_USAGE = "Usage";
	public static final String ATTR_DATA_TYPE = "Datatype";
	public static final String ATTR_MAX_LENGTH = "MaxLength";
	public static final String ATTR_MIN_LENGTH = "MinLength";
	public static final String ATTR_PROFILE = "Profile";
	public static final String ATTR_ID = "id";
	public static final String ATTR_LOCATION = "location";
	public static final String ATTR_VALUE = "value";
	public static final String ATTR_PREDICATE_FALSE_USAGE = "PredicateFalseUsage";
	public static final String ATTR_PREDICATE_TRUE_USAGE = "PredicateTrueUsage";
	public static final String ATTR_TABLE = "Table";
	public static final String ATTR_REGEX = "regex";
	public static final String ATTR_CSV = "csv";
	
	//attribute(s) definition for each of the tags
	public static final String ATTR_DEF_EMPTY[] = new String[]{};
	public static final String ATTR_DEF_TAG_DOCUMENT_ROOT[] = new String[]{ATTR_HL7VERSION,ATTR_PROFILE_TYPE};
	public static final String ATTR_DEF_TAG_META_DATA_ROOT[] = new String[]{ATTR_NAME,ATTR_ORG_NAME,ATTR_STATUS,ATTR_TOPICS,ATTR_VERSION,ATTR_DATE};
	public static final String ATTR_DEF_TAG_META_DATA_MSG[] = new String[]{ATTR_NAME,ATTR_ORG_NAME,ATTR_STATUS,ATTR_TOPICS,ATTR_VERSION};
	public static String ATTR_DEF_TAG_ENCODINGS[] = ATTR_DEF_EMPTY;
	public static String ATTR_DEF_TAG_ENCODING[] = ATTR_DEF_EMPTY;
	public static final String ATTR_DEF_TAG_DYNAMIC_DEF[] = new String[]{ATTR_ACC_ACK,ATTR_APP_ACK,ATTR_MSG_ACK_MODE};
	public static final String ATTR_DEF_TAG_HL7_V2_STATIC_DEF[] = new String[]{ATTR_EVENT_DESC,ATTR_EVENT_TYPE,ATTR_MSG_STRUCT_ID,ATTR_MSG_TYPE,ATTR_ORDER_CONTROL,ATTR_ROLE};
	public static final String ATTR_DEF_TAG_SEGMENT[] = new String[]{ATTR_LONG_NAME,ATTR_MAX,ATTR_MIN,ATTR_NAME,ATTR_USAGE};
	public static final String ATTR_DEF_TAG_SEGMENT_GRP[] = new String[]{ATTR_LONG_NAME,ATTR_MAX,ATTR_MIN,ATTR_NAME,ATTR_USAGE};
	public static final String ATTR_DEF_TAG_FIELD[] = new String[]{ATTR_DATA_TYPE,ATTR_MAX,ATTR_MAX_LENGTH,ATTR_MIN,ATTR_MIN_LENGTH,ATTR_NAME,ATTR_TABLE,ATTR_USAGE};
	public static final String ATTR_DEF_TAG_COMPONENT[] = new String[]{ATTR_DATA_TYPE,ATTR_MAX_LENGTH,ATTR_MIN_LENGTH,ATTR_NAME,ATTR_TABLE,ATTR_USAGE};
	public static final String ATTR_DEF_TAG_SUB_COMPONENT[] = new String[]{ATTR_DATA_TYPE,ATTR_MAX_LENGTH,ATTR_MIN_LENGTH,ATTR_NAME,ATTR_TABLE,ATTR_USAGE};
	public static final String ATTR_DEF_TAG_CONFORMANCE_STAT[] = new String[]{ATTR_PROFILE,ATTR_ID};
	public static String ATTR_DEF_TAG_ENG_DESC[] = ATTR_DEF_EMPTY;
	public static String ATTR_DEF_TAG_ASSERTION[] = ATTR_DEF_EMPTY;
	public static final String ATTR_DEF_TAG_PLAIN_TEXT[] = new String[]{ATTR_LOCATION,ATTR_VALUE};
	public static String ATTR_DEF_TAG_PREDICATE[] = ATTR_DEF_EMPTY;
	public static String ATTR_DEF_TAG_CONDITION[] = ATTR_DEF_EMPTY;
	public static String ATTR_DEF_TAG_NOT[] = ATTR_DEF_EMPTY;
	public static String ATTR_DEF_TAG_AND[] = ATTR_DEF_EMPTY;
	public static String ATTR_DEF_TAG_OR[] = ATTR_DEF_EMPTY;
	public static String ATTR_DEF_TAG_IF[] = ATTR_DEF_EMPTY;
	public static final String ATTR_DEF_TAG_VALUED[] = new String[]{ATTR_LOCATION};
	public static final String ATTR_DEF_TAG_REGEX[] = new String[]{ATTR_LOCATION,ATTR_REGEX};
	public static final String ATTR_DEF_TAG_LIST[] = new String[]{ATTR_CSV,ATTR_LOCATION};
	
	//constant string values
	public static final String INFORMATIVE = "Informative";
	public static final String MSG_ACK_MODE_IMMEDIATE = "Immediate";
	public static final String XML_COMMENT = "Generated using Implementation Guide Authoring Management Tool, available from NIST ITL.";
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
	public static final String UNKOWN_ATTR_VALUE = "{unkown_value}";
	
}
