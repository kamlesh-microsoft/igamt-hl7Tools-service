package gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain;

import java.util.List;

public class Event {
	
	private String id;
	private String description;
	private String section;
	private List<Interaction> interactions;
	
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
	/**
	 * @return the interactions
	 */
	public List<Interaction> getInteractions() {
		return interactions;
	}
	/**
	 * @param interactions the interactions to set
	 */
	public void setInteractions(List<Interaction> interactions) {
		this.interactions = interactions;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format(
				"Event [id=%s, description=%s, section=%s]",
				id, description, section);
	}
}
