package gov.nist.healthcare.hl7tools.service.serializer;

import static gov.nist.healthcare.hl7tools.service.serializer.XMLUtils.isSchemaPriorToV29;
import static gov.nist.healthcare.hl7tools.service.serializer.XMLUtils.tagName;
import gov.nist.healthcare.hl7tools.domain.CodeTable;
import gov.nist.healthcare.hl7tools.domain.Component;
import gov.nist.healthcare.hl7tools.domain.ConformanceStatement;
import gov.nist.healthcare.hl7tools.domain.Datatype;
import gov.nist.healthcare.hl7tools.domain.DatatypeLibrary;
import gov.nist.healthcare.hl7tools.domain.EBoolean;
import gov.nist.healthcare.hl7tools.domain.ElementType;
import gov.nist.healthcare.hl7tools.domain.Field;
import gov.nist.healthcare.hl7tools.domain.Message;
import gov.nist.healthcare.hl7tools.domain.Predicate;
import gov.nist.healthcare.hl7tools.domain.Segment;
import gov.nist.healthcare.hl7tools.domain.SegmentLibrary;
import gov.nist.healthcare.hl7tools.domain.StatementDetails;
import gov.nist.healthcare.hl7tools.domain.Usage;
import java.util.List;
import nu.xom.Attribute;
import nu.xom.Element;

public class XMLSerializer {
    private Message message;

    public Element serialize(Message m, String hl7Version,
            ProfileSchemaVersion schemaVersion) {
        Element root = new Element("HL7v2xConformanceProfile");
        Attribute hl7VersionAtt = new Attribute("HL7Version", hl7Version);
        root.addAttribute(hl7VersionAtt);
        Element staticDef = serialize(m, schemaVersion);
        this.message = m;
        root.appendChild(staticDef);
        return root;
    }

    private Element serialize(Message m, ProfileSchemaVersion schemaVersion) {
        Element tmp = new Element("HL7v2xStaticDef");
        tmp.addAttribute(new Attribute("MsgStructID", m.getName())); // FIXME
                                                                     // [Salifou]
                                                                     // MsgStructID
                                                                     // is not
                                                                     // required
                                                                     // in V25
        tmp.addAttribute(new Attribute("MsgType", m.getMessageType()));
        // FIXME: [Salifou] Some messages don't have event type but the schema
        // require this attribute
        tmp.addAttribute(new Attribute("EventType", m.getEvent()));
        // FIXME: [Salifou] Some messages don't have event but the schema
        // require this attribute
        tmp.addAttribute(new Attribute("EventDesc",
                m.getEventDescription() != null ? m.getEventDescription() : ""));
        if (m.getChildren() != null) {
            for (gov.nist.healthcare.hl7tools.domain.Element e : m.getChildren())
                tmp.appendChild(serialize(e, schemaVersion));
        }
        // Schema starting from V29 need a Datatypes section
        if (!isSchemaPriorToV29(schemaVersion)) {
            Element datatypes = new Element("Datatypes");
            for (Datatype dt : m.getDatatypeSet())
                datatypes.appendChild(serialize(dt, schemaVersion));
            tmp.appendChild(datatypes);
        }
        return tmp;
    }

    public Element serialize(DatatypeLibrary library, String hl7Version,
            ProfileSchemaVersion schemaVersion) {
        Element rootElement = new Element("DatatypeLibrary");
        rootElement.addAttribute(new Attribute("HL7Version", hl7Version));

        if (!library.isEmpty()) {
            for (Datatype datatype : library.values()) {
                rootElement.appendChild(serialize(datatype, schemaVersion));
            }
        }
        return rootElement;
    }

    public Element serialize(SegmentLibrary library, String hl7Version,
            ProfileSchemaVersion schemaVersion) {
        Element rootElement = new Element("SegmentLibrary");
        rootElement.addAttribute(new Attribute("HL7Version", hl7Version));
        if (!library.isEmpty()) {
            for (Segment segment : library.values()) {
                rootElement.appendChild(serialize(segment, schemaVersion));
            }
        }
        return rootElement;
    }

    private Element serialize(
            gov.nist.healthcare.hl7tools.domain.Segment segment,
            ProfileSchemaVersion schemaVersion) {
        Element segNode = new Element("Segment");
        segNode.addAttribute(new Attribute("Name", segment.getName()));
        segNode.addAttribute(new Attribute("LongName", segment.getDescription()));
        if (segment.getFields() != null)
            for (Field f : segment.getFields())
                segNode.appendChild(serialize(f, schemaVersion));
        return segNode;
    }

    private Element serialize(gov.nist.healthcare.hl7tools.domain.Element e,
            ProfileSchemaVersion schemaVersion) {
        Element tmp = new Element(tagName(e, schemaVersion));
        tmp.addAttribute(new Attribute("Name", e.getName()));
        tmp.addAttribute(new Attribute("Usage",
                e.getUsage().getValue().startsWith("C")
                        || e.getPredicate() != null ? Usage.C.name()
                        : e.getUsage().getValue()));
        tmp.addAttribute(new Attribute("Min", e.getMin() + ""));
        tmp.addAttribute(new Attribute("Max", e.getMax()));
        handlePredicateAndConformanceStatements(tmp, e.getPredicate(),
                e.getConformanceStatementList(), schemaVersion);
        if (e.getType() == ElementType.SEGEMENT) {
            updateSegmentNode(tmp, e, schemaVersion);
        } else {
            for (gov.nist.healthcare.hl7tools.domain.Element ee : e.getChildren())
                tmp.appendChild(serialize(ee, schemaVersion));
        }
        return tmp;
    }

    private void updateSegmentNode(Element segNode,
            gov.nist.healthcare.hl7tools.domain.Element e,
            ProfileSchemaVersion schemaVersion) {
        segNode.addAttribute(new Attribute("LongName",
                e.getSegment().getDescription()));
        if (e.getSegment().getFields() != null)
            for (Field f : e.getSegment().getFields())
                segNode.appendChild(serialize(f, schemaVersion));
    }

    private Element serialize(Field f, ProfileSchemaVersion schemaVersion) {
        Element tmp = new Element("Field");
        tmp.addAttribute(new Attribute("Name", f.getDescription()));
        tmp.addAttribute(new Attribute("Datatype", f.getDatatype().getName()));
        tmp.addAttribute(new Attribute("Usage",
                f.getUsage().getValue().startsWith("C")
                        || f.getPredicate() != null ? Usage.C.name()
                        : f.getUsage().getValue()));
        tmp.addAttribute(new Attribute("Min", f.getMin() + ""));
        tmp.addAttribute(new Attribute("Max", f.getMax() + ""));
        handleLengthAttributes(tmp, f.getMinLength(), f.getMaxLength(),
                f.getConfLength(), f.getTruncationAllowed(), schemaVersion);
        handleCodeTableAttribute(tmp, f.getCodeTable(), schemaVersion);
        if (f.getItemNo() != null)
            tmp.addAttribute(new Attribute("ItemNo", f.getItemNo()));
        handlePredicateAndConformanceStatements(tmp, f.getPredicate(),
                f.getConformanceStatementList(), schemaVersion);
        // Profile schema prior to V29 need components and subcomponents
        if (isSchemaPriorToV29(schemaVersion)
                && (f.getDatatype().getComponents() != null && !f.getDatatype().getComponents().isEmpty())) {
            for (Component child : f.getDatatype().getComponents())
                tmp.appendChild(serialize(0, child, schemaVersion));
        }
        return tmp;
    }

    private Element serialize(Datatype dt, ProfileSchemaVersion schemaVersion) {
        Element tmp = new Element("Datatype");
        tmp.addAttribute(new Attribute("Name", dt.getName()));
        tmp.addAttribute(new Attribute("Description", dt.getDescription()));
        if (dt.getComponents() != null && !dt.getComponents().isEmpty())
            for (Component c : dt.getComponents())
                tmp.appendChild(serialize(0, c, schemaVersion));
        return tmp;
    }

    private Element serialize(int level, Component c,
            ProfileSchemaVersion schemaVersion) {
        Element tmp = level == 0 ? new Element("Component") : new Element(
                "SubComponent");
        tmp.addAttribute(new Attribute("Name", c.getDescription()));
        tmp.addAttribute(new Attribute("Datatype", c.getDatatype().getName()));
        tmp.addAttribute(new Attribute("Usage",
                c.getUsage().getValue().startsWith("C")
                        || c.getPredicate() != null ? Usage.C.name()
                        : c.getUsage().getValue()));
        handleLengthAttributes(tmp, c.getMinLength(), c.getMaxLength(),
                c.getConfLength(), c.getTruncationAllowed(), schemaVersion);
        handleCodeTableAttribute(tmp, c.getCodeTable(), schemaVersion);
        handlePredicateAndConformanceStatements(tmp, c.getPredicate(),
                c.getConformanceStatementList(), schemaVersion);
        // For older schemas new need subcomponents
        if (schemaVersion != ProfileSchemaVersion.V29
                && level == 0
                && (c.getDatatype().getComponents() != null && !c.getDatatype().getComponents().isEmpty())) {
            for (Component child : c.getDatatype().getComponents())
                tmp.appendChild(serialize(1, child, schemaVersion));
        }
        return tmp;
    }

    private void handlePredicateAndConformanceStatements(Element tmp,
            Predicate predicate, List<ConformanceStatement> csList,
            ProfileSchemaVersion schemaVersion) {
        if (predicate != null)
            addPredicate(tmp, predicate, schemaVersion);
        if (csList != null)
            for (ConformanceStatement cs : csList)
                addConformanceStatements(tmp, cs, schemaVersion);
    }

    private void addPredicate(Element tmp, Predicate p,
            ProfileSchemaVersion schemaVersion) {
    	
    	// FIXME: [WOO] CS is disabled for demo.
        if (schemaVersion == ProfileSchemaVersion.V25)
            return;
        Attribute trueUsage = new Attribute("PredicateTrueUsage",
                p.getTrueUsage().name());
        Attribute falseUsage = new Attribute("PredicateFalseUsage",
                p.getFalseUsage().name());
        Element predicate = new Element("Predicate");
        if (isSchemaPriorToV29(schemaVersion)) {
            tmp.addAttribute(trueUsage);
            tmp.addAttribute(falseUsage);
            Element el = new Element("EnglishDescription");
            el.appendChild(p.getDescription());
            predicate.appendChild(el);
        } else {
            predicate.addAttribute(trueUsage);
            predicate.addAttribute(falseUsage);
            Element el = new Element("Description");
            el.appendChild(p.getDescription());
            predicate.appendChild(el);
        }
        Element cpWithAssertion = ((StatementDetails) p.getPredicateDetails()).generateAssertionElm(predicate, "cp");
        
        if(cpWithAssertion == null){
/*        	System.out.println("-------------Deleted CP--------------");
        	System.out.println("[Location]"+  ((StatementDetails)p.getPredicateDetails()).getTargetObjectName() + "." +((StatementDetails)p.getPredicateDetails()).getLocation());
        	System.out.println("[DESC]"+ p.getDescription());*/
        }else {
        	tmp.appendChild(cpWithAssertion);
        }
    }

    private void addConformanceStatements(Element tmp, ConformanceStatement cs,
            ProfileSchemaVersion schemaVersion) {
    	// FIXME: [WOO] CS is disabled for demo.
    	
        if (schemaVersion == ProfileSchemaVersion.V25)
            return;
        Element confStmt = new Element("ConformanceStatement");
        // FIXME: [salifou] ID and id are used depending on the schema will need
        // to fix this.
        // Code that parses the profile for the core validation also needs to be
        // updated.
        confStmt.addAttribute(new Attribute("id", cs.getName()));
        Element desciption = isSchemaPriorToV29(schemaVersion) ? new Element(
                "EnglishDescription") : new Element("Description");
        desciption.appendChild(cs.getDescription());
        confStmt.appendChild(desciption);
        Element csWithAssertion = ((StatementDetails) cs.getStatementDetails()).generateAssertionElm(confStmt, "cs");
        
        if(csWithAssertion == null){
/*        	System.out.println("-------------Deleted CS--------------");
        	System.out.println("[NAME]"+ cs.getName());
        	System.out.println("[Location]"+  ((StatementDetails)cs.getStatementDetails()).getTargetObjectName() + "." +((StatementDetails)cs.getStatementDetails()).getLocation());
        	System.out.println("[DESC]"+ cs.getDescription());*/
        }else {
        	tmp.appendChild(csWithAssertion);
        }
        
        
        
    }

    private void handleLengthAttributes(Element tmp, int minLength,
            String maxLength, String confLength, EBoolean truncation,
            ProfileSchemaVersion schemaVersion) {
        if (schemaVersion == ProfileSchemaVersion.V25)
            // [changed by Harold]to fix previous generated profiles that have
            // empty max length
            tmp.addAttribute(new Attribute("Length", maxLength != null
                    && !"".equals(maxLength) ? maxLength : "*"));

        else {
            tmp.addAttribute(new Attribute("MinLength", minLength + ""));
            // [changed by Harold]to fix previous generated profiles that have
            // empty max length
            tmp.addAttribute(new Attribute("MaxLength", maxLength != null
                    && !"".equals(maxLength) ? maxLength : "*"));
            if (confLength != null)
                tmp.addAttribute(new Attribute("ConformanceLength", confLength
                        + ""));
            if (truncation != EBoolean.NA)
                tmp.addAttribute(new Attribute("Truncation",
                        truncation == EBoolean.YES ? "true" : "false"));
        }
    }

    private void handleCodeTableAttribute(Element tmp, CodeTable ct,
            ProfileSchemaVersion schemaVersion) {
        if (ct == null)
            return;
        if (isSchemaPriorToV29(schemaVersion))
            tmp.addAttribute(new Attribute("Table", ct.getName()));
        // FIXME: [Salifou] Handle code table for v29
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
