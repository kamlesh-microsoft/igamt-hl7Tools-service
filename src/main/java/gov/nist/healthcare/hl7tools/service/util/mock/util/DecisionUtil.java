package gov.nist.healthcare.hl7tools.service.util.mock.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DecisionUtil {

  public static final String DECISION_FOLDER = "src/main/resources/decisions/";

  /**
   * 
   * @param decision
   */
  public static void writeDatatypeDecision(String decision, String hl7Version) {
    write(decision, new File(DECISION_FOLDER + hl7Version + "/datatypes.html"));
  }

  public static void writeUsageDecision(String decision, String hl7Version) {
    write(decision, new File(DECISION_FOLDER + hl7Version + "/usages.html"));
  }


  /**
   * 
   * @param decision
   * @param f
   */
  public static void write(String content, File f) {
    BufferedWriter bw = null;
    FileWriter fw = null;
    try {
      if (!f.exists()) {
        File folder = f.getParentFile();
        if (!folder.exists()) {
          folder.mkdir();
        }
        f.createNewFile();
      }
      fw = new FileWriter(f, true);
      bw = new BufferedWriter(fw);
      bw.write(content);
      bw.write("\n");
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (bw != null)
          bw.close();
        if (fw != null)
          fw.close();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
  }
}
