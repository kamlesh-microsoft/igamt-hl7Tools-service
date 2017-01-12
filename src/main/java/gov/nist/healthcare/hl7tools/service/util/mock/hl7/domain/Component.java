package gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain;

public class Component {
	
	private Integer id;
	private String parentDatatypeId;
	private String datatypeId;
	private int position;
	private String description;
	private Usage usage;
	private int minLength;
	private int maxLength;
	private String confLength;
	private String tableId;
	//FIXME Handle truncation allowed 
	private String truncation = "NA";
	//FIXME maxLegnth type string or int ?
	
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the parentDatatypeId
	 */
	public String getParentDatatypeId() {
		return parentDatatypeId;
	}
	/**
	 * @param parentDatatypeId the parentDatatypeId to set
	 */
	public void setParentDatatypeId(String parentDatatypeId) {
		this.parentDatatypeId = parentDatatypeId;
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
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}
	/**
	 * @param position the position to set
	 */
	public void setPosition(int position) {
		this.position = position;
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
	 * @return the usage
	 */
	public Usage getUsage() {
		return usage;
	}
	/**
	 * @param usage the usage to set
	 */
	public void setUsage(Usage usage) {
		this.usage = usage;
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
	public String getConfLength() {
		return confLength;
	}
	/**
	 * @param confLength the confLength to set
	 */
	public void setConfLength(String confLength) {
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
				.format("Component [id=%s, parentDatatypeId=%s, datatypeId=%s, position=%s, description=%s, usage=%s, minLength=%s, maxLength=%s, confLength=%s, tableId=%s]",
						id, parentDatatypeId, datatypeId, position,
						description, usage, minLength, maxLength, confLength,
						tableId);
	}
}
