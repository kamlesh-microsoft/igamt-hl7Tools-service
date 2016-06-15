package gov.nist.healthcare.hl7tools.service.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import gov.nist.healthcare.hl7tools.service.util.HL72JSONConverter.GroupStacker;
import gov.nist.healthcare.hl7tools.service.util.HL72JSONConverter.Helper;
import gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Component;
import gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.DataElement;
import gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Datatype;
import gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Event;
import gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Field;
import gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Group;
import gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Interaction;
import gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Message;
import gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Segment;

public class HL72JSONConverterTest {

	static HL72JSONConverter sut;
	static final String hl7Version = "2.8.2";

	@BeforeClass
	public static void beforeClass() {
		sut = new HL72JSONConverter(hl7Version);
	}

//	@Test
	public void testSQL4_MESSAGETYPE() {
		fail("Not yet implemented");
	}

	@Test
	public void testSQL4_MESSAGE() {
		 String sql = sut.SQL4_MESSAGE();
		 Helper<Message> hlpMessage = sut.new Helper<Message>();
		 List<Message> messages = (hlpMessage.fetch(sql,
		 sut.RM4_MESSAGE));
		 hlpMessage.write(messages, "messages");
	}

//	@Test
	public void testSQL4_GROUP() {
		String sql = sut.SQL4_GROUP();
		Helper<Group> hlp = sut.new Helper<Group>();
		List<Group> rs = (hlp.fetch(sql, sut.RM4_GROUP));
		assertNotNull(rs);
		assertTrue(rs.size() > 0);
		// List<Group> rs1 = sut.assembleGroups(rs);
	}

//	@Test
	public void testSQL4_DATATYPE() {
		String sql = sut.SQL4_DATATYPE();
		Helper<Datatype> hlp = sut.new Helper<Datatype>();
		List<Datatype> rs = (hlp.fetch(sql, sut.RM4_DATATYPE));
		assertNotNull(rs);
		assertTrue(rs.size() > 0);
	}

//	@Test
	public void testSQL4_COMPONENT() {
		String sql = sut.SQL4_COMPONENT();
		Helper<Component> hlp = sut.new Helper<Component>();
		List<Component> rs = (hlp.fetch(sql, sut.RM4_COMPONENT));
		assertNotNull(rs);
		assertTrue(rs.size() > 0);
	}

//	@Test
	public void testSQL4_FIELD() {
		String sql = sut.SQL4_FIELD();
		Helper<Field> hlp = sut.new Helper<Field>();
		List<Field> rs = (hlp.fetch(sql, sut.RM4_FIELD));
		assertNotNull(rs);
		assertTrue(rs.size() > 0);
	}

//	@Test
	public void testSQL4_SEGMENT() {
		String sql = sut.SQL4_SEGMENT();
		Helper<Segment> hlp = sut.new Helper<Segment>();
		List<Segment> rs = (hlp.fetch(sut.SQL4_SEGMENT(), sut.RM4_SEGMENT));
		assertNotNull(rs);
		assertTrue(rs.size() > 0);
	}

//	@Test
	public void testSQL4_DATAELEMENT() {
		String sql = sut.SQL4_DATAELEMENT();
		Helper<DataElement> hlp = sut.new Helper<DataElement>();
		List<DataElement> rs = (hlp.fetch(sql, sut.RM4_DATAELEMENT));
		assertNotNull(rs);
		assertTrue(rs.size() > 0);
	}

//	@Test
	public void testSQL4_EVENT() {
		String sql = sut.SQL4_EVENT();
		Helper<Event> hlp = sut.new Helper<Event>();
		List<Event> rs = (hlp.fetch(sql, sut.RM4_EVENT));
		assertNotNull(rs);
		assertTrue(rs.size() > 0);
	}

//	@Test
	public void testSQL4_INTERACTION() {
		String sql = sut.SQL4_INTERACTION();
		Helper<Interaction> hlp = sut.new Helper<Interaction>();
		List<Interaction> rs = (hlp.fetch(sql, sut.RM4_INTERACTION));
		assertNotNull(rs);
		assertTrue(rs.size() > 0);
	}
	
//	@Test
	public void testGroupStacker() {
		GroupStacker gs = sut.new GroupStacker();
		Group grp1 = new Group();
		grp1.setId(1);
		grp1.setName("Name 1");
		Group grp2 = new Group();
		grp2.setId(2);
		grp2.setName("Name 2");
		Group grp3 = new Group();
		grp3.setId(3);
		grp3.setName("Name 3");
		Group grp4 = new Group();
		grp4.setId(4);
//		assertFalse(gs.waw(grp4));
//		assertFalse(gs.waw(grp1));
//		assertEquals(grp1.getId().intValue(), gs.getCurrGroupId());
//		assertFalse(gs.waw(grp2));
//		assertEquals(grp2.getId().intValue(), gs.getCurrGroupId());
//		assertFalse(gs.waw(grp3));
//		assertEquals(grp3.getId().intValue(), gs.getCurrGroupId());
//		assertTrue(gs.waw(grp3));
//		assertEquals(grp2.getId().intValue(), gs.getCurrGroupId());
//		assertTrue(gs.waw(grp2));
//		assertEquals(grp1.getId().intValue(), gs.getCurrGroupId());
//		assertTrue(gs.waw(grp1));
//		assertEquals(-1, gs.getCurrGroupId());
	}
}
