package gov.nist.healthcare.hl7tools.service.util;

import com.fasterxml.jackson.annotation.JsonProperty;

abstract class CodeMixIn {
	CodeMixIn(
			@JsonProperty("table_id") String tableId
			) {}
	  @JsonProperty("table_id") abstract String getTableId();
}
