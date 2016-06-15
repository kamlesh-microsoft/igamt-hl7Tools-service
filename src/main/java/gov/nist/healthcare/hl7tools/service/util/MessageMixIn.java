package gov.nist.healthcare.hl7tools.service.util;

import com.fasterxml.jackson.annotation.JsonProperty;

abstract class MessageMixIn {
	MessageMixIn(
			@JsonProperty("event_id") String event_id,
			@JsonProperty("msg_type_id") String msg_type_id
			) {}
	  @JsonProperty("event_id") abstract String getEvent_id();
	  @JsonProperty("msg_type_id") abstract String getMsg_type_id();
}
