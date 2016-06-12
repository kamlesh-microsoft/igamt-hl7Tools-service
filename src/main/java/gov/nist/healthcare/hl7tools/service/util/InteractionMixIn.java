package gov.nist.healthcare.hl7tools.service.util;

import com.fasterxml.jackson.annotation.JsonProperty;

abstract class InteractionMixIn {
	InteractionMixIn(
			@JsonProperty("event_id") String eventId,
			@JsonProperty("sender_msg") String senderMsg,
			@JsonProperty("receiver_msg") String receiverMsg
			) {}
	  @JsonProperty("event_id") abstract String getEventId();
	  @JsonProperty("sender_msg") abstract String getSenderMsg();
	  @JsonProperty("receiver_msg") abstract String getReceiverMsg();
}
