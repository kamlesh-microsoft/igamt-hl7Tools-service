package gov.nist.healthcare.hl7tools.service.util.mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gov.nist.healthcare.hl7tools.domain.Element;
import gov.nist.healthcare.hl7tools.domain.ElementType;
import gov.nist.healthcare.hl7tools.domain.Message;
import gov.nist.healthcare.hl7tools.domain.MessageLibrary;
import gov.nist.healthcare.hl7tools.domain.SegmentLibrary;
import gov.nist.healthcare.hl7tools.domain.Usage;

public class MessageLibraryBuilder {
	
	public static MessageLibrary build( SegmentLibrary segmentLibrary,
			Map<Integer, gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Group> map) throws Exception {
		List<Element> topLevelGroup = buildGroups(segmentLibrary, map);
		MessageLibrary library = new MessageLibrary();
		for(Element e: topLevelGroup)
			library.put(createMessage(e));
		return library;
	}
	
	private static  Message createMessage(Element e){
		Message m = new Message();
		m.setName(e.getName().substring(0, e.getName().indexOf(".ROOT")));
		m.setKey(m.getName());
		m.setChildren(e.getChildren());
		return m;
	}
	
	/*
	 * Build the Message graph and return the top level groups
	 */
	private static List<Element> buildGroups (SegmentLibrary segmentLibrary, Map<Integer, gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Group> map) {
		List<Element> topLevelGroup = new ArrayList<Element>();
		Map<Integer, Element> gm = new HashMap<Integer, Element>();
		// create groups map
		for(gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Group g: map.values()) {
			Element e = new Element();
			e.setName(g.getName());
			e.setType(g.isChoice() ? ElementType.CHOICE : ElementType.GROUP);
			gm.put(g.getId(), e);
			if(g.getName().contains(".ROOT"))
				topLevelGroup.add(e);
		}
		// update groups children
		for(gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Group g: map.values()) {
			List<Element> children = new ArrayList<Element>();
			if(g.getChildren() != null) //Some messages in old versions don't have children
				for(gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Element ee: g.getChildren()) {
					//FIXME: Temporary hack to fix the position
					Element tmp = updateChildren(ee, map, gm, segmentLibrary);
					children.add( tmp );
					tmp.setPosition( children.size() );
				}
			gm.get(g.getId()).setChildren(children);
		}
		return topLevelGroup;
	}
	
	private static Element updateChildren(gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Element ee, 
			Map<Integer, gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Group> map,
			Map<Integer, Element> groupMap, SegmentLibrary segmentLibrary) {
		Element e = ee.getSegmentId() != null ? new Element() : groupMap.get(ee.getGroupId()); 
		//e.setPosition(ee.getPosition());
		e.setUsage(Usage.valueOf(ee.getUsage().name()));
		e.setMax(ee.getMax());
		e.setMin(ee.getMin());
		if(ee.getSegmentId() != null) {
			e.setType(ElementType.SEGEMENT);
			e.setSegment(segmentLibrary.get(ee.getSegmentId()));
			e.setName(e.getSegment().getName());
		} else {
			List<Element> children = new ArrayList<Element>();
			for(gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Element eee :  map.get(ee.getGroupId()).getChildren()) {
				//FIXME: Temporary hack to fix the position
				Element tmp = updateChildren(eee, map, groupMap, segmentLibrary);
				children.add(tmp);
				tmp.setPosition( children.size() );
			}
			e.setChildren(children);
		}
		return e;	
	}

}
