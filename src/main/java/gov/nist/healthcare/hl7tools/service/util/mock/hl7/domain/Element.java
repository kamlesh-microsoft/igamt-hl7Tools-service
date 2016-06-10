package gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain;

import java.util.List;

public class Element {
	
	private Integer id;
	private Integer parentId;
	private String segmentId;
	private Integer groupId;
	private int position;
	private Usage usage;
	private int min;
	private String max;
	private List<Element> children;
	
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
	 * @return the parentId
	 */
	public Integer getParentId() {
		return parentId;
	}
	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
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
	 * @return the groupId
	 */
	public Integer getGroupId() {
		return groupId;
	}
	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	/**
	 * @return the position
	 */
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
	 * @return the children
	 */
	public List<Element> getChildren() {
		return children;
	}
	/**
	 * @param children the children to set
	 */
	public void setChildren(List<Element> children) {
		this.children = children;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String
				.format("Element [id=%s, parentId=%s, segmentId=%s, groupId=%s, position=%s, usage=%s, min=%s, max=%s]",
						id, parentId, segmentId, groupId, position, usage, min,
						max);
	}

}
