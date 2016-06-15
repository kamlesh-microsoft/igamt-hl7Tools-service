package gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public class Field {
	
	private Integer id;
	private String segmentId;
	private String dataElementId;
	private int position;
	private Usage usage;
	@JsonSerialize(using = ToStringSerializer.class)
	public int min;
	private String max;
	
	private DataElement dataElement;

	
	public String getDescription() {
		return dataElement != null ? dataElement.getDescription() : null;
	}
	
	public String getDatatype() {
		return dataElement != null ? dataElement.getDatatypeId() : null;
	}
	
	public Integer getMinLength() {
		return dataElement != null ? dataElement.getMinLength() : -1;
	}
	
	public Integer getMaxLength() {
		return dataElement != null ? dataElement.getMaxLength() : null;
	}
	public Integer getConfLength() {
		return dataElement != null ? dataElement.getConfLength() : -1;
	}

	public String getTableId() {
		return dataElement != null ? dataElement.getTableId() : null;
	}
	
	public String getTruncation() {
		return dataElement != null ? dataElement.getTruncation() : "NA";
	}
	
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
	 * @return the segmentId
	 */
	public String getSegmentId() {
		return segmentId;
	}

	/**
	 * @param segmentId the segmentId to set
	 */
	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
	}

	/**
	 * @return the dataElementId
	 */
	public String getDataElementId() {
		return dataElementId;
	}

	/**
	 * @param dataElementId the dataElementId to set
	 */
	public void setDataElementId(String dataElementId) {
		this.dataElementId = dataElementId;
	}

	/**
	 * @return the position
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	public int getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * @return the usage
	 */
	public Usage getUsage() {
		return usage;
	}

	/**
	 * @param usage the usage to set
	 */
	public void setUsage(Usage usage) {
		this.usage = usage;
	}

	/**
	 * @return the min
	 */
	public int getMin() {
		return min;
	}

	/**
	 * @param min the min to set
	 */
	public void setMin(int min) {
		this.min = min;
	}

	/**
	 * @return the max
	 */
	public String getMax() {
		return max;
	}

	/**
	 * @param max the max to set
	 */
	public void setMax(String max) {
		this.max = max;
	}

	/**
	 * @return the dataElement
	 */
	public DataElement getDataElement() {
		return dataElement;
	}

	/**
	 * @param dataElement the dataElement to set
	 */
	public void setDataElement(DataElement dataElement) {
		this.dataElement = dataElement;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String
				.format("Field [position=%s, usage=%s, min=%s, max=%s, segmentId=%s, datatype=%s]",
						position, usage, min, max, segmentId, getDatatype());
	}

}
