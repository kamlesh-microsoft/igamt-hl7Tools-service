package gov.nist.healthcare.igamt.service;

import gov.nist.healthcare.hl7tools.domain.Code;
import gov.nist.healthcare.hl7tools.domain.CodeTable;
import gov.nist.healthcare.hl7tools.domain.Datatype;
import gov.nist.healthcare.hl7tools.domain.Element;
import gov.nist.healthcare.hl7tools.domain.ElementType;
import gov.nist.healthcare.hl7tools.domain.Field;
import gov.nist.healthcare.hl7tools.domain.HL7Table;
import gov.nist.healthcare.hl7tools.domain.IGLibrary;
import gov.nist.healthcare.hl7tools.domain.Segment;
import gov.nist.healthcare.hl7tools.domain.SegmentLibrary;
import gov.nist.healthcare.hl7tools.domain.Usage;
import gov.nist.healthcare.hl7tools.service.HL7DBMockServiceImpl;
import gov.nist.healthcare.hl7tools.service.HL7DBService;
import gov.nist.healthcare.hl7tools.service.HL7DBServiceException;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class HL7DBServiceTest extends TestCase {

	private final HL7DBService service = new HL7DBMockServiceImpl();
	private final String version;
	private final Integer expectedMessageCount;
	@SuppressWarnings("unused")
	private final Integer expectedEventCount;
	private final Integer expectedCodeTableCount;
	private final Integer expectedDatatypeCount;
	private final Integer expectedSegmentCount;

	public HL7DBServiceTest(String version, Integer expectedMessageCount, Integer expectedEventCount,
			Integer expectedCodeTableCount, Integer expectedDatatypeCount, Integer expectedSegmentCount) {
		this.version = version;
		this.expectedMessageCount = expectedMessageCount;
		this.expectedEventCount = expectedEventCount;
		this.expectedCodeTableCount = expectedCodeTableCount;
		this.expectedDatatypeCount = expectedDatatypeCount;
		this.expectedSegmentCount = expectedSegmentCount;
	}

	@Parameters
	public static Collection<Object[]> initTestData() {
		Object[][] data = new Object[][] { { "2.7", 193, 313, 535, 86, 177 }, { "2.6", 212, 336, 538, 91, 174 },
				{ "2.5.1", 190, 316, 477, 91, 153 }, { "2.5", 191, 316, 477, 90, 153 },
				{ "2.4", 182, 291, 408, 243, 141 }, { "2.3.1", 134, 195, 306, 165, 113 },
				{ "2.3", 124, 185, 292, 140, 112 }, { "2.2", 1, 56, 189, 85, 63 }, { "2.1", 1, 37, 128, 23, 40 } };
		return Arrays.asList(data);
	}

	@Test
	public void testGetSupportedHL7Versions() {
		assertNotNull(service);
		// We want to run this test one time only
		if ("2.5.1".equals(version))
			assertEquals(
					new HashSet<String>(
							Arrays.asList("2.7", "2.6", "2.5", "2.5.1", "2.4", "2.3.1", "2.3", "2.2", "2.1")),
					new HashSet<String>(service.getSupportedHL7Versions()));
	}

	@Test
	public void testGetMessageListByVersion() throws HL7DBServiceException {
		assertNotNull(service);
		List<String[]> messageList = service.getMessageListByVersion(version);
		assertNotNull(generatePrefix() + " The message list shouldn't be empty.", messageList);
		Integer messageCount = messageList.size();
		assertEquals(generatePrefix() + " *** Message count failure *** ", expectedMessageCount, messageCount);
		runMessageAssertions(messageList);
	}

	/*
	 * @Test public void testGetEventListByVersion() throws
	 * HL7DBServiceException{ assertNotNull(service); List<Event> eventList =
	 * service.getEventListByVersion(version); assertNotNull( generatePrefix() +
	 * " The event list shouldn't be empty.", eventList); Integer eventCount =
	 * eventList.size(); assertEquals( generatePrefix() +
	 * " *** Event count failure *** ", expectedEventCount, eventCount);
	 * runEventAssertions(eventList); }
	 */

	@Test
	public void testBuildIGFromMessageList() throws HL7DBServiceException {
		if ("2.5.1".equals(version)) {
			assertNotNull(service);
			List<String> messageList = Arrays.asList("ORU_R01");
			IGLibrary ig = service.buildIGFromMessageList(version, messageList);
			assertNotNull(ig);
			assertNotNull(ig.getMessageLibrary());
			assertEquals(messageList.size(), ig.getMessageLibrary().size());
			run_v251_ORU_R01_Assertions(ig.getMessageLibrary().get("ORU_R01"));
			runSegmentLibraryAssertions(ig.getSegmentLibrary());
		}
	}

	@Test
	public void testBuildIGFromMessageList1() throws HL7DBServiceException {
		assertNotNull(service);
		List<String> messageList = Collections.emptyList();
		IGLibrary ig = service.buildIGFromMessageList(version, messageList);
		assertNotNull(ig);
		assertNotNull(ig.getMessageLibrary());
		// assertEquals(messageList.size(), ig.getMessageLibrary().size());
//		run_v251_ORU_R01_Assertions(ig.getMessageLibrary().get("ORU_R01"));
//		runSegmentLibraryAssertions(ig.getSegmentLibrary());
	}

	@Test
	public void testGetCodeTableListByVersion() throws HL7DBServiceException {
		assertNotNull(service);
		List<CodeTable> codeTableList = service.getCodeTableListByVersion(version);
		assertNotNull(generatePrefix() + " The code table list shouldn't be empty.", codeTableList);
		Integer codeTableCount = codeTableList.size();
		assertEquals(generatePrefix() + " *** CodeTable count failure *** ", expectedCodeTableCount, codeTableCount);
		for (CodeTable ct : codeTableList)
			runCodeTableAssertions(ct);
	}

	@Test
	public void testGetDatatypeListByVersion() throws HL7DBServiceException {
		assertNotNull(service);
		List<Datatype> datatypeList = service.getDatatypeListByVersion(version);
		assertNotNull(generatePrefix() + " The datatype list shouldn't be empty.", datatypeList);
		Integer datatypeCount = datatypeList.size();
		assertEquals(generatePrefix() + " *** Datatype count failure *** ", expectedDatatypeCount, datatypeCount);
		// FIXME
		// for(Datatype dt: DatatypeList)
		// runDatatypeAssertions(dt);
	}

	@Test
	public void testGetSegmentListByVersion() throws HL7DBServiceException {
		assertNotNull(service);
		List<Segment> segmentList = service.getSegmentListByVersion(version);
		assertNotNull(generatePrefix() + " The segement list shouldn't be empty.", segmentList);
		Integer segmentCount = segmentList.size();
		assertEquals(generatePrefix() + " *** Segment count failure *** ", expectedSegmentCount, segmentCount);
		// FIXME
		// for(Datatype dt: DatatypeList)
		// runDatatypeAssertions(dt);
	}

	@Test
	public void testGetMessageByIdAndVersion() throws HL7DBServiceException {
		assertNotNull(service);
		gov.nist.healthcare.hl7tools.domain.Message message = service.getMessage(version, "ACK");
		assertNotNull(generatePrefix() + " The message shouldn't be null.", message);
		// TODO Check structure
	}

	/*
	 * For each message the id, description and the section shouldn't be empty
	 */
	private void runMessageAssertions(List<String[]> messageList) {
		for (String[] msg : messageList) {
			assertNotNull(msg);
			assertTrue(generatePrefix(msg) + " The message id shouldn't be empty. " + msg, notEmpty(msg[0]));
			assertTrue(generatePrefix(msg) + " The message description shouldn't be empty. " + msg, notEmpty(msg[1]));
			assertTrue(generatePrefix(msg) + " The message section shouldn't be empty. " + msg, notEmpty(msg[2]));
			if ("2.5.1".equals(version) && "ADR_A19".equals(msg[0]))
				run_v251_ADR_A19_Assertions(msg);
		}
	}

	/*
	 * The description of the message ADR_A19 in the version '2.5.1' should be
	 * 'ADT response'
	 */
	private void run_v251_ADR_A19_Assertions(String[] msg) {
		assertEquals(generatePrefix(msg) + "*** Description Failure ***", "ADT response", msg[1]);
	}

	/*
	 * For each event the id, description and the section shouldn't be empty
	 */
	/*
	 * private void runEventAssertions(List<Event> eventList){ for(Event e:
	 * eventList) { assertNotNull(e); assertTrue( generatePrefix(e) +
	 * " The event id shouldn't be empty. " + e, notEmpty(e.getId()) );
	 * assertTrue( generatePrefix(e) +
	 * " The event description shouldn't be empty. " + e,
	 * notEmpty(e.getDescription()) ); assertTrue( generatePrefix(e) +
	 * " The event section shouldn't be empty. " + e, notEmpty(e.getSection()));
	 * if("2.5.1".equals(version) && "R01".equals(e.getId()))
	 * run_v251_R01_Assertions(e); } }
	 */

	/*
	 * The event R01 in the version '2.5.1' description should be 'ORU/ACK -
	 * Unsolicited transmission of an observation message'. It should be have
	 * exactly 1 interaction. The interaction number should be 1, the sender
	 * message should be ORU_R01 and the receiver message should be ACK.
	 */
	/*
	 * private void run_v251_R01_Assertions(Event e) { assertEquals(
	 * generatePrefix(e) + "*** Description Failure ***",
	 * "ORU/ACK - Unsolicited transmission of an observation message",
	 * e.getDescription()); List<Interaction> interactionList =
	 * e.getInteractions(); assertNotNull(interactionList); assertEquals(
	 * generatePrefix() + " The event R01 should have one interaction.", 1,
	 * interactionList.size() ); Interaction i = interactionList.get(0);
	 * assertNotNull(i); assertEquals( generatePrefix() +
	 * " [Event R01] The interaction number should be 1.", 1, i.getNumber() );
	 * assertEquals( generatePrefix() +
	 * " [Event R01] The sender message should be 'ORU_R01'.", "ORU_R01",
	 * i.getSenderMsg() ); assertEquals( generatePrefix() +
	 * " [Event R01] The receiver message should be 'ACK'.", "ACK",
	 * i.getReceiverMsg() );
	 * 
	 * }
	 */

	private void run_v251_ORU_R01_Assertions(gov.nist.healthcare.hl7tools.domain.Message m) {
		assertNotNull(m);
		String expectedDesc = "Unsolicited transmission of an observation message";
		String expectedEventDesc = "ORU/ACK - Unsolicited transmission of an observation message";
		assertEquals("[HL7 v251] ORU_R01 description should be '" + expectedDesc + "'.", expectedDesc,
				m.getDescription());
		assertEquals("[HL7 v251] ORU_R01 event description should be '" + expectedEventDesc + "'.", expectedEventDesc,
				m.getEventDescription());
		run_v251_ORU_R01_Structure_Assertions(m.getChildren());
	}

	private void run_v251_ORU_R01_Structure_Assertions(List<Element> children) {
		assertNotNull(children);
		assertEquals(generatePrefix() + " ORU_R01 should have 4 children.", 4, children.size());
		Element msh = children.get(0);
		runElementAssertions(msh, ElementType.SEGEMENT, "MSH", 1, Usage.R, 1, "1");
		Element sft = children.get(1);
		runElementAssertions(sft, ElementType.SEGEMENT, "SFT", 2, Usage.O, 0, "*");
		Element patient_result = children.get(2);
		runElementAssertions(patient_result, ElementType.GROUP, "ORU_R01.PATIENT_RESULT", 3, Usage.R, 1, "*");
		Element dsc = children.get(3);
		runElementAssertions(dsc, ElementType.SEGEMENT, "DSC", 4, Usage.O, 0, "1");
		run_v251_ORU_R01_PATIENT_RESULT_Structure_Assertions(patient_result.getChildren());
	}

	private void run_v251_ORU_R01_PATIENT_RESULT_Structure_Assertions(List<Element> children) {
		assertNotNull(children);
		assertEquals(generatePrefix() + " ORU_R01.PATIENT_RESULT should have 2 children.", 2, children.size());
		Element patient = children.get(0);
		runElementAssertions(patient, ElementType.GROUP, "ORU_R01.PATIENT_RESULT.PATIENT", 1, Usage.O, 0, "1");
		Element order_observation = children.get(1);
		runElementAssertions(order_observation, ElementType.GROUP, "ORU_R01.PATIENT_RESULT.ORDER_OBSERVATION", 2,
				Usage.R, 1, "*");
	}

	private void runElementAssertions(Element e, ElementType type, String expectedName, int expectedPosition,
			Usage expectedUsage, int expectedMin, String expectedMax) {
		assertNotNull(e);
		assertEquals(generatePrefix(e) + " *** Name Assertion Failure ***", expectedName, e.getName());
		// FIXME : Position not computed correctly in HL7DB
		// assertEquals( generatePrefix(e) +
		// " *** Position Assertion Failure ***", expectedPosition,
		// e.getPosition());
		assertEquals(generatePrefix(e) + " *** Usage Assertion Failure ***", expectedUsage, e.getUsage());
		assertEquals(generatePrefix(e) + " *** Min Assertion Failure ***", expectedMin, e.getMin());
		assertEquals(generatePrefix(e) + " *** Max Assertion Failure ***", expectedMax, e.getMax());
	}

	private void runSegmentLibraryAssertions(SegmentLibrary segmentLibrary) {
		assertNotNull(segmentLibrary);
		List<String> expectedSegList = Arrays.asList("MSH", "SFT", "DSC", "PID", "PD1", "NTE", "NK1", "PV1", "PV2",
				"ORC", "OBR", "CTD", "FT1", "CTI", "TQ1", "TQ2", "OBX", "SPM");
		Set<String> segmentSet = new HashSet<String>(expectedSegList);
		assertEquals(generatePrefix() + "*** Segment Library number of elements failure ***", segmentSet,
				segmentLibrary.keySet());
		for (Segment s : segmentLibrary.values())
			runSegmentAssertions(s);
	}

	private void runSegmentAssertions(Segment segment) {
		assertNotNull(segment);
		// TODO TO BE COMPLETED
		assertNotNull(segment.getFields());
		assertTrue(segment.getFields().size() > 0);
		for (Field f : segment.getFields()) {
			assertNotNull(f);
		}
	}

	/*
	 * Code Table Assertions
	 */
	private void runCodeTableAssertions(CodeTable ct) {
		assertNotNull(ct);
		assertNotNull(ct.getKey());
		assertNotNull(ct.getName());
		assertNotNull(ct.getDescription());
		assertTrue(ct.getKey().equals(ct.getName()));
		assertTrue(ct instanceof HL7Table);
		String tableType = ((HL7Table) ct).getType();
		// FIXME assertTrue( "HL7".equals(tableType) || "USER" );
		assertNotNull(tableType);
		if (ct.getCodes() != null)
			for (Code c : ct.getCodes())
				runCodeAssertions(c);
	}

	private void runCodeAssertions(Code c) {
		assertNotNull(c);
		assertNotNull(c.getValue());
		assertNotNull(c.getDescription());
	}

	// Helper

	/*
	 * Return true if the string is not empty
	 */
	private boolean notEmpty(String s) {
		return s != null && !s.isEmpty();
	}

	private String generatePrefix(String version) {
		return "[HL7 v" + version + "]";
	}

	private String generatePrefix(String version, Object o) {
		return "[HL7 v" + version + "] " + o;
	}

	private String generatePrefix() {
		return generatePrefix(version);
	}

	private String generatePrefix(Object o) {
		return generatePrefix(version, o);
	}

	public static void main(String[] args) {
		HL7DBServiceTest app = new HL7DBServiceTest("2.5.1", -1, -1, -1, -1, -1);
		try {
			app.testBuildIGFromMessageList1();
		} catch (HL7DBServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
