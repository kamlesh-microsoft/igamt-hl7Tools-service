package gov.nist.healthcare.hl7tools.service.util;

import com.fasterxml.jackson.annotation.JsonProperty;

abstract class ComponentMixIn {
	ComponentMixIn(
			@JsonProperty("parent_datatype_id") String parentDatatypeId, 
			@JsonProperty("datatype_id") String datatypeId,
			@JsonProperty("min_length") int minLength,
			@JsonProperty("max_length") int maxLength,
			@JsonProperty("conf_length") int confLength,
			@JsonProperty("table_id") String tableId
			) {}
	  @JsonProperty("parent_datatype_id") abstract String getParentDatatypeId();
	  @JsonProperty("datatype_id") abstract String getDatatypeId();
	  @JsonProperty("min_length") abstract int getMinLength();
	  @JsonProperty("max_length") abstract int getMaxLength();
	  @JsonProperty("conf_length") abstract int getConfLength();
	  @JsonProperty("table_id") abstract String getTableId();
}
