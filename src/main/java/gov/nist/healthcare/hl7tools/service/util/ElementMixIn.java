package gov.nist.healthcare.hl7tools.service.util;

import com.fasterxml.jackson.annotation.JsonProperty;

abstract class ElementMixIn {
	ElementMixIn(
			@JsonProperty("parent_id") String parentId,
			@JsonProperty("segment_id") String segmentId,
			@JsonProperty("group_id") String groupId
			) {}
	  @JsonProperty("parent_id") abstract String getParentId();
	  @JsonProperty("segment_id") abstract String getSegmentId();
	  @JsonProperty("group_id") abstract String getGroupId();
}
