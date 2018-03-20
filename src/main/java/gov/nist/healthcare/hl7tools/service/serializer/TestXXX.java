package gov.nist.healthcare.hl7tools.service.serializer;

import java.io.IOException;

import gov.nist.healthcare.hl7tools.domain.Component;
import gov.nist.healthcare.hl7tools.domain.Datatype;
import gov.nist.healthcare.hl7tools.domain.ElementType;
import gov.nist.healthcare.hl7tools.domain.Field;
import gov.nist.healthcare.hl7tools.domain.Message;
import gov.nist.healthcare.hl7tools.domain.Segment;
import gov.nist.healthcare.hl7tools.service.HL7DBMockServiceImpl;
import gov.nist.healthcare.hl7tools.service.HL7DBService;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Serializer;

public class TestXXX {

  public static void prettyPrint(Element root) throws IOException {
    Serializer serializer = new Serializer(System.out, "UTF-8");
    serializer.setIndent(4);
    serializer.setMaxLength(400);
    serializer.write(new Document(root));
  }

  private static void print(Message m) {
    System.out.println(m);
    if (m.getChildren() != null) {
      for (gov.nist.healthcare.hl7tools.domain.Element e : m.getChildren())
        print(e, "\t");
    }
  }

  private static void print(gov.nist.healthcare.hl7tools.domain.Element e, String tab) {
    System.out.println(tab + e);
    if (e.getType() == ElementType.SEGEMENT)
      print(e.getSegment(), tab + "\t");
    if (e.getChildren() != null) {
      for (gov.nist.healthcare.hl7tools.domain.Element ee : e.getChildren())
        print(ee, tab);
    }
  }

  private static void print(Segment s, String tab) {
    if (s.getFields() != null) {
      for (Field f : s.getFields())
        print(f, tab + "\t");
    }
  }

  private static void print(Field f, String tab) {
    System.out.println(tab + f);
    print(f.getDatatype(), tab);
  }

  private static void print(Datatype dt, String tab) {
    if (dt.getComponents() == null || dt.getComponents().isEmpty())
      return;
    if (dt.getComponents() != null) {
      for (Component c : dt.getComponents())
        print(c, tab + "\t");
    }
  }

  private static void print(Component c, String tab) {
    System.out.println(tab + c);
  }

  public static void main(String[] arg) {
    HL7DBService service = new HL7DBMockServiceImpl();

    try {

      XMLSerializer serializer = new XMLSerializer();
      Message m = service.getMessage("2.5.1", "VXU_V04");

      ProfileSchemaVersion schemaVersion = ProfileSchemaVersion.V271;
      Element result = serializer.serialize(m, "2.5.1", schemaVersion);

      // Element serResult = serializer.serialize(m, schemaVersion);

      prettyPrint(result);

      // XMLDeserializer deserializer = new XMLDeserializer();

      // m = deserializer.deserialize( new
      // ByteArrayInputStream(result.toXML().getBytes()) , schemaVersion);

      // print(m);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
