package gov.nist.healthcare.hl7tools.service.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DBDiffUtil {
  
  public final static String OLD_DIR = "src/main/resources/hl7db2-old";
  public final static String NEW_DIR = "src/main/resources/hl7db-new";
  public final static String DIFF_DIR = "src/main/resources/hl7db-diff";

  
  public static void main(String[] versions) throws JsonProcessingException, IOException{
    diffMessasges(versions);
  }
   
  
  private static void diffMessasges(String[] versions) throws JsonProcessingException, IOException{
    if(versions !=null && versions.length > 0){
     for(String version: versions){
       sortMessasge(OLD_DIR + "/" + version + "/messages.json");
       sortMessasge(NEW_DIR + "/" + version + "/messages.json");
       //generateDiff(version, "message");
     }
 }
 }
 
  
  private static void generateDiff(String version,String type) throws IOException{
    String diffPath = DIFF_DIR + "/"+ version + "/"+ type + "s.txt";
    File diffFolder = new File(DIFF_DIR + "/"+ version );
    if(!diffFolder.exists()){
      diffFolder.mkdirs();
    }
    File diffFile = new File(diffPath);
    if(!diffFile.exists())
      diffFile.createNewFile();
    String cmd = "/usr/local/bin/json-diff " + OLD_DIR + "/" + version + "/"+ type + "s.json " + NEW_DIR  + "/"+ version + "/"+ type + "s.json"+ "> " + diffPath;
    System.out.println(cmd);
    Runtime.getRuntime().exec(cmd);
  }
 
  
  
  private static void sortMessasge(String path) throws JsonProcessingException, IOException{
    ObjectMapper mapper = new ObjectMapper();    
    JsonNode root = mapper.readTree(new File(path));
    Iterator<JsonNode> it= root.elements();
    List<JsonNode> nodes = new ArrayList<JsonNode>();
    while(it.hasNext()){
      nodes.add(it.next());
    }
    Comparator<JsonNode> cmp = new Comparator<JsonNode>(){
      @Override
      public int compare(JsonNode o1, JsonNode o2) {
        // TODO Auto-generated method stub
        return o1.findValue("id").asText().compareTo(o2.findValue("id").asText());
      }
    };
    Collections.sort(nodes, cmp);
    writeTo(path, nodes);
 } 
  
  
  private static void writeTo(String path,  List<JsonNode> nodes) throws JsonGenerationException, JsonMappingException, IOException{
    ObjectMapper mapper = new ObjectMapper();
    System.out.println("Writing to file...");
    mapper.writerWithDefaultPrettyPrinter().writeValue(new File(path), nodes);
  }
  
}
