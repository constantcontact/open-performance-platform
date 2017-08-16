package com.opp.dto;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by ctobe on 8/16/17.
 */
public class LoadTestSlaResult {

    private boolean passed;
    private int totalTransactions;
    private int totalSlas;
    private int errorCount;
    private int warningCount;

    private List<Failures> failures;
    private List<Warnings> warnings;
    private List<Map<String, Object>> rawData;

    public LoadTestSlaResult() {
        this.passed = true;
        this.failures = Collections.emptyList();
        this.warnings = Collections.emptyList();
        this.rawData = Collections.emptyList();
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public int getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(int totalTransactions) {
        this.totalTransactions = totalTransactions;
    }

    public int getTotalSlas() {
        return totalSlas;
    }

    public void setTotalSlas(int totalSlas) {
        this.totalSlas = totalSlas;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

    public int getWarningCount() {
        return warningCount;
    }

    public void setWarningCount(int warningCount) {
        this.warningCount = warningCount;
    }

    public List<Failures> getFailures() {
        return failures;
    }

    public void setFailures(List<Failures> failures) {
        this.failures = failures;
    }

    public List<Warnings> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<Warnings> warnings) {
        this.warnings = warnings;
    }

    public List<Map<String, Object>> getRawData() {
        return rawData;
    }

    public void setRawData(List<Map<String, Object>> rawData) {
        this.rawData = rawData;
    }

    public static class Failures {
        private String type;
        private String name;
        private String msg;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    public static class Warnings {
        private String type;
        private String transaction;
        private String severity;
        private String msg;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTransaction() {
            return transaction;
        }

        public void setTransaction(String transaction) {
            this.transaction = transaction;
        }

        public String getSeverity() {
            return severity;
        }

        public void setSeverity(String severity) {
            this.severity = severity;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

}
