package gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain;

public enum Usage {

  R, O, X, W, B, C, RE, CE, NA;

  // gcr Copied from gov.nist.healthcare.tools.hl7.v2.igamt.lite.domain.Usage
  // There are values in the database that are not included in this enum.
  // We are assuming setting these to 'C' is acceptable.
  // It appears to be the prevailing practice.
  public static Usage fromValue(String v) {
    try {
      Usage usage = !"".equals(v) && v != null ? valueOf(v) : Usage.NA;
      return usage;
    } catch (IllegalArgumentException e) {
      return Usage.NA; // ????
    }
  }
}
