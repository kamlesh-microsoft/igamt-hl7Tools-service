package gov.nist.healthcare.hl7tools.service.util;

import com.fasterxml.jackson.annotation.JsonIgnore;

import gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain.Element;

public class InterimElement extends Element {
	
	String groupName;
	String messageStucture;
	int seqNo;

	@JsonIgnore
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@JsonIgnore
	public String getMessageStucture() {
		return messageStucture;
	}

	public void setMessageStucture(String messageStucture) {
		this.messageStucture = messageStucture;
	}
	
	@JsonIgnore
	public int getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}

	public String toString() {
		StringBuilder bld = new StringBuilder();
		bld.append(" ParentId=" + this.getParentId());
		bld.append(" MessageStucture=" + this.getMessageStucture());
		bld.append(" SegmentId=" + this.getSegmentId());
		bld.append(" GroupId=" + this.getGroupId());
		bld.append(" GroupName=" + this.getGroupName());
		return bld.toString();
	}
}
