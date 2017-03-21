package com.opp.dto;

import com.opp.domain.LoadTest;
import com.opp.dto.aggregate.LoadTestAggregateDataResp;

/**
 * Created by jhermida on 10/10/16.
 */
public class SoastaCloudTestResponse {
    private boolean success;
    private long completionTime;
    private String soastaDataExport;
    private DataImport dataImport;
    private LoadTestAggregateDataResp dataAggregation;
    private LoadTest loadTest;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public long getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(long completionTime) {
        this.completionTime = completionTime;
    }

    public String getSoastaDataExport() {
        return soastaDataExport;
    }

    public void setSoastaDataExport(String soastaDataExport) {
        this.soastaDataExport = soastaDataExport;
    }

    public DataImport getDataImport() {
        return dataImport;
    }

    public void setDataImport(DataImport dataImport) {
        this.dataImport = dataImport;
    }

    public LoadTestAggregateDataResp getDataAggregation() {
        return dataAggregation;
    }

    public void setDataAggregation(LoadTestAggregateDataResp dataAggregation) {
        this.dataAggregation = dataAggregation;
    }

    public LoadTest getLoadTest() {
        return loadTest;
    }

    public void setLoadTest(LoadTest loadTest) {
        this.loadTest = loadTest;
    }

    public static class DataImport {
        private int lineCount;
        private int inserted;
        private long time;

        public int getLineCount() {
            return lineCount;
        }

        public void setLineCount(int lineCount) {
            this.lineCount = lineCount;
        }

        public int getInserted() {
            return inserted;
        }

        public void setInserted(int inserted) {
            this.inserted = inserted;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        @Override
        public String toString() {
            return "DataImport{" +
                    "lineCount=" + lineCount +
                    ", inserted=" + inserted +
                    ", time=" + time +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "SoastaCloudTestResponse{" +
                "success=" + success +
                ", completionTime=" + completionTime +
                ", soastaDataExport='" + soastaDataExport + '\'' +
                ", dataImport=" + dataImport +
                ", dataAggregation=" + dataAggregation +
                ", loadTest=" + loadTest +
                '}';
    }
}
