package gov.nist.healthcare.hl7tools.service.util.mock;

import gov.nist.healthcare.hl7tools.domain.CodeTableLibrary;
import gov.nist.healthcare.hl7tools.domain.DatatypeLibrary;
import gov.nist.healthcare.hl7tools.domain.EBoolean;
import gov.nist.healthcare.hl7tools.domain.Field;
import gov.nist.healthcare.hl7tools.domain.Segment;
import gov.nist.healthcare.hl7tools.domain.SegmentLibrary;
import gov.nist.healthcare.hl7tools.service.util.mock.util.Convertor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SegmentLibraryBuilder {
	
	static Logger log = LoggerFactory.getLogger(SegmentLibraryBuilder.class);
	
	public static SegmentLibrary build(CodeTableLibrary codeTableLibrary, 
			DatatypeLibrary datatypeLibrary, 
			Map<String, gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Segment> map) throws Exception {
		SegmentLibrary library = new SegmentLibrary();
		for(gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Segment seg:  map.values())
			library.put(transform(seg, codeTableLibrary, datatypeLibrary));
		return library;
	}
	
	private static Segment transform(gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Segment seg, 
			CodeTableLibrary codeTableLibrary, DatatypeLibrary datatypeLibrary) {
		Segment s = new Segment();
		s.setKey(seg.getId());
		s.setName(seg.getId());
		s.setDescription(seg.getDescription());
		s.setSection(seg.getSection());
		if(seg.getFields() != null) {
			List<Field> fields = new ArrayList<Field>();
			for(gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Field fd : seg.getFields()) {
				Field f = new Field();
				f.setCodeTable(codeTableLibrary.get(fd.getTableId()));
				f.setConfLength(fd.getConfLength());
				f.setDatatype(datatypeLibrary.get(fd.getDatatype()));
				f.setDescription(fd.getDescription());
				f.setItemNo(fd.getDataElementId());
				f.setMax(fd.getMax());
				f.setMaxLength(Convertor.convertLength(fd.getMaxLength()));
				f.setMin(fd.getMin());
				f.setMinLength(fd.getMinLength());
				f.setPosition(fd.getPosition());
				f.setTruncationAllowed(EBoolean.valueOf( fd.getTruncation() ));
				f.setUsage(gov.nist.healthcare.hl7tools.domain.Usage.valueOf(fd.getUsage().name()));
				if (f.getDatatype() == null) {
					log.info("here");
				}
				fields.add(f);
				log.debug("f=" + f.getDescription()+ " f.getDatatype()=" + f.getDatatype());
			}
			s.setFields(fields);
		}
		return s;
	}
}
