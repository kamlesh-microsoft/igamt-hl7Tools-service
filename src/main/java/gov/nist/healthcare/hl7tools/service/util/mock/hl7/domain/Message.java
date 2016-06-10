package gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain;

public class Message {
	
	private String id;
	private String description;
	private String section;
	private String msg_type_id;
	private String event_id;
	
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
	
	public String getMsg_type_id() {
		return msg_type_id;
	}
	
	public void setMsg_type_id(String msg_type_id) {
		this.msg_type_id = msg_type_id;
	}
	public String getEvent_id() {
		return event_id;
	}
	
	public void setEvent_id(String event_id) {
		this.event_id = event_id;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format(
				"Message [id=%s, description=%s, section=%s]", id,
				description, section);
	}
}
