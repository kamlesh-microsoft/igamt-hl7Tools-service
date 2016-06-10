package gov.nist.healthcare.hl7tools.service.transformer;

import gov.nist.healthcare.hl7tools.domain.ConformanceStatement;
import gov.nist.healthcare.hl7tools.domain.Usage;

import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;

public class TransformerUtil {

	public static final int DITA = 0;
	public static final int HTML = 1;

	public static final String nonBreakingSpace = "&#xA0;";
	public static final String colon = ":";
	public static final String tab = nonBreakingSpace + nonBreakingSpace
			+ nonBreakingSpace + nonBreakingSpace;
	public static final String forwardSlash = "/";
	public static final String openParanthesis = "(";
	public static final String closeParanthesis = ")";
	public static final String underScore = "_";

	public static String escape(int format, String input) {
		return format == DITA ? escapeXML(input) : escapeHTML(input);
	}

	public static String escapeXML(String input) {
		return StringEscapeUtils.escapeXml(input);
	}

	public static String escapeHTML(String input) {
		return StringEscapeUtils.escapeHtml4(input);
	}

	public static String ident(String s, int nbOfTabs) {
		String r = "";
		for (int i = 0; i < nbOfTabs; i++)
			r += tab;
		return r + s;
	}

	public static String formatUsage(String usage) {

		return Usage.valueOf(usage).getValue();
		/*
		 * if (usage.charAt(0) == 'c' || usage.charAt(0) == 'C') {
		 * 
		 * String result = "";
		 * 
		 * String[] split = usage.split(underScore);
		 * 
		 * for (int i = 0; i < split.length; i++) { if (i == 0) { result +=
		 * split[i]; } else { if (i == 1) { result +=
		 * openParanthesis+split[i]+forwardSlash; }else if (i == split.length -
		 * 1) { result += split[i] + closeParanthesis; } else { result +=
		 * split[i] + forwardSlash; } } } return result; }
		 * 
		 * return usage;
		 */
	}

	public static String formatCardinality(int minOccurs, String maxOccurs) {
		return "[" + minOccurs + ".." + maxOccurs + "]";
	}

	public static String formatLength(int minLen, String maxLen) {
		return (minLen < 0) ? 0+".."+maxLen : minLen+".."+maxLen;
	}
	
	public static String generateAbstractSyntaxWrapper(String maxOccurs,
			Usage usage) {
		boolean repeat = maxOccurs.equals("*")
				|| Integer.parseInt(maxOccurs) > 1;
		boolean optional = usage == Usage.O;
		if (repeat && optional)
			return "[{%s}]";
		if (repeat)
			return "{%s}";
		if (optional)
			return "[%s]";
		return "%s";
	}

	public static String retrieveConformanceStatementsAsString(
			List<ConformanceStatement> conformanceStatementList) {
		StringBuilder result = new StringBuilder();
		if (conformanceStatementList != null
				&& conformanceStatementList.size() > 0) {

			for (ConformanceStatement s : conformanceStatementList) {

				result.append(((s.getId().contains("[") && s.getId().contains(
						"]")) ? s.getId().substring(s.getId().indexOf("[") + 1,
						s.getId().indexOf("]"))
						+ underScore
						+ s.getId().substring(s.getId().length() - 1,
								s.getId().length())
						+ " "
						+ colon
						+ " "
						+ s.getDescription() + "\n"
						: (s.getId().length() > 0) ? s.getId() + colon + " "
								+ s.getDescription() + "\n" : s
								.getDescription() + "\n"));
			}

		}
		return result.toString();
	}

	public static String breakUpSegmentName(String name) {

		if (name.indexOf(".") > 0) {
			String[] result = name.split("\\.");
			StringBuilder rename = new StringBuilder();
			for (int i = 0; i < result.length; i++) {
				if (i == result.length) {
					rename.append(result[i]);
				} else {
					rename.append(result[i] + " ");
				}
			}
			return rename.toString();
		} else {
			return name;
		}
	}
}
