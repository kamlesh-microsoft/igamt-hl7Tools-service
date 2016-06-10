package gov.nist.healthcare.hl7tools.service.transformer;

import static gov.nist.healthcare.hl7tools.service.transformer.TransformerUtil.DITA;

public class Template {
	
	// GLOBALS
	
	public static String tableRow(int format, String row1, String row2, String row3) {
		return String.format( format == DITA ? threeColDitaRow : threeColHtmlRow, row1, row2, row3);
	}
	
	public static String tableRow(int format, String row1, String row2, String row3, String row4, String row5) {
		return String.format( format == DITA ? fiveColDitaRow : fiveColHtmlRow, row1, row2, row3, row4, row5);
	}
	
	public static String tableRow(int format, String row1, String row2, String row3, String row4, String row5, String row6) {
		return String.format( format == DITA ? sixColDitaRow : sixColHtmlRow, row1, row2, row3, row4, row5, row6);
	}
	
	public static String tableRow(int format, String row1, String row2, String row3, String row4, String row5, String row6, String row7) {
		return String.format( format == DITA ? sevenColDitaRow : sevenColHtmlRow, row1, row2, row3, row4, row5, row6, row7);
	}
	
	public static String tableRow(int format, String row1, String row2, String row3, String row4, String row5, String row6, String row7, String row8) {
		return String.format( format == DITA ? eightColDitaRow : eightColHtmlRow, row1, row2, row3, row4, row5, row6, row7, row8);
	}

	public static String tableRow(int format, String row1, String row2, String row3, String row4, String row5, String row6, String row7, String row8, String row9) {
		return String.format( format == DITA ? nineColDitaRow : nineColHtmlRow, row1, row2, row3, row4, row5, row6, row7, row8, row9);
	}
	
	public static String tableRow(int format, String row1, String row2, String row3, String row4, String row5, String row6, String row7, String row8, String row9, String row10) {
		return String.format( format == DITA ? tenColDitaRow : tenColHtmlRow, row1, row2, row3, row4, row5, row6, row7, row8, row9, row10);
	}
	// MESSAGES
	public static String messageTopicStart(String id, String title, String description) {
		return String.format( topicStart, id, title, description) + messageTableStart;
	}
	
	public static String messageTopicEnd() {
		return messageTableEnd + topicEnd;
	}
	
	public static final String htmlColumnSpec = "<col width=";
			
	private static final String messageTableStart = 
		"<table>" +
			"<tgroup cols=\"5\" rowsep=\"1\" colsep=\"1\">" +
				"<colspec colwidth=\"60pt\"/>" +
				"<colspec colwidth=\"160pt\"/>" +
				"<colspec colwidth=\"50pt\" align=\"center\"/>" +
				"<colspec colwidth=\"60pt\" align=\"center\"/>" +
				"<colspec colwidth=\"1*\"/>" +
				"<thead>" +
					"<row>" +
						"<entry>Segment</entry>" +
						"<entry>Name</entry>" +
						"<entry>Usage</entry>" +
						"<entry>Cardinality</entry>" +
						"<entry>Description</entry>" +
					"</row>" +
				"</thead>" +
				"<tbody>";

	private static final String messageTableEnd = 
				"</tbody>" +
			"</tgroup>" +
		"</table>";
	
	public static final String messageHtmlTableStart = 

			"<table>" +
					"<caption>%s</caption>" +
					"<col width=\"14*\"/>" +
					"<col width=\"25*\"/>" +
					"<col width=\"7*\"/>" +
					"<col width=\"7*\"/>" +
					"<col width=\"22*\"/>" +
					"<col width=\"21*\"/>" +
					"<col width=\"14*\"/>" +
						"<tr>" +
							"<th>Segment</th>" +
							"<th>Name</th>" +
							"<th>Usage</th>" +
							"<th>Cardinality</th>" +
							"<th>Conf. Statement</th>" +
							"<th>Predicate</th>" +
							"<th>Comments</th>" +
						"</tr>";

	public static final String messageHtmlTableEnd = "</table>";

	// SEGMENTS
	
	public static String segmentTopicStart(String id, String title, String description) {
		return String.format( topicStart, id, title, description) + segmentTableStart ;
	}
	
	public static String segmentTopicEnd() {
		return segmentTableEnd + topicEnd;
	}
	
	private static final String segmentTableStart = 
			"<table>" +
				"<tgroup cols=\"8\" rowsep=\"1\" colsep=\"1\">" +
					"<colspec colwidth=\"30pt\" align=\"center\"/>" +
					"<colspec colwidth=\"1*\"/>" +
					"<colspec colwidth=\"30pt\" align=\"center\" />" +
					"<colspec colwidth=\"50pt\" align=\"center\" />" +
					"<colspec colwidth=\"60pt\" align=\"center\" />" +
					"<colspec colwidth=\"60pt\" align=\"center\" />" +
					"<colspec colwidth=\"60pt\" align=\"center\"/>" +
					"<colspec colwidth=\"1*\"/>" +
					"<thead>" +
						"<row>" +
							"<entry>SEQ</entry>" +
							"<entry>Field</entry>" +
							"<entry>DT</entry>" +
							"<entry>Usage</entry>" +
							"<entry>Cardinality</entry>" +
							"<entry>Length</entry>" +
							"<entry>Value Set</entry>" +
							"<entry>Comments</entry>" +
						"</row>" +
					"</thead>" +
					"<tbody>";

	private static final String segmentTableEnd = 
				"</tbody>" +
			"</tgroup>" +
		"</table>";
	
	public static final String segmentHtmlTableStart =

			"<table>" +
					"<caption>%s</caption>" +
					"<col width=\"5*\"/>" +
					"<col width=\"19*\"/>" +
					"<col width=\"16*\"/>" +
					"<col width=\"7*\"/>" +
					"<col width=\"7*\"/>" +
					"<col width=\"7*\" />" +
					"<col width=\"11*\"/>" +
					"<col width=\"25*\"/>" +
					"<col width=\"22*\"/>" +
					"<col width=\"11*\"/>" +
						"<tr>" +
							"<th>SEQ</th>" +
							"<th>Field</th>" +
							"<th>DT</th>" +
							"<th>Usage</th>" +
							"<th>Cardinality</th>" +
							"<th>Length</th>" +
							"<th>Value Set</th>" +
							"<th>Conf. Statement</th>" +
							"<th>Predicate</th>" +
							"<th>Comments</th>" +
						"</tr>";
	
	public static final String segmentHtmlTableEnd = "</table>";
	
	// DATATYPES
	
	public static String datatypeTopicStart(String id, String title, String description) {
		return String.format( topicStart, id, title, description) + datatypeTableStart ;
	}
	
	public static String datatypeTopicEnd() {
		return datatypeTableEnd + topicEnd;
	}
	
	private static final String datatypeTableStart = 
			"<table>" +
				"<tgroup cols=\"10\" rowsep=\"1\" colsep=\"1\">" +
					"<colspec colwidth=\"30pt\" align=\"center\"/>" +
					"<colspec colwidth=\"1*\"/>" +
					"<colspec colwidth=\"30pt\" align=\"center\"/>" +
					"<colspec colwidth=\"50pt\" align=\"center\"/>" +
					"<colspec colwdith=\"60pt\" align=\"center\"/>" +
					"<colspec colwidth=\"60pt\" align=\"center\"/>" +
					"<colspec colwidth=\"30pt\" align=\"center\"/>" +
					"<colspec colwidth=\"1*\" align=\"center\"/>" +
					"<colspec colwidth=\"1*\" align=\"center\"/>" +
					"<colspec colwidth=\"1*\" align=\"center\" />" +
					"<thead>" +
						"<row>" +
							"<entry>SEQ</entry>" +
							"<entry>Component</entry>" +
							"<entry>DT</entry>" +
							"<entry>Usage</entry>" +
							"<entry>Length</entry>" +
							"<entry>Value Set</entry>" +
							"<entry>Truncation</entry>" +
							"<entry>Conf. Statement</entry>" +
							"<entry>Predicate</entry>" +
							"<entry>Comments</entry>" +
						"</row>" +
					"</thead>" +
					"<tbody>";

	private static final String datatypeTableEnd = 
				"</tbody>" +
			"</tgroup>" +
		"</table>";
	
	public static final String datatypeHtmlTableStart =

			"<table>" +
					"<caption>%s</caption>" +
					"<col width=\"6*\"/>" +
					"<col width=\"13*\"/>" +
					"<col width=\"5*\"/>" +
					"<col width=\"10*\"/>" +
					"<col width=\"10*\" />" +
					"<col width=\"7*\"/>" +
					"<col width=\"13*\"/>" +
					"<col width=\"21*\"/>" +
					"<col width=\"22*\"/>" +
					"<col width=\"18*\"/>" +
						"<tr>" +
							"<th>SEQ</th>" +
							"<th>Component</th>" +
							"<th>DT</th>" +
							"<th>Usage</th>" +
							"<th>Length</th>" +
							"<th>Value Set</th>" +
							"<th>Truncation</th>" +
							"<th>Conf. Statement</th>" +
							"<th>Predicate</th>" +
							"<th>Comments</th>" +
						"</tr>";
	
	public static final String datatypeHtmlTableEnd = "</table>";	
	
	// Code Tables
	
	public static String codeTableTopicStart(String id, String title, String description) {
		return String.format( topicStart, id, title, description) + codeTableTableStart ;
	}
		
	public static String codeTableTopicEnd() {
		return codeTableTableEnd + topicEnd;
	}
	
	private static final String codeTableTableStart = 
		"<table>" +
			"<tgroup cols=\"3\" rowsep=\"1\" colsep=\"1\">" +
				"<colspec colwidth=\"30pt\" align=\"center\"/>" +
				"<colspec colwidth=\"1*\"/>" +
				"<colspec colwidth=\"1*\"/>" +
				"<thead>" +
					"<row>" +
						"<entry>Value</entry>" +
						"<entry>Description</entry>" +
						"<entry>Comments</entry>" +
					"</row>" +
				"</thead>" +
				"<tbody>";

	private static final String codeTableTableEnd = 
				"</tbody>" +
			"</tgroup>" +
		"</table>";

	public static final String codeTableHtmlTableStart =

			"<table>" +
					"<caption>%s</caption>" +
						"<tr>" +
							"<th>Code</th>" +
							"<th>Description</th>" +
							"<th>Comments</th>" +
							"<th>Code System</th>" +
							"<th>Usage</th>" +
						"</tr>";
	
	public static final String codeTableHtmlTableEnd = 	"</table>";	

	// HELPERS
	
	private static final String topicStart = buildDitaTopicStart();
	private static final String topicEnd   = buildDitaTopicEnd();
	private static final String threeColDitaRow  = buildDitaTableRow(3);
	private static final String fiveColDitaRow   = buildDitaTableRow(5);
	private static final String sixColDitaRow    = buildDitaTableRow(6);
	private static final String sevenColDitaRow  = buildDitaTableRow(7);
	private static final String eightColDitaRow  = buildDitaTableRow(8);
	private static final String nineColDitaRow = buildDitaTableRow(9);
	private static final String tenColDitaRow = buildDitaTableRow(10);
	
	public static final String htmlTableStart    = "<table>";
	public static final String htmlTableEnd      = "</table>";
	private static final String threeColHtmlRow  = ditaRowToHtml( threeColDitaRow );
	private static final String fiveColHtmlRow   = ditaRowToHtml( fiveColDitaRow );
	private static final String sixColHtmlRow    = ditaRowToHtml( sixColDitaRow  );
	private static final String sevenColHtmlRow  = ditaRowToHtml( sevenColDitaRow  );
	private static final String eightColHtmlRow  = ditaRowToHtml( eightColDitaRow );
	private static final String nineColHtmlRow = ditaRowToHtml( nineColDitaRow );
	private static final String tenColHtmlRow = ditaRowToHtml ( tenColDitaRow );
	
	private static String buildDitaTopicStart() {
		return "<topic id=\"%s\" xml:lang=\"en-us\"><title>%s</title><shortdesc>%s</shortdesc><body>";
	}
	
	private static String buildDitaTopicEnd() {
		return "</body></topic>";
	}
	
	private static String buildDitaTableRow(int numberOfColumns) {
		String r = "<row>";
		for (int i = 0; i < numberOfColumns; i++)
			r += "<entry>%s</entry>";
		return r + "</row>";
	}
	
	private static String ditaRowToHtml( String row ) {
		return row.replaceAll("row", "tr").replaceAll("entry", "td");
	}
	
	@SuppressWarnings("unused")
	private static String htmlRowToDita( String row ) {
		
		return row.replaceAll("tr", "row").replaceAll("td", "entry");
	}
}
