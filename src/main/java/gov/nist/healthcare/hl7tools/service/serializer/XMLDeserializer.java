package gov.nist.healthcare.hl7tools.service.serializer;

import static gov.nist.healthcare.hl7tools.service.serializer.XMLUtils.confLength;
import static gov.nist.healthcare.hl7tools.service.serializer.XMLUtils.getSchema;
import static gov.nist.healthcare.hl7tools.service.serializer.XMLUtils.isSchemaPriorToV29;
import static gov.nist.healthcare.hl7tools.service.serializer.XMLUtils.maxLength;
import static gov.nist.healthcare.hl7tools.service.serializer.XMLUtils.minLength;
import static gov.nist.healthcare.hl7tools.service.serializer.XMLUtils.truncation;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import gov.nist.healthcare.hl7tools.domain.Component;
import gov.nist.healthcare.hl7tools.domain.ConformanceStatement;
import gov.nist.healthcare.hl7tools.domain.Datatype;
import gov.nist.healthcare.hl7tools.domain.ElementType;
import gov.nist.healthcare.hl7tools.domain.Field;
import gov.nist.healthcare.hl7tools.domain.Message;
import gov.nist.healthcare.hl7tools.domain.Predicate;
import gov.nist.healthcare.hl7tools.domain.Segment;
import gov.nist.healthcare.hl7tools.domain.Usage;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;

public class XMLDeserializer {

  private Map<String, Datatype> datatypeMap;

  public Message deserialize(InputStream xml, ProfileSchemaVersion schemaVersion) throws Exception {
    if (schemaVersion != ProfileSchemaVersion.V29)
      throw new Exception(
          "Deserialization for Profile Schema prior to version 2.9 is not yet implemented.");
    Document document = build(xml, schemaVersion);
    Element staticDef = document.getRootElement();
    return deserialize(staticDef, schemaVersion);
  }

  private Message deserialize(Element e, ProfileSchemaVersion schemaVersion) throws Exception {
    Message m = new Message();
    m.setName(e.getAttributeValue("MsgStructID"));
    m.setDescription(""); // FIXME Message type description is not in the schema
    String eventDesc = e.getAttributeValue("EventDesc");
    m.setEventDescription(eventDesc != null ? eventDesc : "");
    datatypeMap = buildPartialDatatypeLibrary(e, schemaVersion);
    updateDatatypeComponents(datatypeMap, e, schemaVersion);
    m.setChildren(processElements(e.getChildElements(), schemaVersion));
    return m;
  }

  private List<gov.nist.healthcare.hl7tools.domain.Element> processElements(Elements children,
      ProfileSchemaVersion schemaVersion) {
    List<gov.nist.healthcare.hl7tools.domain.Element> result =
        new ArrayList<gov.nist.healthcare.hl7tools.domain.Element>();
    int position = 1;
    for (int i = 0; i < children.size(); i++) {
      Element tmp = children.get(i);
      if ("Segment".equals(tmp.getLocalName()) || "Group".equals(tmp.getLocalName())
          || "Choice".equals(tmp.getLocalName()) || "SegGroup".equals(tmp.getLocalName()))
        result.add(processElement(tmp, position++, schemaVersion));
    }
    return result;
  }

  private gov.nist.healthcare.hl7tools.domain.Element processElement(Element e, int position,
      ProfileSchemaVersion schemaVersion) {
    gov.nist.healthcare.hl7tools.domain.Element ee =
        new gov.nist.healthcare.hl7tools.domain.Element();
    ee.setPosition(position);
    ee.setName(e.getAttributeValue("Name"));
    ee.setMax(e.getAttributeValue("Max"));
    ee.setMin(new Integer(e.getAttributeValue("Min")));
    ee.setUsage(Usage.valueOf(e.getAttributeValue("Usage")));
    ee.setConformanceStatementList(
        processConformanceStatements(e.getChildElements("ConformanceStatement")));
    Usage trueUsage = isSchemaPriorToV29(schemaVersion)
        ? Usage.valueOf(e.getAttributeValue("PredicateTrueUsage")) : null;
    Usage falseUsage = isSchemaPriorToV29(schemaVersion)
        ? Usage.valueOf(e.getAttributeValue("PredicateFalseUsage")) : null;
    ee.setPredicate(processPredicate(e.getFirstChildElement("Predicate"), trueUsage, falseUsage,
        schemaVersion));
    if ("Segment".equals(e.getLocalName())) {
      ee.setType(ElementType.SEGEMENT);
      ee.setSegment(processSegment(e, schemaVersion));
    } else {
      ElementType type = "Choice".equals(e.getLocalName()) ? ElementType.CHOICE : ElementType.GROUP;
      ee.setType(type);
      ee.setChildren(processElements(e.getChildElements(), schemaVersion));
    }
    return ee;
  }

  private Segment processSegment(Element e, ProfileSchemaVersion schemaVersion) {
    Segment s = new Segment();
    s.setName(e.getAttributeValue("Name"));
    s.setDescription(e.getAttributeValue("LongName"));
    List<Field> fields = new ArrayList<Field>();
    Elements fieldElms = e.getChildElements("Field");
    for (int i = 0; i < fieldElms.size(); i++)
      fields.add(processField(fieldElms.get(i), i + 1, schemaVersion));
    s.setFields(fields.isEmpty() ? null : fields);
    return s;
  }

  private Field processField(Element e, int position, ProfileSchemaVersion schemaVersion) {
    Field f = new Field();
    // f.setCodeTable(codeTable); //FIXME
    f.setConfLength(confLength(e, schemaVersion));
    f.setDatatype(getDatatype(e.getAttributeValue("Datatype")));
    f.setDescription(e.getAttributeValue("Name"));
    f.setItemNo(e.getAttributeValue("ItemNo"));
    f.setMax(e.getAttributeValue("Max"));
    f.setMaxLength(maxLength(e, schemaVersion));
    f.setMin(new Integer(e.getAttributeValue("Min")));
    f.setMinLength(minLength(e, schemaVersion));
    f.setPosition(position);
    f.setTruncationAllowed(truncation(e, schemaVersion));
    f.setUsage(Usage.valueOf(e.getAttributeValue("Usage")));
    f.setConformanceStatementList(
        processConformanceStatements(e.getChildElements("ConformanceStatement")));
    Usage trueUsage = isSchemaPriorToV29(schemaVersion)
        ? Usage.valueOf(e.getAttributeValue("PredicateTrueUsage")) : null;
    Usage falseUsage = isSchemaPriorToV29(schemaVersion)
        ? Usage.valueOf(e.getAttributeValue("PredicateFalseUsage")) : null;
    f.setPredicate(processPredicate(e.getFirstChildElement("Predicate"), trueUsage, falseUsage,
        schemaVersion));
    return f;
  }

  private Component processComponent(Element e, int position, ProfileSchemaVersion schemaVersion) {
    Component c = new Component();
    // c.setCodeTable(codeTable); //FIXME
    c.setConfLength(confLength(e, schemaVersion));
    c.setDatatype(getDatatype(e.getAttributeValue("Datatype")));
    c.setDescription(e.getAttributeValue("Name"));
    c.setMaxLength(maxLength(e, schemaVersion));
    c.setMinLength(minLength(e, schemaVersion));
    c.setPosition(position);
    c.setTruncationAllowed(truncation(e, schemaVersion));
    c.setUsage(Usage.valueOf(e.getAttributeValue("Usage")));
    c.setConformanceStatementList(
        processConformanceStatements(e.getChildElements("ConformanceStatement")));
    Usage trueUsage = isSchemaPriorToV29(schemaVersion)
        ? Usage.valueOf(e.getAttributeValue("PredicateTrueUsage")) : null;
    Usage falseUsage = isSchemaPriorToV29(schemaVersion)
        ? Usage.valueOf(e.getAttributeValue("PredicateFalseUsage")) : null;
    c.setPredicate(processPredicate(e.getFirstChildElement("Predicate"), trueUsage, falseUsage,
        schemaVersion));
    return c;
  }

  private Predicate processPredicate(Element predicate, Usage trueUsage, Usage falseUsage,
      ProfileSchemaVersion schemaVersion) {
    if (predicate == null)
      return null;
    Predicate p = new Predicate();
    p.setDescription(predicate.getFirstChildElement("Description").getValue());
    p.setTrueUsage(trueUsage != null ? trueUsage
        : Usage.valueOf(predicate.getAttributeValue("PredicateTrueUsage")));
    p.setFalseUsage(falseUsage != null ? falseUsage
        : Usage.valueOf(predicate.getAttributeValue("PredicateFalseUsage")));
    p.setPredicateDetails(null); // FIXME Not in the schemas
    return p;
  }

  private List<ConformanceStatement> processConformanceStatements(Elements confStmts) {
    if (confStmts == null)
      return null;
    List<ConformanceStatement> result = new ArrayList<ConformanceStatement>();
    for (int i = 0; i < confStmts.size(); i++) {
      ConformanceStatement cs = new ConformanceStatement();
      cs.setId(confStmts.get(i).getAttributeValue(""));
      cs.setDescription(confStmts.get(i).getFirstChildElement("Description").getValue());
      // cs.setStatementDetails(null); //FIXME Not in the schema
      result.add(cs);
    }
    return result;
  }

  private Map<String, Datatype> buildPartialDatatypeLibrary(Element staticDef,
      ProfileSchemaVersion schemaVersion) {
    Map<String, Datatype> dtLib = new HashMap<String, Datatype>();
    if (schemaVersion == ProfileSchemaVersion.V29) {
      Elements datatypes = staticDef.getFirstChildElement("Datatypes").getChildElements("Datatype");
      for (int i = 0; i < datatypes.size(); i++) {
        Datatype dt = new Datatype();
        dt.setName(datatypes.get(i).getAttributeValue("Name"));
        dt.setDescription(datatypes.get(i).getAttributeValue("Description"));
        dtLib.put(dt.getName(), dt);
      }
      return dtLib;
    } else {
      // FIXME
    }
    return null;
  }

  private void updateDatatypeComponents(Map<String, Datatype> dtLib, Element staticDef,
      ProfileSchemaVersion schemaVersion) {
    if (schemaVersion == ProfileSchemaVersion.V29) {
      Elements datatypes = staticDef.getFirstChildElement("Datatypes").getChildElements("Datatype");
      for (int i = 0; i < datatypes.size(); i++) {
        String datatypeName = datatypes.get(i).getAttributeValue("Name");
        Datatype dt = dtLib.get(datatypeName);
        List<Component> components = new ArrayList<Component>();
        Elements componentElms = datatypes.get(i).getChildElements("Component");
        for (int j = 0; j < componentElms.size(); j++)
          components.add(processComponent(componentElms.get(j), j + 1, schemaVersion));
        dt.setComponents(components.isEmpty() ? null : components);
      }
    } else {
      // FIXME
    }
  }


  private Datatype getDatatype(String dt) {
    if (datatypeMap.containsKey(dt))
      return datatypeMap.get(dt);
    throw new IllegalArgumentException("[Error] Can't find datatype '" + dt + "' in the library");
  }

  // Parser and return XOM Document corresponding to the XML
  private Document build(InputStream xml, ProfileSchemaVersion schemaVersion) throws Exception {
    try {
      XMLReader reader = createSAXReader(schemaVersion);
      Builder builder = new Builder(reader);
      return builder.build(xml);
    } catch (Exception e) {
      throw new Exception(
          "[Error] An error occurs while parsing or validating the XML document. " + e.getMessage(),
          e.getCause());
    }
  }

  // SAX REader configuration
  private XMLReader createSAXReader(ProfileSchemaVersion schemaVersion)
      throws SAXException, ParserConfigurationException {
    InputStream xsd = getSchema(schemaVersion);
    SAXParserFactory factory = SAXParserFactory.newInstance();
    factory.setNamespaceAware(true);
    // Schema validation
    // factory.setValidating(true);
    SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
    factory.setSchema(schemaFactory.newSchema(new Source[] {new StreamSource(xsd)}));

    SAXParser parser = factory.newSAXParser();
    XMLReader reader = parser.getXMLReader();
    // Error handling
    reader.setErrorHandler(new SAXErrorHandler());
    return reader;
  }

}
