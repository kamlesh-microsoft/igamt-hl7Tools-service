/*
 * NIST IGAMT
 *
 * This code was produced by the National Institute of Standards and
 * Technology (NIST). See the "nist.disclaimer" file given in the distribution
 * for information on the use and redistribution of this software.
 */
package gov.nist.healthcare.hl7tools.service.util;

import gov.nist.healthcare.hl7tools.domain.CodeTable;
import gov.nist.healthcare.hl7tools.domain.Component;
import gov.nist.healthcare.hl7tools.domain.ConformanceStatement;
import gov.nist.healthcare.hl7tools.domain.Datatype;
import gov.nist.healthcare.hl7tools.domain.Element;
import gov.nist.healthcare.hl7tools.domain.ElementType;
import gov.nist.healthcare.hl7tools.domain.Field;
import gov.nist.healthcare.hl7tools.domain.Message;
import gov.nist.healthcare.hl7tools.domain.MetaData;
import gov.nist.healthcare.hl7tools.domain.Predicate;
import gov.nist.healthcare.hl7tools.domain.Segment;
import java.util.ArrayList;
import java.util.List;

/**
 * This class has copy methods for various HL7 objects.
 * 
 * @author Rizwan Tanoli (AEGIS.net)
 */
public class CopierUtil {

    public static Message copy(Message toCopy) {

        Message temp = new Message();

        temp.setChildren(copy(toCopy.getChildren()));
        temp.setDescription(toCopy.getDescription());
        temp.setEventDescription(toCopy.getEventDescription());
        temp.setKey(toCopy.getKey());
        temp.setName(toCopy.getName());
        temp.setSection(toCopy.getSection());
        temp.invalidateCache();

        return temp;

    }

    public static Element copy(Element toCopy) {

        Element temp = new Element();

        temp.setChildren(copy(toCopy.getChildren()));
        temp.setConformanceStatementList(copyConformanceStatements(toCopy.getConformanceStatementList()));
        temp.setComment(toCopy.getComment());
        temp.setMax(toCopy.getMax());
        temp.setMin(toCopy.getMin());
        temp.setName(toCopy.getName());
        temp.setPosition(toCopy.getPosition());
        temp.setPredicate(copy(toCopy.getPredicate()));
        temp.setType(toCopy.getType());
        temp.setUsage(toCopy.getUsage());
        if (toCopy.getType().toString().equalsIgnoreCase(
                ElementType.SEGEMENT.toString())) {
            temp.setSegment(copy(toCopy.getSegment()));
        }

        return temp;
    }

    public static List<Element> copy(List<Element> toCopy) {

        if (toCopy != null && toCopy.size() > 0) {
            List<Element> temp = new ArrayList<Element>();

            for (Element e : toCopy) {
                temp.add(copy(e));
            }

            return temp;
        } else {
            return null;
        }
    }

    public static Segment copy(Segment toCopy) {

        if (toCopy != null) {
            Segment s = new Segment();

            s.setComment(toCopy.getComment());
            s.setDescription(toCopy.getDescription());
            s.setFields(copyFields(toCopy.getFields()));
            // s.setId(toCopy.getId());
            s.setKey(toCopy.getKey());
            s.setName(toCopy.getName());
            s.setRoot(toCopy.getRoot());
            s.setSection(toCopy.getSection());

            return s;
        } else {
            return null;
        }
    }

    public static List<Field> copyFields(List<Field> fields) {

        if (fields != null && fields.size() > 0) {
            List<Field> f = new ArrayList<Field>();
            for (Field ff : fields) {
                f.add(copy(ff));
            }

            return f;
        } else {
            return null;
        }
    }

    public static Field copy(Field toCopy) {

        if (toCopy != null) {
            Field f = new Field();
            f.setCodeTable(copy(toCopy.getCodeTable()));
            f.setComment(toCopy.getComment());
            f.setConfLength(toCopy.getConfLength());
            f.setConformanceStatementList(copyConformanceStatements(toCopy.getConformanceStatementList()));
            f.setDatatype(copy(toCopy.getDatatype()));
            f.setDescription(toCopy.getDescription());
            f.setItemNo(toCopy.getItemNo());
            f.setMax(toCopy.getMax());
            f.setMaxLength(toCopy.getMaxLength());
            f.setMin(toCopy.getMin());
            f.setMinLength(toCopy.getMinLength());
            f.setPosition(toCopy.getPosition());
            f.setPredicate(copy(toCopy.getPredicate()));
            f.setTruncationAllowed(toCopy.getTruncationAllowed());
            f.setUsage(toCopy.getUsage());

            return f;
        } else {
            return null;
        }
    }

    public static Datatype copy(Datatype datatype) {

        if (datatype != null) {
            Datatype dt = new Datatype();

            dt.setComment(datatype.getComment());
            dt.setComponents(copyComponents(datatype.getComponents()));
            dt.setDescription(datatype.getDescription());

            dt.setKey(datatype.getKey());
            dt.setName(datatype.getName());
            dt.setRoot(datatype.getRoot());
            dt.setSection(datatype.getSection());
            return dt;
        } else {
            return null;
        }
    }

    public static List<Component> copyComponents(List<Component> components) {

        if (components != null) {
            List<Component> c = new ArrayList<Component>();

            for (Component cc : components) {
                c.add(copy(cc));
            }
            return c;
        } else {
            return null;
        }
    }

    public static CodeTable copy(CodeTable codeTable) {

        return codeTable;

    }

    public static Component copy(Component toCopy) {

        if (toCopy != null) {

            Component c = new Component();

            c.setCodeTable(copy(toCopy.getCodeTable()));
            c.setComment(toCopy.getComment());
            c.setConfLength(toCopy.getConfLength());
            c.setConformanceStatementList(copyConformanceStatements(toCopy.getConformanceStatementList()));
            c.setDatatype(copy(toCopy.getDatatype()));
            c.setDescription(toCopy.getDescription());
            c.setMaxLength(toCopy.getMaxLength());
            c.setMinLength(toCopy.getMinLength());
            c.setPosition(toCopy.getPosition());
            c.setPredicate(copy(toCopy.getPredicate()));
            c.setTruncationAllowed(toCopy.getTruncationAllowed());
            c.setUsage(toCopy.getUsage());

            return c;
        } else {
            return null;
        }
    }

    public static Predicate copy(Predicate predicate) {

        if (predicate != null) {
            Predicate p = new Predicate();
            p.setDescription(predicate.getDescription());
            p.setFalseUsage(predicate.getFalseUsage());
            p.setPredicateDetails(predicate.getPredicateDetails());
            p.setTrueUsage(predicate.getTrueUsage());

            return p;
        } else {
            return null;
        }
    }

    public static List<ConformanceStatement> copyConformanceStatements(
            List<ConformanceStatement> csl) {

        if (csl != null && csl.size() > 0) {

            List<ConformanceStatement> l = new ArrayList<ConformanceStatement>();

            for (ConformanceStatement ct : csl) {
                l.add(copy(ct));
            }

            return l;
        } else {
            return null;
        }
    }

    public static ConformanceStatement copy(ConformanceStatement ct) {

        if (ct != null) {

            ConformanceStatement c = new ConformanceStatement();

            c.setDescription(ct.getDescription());
            c.setId(ct.getId());
            c.setStatementDetails(ct.getStatementDetails());

            return c;
        } else {
            return null;
        }
    }

    public static MetaData copy(MetaData toCopy) {

        MetaData md = new MetaData();

        md.setAdheranceType(toCopy.getAdheranceType());
        md.setConformanceType(toCopy.getConformanceType());
        md.setHl7Event(toCopy.getHl7Event());
        md.setHl7RulesSchema(toCopy.getHl7RulesSchema());
        md.setHl7Version(toCopy.getHl7Version());

        return md;

    }

}
