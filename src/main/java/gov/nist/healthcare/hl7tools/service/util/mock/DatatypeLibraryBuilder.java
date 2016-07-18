package gov.nist.healthcare.hl7tools.service.util.mock;

import gov.nist.healthcare.hl7tools.domain.CodeTableLibrary;
import gov.nist.healthcare.hl7tools.domain.Component;
import gov.nist.healthcare.hl7tools.domain.Datatype;
import gov.nist.healthcare.hl7tools.domain.DatatypeLibrary;
import gov.nist.healthcare.hl7tools.domain.EBoolean;
import gov.nist.healthcare.hl7tools.domain.Usage;
import gov.nist.healthcare.hl7tools.service.util.mock.util.Convertor;
import net.sourceforge.plantuml.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatatypeLibraryBuilder {

	static Logger log = LoggerFactory.getLogger(DatatypeLibraryBuilder.class);
	
	public static DatatypeLibrary build(
			CodeTableLibrary codeTableLibrary,
			Map<String, gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Datatype> map)
			throws Exception {
		DatatypeLibrary library = new DatatypeLibrary();
		for (gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Datatype dtt : map
				.values())
			library.put(transform(dtt));
		updateComponent(library, codeTableLibrary, map);
		return library;
	}

	private static Datatype transform(
			gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Datatype dtt) {
		Datatype dt = new Datatype();
		dt.setKey(dtt.getId());
		dt.setName(dtt.getId());
		
		dt.setDescription(dtt.getDescription());
		dt.setSection(dtt.getSection());
		return dt;
	}

	private static void updateComponent(
			DatatypeLibrary library,
			CodeTableLibrary codeTableLibrary,
			Map<String, gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Datatype> map) {
		gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Datatype dtt;
		for (Datatype dt : library.values()) {
			dtt = map.get(dt.getKey());
			if (dtt.getComponents() != null) {
				List<Component> componentList = new ArrayList<Component>();
				for (gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Component cc : dtt
						.getComponents()) {
					Component c = new Component();
					c.setCodeTable(codeTableLibrary.get(cc.getTableId()));
					c.setConfLength(cc.getConfLength());
					c.setDatatype(library.get(cc.getDatatypeId()));
					c.setDescription(cc.getDescription());
					c.setMaxLength(Convertor.convertLength(cc.getMaxLength()));
					c.setMinLength(cc.getMinLength());
					c.setPosition(cc.getPosition());
					c.setTruncationAllowed(EBoolean.valueOf(cc.getTruncation()));
					c.setUsage(Usage.valueOf(cc.getUsage().name()));
					componentList.add(c);
					if(c.getDatatype() == null) {
						log.info(c.toString());
					}
				}
				dt.setComponents(componentList);
			}
		}
	}

}
