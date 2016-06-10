package gov.nist.healthcare.hl7tools.service.util.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gov.nist.healthcare.hl7tools.domain.Code;
import gov.nist.healthcare.hl7tools.domain.CodeTable;
import gov.nist.healthcare.hl7tools.domain.CodeTableLibrary;
import gov.nist.healthcare.hl7tools.domain.HL7Table;
import gov.nist.healthcare.hl7tools.domain.Usage;
import gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Table;

public class CodeTableLibraryBuilder {
	
	public static CodeTableLibrary build(Map<String, gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Table> map) throws Exception {
		CodeTableLibrary library = new CodeTableLibrary();
		for(Table t: map.values())
			library.put(transform(t));
		return library;
	}
	
	private static CodeTable transform(gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Table tt) {
		HL7Table t = new HL7Table();
		t.setKey(tt.getId());
		t.setName(tt.getId());
		t.setDescription(tt.getDescription());
		t.setOid(tt.getOid());
		
		t.setType(tt.getType());
		if(tt.getCodes() != null) {
			List<Code> codes = new ArrayList<Code>();
			for(gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Code cc : tt.getCodes()) {
				Code c = new Code();
				c.setValue(cc.getName());
				c.setDescription(cc.getDescription());
				c.setUsage(Usage.valueOf(cc.getUsage().name()));
				c.setCodeSystem( (HL7Table) t);
				codes.add(c);
			}
			t.setCodes(codes);
		}
		return t;
	}
}
