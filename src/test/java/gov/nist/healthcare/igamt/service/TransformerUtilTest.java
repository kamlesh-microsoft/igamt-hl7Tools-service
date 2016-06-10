package gov.nist.healthcare.igamt.service;

import static org.junit.Assert.*;
import gov.nist.healthcare.hl7tools.service.transformer.TransformerUtil;

import org.junit.Before;
import org.junit.Test;

public class TransformerUtilTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testFormatDataTypeUsage() {
		
		String usage = "C_X_R_B_Y_Q";
		
		String formatted;
		
		usage = "C_X_R";
		
		formatted = TransformerUtil.formatUsage(usage);
		assertTrue(formatted.equalsIgnoreCase("C(X/R)"));
		
	}

}
