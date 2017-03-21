package com.opp.service;

import com.opp.domain.LoadTestData;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LoadTestDataFactory {

    public enum CollectionDetailsColumn {
        BAND_NAME("Band Name", 0),
        BAND_INDEX("Band Index", 1),
        TRACK_NAME("Track Name", 2),
        TRACK_INDEX("Track Index", 3),
        PARENT_NAME("Parent Name", 4),
        CONTAINER_NAME("Container Name", 5),
        CONTAINER_INDEX("Container Index", 6),
        LOCATION("Location", 7),
        ERROR("Error", 8),
        TYPE("Type", 9),
        COMPLETION_STATUS("Completion Status", 10),
        MESSAGE_RESPONSE_COUNT("Message Response Count", 11),
        TOTAL_MSG_RESPONSE_TIME("Total Msg. Response Time", 12),
        MIN_MSG_RESPONSE_TIME("Min. Msg. Response Time", 13),
        MAX_MSG_RESPONSE_TIME("Max. Msg. Response Time", 14),
        BYTES_SENT("Bytes Sent", 15),
        BYTES_RECEIVED("Bytes Received", 16),
        DURATION("Duration", 17),
        START_OFFSET("Start (Offset)", 18),
        START_ABSOLUTE("Start (Absolute)", 19),
        MESSAGE_COUNT("Message Count", 20),
        BROWSER_ACTION_COUNT("BrowserAction Count", 21),
        SCRIPT_COUNT("Script Count", 22),
        CHECKPOINT_COUNT("Checkpoint Count", 23),
        DELAY_COUNT("Delay Count", 24),
        EFFECTIVE_DURATION("Effective Duration", 25);

        public final String columnName;
        public final int columnIndex;

        CollectionDetailsColumn(String columnName, int columnIndex) {
            this.columnName = columnName;
            this.columnIndex = columnIndex;
        }
    }

    public enum ClipElementDetailsColumn {
        BAND_NAME("Band Name", 0),
        BAND_INDEX("Band Index", 1),
        TRACK_NAME("Track Name", 2),
        TRACK_INDEX("Track Index", 3),
        CONTAINER_NAME("Container Name", 4),
        CONTAINER_INDEX("Container Index", 5),
        CLIP_ELEMENT_NAME("Clip Element Name", 6),
        CLIP_ELEMENT_INDEX("Clip Element Index", 7),
        CLIP_ELEMENT_TYPE("Clip Element Type", 8),
        TARGET_NAME("Target Name", 9),
        OPERATION("Operation", 10),
        SERVER("Server", 11),
        LOCATION("Location", 12),
        START_OFFSET("Start (Offset)", 13),
        START_ABSOLUTE("Start (Absolute)", 14),
        SEND_TIME("Send Time", 15),
        RECEIVE_TIME("Receive Time", 16),
        CONNECTION_ESTABLISH_TIME("Connection Establish Time", 17),
        RESPONSE_TIME("Response Time", 18),
        TTFB("TTFB", 19),
        TTLB("TTLB", 20),
        DNS_LOOKUP_DURATION("DNS Lookup Duration", 21),
        OFFSET_IN_CONTAINER("Offset in Container", 22),
        WAIT_FOR_CONNECTION_POOL_TIME("Wait for Connection Pool Time", 23),
        DELTA_FROM_SCHEDULED_TIME("Delta From Scheduled Time", 24),
        DELTA_SCHEDULED_DURATION("Delta Scheduled Duration", 25),
        VALIDATION_ERROR("Validation Error", 26),
        BYTES_SENT("Bytes Sent", 27),
        BYTES_RECEIVED("Bytes Received", 28);

        public final String columnName;
        public final int columnIndex;

        ClipElementDetailsColumn(String columnName, int columnIndex) {
            this.columnName = columnName;
            this.columnIndex = columnIndex;
        }
    }

    private LoadTestDataFactory(){}

    public static LoadTestData create(String testDataType, int loadTestId, List<String> soastaResults) {

        if("resultCollectionDetails".equalsIgnoreCase(testDataType)) {
            return collectionDetailsType(loadTestId, soastaResults);
        } else if ("resultClipElementDetails".equalsIgnoreCase(testDataType)) {
            return clipElementDetailsType(loadTestId, soastaResults);
        }

        return null;
    }

    private static LoadTestData collectionDetailsType(int loadTestId, List<String> soastaResults) {
        LoadTestData loadTestData = new LoadTestData();

        loadTestData.setId(loadTestId);
        loadTestData.setBytesReceived(Integer.parseInt(soastaResults.get(CollectionDetailsColumn.BYTES_RECEIVED.columnIndex)));
        loadTestData.setBytesReceived(Integer.parseInt(soastaResults.get(CollectionDetailsColumn.BYTES_SENT.columnIndex)));
        loadTestData.setErrorCount(StringUtils.isEmpty(soastaResults.get(CollectionDetailsColumn.ERROR.columnIndex)) ? 0 : 1);
        loadTestData.setResponseTime(Integer.parseInt(soastaResults.get(CollectionDetailsColumn.EFFECTIVE_DURATION.columnIndex)));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
        LocalDate localDate = LocalDate.parse(soastaResults.get(CollectionDetailsColumn.START_ABSOLUTE.columnIndex).replace("edt", "EST"), formatter);
        loadTestData.setStartTime(localDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli());

        loadTestData.setTransactionName(soastaResults.get(CollectionDetailsColumn.CONTAINER_NAME.columnIndex));

        return loadTestData;
    }

    private static LoadTestData clipElementDetailsType(int loadTestId, List<String> soastaResults) {
        LoadTestData loadTestData = new LoadTestData();

        loadTestData.setId(loadTestId);
        loadTestData.setBytesReceived(Integer.parseInt(soastaResults.get(ClipElementDetailsColumn.BYTES_RECEIVED.columnIndex)));
        loadTestData.setBytesSent(Integer.parseInt(soastaResults.get(ClipElementDetailsColumn.BYTES_SENT.columnIndex)));
        loadTestData.setConnectionTime(Integer.parseInt(soastaResults.get(ClipElementDetailsColumn.CONNECTION_ESTABLISH_TIME.columnIndex)));
        loadTestData.setDnsLookup(Integer.parseInt(soastaResults.get(ClipElementDetailsColumn.DNS_LOOKUP_DURATION.columnIndex)));
        loadTestData.setErrorCount(StringUtils.isEmpty(soastaResults.get(ClipElementDetailsColumn.VALIDATION_ERROR.columnIndex)) ? 0 : 1);
        loadTestData.setLocation(soastaResults.get(ClipElementDetailsColumn.LOCATION.columnIndex));
        loadTestData.setOperation(soastaResults.get(ClipElementDetailsColumn.OPERATION.columnIndex));
        loadTestData.setReceiveTime(Integer.parseInt(soastaResults.get(ClipElementDetailsColumn.RECEIVE_TIME.columnIndex)));
        loadTestData.setResponseTime(Integer.parseInt(soastaResults.get(ClipElementDetailsColumn.RESPONSE_TIME.columnIndex)));
        loadTestData.setSendTime(Integer.parseInt(soastaResults.get(ClipElementDetailsColumn.SEND_TIME.columnIndex)));
        loadTestData.setServer(soastaResults.get(ClipElementDetailsColumn.SERVER.columnIndex));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
        LocalDate localDate = LocalDate.parse(soastaResults.get(ClipElementDetailsColumn.START_ABSOLUTE.columnIndex).replace("edt", "EST"), formatter);
        loadTestData.setStartTime(localDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli());

        loadTestData.setTarget(soastaResults.get(ClipElementDetailsColumn.TARGET_NAME.columnIndex));
        loadTestData.setTransactionName(soastaResults.get(ClipElementDetailsColumn.CONTAINER_NAME.columnIndex));
        loadTestData.setTtfb(Integer.parseInt(soastaResults.get(ClipElementDetailsColumn.TTFB.columnIndex)));
        loadTestData.setTtlb(Integer.parseInt(soastaResults.get(ClipElementDetailsColumn.TTLB.columnIndex)));

        return loadTestData;

    }

}
