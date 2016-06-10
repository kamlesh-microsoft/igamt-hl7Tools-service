package gov.nist.healthcare.hl7tools.service.util;

import com.fasterxml.jackson.annotation.JsonProperty;

abstract class GroupMixIn {
	GroupMixIn(
			@JsonProperty("is_root") boolean isRoot, 
			@JsonProperty("is_choice") boolean isChoice,
			@JsonProperty("message_id") String messageId
			) {}
	  @JsonProperty("is_root") abstract int isRoot(); // rename property
	  @JsonProperty("is_choice") abstract int isChoice(); // rename property
	  @JsonProperty("message_id") abstract int getMessageId(); // rename property
}
