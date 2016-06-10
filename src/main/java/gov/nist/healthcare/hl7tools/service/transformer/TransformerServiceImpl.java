package gov.nist.healthcare.hl7tools.service.transformer;

import static gov.nist.healthcare.hl7tools.service.transformer.TransformerUtil.DITA;
import static gov.nist.healthcare.hl7tools.service.transformer.TransformerUtil.HTML;
import static gov.nist.healthcare.hl7tools.service.transformer.TransformerUtil.breakUpSegmentName;
import static gov.nist.healthcare.hl7tools.service.transformer.TransformerUtil.escape;
import static gov.nist.healthcare.hl7tools.service.transformer.TransformerUtil.formatCardinality;
import static gov.nist.healthcare.hl7tools.service.transformer.TransformerUtil.formatLength;
import static gov.nist.healthcare.hl7tools.service.transformer.TransformerUtil.formatUsage;
import static gov.nist.healthcare.hl7tools.service.transformer.TransformerUtil.ident;
import static gov.nist.healthcare.hl7tools.service.transformer.TransformerUtil.retrieveConformanceStatementsAsString;
import gov.nist.healthcare.hl7tools.domain.Code;
import gov.nist.healthcare.hl7tools.domain.CodeTable;
import gov.nist.healthcare.hl7tools.domain.Component;
import gov.nist.healthcare.hl7tools.domain.Datatype;
import gov.nist.healthcare.hl7tools.domain.Element;
import gov.nist.healthcare.hl7tools.domain.ElementType;
import gov.nist.healthcare.hl7tools.domain.Field;
import gov.nist.healthcare.hl7tools.domain.Message;
import gov.nist.healthcare.hl7tools.domain.Segment;
import gov.nist.healthcare.hl7tools.service.TransformerService;

import java.text.Normalizer;
 

public class TransformerServiceImpl implements TransformerService {
	
	public String toDita(Message message) {
		return trasform( DITA, message );
	}
	
	public String toHtml(Message message) {
		return trasform( HTML, message );
	}
	
	public String toDita(Segment segment) {
		return trasform( DITA, segment );
	}
	
	public String toHtml(Segment segment) {
		return trasform( HTML, segment );
	}
	
	public String toDita(Datatype datatype) {
		return trasform( DITA, datatype );
	}
	
	public String toHtml(Datatype datatype) {
		return trasform( HTML, datatype );
	}
	
	public String toDita(CodeTable codeTable) {
		return trasform( DITA, codeTable );
	}
	
	public String toHtml(CodeTable codeTable) {
		return trasform( HTML, codeTable );
	}
	
	private String trasform(int format, Message message) {
		StringBuilder sb = new StringBuilder();
		String id = message.getKey();
		String title = message.getName();
		String shortDesc = message.getDescription();
		
		sb.append( format == DITA ? Template.messageTopicStart( id, title, shortDesc) :  String.format( Template.messageHtmlTableStart, message.getName()));
		
		for(Element e: message.getChildren()) {
			sb.append( transform( format , e, 0) );
		}
		sb.append( format == DITA ? Template.messageTopicEnd() : Template.messageHtmlTableEnd );
		return sb.toString();
	}
	
	private String transform(int format, Element e, int level) {
		StringBuilder sb = new StringBuilder();
		if(e.getType() == ElementType.SEGEMENT) {
			String row1 = ident(e.getName(), level); 
			String row2 = escape(format, breakUpSegmentName(e.getSegment().getDescription()));
			String row3 = formatUsage(e.getUsage().name());
			String row4 = formatCardinality(e.getMin(), e.getMax());
			String row5 = escape(format,retrieveConformanceStatementsAsString(e.getConformanceStatementList()));
			String row6 = (e.getPredicate() != null) ? escape(format,e.getPredicate().getDescription()) : "";
			String row7 = escape(format,(e.getComment() != null) ? e.getComment() : "");
			sb.append( Template.tableRow( format, row1, row2, row3, row4, row5, row6, row7) );
		} else if( e.getType() == ElementType.GROUP || e.getType() == ElementType.CHOICE ) {
			sb.append( Template.tableRow( format, "", "--- "+ breakUpSegmentName(e.getName()) + " begin", formatUsage(e.getUsage().name()), formatCardinality(e.getMin(), e.getMax()), retrieveConformanceStatementsAsString(e.getConformanceStatementList()),(e.getPredicate() != null) ? e.getPredicate().getDescription() : "",(e.getComment() != null) ? e.getComment() : ""));
			for(Element ee: e.getChildren())
				sb.append( transform( format, ee, level + 1) );
			sb.append( Template.tableRow( format, "", "--- "+ breakUpSegmentName(e.getName()) + " end", "", "","","", "") );
		}
		return sb.toString();
	}
	
	// SEGMENT
	private String trasform(int format, Segment s) {
		StringBuilder sb = new StringBuilder();
		String id = s.getKey();
		//sement name with "." becomes too long for DITA
		//and doesn't fit in DITA Table
//		String title = s.getName();
		String title = s.getRoot();
		String shortDesc = escape(format, s.getDescription());
		
		sb.append( format == DITA ? Template.segmentTopicStart( id, title, shortDesc) : String.format(Template.segmentHtmlTableStart, title) );
		if( s.getFields() != null ) {
			for( Field f: s.getFields() ) {
				sb.append( transform( format , f ) );
			}
		}
		
		sb.append( format == DITA ? Template.segmentTopicEnd() : Template.segmentHtmlTableEnd );
		return sb.toString();
	}
	
	private String transform(int format, Field f) {
		StringBuilder sb = new StringBuilder();
		String row1 = ""+f.getPosition(); 
		String row2 = escape(format, breakUpSegmentName(f.getDescription()));
		String row3 = f.getDatatype().getKey();
		String row4 = formatUsage(f.getUsage().name());
		String row5 = formatCardinality(f.getMin(), f.getMax());
		String row6 = (f.getDatatype() != null && f.getDatatype().getComponents() != null && f.getDatatype().getComponents().size() > 0)
				? "" : formatLength(f.getMinLength(), f.getMaxLength());
		String row7 = f.getCodeTable() != null ? f.getCodeTable().getKey() : "";
		String row8 = escape(format,retrieveConformanceStatementsAsString(f.getConformanceStatementList()));
		String row9 = (f.getPredicate() != null) ? escape(format,f.getPredicate().getDescription()) : "";
		String row10 = escape(format, (f.getComment() != null) ? f.getComment() : "");
		sb.append( Template.tableRow( format, row1, row2, row3, row4, row5, row6, row7, row8, row9, row10) );
		return sb.toString();
	}
	
	
	// DATATYPE
	private String trasform(int format, Datatype dt) {
		StringBuilder sb = new StringBuilder();
		String id = dt.getKey();
		String title = dt.getName();
		String shortDesc = escape(format, dt.getDescription());
		
		sb.append( format == DITA ? Template.datatypeTopicStart( id, title, shortDesc) : String.format(Template.datatypeHtmlTableStart, title) );
		if( dt.getComponents() != null && !dt.getComponents().isEmpty() ) {
			for( Component c: dt.getComponents() ) {
				sb.append( transform( format , c ) );
			}
		} else if(format == DITA) //DITA doesn't like empty tbody tags
			sb.append( Template.tableRow( format, "", "", "", "", "", "") );
		
		sb.append( format == DITA ? Template.datatypeTopicEnd() : Template.datatypeHtmlTableEnd );
		return sb.toString();
	}
	
	private String transform(int format, Component c) {
		StringBuilder sb = new StringBuilder();
		String row1 = ""+c.getPosition(); 
		String row2 = escape(format, c.getDescription());
		String row3 = c.getDatatype().getKey();
		String row4 = formatUsage(c.getUsage().name()); 
		String row5 = (c.getDatatype() != null && c.getDatatype().getComponents() != null && c.getDatatype().getComponents().size() > 0) ?
				"" : formatLength(c.getMinLength(),c.getMaxLength());
		String row6 = c.getCodeTable() != null ? c.getCodeTable().getKey() : "";
		String row7 = c.getTruncationAllowed() != null ? c.getTruncationAllowed().name() : "";
		String row8 = escape(format,retrieveConformanceStatementsAsString(c.getConformanceStatementList()));
		//row7 = row7.length() > 0 ? escape(format,row7) : "";
		String row9 = escape(format, (c.getPredicate() != null) ? c.getPredicate().getDescription() : "");
		String row10 = escape(format, (c.getComment() != null) ? c.getComment() : ""); 
		sb.append( Template.tableRow( format, row1, row2, row3, row4, row5, row6, row7, row8, row9, row10) );
		return sb.toString();
	}
	
	//Code Tables
	private String trasform(int format, CodeTable ct) {
		StringBuilder sb = new StringBuilder();
		//FIXME Since 0001.dita is not a valid CNAME we need to prefix the id with HL7
		// May this should be fixed in the HL7 database
		String id = "HL7"+ct.getKey();
		String title = ct.getName();
		String shortDesc = escape(format, ct.getDescription());
		@SuppressWarnings("unused")
		String comment = escape(format, (ct.getComment() != null) ? ct.getComment() : "");
		if(ct.getCodes() == null || ct.getCodes().size() == 0){
			sb.append("<p>No Code</p>");
		}else {
			sb.append( format == DITA ? Template.codeTableTopicStart( id, title, shortDesc) : String.format(Template.codeTableHtmlTableStart, ct.getName()) );
			if( ct.getCodes() != null && !ct.getCodes().isEmpty()) {
				for( Code c: ct.getCodes() ) {
					sb.append( transform( format , c ) );
				}
			} else if ( format == DITA ) //DITA doesn't like empty tbody tags
				sb.append( Template.tableRow( format, "", "", "") ); 
			
			sb.append( format == DITA ? Template.codeTableTopicEnd() : Template.codeTableHtmlTableEnd );	
		}
		return sb.toString();
	}
	
	private String transform(int format, Code c) {		
		StringBuilder sb = new StringBuilder();
		String row1 = c.getValue().replace("<", "&lt;").replace(">", "&gt;").replace("'", "&#39;");
		String row2 = Normalizer.normalize(c.getDescription(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").replace("&", "and").replace("<", "&lt;").replace(">", "&gt;").replace("'", "&#39;");
		String row3 = c.getComment() != null ? c.getComment() : "";
		String row4 = c.getCodeSystem() != null ? c.getCodeSystem().getName() : "";
		String row5 = c.getUsage() != null ? c.getUsage().name() : "";
		sb.append( Template.tableRow( format, row1, row2, row3, row4, row5) );
		return sb.toString();
	}
}
