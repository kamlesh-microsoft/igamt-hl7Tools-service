package gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain;

import java.util.List;

public class Table {
	
	private String id;
	private String description;
	private String type;
	private String oid;
	private String section;
	private List<Code> codes;
	
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
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the oid
	 */
	public String getOid() {
		return oid;
	}
	/**
	 * @param oid the oid to set
	 */
	public void setOid(String oid) {
		this.oid = oid;
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
	 * @return the codes
	 */
	public List<Code> getCodes() {
		return codes;
	}
	/**
	 * @param codes the codes to set
	 */
	public void setCodes(List<Code> codes) {
		this.codes = codes;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format(
				"Table [id=%s, description=%s, type=%s, oid=%s, section=%s]",
				id, description, type, oid, section);
	}

}
