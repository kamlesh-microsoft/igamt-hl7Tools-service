package gov.nist.healthcare.hl7tools.service.serializer;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class SAXErrorHandler implements ErrorHandler {
	
	private String getParseExceptionInfo(SAXParseException spe) {
		String info = spe.getLineNumber() + ":" + spe.getColumnNumber()+" : " + spe.getMessage();
		return info;
	}

	public void warning(SAXParseException spe) {
		System.out.println("[Warning] " + getParseExceptionInfo(spe)); //FIXME use logging instead of system out
	}

	public void error(SAXParseException spe) throws SAXException {
		String message = "[Error] " + getParseExceptionInfo(spe);
		throw new SAXException(message);
	}

	public void fatalError(SAXParseException spe) throws SAXException {
		String message = "[Error] " + getParseExceptionInfo(spe);
		throw new SAXException(message);
	}

}
