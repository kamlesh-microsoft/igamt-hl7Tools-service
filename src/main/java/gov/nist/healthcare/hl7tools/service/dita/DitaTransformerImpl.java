/**
 * This class handles the generation of dita artifacts for a given Model object i.e an Implementation Guide or a Section. 
 * It also provides methods to zip up the generated dita artifacts and to generate a document in formats such as PDF and Word. 
 */
package gov.nist.healthcare.hl7tools.service.dita;

import gov.nist.dita.DitaRunner;
import gov.nist.healthcare.hl7tools.domain.Document;
import gov.nist.healthcare.hl7tools.domain.Model;
import gov.nist.healthcare.hl7tools.domain.Section;
import gov.nist.healthcare.hl7tools.service.transformer.Template;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

/**
 * @author rchitnis
 * 
 */
public class DitaTransformerImpl implements DitaTransformer {
//	private static final Logger logger = Logger
//			.getLogger(DitaTransformerImpl.class);
//
//	private static final String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
//	private static final String DITAMAP_DOC_TYPE_DECLARATION = "<!DOCTYPE bookmap PUBLIC \"-//OASIS//DTD DITA BookMap//EN\" \"bookmap.dtd\">";
//	private static final String TOPIC_DOC_TYPE_DECLARATION = "<!DOCTYPE topic PUBLIC \"-//OASIS//DTD DITA Topic//EN\" \"topic.dtd\">";
//	private static final String TOPIC_DIR = "topics/";
//	private static final String DITA_FILE_EXTENSION = ".dita";
//	private static final String DITAMAP_FILE_EXTENSION = ".ditamap";
//	private static final String IMPL_GUIDE_DITAMAP_FILE_NAME = "ImplGuide";
//	private static final String BOOKMAP_OPENING_TAG = "<bookmap id=\"ig\">";
//	private static final String BOOKMAP_CLOSING_TAG = "</bookmap>";
//	private static final String CHAPTER_CLOSING_TAG = "</chapter>";
//	private static final String TOPIC_CLOSING_TAG = "</topic>";
//	private static final String TOPICREF_CLOSING_TAG = "</topicref>";
//
//	public static final String DITA_OT_HOME = System.getenv("DITA_OT_HOME");
//	public static final String DITA_ARTIFACTS_FOLDER = System
//			.getenv("DITA_ARTIFACTS_FOLDER");
//
//	private static Properties prop = null;
//
//	/**
//	 * Generates all the dita artifacts i.e. a ditamap file at the
//	 * Implementation Guide level and dita file(s) for each chapter in it for
//	 * the supplied Implementation Guide and then creates a zip file to contain
//	 * all dita artifacts.
//	 * 
//	 * @param document
//	 *            Implmentation Guide model object.
//	 * @return the created zip PDF file name. The file name has the time stamp
//	 *         to indicate the time the file was created.
//	 */
//	@Override
//	public String generateDitaZip(Model model) {
//		String formattedCurrentDateTime = formatCurrentDateTime();
//		transformToDita(model, formattedCurrentDateTime);
//		return createDitaZipFile(formattedCurrentDateTime);
//	}
//
//	public String generateDitaZipwithImages(Model model, File[] listofImage) {
//		String formattedCurrentDateTime = formatCurrentDateTime();
//		moveToImageFilesToDITATopics(listofImage, formattedCurrentDateTime);
//		transformToDita(model, formattedCurrentDateTime);
//		return createDitaZipFile(formattedCurrentDateTime);
//	}
//
//	/**
//	 * Generates a PDF file for the supplied Model by first generating all the
//	 * dita artifacts i.e. a ditamap file at the root level and dita file(s) for
//	 * each chapter in it and then calling dita-runner which is a wrapper around
//	 * dita tool kit API.
//	 * 
//	 * @param model
//	 *            Model object.
//	 * @return the generated PDF file name. The file name has the time stamp to
//	 *         indicate the time the file was created.
//	 * 
//	 */
//	@Override
//	public String generatePDF(Model model) {
//		String formattedCurrentDateTime = formatCurrentDateTime();
//		String ditaMapFileName = transformToDita(model,
//				formattedCurrentDateTime);
//		String ditaOutputfolder = null;
//		String outputFileName = null;
//
//		try {
//			if (DITA_OT_HOME == null) {
//				throw new Exception(
//						"Environment variable DITA_OT_HOME does not exist.");
//			}
//			if (model == null
//					|| (!(model instanceof Document) && !(model instanceof Section))) {
//				throw new Exception(
//						"model is null or model is not a document or section");
//			}
//
//			DitaRunner dr = new DitaRunner(DITA_OT_HOME);
//
//			ditaOutputfolder = getDitaOutputFolder(formattedCurrentDateTime);
//
//			if (model instanceof Document) {
//				outputFileName = ditaOutputfolder
//						.concat(IMPL_GUIDE_DITAMAP_FILE_NAME).concat("-")
//						.concat(formattedCurrentDateTime).concat(".pdf");
//			} else if (model instanceof Section) {
//				Section section = (Section) model;
//				outputFileName = ditaOutputfolder
//						.concat(stripSpecialChars(section.getId()))
//						.concat("-").concat(formattedCurrentDateTime)
//						.concat(".pdf");
//			}
//			new File(ditaOutputfolder).mkdirs();
//			dr.generateDocument(new File(ditaMapFileName), new File(
//					ditaOutputfolder), "PDF");
//
//			return outputFileName;
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error(e);
//		}
//		return null;
//	}
//
//	public String generatePDFwithImages(Model model, File[] listofImage) {
//		String formattedCurrentDateTime = formatCurrentDateTime();
//
//		moveToImageFilesToDITATopics(listofImage, formattedCurrentDateTime);
//		String ditaMapFileName = transformToDita(model,
//				formattedCurrentDateTime);
//		String ditaOutputfolder = null;
//		String outputFileName = null;
//		
//		try {
//			if (DITA_OT_HOME == null) {
//				throw new Exception(
//						"Environment variable DITA_OT_HOME does not exist.");
//			}
//			if (model == null
//					|| (!(model instanceof Document) && !(model instanceof Section))) {
//				throw new Exception(
//						"model is null or model is not a document or section");
//			}
//
//			DitaRunner dr = new DitaRunner(DITA_OT_HOME);
//
//			ditaOutputfolder = getDitaOutputFolder(formattedCurrentDateTime);
//
//			if (model instanceof Document) {
//				outputFileName = ditaOutputfolder
//						.concat(IMPL_GUIDE_DITAMAP_FILE_NAME).concat("-")
//						.concat(formattedCurrentDateTime).concat(".pdf");
//			} else if (model instanceof Section) {
//				Section section = (Section) model;
//				outputFileName = ditaOutputfolder
//						.concat(stripSpecialChars(section.getId()))
//						.concat("-").concat(formattedCurrentDateTime)
//						.concat(".pdf");
//			}
//			new File(ditaOutputfolder).mkdirs();
//			dr.generateDocument(new File(ditaMapFileName), new File(
//					ditaOutputfolder), "PDF");
//
//			return outputFileName;
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error(e);
//		}
//		return null;
//	}
//
//	@Override
//	public String generateRTF(Model model) {
//		String formattedCurrentDateTime = formatCurrentDateTime();
//		String ditaMapFileName = transformToDita(model,
//				formattedCurrentDateTime);
//		String ditaOutputfolder = null;
//		String outputFileName = null;
//
//		try {
//			if (DITA_OT_HOME == null) {
//				throw new Exception(
//						"Environment variable DITA_OT_HOME does not exist.");
//			}
//			if (model == null
//					|| (!(model instanceof Document) && !(model instanceof Section))) {
//				throw new Exception(
//						"model is null or model is not a document or section");
//			}
//
//			DitaRunner dr = new DitaRunner(DITA_OT_HOME);
//
//			ditaOutputfolder = getDitaOutputFolder(formattedCurrentDateTime);
//
//			if (model instanceof Document) {
//				outputFileName = ditaOutputfolder
//						.concat(IMPL_GUIDE_DITAMAP_FILE_NAME).concat("-")
//						.concat(formattedCurrentDateTime).concat(".rtf");
//			} else if (model instanceof Section) {
//				Section section = (Section) model;
//				outputFileName = ditaOutputfolder
//						.concat(stripSpecialChars(section.getId()))
//						.concat("-").concat(formattedCurrentDateTime)
//						.concat(".rtf");
//			}
//
//			new File(ditaOutputfolder).mkdirs();
//			dr.generateDocument(new File(ditaMapFileName), new File(
//					ditaOutputfolder), "WORDRTF");
//
//			return outputFileName;
//		} catch (Exception e) {
//			logger.error(e);
//		}
//		return null;
//	}
//
//	public String generateRTFwithImages(Model model, File[] listofImage) {
//		String formattedCurrentDateTime = formatCurrentDateTime();
//		moveToImageFilesToDITATopics(listofImage, formattedCurrentDateTime);
//		String ditaMapFileName = transformToDita(model,
//				formattedCurrentDateTime);
//		String ditaOutputfolder = null;
//		String outputFileName = null;
//
//		try {
//			if (DITA_OT_HOME == null) {
//				throw new Exception(
//						"Environment variable DITA_OT_HOME does not exist.");
//			}
//			if (model == null
//					|| (!(model instanceof Document) && !(model instanceof Section))) {
//				throw new Exception(
//						"model is null or model is not a document or section");
//			}
//
//			DitaRunner dr = new DitaRunner(DITA_OT_HOME);
//
//			ditaOutputfolder = getDitaOutputFolder(formattedCurrentDateTime);
//
//			if (model instanceof Document) {
//				outputFileName = ditaOutputfolder
//						.concat(IMPL_GUIDE_DITAMAP_FILE_NAME).concat("-")
//						.concat(formattedCurrentDateTime).concat(".rtf");
//			} else if (model instanceof Section) {
//				Section section = (Section) model;
//				outputFileName = ditaOutputfolder
//						.concat(stripSpecialChars(section.getId()))
//						.concat("-").concat(formattedCurrentDateTime)
//						.concat(".rtf");
//			}
//
//			new File(ditaOutputfolder).mkdirs();
//			dr.generateDocument(new File(ditaMapFileName), new File(
//					ditaOutputfolder), "WORDRTF");
//
//			return outputFileName;
//		} catch (Exception e) {
//			logger.error(e);
//		}
//		return null;
//	}
//
//	/**
//	 * Creates dita xml files for the supplied Model.
//	 * 
//	 * @param model
//	 *            Model object.
//	 * @param timestamp
//	 *            the current date time stamp string. it will be used to create
//	 *            the folder to store ditamap and dita files
//	 * @return the generated ditamap file name. The file name has the time stamp
//	 *         to indicate the time the file was created.
//	 */
//	private String transformToDita(Model model, String timestamp) {
//		if (model == null
//				|| (!(model instanceof Document) && !(model instanceof Section))) {
//			logger.error("model is null or model is not a document or section");
//			return null;
//		}
//
//		if (model instanceof Document) {
//			Document document = (Document) model;
//			if (document.getSections() == null
//					|| document.getSections().isEmpty()) {
//				logger.info("model does not have any sections");
//				return null;
//			}
//		} else if (model instanceof Section) {
//			Section section = (Section) model;
//			if (section.getSubSections() == null
//					|| section.getSubSections().isEmpty()) {
//				logger.info("model does not have any sections");
//			}
//		}
//
//		return generateDitaMap(model, timestamp);
//	}
//
//	@SuppressWarnings("unused")
//	private String generateDita(Document document) {
//		return null;
//	}
//
//	/**
//	 * Generates ditamap xml and dita xml by looping over the sections of the
//	 * supplied Model object.
//	 * 
//	 * @param model
//	 *            Model object.
//	 * @param timestamp
//	 *            the current date time stamp string. it will be used to create
//	 *            the folder to store ditamap and dita files
//	 * 
//	 * @return the generated ditamap xml string for the supplied Model.
//	 */
//	@SuppressWarnings("unused")
//	private String generateDitaMap(Model model, String timestamp) {
//		int sectionNum = 0;
//		StringBuilder ditaMapStr = new StringBuilder();
//		String bookTitleStr = generateBookTitle(model);
//		String frontMatterStr = generateFrontMatter(model);
//		String appendixStr = generateAppendix(model);
//		String backMatterStr = generateBackMatter(model);
//		StringBuilder sectionStr = new StringBuilder();
//
//		List<Section> sectionList = new ArrayList<Section>();
//		if (model instanceof Document) {
//			sectionList = ((Document) model).getSections();
//		} else if (model instanceof Section) {
//			sectionList.add((Section) model);
//		}
//
//		int index = 0;
//		for (Section section : sectionList) {
//			if(section.getId() == null){
//				section.setId("section_"+ index++);
//			}
//			
//			sectionStr.append("\t");
//			processSections(section, sectionStr, Boolean.TRUE, -1, 0, timestamp);
//			if (sectionNum++ < sectionList.size() - 1) {
//				sectionStr.append("\n");
//			}
//		}
//
//		ditaMapStr.append(XML_DECLARATION).append("\n")
//				.append(DITAMAP_DOC_TYPE_DECLARATION).append("\n")
//				.append(BOOKMAP_OPENING_TAG).append("\n").append("\t")
//				.append(bookTitleStr).append("\n").append("\t")
//				.append(frontMatterStr).append("\n").append(sectionStr)
//				.append("\n").append("\t").append(backMatterStr).append("\n")
//				.append(BOOKMAP_CLOSING_TAG);
//
//		return writeDitaMapFile(model, ditaMapStr.toString(), timestamp);
//	}
//
//	private void moveToImageFilesToDITATopics(File[] listofImage,
//			String timestamp) {
//
//		if (listofImage != null) {
//			String ditaArtifactsFolder = getDitaArtifactsFolder(timestamp);
//			String pathName = ditaArtifactsFolder + TOPIC_DIR;
//			for (int i = 0; i < listofImage.length; i++) {
//
//				if (listofImage[i].isFile()) {
//					File source = listofImage[i];
//					File dest = new File(pathName + listofImage[i].getName());
//					try {
//						FileUtils.copyFile(source, dest);
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//
//				}
//			}
//		}
//	}
//
//	/**
//	 * Recursively processes the supplied section of the Model until all its
//	 * sub-sections are processed.
//	 * 
//	 * @param section
//	 *            Section model object.
//	 * @param ditaMapSectionStr
//	 *            ditamap xml string for the supplied section that gets appended
//	 *            to in each recursion.
//	 * @param root
//	 *            flag to indicate if this is a sub section.
//	 * @param parentHeight
//	 *            number to indicate how many tabs should be added before
//	 *            closing the tag for a section.
//	 * @param nodeHeight
//	 *            number to indicate how many tabs should be added before
//	 *            opening the tag for a section.
//	 * @param timestamp
//	 *            the current date time stamp string. it represents the folder
//	 *            to store ditamap and dita files
//	 * 
//	 * @return the generated ditamap xml string for the supplied section and its
//	 *         sub-sections.
//	 */
//	private StringBuilder processSections(Section section,
//			StringBuilder ditaMapSectionStr, boolean root, int parentHeight,
//			int nodeHeight, String timestamp) {
//		String sectionDitaStr = generateDita(section);
//		String ditaFileName = stripSpecialChars(section.getId());
//		writeDitaFile(sectionDitaStr, ditaFileName.concat(DITA_FILE_EXTENSION),
//				timestamp);
//
//		if (section.getSubSections() == null
//				|| section.getSubSections().isEmpty()) {
//			if (root) {
//				ditaMapSectionStr.append(
//						generateChapterOpeningTag(ditaFileName)).append(
//						CHAPTER_CLOSING_TAG);
//			} else {
//				for (int repeatTab = 1; repeatTab <= nodeHeight + 1; repeatTab++) {
//					ditaMapSectionStr.append("\t");
//				}
//				ditaMapSectionStr.append(generateTopicRef(ditaFileName))
//						.append("\n");
//			}
//		} else {
//			if (root) {
//				ditaMapSectionStr.append(
//						generateChapterOpeningTag(ditaFileName)).append("\n");
//			} else {
//				for (int repeatTab = 1; repeatTab <= nodeHeight + 1; repeatTab++) {
//					ditaMapSectionStr.append("\t");
//				}
//				ditaMapSectionStr.append(
//						generateTopicRefOpeningTag(ditaFileName)).append("\n");
//			}
//			nodeHeight++;
//			parentHeight++;
//			for (Section subSection : section.getSubSections()) {
//				processSections(subSection, ditaMapSectionStr, Boolean.FALSE,
//						parentHeight, nodeHeight, timestamp);
//			}
//			if (root) {
//				ditaMapSectionStr.append("\t").append(CHAPTER_CLOSING_TAG);
//			} else {
//				for (int repeatTab = 1; repeatTab <= parentHeight + 1; repeatTab++) {
//					ditaMapSectionStr.append("\t");
//				}
//				ditaMapSectionStr.append(TOPICREF_CLOSING_TAG).append("\n");
//			}
//		}
//		return ditaMapSectionStr;
//	}
//
//	/**
//	 * Generates dita xml string for the supplied section of Model.
//	 * 
//	 * @param section
//	 *            Section model object.
//	 * @return the generated dita xml string.
//	 * 
//	 */
//	private String generateDita(Section section) {
//		StringBuilder ditaSectionStr = new StringBuilder();
//		StringBuilder ditaSectionContentStr = new StringBuilder();
//
//		ditaSectionContentStr.append(processContent(section.getStrContent()));
//
//		ditaSectionStr
//				.append(XML_DECLARATION)
//				.append("\n")
//				.append(TOPIC_DOC_TYPE_DECLARATION)
//				.append("\n")
//				.append("<topic id=\"" + stripSpecialChars(section.getId())
//						+ "\" xml:lang=\"en-us\">").append("\n").append("\t")
//				.append("<title>" + section.getTitle() + "</title>")
//				/* .append("\n").append("\t").append("<shortdesc></shortdesc>") */
//				.append("\n").append("\t").append("<body>")
//				.append(ditaSectionContentStr).append("\n").append("\t")
//				.append("</body>").append("\n").append(TOPIC_CLOSING_TAG);
//
//		return ditaSectionStr.toString();
//	}
//
//	private String generateTopicRef(String ditaFileName) {
//		return "<topicref href=\"" + TOPIC_DIR + ditaFileName + ".dita\"/>";
//	}
//
//	private String generateChapterOpeningTag(String ditaFileName) {
//		return "<chapter href=\"" + TOPIC_DIR + ditaFileName + ".dita\">";
//	}
//
//	private String generateTopicRefOpeningTag(String ditaFileName) {
//		return "<topicref href=\"" + TOPIC_DIR + ditaFileName + ".dita\">";
//	}
//
//	@SuppressWarnings("unused")
//	private String generateDitaMap(Section section) {
//		return null;
//	}
//
//	private String generateBookTitle(Model model) {
//		StringBuilder bookTitleStr = new StringBuilder();
//		if (model instanceof Document) {
//			bookTitleStr
//					.append("<booktitle>")
//					.append("\n")
//					.append("\t")
//					.append("\t")
//					.append("<booklibrary>" + ((Document) model).getTitle() + "</booklibrary>")
//					.append("\n")
//					.append("\t")
//					.append("\t")
//					.append("<mainbooktitle>" + ((Document) model).getDescription() + "</mainbooktitle>")
//					.append("\n").append("\t").append("</booktitle>");
//		} else {
//			if (model instanceof Section) {
//				bookTitleStr
//						.append("<booktitle>")
//						.append("\n")
//						.append("\t")
//						.append("\t")
//						.append("<booklibrary>")
//						.append(((Section) model).getTitle() + "</booklibrary>")
//						.append("\n").append("\t").append("\t")
//						.append("<mainbooktitle> </mainbooktitle>")
//						.append("\n").append("\t").append("</booktitle>");
//			} else {
//				bookTitleStr
//						.append("<booktitle>")
//						.append("\n")
//						.append("\t")
//						.append("\t")
//						.append("<booklibrary>")
//						.append("Implementation Guide for HL7 Version V2.X</booklibrary>")
//						.append("\n").append("\t").append("\t")
//						.append("<mainbooktitle> </mainbooktitle>")
//						.append("\n").append("\t").append("</booktitle>");
//			}
//		}
//		return bookTitleStr.toString();
//	}
//
//	private String generateFrontMatter(Model model) {
//		StringBuilder frontMatterStr = new StringBuilder();
//		frontMatterStr.append("<frontmatter>").append("\n").append("\t")
//				.append("\t").append("<booklists>").append("\n").append("\t")
//				.append("\t").append("\t").append("<toc/>").append("\n")
//				.append("\t").append("\t").append("</booklists>").append("\n")
//				.append("\t").append("</frontmatter>");
//
//		return frontMatterStr.toString();
//	}
//
//	private String generateAppendix(Model model) {
//		return null;
//	}
//
//	private String generateBackMatter(Model model) {
//		StringBuilder backMatterStr = new StringBuilder();
//		backMatterStr.append("<backmatter>").append("\n").append("\t")
//				.append("\t").append("<booklists>").append("\n").append("\t")
//				.append("\t").append("\t").append("<indexlist/>").append("\n")
//				.append("\t").append("\t").append("</booklists>").append("\n")
//				.append("\t").append("</backmatter>");
//
//		return backMatterStr.toString();
//	}
//
//	/**
//	 * Creates a file in the file system and writes the supplied ditamap xml in
//	 * it.
//	 * 
//	 * @param model
//	 *            Model object
//	 * @param content
//	 *            ditamap xml to be written in the file.
//	 * @param timestamp
//	 *            to suffix to the ditamap file name.
//	 * @return the created file name.
//	 */
//	private String writeDitaMapFile(Model model, String content,
//			String timestamp) {
//		String pathName = null;
//		String fileName = null;
//		PrintWriter writer = null;
//		String ditaArtifactsFolder = null;
//		try {
//
//			ditaArtifactsFolder = getDitaArtifactsFolder(timestamp);
//
//			if (model instanceof Document) {
//				fileName = ditaArtifactsFolder
//						.concat(IMPL_GUIDE_DITAMAP_FILE_NAME).concat("-")
//						.concat(timestamp).concat(DITAMAP_FILE_EXTENSION);
//			} else if (model instanceof Section) {
//				Section section = (Section) model;
//				fileName = ditaArtifactsFolder
//						.concat(stripSpecialChars(section.getId()))
//						.concat("-").concat(timestamp)
//						.concat(DITAMAP_FILE_EXTENSION);
//			}
//
//			pathName = ditaArtifactsFolder;
//			File path = new File(pathName);
//			File file = new File(fileName);
//			path.mkdirs();
//			writer = new PrintWriter(file);
//			writer.print(content);
//		}
//
//		catch (FileNotFoundException fnfe) {
//			logger.error(fnfe);
//			fileName = null;
//		} catch (SecurityException se) {
//			logger.error(se);
//			fileName = null;
//		} catch (Exception e) {
//			logger.error(e);
//			fileName = null;
//		} finally {
//			if (writer != null) {
//				writer.close();
//			}
//		}
//		return fileName;
//	}
//
//	/**
//	 * Creates a file in the file system and writes the supplied dita xml in it.
//	 * 
//	 * @param content
//	 *            dita xml to be written in the file.
//	 * @param filename
//	 *            to be created.
//	 * @param timestamp
//	 *            timestamp representing the folder to store ditamap and dita
//	 *            files.
//	 * 
//	 */
//	private void writeDitaFile(String content, String filename, String timestamp) {
//		PrintWriter writer = null;
//		String ditaArtifactsFolder = null;
//		try {
//
//			ditaArtifactsFolder = getDitaArtifactsFolder(timestamp);
//
//			String pathName = ditaArtifactsFolder + TOPIC_DIR;
//			String fileName = pathName + filename;
//
//			File path = new File(pathName);
//			File file = new File(fileName);
//			path.mkdirs();
//			writer = new PrintWriter(file);
//			writer.print(content);
//		}
//
//		catch (FileNotFoundException fnfe) {
//			logger.error(fnfe);
//		} catch (SecurityException se) {
//			logger.error(se);
//		} catch (Exception e) {
//			logger.error(e);
//		} finally {
//			if (writer != null) {
//				writer.close();
//			}
//		}
//
//	}
//
//	private String stripSpecialChars(String s) {
//		String pattern = "[\\s,()\"\'\\/{}:-]";
//		String replace = "";
//		Pattern p = Pattern.compile(pattern);
//		Matcher m = p.matcher(s);
//		s = m.replaceAll(replace);
//		s = s.replaceAll("#", replace);
//		s = s.replaceAll("~", replace);
//		return s;
//	}
//
//	/**
//	 * Processes the supplied content string to convert the html tags in it to
//	 * the corresponding dita tags.
//	 * 
//	 * @param content
//	 * @return processed content as per the dita specifications.
//	 */
//	private String processContent(String content) {
//		content = content.trim().replaceFirst(
//				System.getProperty("line.separator"), "");
//		content = content.replaceFirst("\t", "");
//		/* remove <br/> and empty <ul></ul> tags. */
//		content = content.replace("<br/>", " ").replace("<br />", " ")
//				.replace("<br>", " ").replace("</br>", " ");
//		content = content.replace("<ul></ul>", "");
//		content = content.replaceAll("<strong>", "<b>");
//		content = content.replaceAll("</strong>", "</b>");
//		content = content.replaceAll("<em>", "<i>");
//		content = content.replaceAll("</em>", "</i>");
//		content = content.replaceAll("<tbody>", "");
//		content = content.replaceAll("</tbody>", "");
//		content = content.replaceAll("<thead>", "");
//		content = content.replaceAll("</thead>", "");
//		content = content.replaceAll("<div>", "");
//		content = content.replaceAll("</div>", "");
//		content = content.replaceAll("<p>&nbsp;</p>", "");
//		content = content.replaceAll("&nbsp;", "");
//		content = content.replaceAll("&rsquo;", "'");
//		content = content.replaceAll("&acirc;", "'");
//		content = content.replaceAll("&ldquo;", "'" );
//		content = content.replaceAll("&rdquo;", "'" );
//		content = content.replaceAll("&ndash;", "-" );
//		content = removeSpace(content);
//		content = convertHTMLImgToDitaImage(content);
//		content = convertHTMLTableToDitaTable(content);
//		content = convertHtmlColWidthToDitaColWidth(content);
//
//		if (content.contains("<table>")) {
//			content = convertHtmlTableToDitaTable(content);
//		}
//
//		/* replace table related html tags with dita tags */
//		content = content.replace("<caption>", "<title>");
//		content = content.replace("</caption>", "</title>");
//		content = content.replace("<tr>", "<row>");
//		content = content.replace("</tr>", "</row>");
//		content = content.replace("<td>", "<entry>");
//		content = content.replace("</td>", "</entry>");
//		content = content.replace("<th>", "<entry>");
//		content = content.replace("</th>", "</entry>");
//
//		content = convertHtmlHyperLinksToDitaLinks(content);
//
//		if (!(content.isEmpty()) && !(content.startsWith("<p>"))
//				&& !(content.startsWith("<table>"))
//				&& !(content.startsWith("<ul>"))
//				&& !(content.startsWith("<ol>"))) {
//			content = "<p>" + content + "</p>";
//		}
//		return content;
//	}
//
//	private String removeSpace(String content){
//		content = content.replaceAll("<p></p>",""); 
//		return content;
//	}
//	/**
//	 * Converts the html hyper link tags in the supplied content string to the
//	 * corresponding dita xref tags.
//	 * 
//	 * @param content
//	 * @return processed content as per the dita specifications.
//	 */
//
//	private String convertHTMLImgToDitaImage(String content) {
//		if (content.indexOf("<img") >= 0) {
//			int beginIndex = content.indexOf("<img");
//			int endIndex = content.indexOf(">", beginIndex) +1;
//			String htmlImgaeTag = content.substring(beginIndex, endIndex);
//			
//			int srcBeginIndex = htmlImgaeTag.indexOf("src=") + "src=".length() + 1;
//			srcBeginIndex = htmlImgaeTag.indexOf("/", srcBeginIndex) + 1;
//			int srcEndIndex = htmlImgaeTag.indexOf("\"", srcBeginIndex);
//			String src = htmlImgaeTag.substring(srcBeginIndex, srcEndIndex);
//			src = src.replaceAll(" ", "%20");
//			
//			int altBeginIndex = htmlImgaeTag.indexOf("alt=") + "alt=".length() + 1;
//			int altEndIndex = htmlImgaeTag.indexOf("\"", altBeginIndex);
//			String alt = htmlImgaeTag.substring(altBeginIndex, altEndIndex);
//			
//			content = content
//					.replace(htmlImgaeTag,
//							"<image href=\"" + src + "\" placement=\"break\" width=\"480\"><alt>" + alt + "</alt></image>");
//			content = convertHTMLImgToDitaImage(content);
//		}
//		return content;
//	}
//	
//	private String convertHTMLTableToDitaTable(String content) {
//		if (content.indexOf("<table ") >= 0) {
//			int beginIndex = content.indexOf("<table ");
//			int endIndex = content.indexOf(">", beginIndex) +1;
//			String htmlTableTag = content.substring(beginIndex, endIndex);
//			content = content
//					.replaceAll(htmlTableTag,
//							"<table>");
//			content = convertHTMLTableToDitaTable(content);
//		}
//		
//		if (content.indexOf("<tr ") >= 0) {
//			int beginIndex = content.indexOf("<tr ");
//			int endIndex = content.indexOf(">", beginIndex) +1;
//			String htmlTR = content.substring(beginIndex, endIndex);
//			content = content
//					.replaceAll(htmlTR,
//							"<tr>");
//			content = convertHTMLTableToDitaTable(content);
//		}
//		
//		if (content.indexOf("<td ") >= 0) {
//			int beginIndex = content.indexOf("<td ");
//			int endIndex = content.indexOf(">", beginIndex) +1;
//			String htmlTD = content.substring(beginIndex, endIndex);
//			content = content
//					.replaceAll(htmlTD,
//							"<td>");
//			content = convertHTMLTableToDitaTable(content);
//		}
//		
//		if (content.indexOf("<th ") >= 0) {
//			int beginIndex = content.indexOf("<th ");
//			int endIndex = content.indexOf(">", beginIndex) +1;
//			String htmlTH = content.substring(beginIndex, endIndex);
//			content = content
//					.replaceAll(htmlTH,
//							"<th>");
//			content = convertHTMLTableToDitaTable(content);
//		}
//		
//		if (content.indexOf("<p ") >= 0) {
//			int beginIndex = content.indexOf("<p ");
//			int endIndex = content.indexOf(">", beginIndex) +1;
//			String htmlTH = content.substring(beginIndex, endIndex);
//			content = content
//					.replaceAll(htmlTH,
//							"<p>");
//			content = convertHTMLTableToDitaTable(content);
//		}
//		return content;
//	}
//
//	@SuppressWarnings("unused")
//	private String convertHtmlHyperLinksToDitaLinks(String content) {
//		int fromIndex = 0, linkStartIndex, linkEndIndex;
//		String htmlLinkOpeningTag = "<a";
//		String htmlLinkClosingTag = "</a>";
//		String ditaLinkOpeningTag = "<xref";
//		String ditaLinkClosingTag = "</xref>";
//
//		while (content.indexOf(htmlLinkOpeningTag, fromIndex) > -1
//				&& content.indexOf(htmlLinkClosingTag, fromIndex) > -1) {
//			linkStartIndex = content.indexOf(htmlLinkOpeningTag, fromIndex);
//			linkEndIndex = content.indexOf(htmlLinkClosingTag, fromIndex);
//
//			content = content.replaceFirst(htmlLinkOpeningTag,
//					ditaLinkOpeningTag);
//			content = content.replaceFirst(htmlLinkClosingTag,
//					ditaLinkClosingTag);
//
//			fromIndex += linkEndIndex
//					+ (ditaLinkOpeningTag.length() - htmlLinkOpeningTag
//							.length()) + ditaLinkClosingTag.length();
//		}
//
//		return content;
//	}
//
//	private String convertHtmlColWidthToDitaColWidth(String content) {
//		int fromIndex = 0;
//		String htmlColOpeningTag = "<col ";
//		String htmlColWidth = " width";
//		String ditaColOpeningTag = "<colspec ";
//		String ditaColWidth = " colwidth";
//
//		while (content.indexOf(Template.htmlColumnSpec, fromIndex) > -1) {
//			content = content
//					.replaceFirst(htmlColOpeningTag, ditaColOpeningTag);
//			content = content.replaceFirst(htmlColWidth, ditaColWidth);
//		}
//
//		return content;
//	}
//
//	/**
//	 * Converts the html table tags in the supplied content string to the
//	 * corresponding table tags.
//	 * 
//	 * @param content
//	 * @return processed content as per the dita specifications.
//	 */
//	private String convertHtmlTableToDitaTable(String content) {
//		StringBuilder sb = new StringBuilder(content);
//		int fromIndex = 0, tableStartIndex, tableEndIndex, firstRowStartIndex, firstRowEndIndex, colSpecIndex;
//		boolean hasHeader = false, hasColSpec = false;
//		String tableOpeningTag = "<table>";
//		String tableClosingTag = "</table>";
//		StringBuilder tgroupOpeningTag = new StringBuilder("<tgroup cols=");
//		String tgroupClosingTag = "</tgroup>";
//		String theadOpeningTag = "<thead>";
//		;
//		String theadClosingTag = "</thead>";
//		String tbodyOpeningTag = "<tbody>";
//		String tbodyClosingTag = "</tbody>";
//		String htmlRowOpeningTag = "<tr>";
//		String htmlRowClosingTag = "</tr>";
//		String htmlcolumnOpeningTag = "<td>";
//		String htmlHeaderColumnOpeningTag = "<th>";
//		String htmlHeaderColumnClosingTag = "</th>";
//		String colspecOpeningTag = "<colspec";
//
//		// loop through the content and re-construct each <table> tag as per
//		// dita specs
//		while (content.indexOf(tableOpeningTag, fromIndex) > -1
//				&& content.indexOf(tableClosingTag, fromIndex) > -1) {
//
//			tableStartIndex = content.indexOf(tableOpeningTag, fromIndex);
//			tableEndIndex = content.indexOf(tableClosingTag, fromIndex);			
//			String tableContent = content.substring(tableStartIndex, tableEndIndex);
//			if (tableContent.indexOf(htmlRowOpeningTag) > -1
//					&& tableContent.indexOf(htmlRowClosingTag) > -1) {
//				colSpecIndex = tableContent.indexOf(colspecOpeningTag);
//
//				if (colSpecIndex > 0) {
//					hasColSpec = true;
//				}
//				firstRowStartIndex = tableContent.indexOf(htmlRowOpeningTag);
//				firstRowEndIndex = tableContent.indexOf(htmlRowClosingTag);
//				String rowContent = tableContent.substring(firstRowStartIndex,
//						firstRowEndIndex);
//				if (rowContent.indexOf(htmlHeaderColumnOpeningTag) > -1
//						&& rowContent.indexOf(htmlHeaderColumnClosingTag) > -1) {
//					hasHeader = true;
//				}
//				// count number of columns in the table and append the count to
//				// tgroupOpeningTag
//				Pattern p = null;
//				if (hasHeader) {
//					p = Pattern.compile(htmlHeaderColumnOpeningTag);
//				} else {
//					p = Pattern.compile(htmlcolumnOpeningTag);
//				}
//				Matcher m = p.matcher(rowContent);
//				int columnCount = 0, offset = 0;
//				while (m.find()) {
//					columnCount += 1;
//				}
//				tgroupOpeningTag.append("\"" + columnCount).append("\"")
//						.append(" colsep=\"1\" rowsep=\"1\"").append(">");
//
//				if (hasHeader) {
//					if (hasColSpec) {
//						offset = tableStartIndex + colSpecIndex;
//						sb.insert(offset, tgroupOpeningTag);
//						offset = tableStartIndex + firstRowStartIndex
//								+ tgroupOpeningTag.length();
//						sb.insert(offset, theadOpeningTag);
//					} else {
//						offset = tableStartIndex + firstRowStartIndex;
//						sb.insert(offset, tgroupOpeningTag + theadOpeningTag);
//					}
//					offset = tableStartIndex + firstRowEndIndex
//							+ htmlRowClosingTag.length()
//							+ tgroupOpeningTag.length()
//							+ theadOpeningTag.length();
//					sb.insert(offset, theadClosingTag);
//					offset = tableStartIndex + firstRowEndIndex
//							+ htmlRowClosingTag.length()
//							+ tgroupOpeningTag.length()
//							+ theadOpeningTag.length()
//							+ theadClosingTag.length();
//					sb.insert(offset, tbodyOpeningTag);
//					offset = tableEndIndex + tgroupOpeningTag.length()
//							+ theadOpeningTag.length()
//							+ theadClosingTag.length()
//							+ tbodyOpeningTag.length();
//					sb.insert(offset, tbodyClosingTag + tgroupClosingTag);
//				} else {
//					if (hasColSpec) {
//						offset = tableStartIndex + colSpecIndex;
//						sb.insert(offset, tgroupOpeningTag);
//					} else {
//						offset = tableStartIndex + firstRowStartIndex;
//						sb.insert(offset, tgroupOpeningTag);
//					}
//					offset = offset + tgroupOpeningTag.length();
//					sb.insert(offset, tbodyOpeningTag);
//					offset = tableEndIndex + tgroupOpeningTag.length()
//							+ tbodyOpeningTag.length();
//					sb.insert(offset, tbodyClosingTag + tgroupClosingTag);
//				}
//			}
//
//			fromIndex = tableEndIndex
//					+ tableClosingTag.length()
//					+ tgroupOpeningTag.length()
//					+ tgroupClosingTag.length()
//					+ tbodyOpeningTag.length()
//					+ tbodyClosingTag.length()
//					+ (hasHeader ? (theadOpeningTag.length() + theadClosingTag
//							.length()) : 0);
//
//			hasHeader = false;
//			hasColSpec = false;
//			tgroupOpeningTag.delete(0, tgroupOpeningTag.length());
//			tgroupOpeningTag.append("<tgroup cols=");
//			
//			content = sb.toString();
//		}
//		return sb.toString();
//	}
//
//	/**
//	 * Zips up the ditamap file and all referenced dita files that are already
//	 * created in the pre-determined folder name. The name of the folder that
//	 * has all the dita files is contained in the application.properties file
//	 * 
//	 * @param timestamp
//	 *            to suffix to the zip file name.
//	 * @return name of the zip file that gets created.
//	 */
//	private String createDitaZipFile(String timestamp) {
//		String ditaZipFileName = null;
//		String ditaArtifactsFolder = null;
//		try {
//			ditaArtifactsFolder = getDitaArtifactsFolder(timestamp);
//
//			ditaZipFileName = ditaArtifactsFolder
//					.concat(IMPL_GUIDE_DITAMAP_FILE_NAME).concat(timestamp)
//					.concat(".zip");
//			FileOutputStream fos = new FileOutputStream(ditaZipFileName);
//			ZipOutputStream zos = new ZipOutputStream(fos);
//
//			addToZipFile(ditaArtifactsFolder
//					+ IMPL_GUIDE_DITAMAP_FILE_NAME.concat("-")
//							.concat(timestamp).concat(DITAMAP_FILE_EXTENSION),
//					zos);
//
//			File[] files = new File(ditaArtifactsFolder + TOPIC_DIR)
//					.listFiles();
//
//			for (File file : files) {
//				if (file.isFile()) {
//					addToZipFile(
//							ditaArtifactsFolder + TOPIC_DIR + file.getName(),
//							zos);
//				}
//			}
//
//			zos.close();
//			fos.flush();
//			fos.close();
//
//		} catch (FileNotFoundException fnfe) {
//			logger.error(fnfe);
//		} catch (IOException ioe) {
//			logger.error(ioe);
//		} catch (Exception e) {
//			logger.error(e);
//		}
//		return ditaZipFileName;
//	}
//
//	private void addToZipFile(String fileName, ZipOutputStream zos)
//			throws FileNotFoundException, IOException {
//
//		logger.info("Writing '" + fileName + "' to zip file");
//
//		File file = new File(fileName);
//		FileInputStream fis = new FileInputStream(file);
//		ZipEntry zipEntry = new ZipEntry(fileName);
//		zos.putNextEntry(zipEntry);
//
//		byte[] bytes = new byte[1024];
//		int length;
//		while ((length = fis.read(bytes)) >= 0) {
//			zos.write(bytes, 0, length);
//		}
//
//		zos.closeEntry();
//		fis.close();
//	}
//
//	private String formatCurrentDateTime() {
//		Date date = new Date();
//		SimpleDateFormat sdf = new SimpleDateFormat("MMMddyyyyhhmmss");
//		return sdf.format(date);
//	}
//
//	/**
//	 * Reads application.properties and loads all properties defined, the first
//	 * time this method is called.
//	 * 
//	 * @param propertyName
//	 *            whose value is requested
//	 * @return value of the property for the supplied property name.
//	 */
//	@SuppressWarnings("unused")
//	private String getProperty(String propertyName) {
//		String propertyValue = null;
//		if (prop == null) {
//			try {
//				prop = new Properties();
//				InputStream applicationProperties = getClass()
//						.getResourceAsStream("/application.properties");
//				prop.load(applicationProperties);
//
//			} catch (IOException ioe) {
//				logger.error(ioe);
//			}
//		}
//		propertyValue = prop == null ? null : prop.getProperty(propertyName);
//		return propertyValue;
//	}
//
//	private String getDitaArtifactsFolder(String timestamp) {
//		if (DITA_ARTIFACTS_FOLDER == null) {
//			logger.info("Environment variable DITA_ARTIFACTS_FOLDER does not exist.");
//			return DITA_OT_HOME + "/igamt/dita/" + timestamp + "/";
//		} else {
//			return DITA_ARTIFACTS_FOLDER + timestamp + "/";
//		}
//	}
//
//	private String getDitaOutputFolder(String formattedCurrentDateTime) {
//		if (DITA_ARTIFACTS_FOLDER == null) {
//			logger.info("Environment variable DITA_ARTIFACTS_FOLDER does not exist.");
//			return DITA_OT_HOME + "/igamt/dita/" + formattedCurrentDateTime
//					+ "/out/";
//		} else {
//			return DITA_ARTIFACTS_FOLDER + formattedCurrentDateTime + "/out/";
//		}
//	}

}
