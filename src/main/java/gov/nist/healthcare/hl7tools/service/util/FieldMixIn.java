package gov.nist.healthcare.hl7tools.service.util;

import com.fasterxml.jackson.annotation.JsonProperty;

abstract class FieldMixIn {
	FieldMixIn(
			@JsonProperty("segment_id") String segmentId, 
			@JsonProperty("data_element_id") String dataElementId
			) {}
	  @JsonProperty("segment_id") abstract String getSegmentId();
	  @JsonProperty("data_element_id") abstract String getDataElementId();
}
