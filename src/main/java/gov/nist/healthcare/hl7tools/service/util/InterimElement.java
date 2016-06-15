package gov.nist.healthcare.hl7tools.service.util;

import com.fasterxml.jackson.annotation.JsonIgnore;

import gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Element;

public class InterimElement extends Element {
	
	String groupName;
	String groupState;
	String messageStucture;

	@JsonIgnore
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@JsonIgnore
	public String getGroupState() {
		return groupState;
	}

	public void setGroupState(String groupState) {
		this.groupState = groupState;
	}

	@JsonIgnore
	public String getMessageStucture() {
		return messageStucture;
	}

	public void setMessageStucture(String messageStucture) {
		this.messageStucture = messageStucture;
	}
}
