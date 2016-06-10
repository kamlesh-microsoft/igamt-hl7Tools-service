package gov.nist.healthcare.hl7tools.service;

public class HL7DBServiceException extends Exception {

	private static final long serialVersionUID = 3946547634202825299L;
	
	public HL7DBServiceException(){
		super();
	}
	
	public HL7DBServiceException(String message){
		super(message);
	}
	
	public HL7DBServiceException(String message, Throwable cause){
		super(message, cause);
	}

}
