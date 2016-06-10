package gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain;

public class DataElement {
	
	private String id;
	private String description;
	private String datatypeId;
	private int minLength;
	private int maxLength;
	private int confLength;
	private String tableId;
	private String section;
	//FIXME Handle truncation allowed 
	private String truncation = "NA";
	//FIXME maxLegnth type string or int ?
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the datatypeId
	 */
	public String getDatatypeId() {
		return datatypeId;
	}
	/**
	 * @param datatypeId the datatypeId to set
	 */
	public void setDatatypeId(String datatypeId) {
		this.datatypeId = datatypeId;
	}
	/**
	 * @return the minLength
	 */
	public int getMinLength() {
		return minLength;
	}
	/**
	 * @param minLength the minLength to set
	 */
	public void setMinLength(int minLength) {
		this.minLength = minLength;
	}
	/**
	 * @return the maxLength
	 */
	public int getMaxLength() {
		return maxLength;
	}
	/**
	 * @param maxLength the maxLength to set
	 */
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}
	/**
	 * @return the confLength
	 */
	public int getConfLength() {
		return confLength;
	}
	/**
	 * @param confLength the confLength to set
	 */
	public void setConfLength(int confLength) {
		this.confLength = confLength;
	}
	/**
	 * @return the tableId
	 */
	public String getTableId() {
		return tableId;
	}
	/**
	 * @param tableId the tableId to set
	 */
	public void setTableId(String tableId) {
		this.tableId = tableId;
	}
	/**
	 * @return the section
	 */
	public String getSection() {
		return section;
	}
	/**
	 * @param section the section to set
	 */
	public void setSection(String section) {
		this.section = section;
	}
	/**
	 * @return the truncation
	 */
	public String getTruncation() {
		return truncation;
	}
	/**
	 * @param truncation the truncation to set
	 */
	public void setTruncation(String truncation) {
		this.truncation = truncation;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String
				.format("DataElement [id=%s, description=%s, datatypeId=%s, minLength=%s, maxLength=%s, confLength=%s, tableId=%s, section=%s]",
						id, description, datatypeId, minLength, maxLength,
						confLength, tableId, section);
	}
}
