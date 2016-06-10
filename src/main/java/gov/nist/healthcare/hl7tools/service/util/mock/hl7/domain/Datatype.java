package gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain;

import java.util.List;

public class Datatype {
	
	private String id;
	private String description;
	private String section;
	private boolean primitive;
	private List<Component> components;
	
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
	public boolean isPrimitive() {
		return primitive;
	}
	public void setPrimitive(boolean primitive) {
		this.primitive = primitive;
	}
	/**
	 * @return the components
	 */
	public List<Component> getComponents() {
		return components;
	}
	/**
	 * @param components the components to set
	 */
	public void setComponents(List<Component> components) {
		this.components = components;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("Datatype [id=%s, description=%s, section=%s]",
				id, description, section);
	}
}
