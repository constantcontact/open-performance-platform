package com.opp.domain;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by ctobe on 9/15/16.
 */
public class WebPageTestResult {
    private String id;
    private String label;
    private String url;
    private Long completed;
    private String wpt_summary;
    private Integer statusCode;
    private String location;
    private String connectivity;
    private Integer loadTime;
    private Integer ttfb;
    private Integer fullyLoaded;
    private Integer lastVisualChange;
    private Integer speedIndex;
    private Integer visualComplete;
    private Images thumbs;
    private Images images;
    private WptPages wpt_pages;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getCompleted() {
        return completed;
    }

    public void setCompleted(Long completed) {
        this.completed = completed;
    }

    public String getWpt_summary() {
        return wpt_summary;
    }

    public void setWpt_summary(String wpt_summary) {
        this.wpt_summary = wpt_summary;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getConnectivity() {
        return connectivity;
    }

    public void setConnectivity(String connectivity) {
        this.connectivity = connectivity;
    }

    public Integer getLoadTime() {
        return loadTime;
    }

    public void setLoadTime(Integer loadTime) {
        this.loadTime = loadTime;
    }

    public Integer getTtfb() {
        return ttfb;
    }

    public void setTtfb(Integer ttfb) {
        this.ttfb = ttfb;
    }

    public Integer getFullyLoaded() {
        return fullyLoaded;
    }

    public void setFullyLoaded(Integer fullyLoaded) {
        this.fullyLoaded = fullyLoaded;
    }

    public Integer getLastVisualChange() {
        return lastVisualChange;
    }

    public void setLastVisualChange(Integer lastVisualChange) {
        this.lastVisualChange = lastVisualChange;
    }

    public Integer getSpeedIndex() {
        return speedIndex;
    }

    public void setSpeedIndex(Integer speedIndex) {
        this.speedIndex = speedIndex;
    }

    public Integer getVisualComplete() {
        return visualComplete;
    }

    public void setVisualComplete(Integer visualComplete) {
        this.visualComplete = visualComplete;
    }

    public Images getThumbs() {
        return thumbs;
    }

    public void setThumbs(Images thumbs) {
        this.thumbs = thumbs;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public WptPages getWpt_pages() {
        return wpt_pages;
    }

    public void setWpt_pages(WptPages wpt_pages) {
        this.wpt_pages = wpt_pages;
    }

    public static class Images {
        private String waterfall;
        private String checklist;
        private String screenShot;
        private String connectionView;

        public String getWaterfall() {
            return waterfall;
        }

        public void setWaterfall(String waterfall) {
            this.waterfall = waterfall;
        }

        public String getChecklist() {
            return checklist;
        }

        public void setChecklist(String checklist) {
            this.checklist = checklist;
        }

        public String getScreenShot() {
            return screenShot;
        }

        public void setScreenShot(String screenShot) {
            this.screenShot = screenShot;
        }

        public String getConnectionView() {
            return connectionView;
        }

        public void setConnectionView(String connectionView) {
            this.connectionView = connectionView;
        }
    }

    public static class WptPages {
        private String details;
        private String checklist;
        private String breakdown;
        private String domains;
        private String screenShot;

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public String getChecklist() {
            return checklist;
        }

        public void setChecklist(String checklist) {
            this.checklist = checklist;
        }

        public String getBreakdown() {
            return breakdown;
        }

        public void setBreakdown(String breakdown) {
            this.breakdown = breakdown;
        }

        public String getDomains() {
            return domains;
        }

        public void setDomains(String domains) {
            this.domains = domains;
        }

        public String getScreenShot() {
            return screenShot;
        }

        public void setScreenShot(String screenShot) {
            this.screenShot = screenShot;
        }
    }
}
