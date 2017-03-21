package com.opp.dto.ux;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ctobe on 9/29/16.
 */
public class WptSlaResults {
    private int totalSlas = 0;
    private int totalPass = 0;
    private int totalFailed = 0;
    private Double totalPassPct = 0.0;
    private List<WptSlaResultDetails> slaDetails = new ArrayList<>();

    public int getTotalSlas() {
        return totalSlas;
    }

    public void setTotalSlas(int totalSlas) {
        this.totalSlas = totalSlas;
    }

    public int getTotalPass() {
        return totalPass;
    }

    public void setTotalPass(int totalPass) {
        this.totalPass = totalPass;
    }

    public int getTotalFailed() {
        return totalFailed;
    }

    public void setTotalFailed(int totalFailed) {
        this.totalFailed = totalFailed;
    }

    public Double getTotalPassPct() {
        return totalPassPct;
    }

    public void setTotalPassPct(Double totalPassPct) {
        this.totalPassPct = totalPassPct;
    }

    public List<WptSlaResultDetails> getSlaDetails() {
        return slaDetails;
    }

    public void setSlaDetails(List<WptSlaResultDetails> slaDetails) {
        this.slaDetails = slaDetails;
    }

    public static class WptSlaResultDetails {
        private String name;
        private Integer sla;
        private Integer actual;
        private Integer diff;
        private Double diffPct;
        private boolean slaPass;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getSla() {
            return sla;
        }

        public void setSla(Integer sla) {
            this.sla = sla;
        }

        public Integer getActual() {
            return actual;
        }

        public void setActual(Integer actual) {
            this.actual = actual;
        }

        public Integer getDiff() {
            return diff;
        }

        public void setDiff(Integer diff) {
            this.diff = diff;
        }

        public Double getDiffPct() {
            return diffPct;
        }

        public void setDiffPct(Double diffPct) {
            this.diffPct = diffPct;
        }

        public boolean isSlaPass() {
            return slaPass;
        }

        public void setSlaPass(boolean slaPass) {
            this.slaPass = slaPass;
        }
    }

}
