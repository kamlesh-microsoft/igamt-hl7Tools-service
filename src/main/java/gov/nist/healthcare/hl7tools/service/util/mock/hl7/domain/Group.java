package gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Group {
	
	private Integer id;
	private String messageId;
	private String name;

	private boolean isRoot;
	private boolean isChoice;
	private List<Element> children;
	private Integer seq;
	
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
	 * @return the messageId
	 */
	public String getMessageId() {
		return messageId;
	}
	/**
	 * @param messageId the messageId to set
	 */
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the isRoot
	 */
	public boolean isRoot() {
		return isRoot;
	}
	/**
	 * @param isRoot the isRoot to set
	 */
	public void setRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}
	/**
	 * @return the isChoice
	 */
	public boolean isChoice() {
		return isChoice;
	}
	/**
	 * @param isChoice the isChoice to set
	 */
	public void setChoice(boolean isChoice) {
		this.isChoice = isChoice;
	}
	
	/**
	 * @return the children
	 */
	public List<Element> getChildren() {
		return children;
	}
	/**
	 * @param children the children to set
	 */
	public void setChildren(List<Element> children) {
		this.children = children;
	}
	
	public Integer getSeq() {
		return seq;
	}
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format(
				"Group [id=%s, messageId=%s, name=%s, isRoot=%s, isChoice=%s, seq=%s]",
				id, messageId, name, isRoot, isChoice, seq);
	}
}
