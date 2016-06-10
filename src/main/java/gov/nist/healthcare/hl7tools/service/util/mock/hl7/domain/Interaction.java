package gov.nist.healthcare.hl7tools.service.util.mock.hl7.domain;

public class Interaction {
	
	private String eventId;
	private int number;
	private String senderMsg;
	private String receiverMsg;
	
	/**
	 * @return the eventId
	 */
	public String getEventId() {
		return eventId;
	}
	/**
	 * @param eventId the eventId to set
	 */
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	/**
	 * @return the number
	 */
	public int getNumber() {
		return number;
	}
	
	/**
	 * @param number the number to set
	 */
	public void setNumber(int number) {
		this.number = number;
	}
	
	/**
	 * @return the senderMsg
	 */
	public String getSenderMsg() {
		return senderMsg;
	}
	/**
	 * @param senderMsg the senderMsg to set
	 */
	public void setSenderMsg(String senderMsg) {
		this.senderMsg = senderMsg;
	}
	/**
	 * @return the receiverMsg
	 */
	public String getReceiverMsg() {
		return receiverMsg;
	}
	/**
	 * @param receiverMsg the receiverMsg to set
	 */
	public void setReceiverMsg(String receiverMsg) {
		this.receiverMsg = receiverMsg;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String
				.format("Interaction [eventId=%s, number=%s, senderMessage=%s, receiverMessage=%s]",
						eventId, number, senderMsg, receiverMsg);
	}

}
