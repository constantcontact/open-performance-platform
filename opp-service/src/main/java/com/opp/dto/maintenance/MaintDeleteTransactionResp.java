package com.opp.dto.maintenance;


/**
 * Created by ctobe on 6/27/16.
 */
public class MaintDeleteTransactionResp {
    int rawDataRowsRemoved;
    int aggregateDataRowsRemoved;

    public int getRawDataRowsRemoved() {
        return rawDataRowsRemoved;
    }

    public void setRawDataRowsRemoved(int rawDataRowsRemoved) {
        this.rawDataRowsRemoved = rawDataRowsRemoved;
    }

    public int getAggregateDataRowsRemoved() {
        return aggregateDataRowsRemoved;
    }

    public void setAggregateDataRowsRemoved(int aggregateDataRowsRemoved) {
        this.aggregateDataRowsRemoved = aggregateDataRowsRemoved;
    }
}
