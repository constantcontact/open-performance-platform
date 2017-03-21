package com.opp.dto;


/**
 * Created by ctobe on 6/27/16.
 */
public class LoadTestDataImportResp {
    int loadTestId;
    int dataRowsImported;
    int dataRowsExpected;

    public LoadTestDataImportResp(int loadTestId, int dataRowsImported, int dataRowsExpected) {
        this.loadTestId = loadTestId;
        this.dataRowsImported = dataRowsImported;
        this.dataRowsExpected = dataRowsExpected;
    }

    public int getDataRowsExpected() {
        return dataRowsExpected;
    }

    public void setDataRowsExpected(int dataRowsExpected) {
        this.dataRowsExpected = dataRowsExpected;
    }

    public int getLoadTestId() {
        return loadTestId;
    }

    public void setLoadTestId(int loadTestId) {
        this.loadTestId = loadTestId;
    }

    public int getDataRowsImported() {
        return dataRowsImported;
    }

    public void setDataRowsImported(int dataRowsImported) {
        this.dataRowsImported = dataRowsImported;
    }
}
