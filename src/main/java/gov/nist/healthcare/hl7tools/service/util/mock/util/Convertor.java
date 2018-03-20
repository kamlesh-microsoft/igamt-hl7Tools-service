package gov.nist.healthcare.hl7tools.service.util.mock.util;

public class Convertor {


  public static String convertLength(String length) {
    return "65536".equals(length) ? "*" : length;
  }

}
