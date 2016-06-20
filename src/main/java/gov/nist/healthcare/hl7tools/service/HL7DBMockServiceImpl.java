package gov.nist.healthcare.hl7tools.service;

import gov.nist.healthcare.hl7tools.domain.CodeTable;
import gov.nist.healthcare.hl7tools.domain.Datatype;
import gov.nist.healthcare.hl7tools.domain.IGLibrary;
import gov.nist.healthcare.hl7tools.domain.Segment;
import gov.nist.healthcare.hl7tools.service.util.mock.HL7DBMockDao;

import java.util.Arrays;
import java.util.List;

public class HL7DBMockServiceImpl implements HL7DBService {

	private final List<String> supportedHL7Versions = Arrays.asList("2.8.2", "2.8.1", "2.8", "2.7.1", "2.7",
			"2.6", "2.5.1", "2.5", "2.4", "2.3.1", "2.3", "2.2", "2.1");

	@Override
	public List<String> getSupportedHL7Versions() {
		return supportedHL7Versions;
	}

	@Override
	public List<String[]> getMessageListByVersion(String hl7Version)
			throws HL7DBServiceException {
		try {
			return HL7DBMockDao.getMessageByVersion(hl7Version);
		} catch (Exception e) {
			throw new HL7DBServiceException(
					"An error occurred while retrieving the list of messages from the HL7 "
							+ "version '" + hl7Version + "' database ", e);
		}
	}

	@Override
	public IGLibrary buildIGFromMessageList(String hl7Version,
			List<String> messageList) throws HL7DBServiceException {
		try {
			return HL7DBMockDao.buildIGFromMessageList(hl7Version, messageList);
		} catch (Exception e) {
			throw new HL7DBServiceException(
					"An error occurred while creating the Implementation Guide Object",
					e);
		}
	}

	@Override
	public IGLibrary buildIGFromEventList(String hl7Version,
			List<String> eventList) throws HL7DBServiceException {
		// TODO TO BE COMPLETED
		throw new UnsupportedOperationException("NOT YET IMPLEMENTED");
	}

	@Override
	public List<Segment> getSegmentListByVersion(String hl7Version)
			throws HL7DBServiceException {
		try {
			return HL7DBMockDao.getSegmentByVersion(hl7Version);
		} catch (Exception e) {
			throw new HL7DBServiceException(
					"An error occurred while retrieving the list of segments from the HL7 "
							+ "version '" + hl7Version + "' database ", e);
		}
	}

	@Override
	public List<CodeTable> getCodeTableListByVersion(String hl7Version)
			throws HL7DBServiceException {
		try {
			return HL7DBMockDao.getCodeTableByVersion(hl7Version);
		} catch (Exception e) {
			throw new HL7DBServiceException(
					"An error occurred while retrieving the list of code tables from the HL7 "
							+ "version '" + hl7Version + "' database ", e);
		}
	}

	@Override
	public List<Datatype> getDatatypeListByVersion(String hl7Version)
			throws HL7DBServiceException {
		try {
			return HL7DBMockDao.getDatatypeByVersion(hl7Version);
		} catch (Exception e) {
			throw new HL7DBServiceException(
					"An error occurred while retrieving the list of datatypes from the HL7 "
							+ "version '" + hl7Version + "' database ", e);
		}
	}

	@Override
	public gov.nist.healthcare.hl7tools.domain.Message getMessage(
			String hl7Version, String messageId) throws HL7DBServiceException {
		try {
			return HL7DBMockDao.getMessageByIdAndVersion(messageId, hl7Version);
		} catch (Exception e) {
			throw new HL7DBServiceException(
					"An error occurred while retrieving the message '"
							+ messageId + "' from the HL7 " + "version '"
							+ hl7Version + "' database ", e);
		}
	}
}
