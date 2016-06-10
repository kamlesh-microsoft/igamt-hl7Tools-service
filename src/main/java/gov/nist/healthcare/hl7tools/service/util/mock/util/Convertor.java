package gov.nist.healthcare.hl7tools.service.util.mock.util;

public class Convertor {
	

	public static String convertLength(int length){
		return length == 65536 ? "*" : Integer.toString(length);
	}

}
