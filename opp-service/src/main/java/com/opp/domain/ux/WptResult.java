package com.opp.domain.ux;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;

/**
 * Created by ctobe on 3/24/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WptResult {
    private String id;
    private String url;
    private String summary;
    private String testUrl;
    private String location;
    private String from;
    private String connectivity;
    private Integer bwDown;
    private Integer bwUp;
    private Integer latency;
    private Integer plr;
    private Long completed;
    private String testerDNS;
    private boolean fvonly;
    private Integer successfulFVRuns;
    private Integer successfulRVRuns;
    private Run average;
    private Run median;
    private Run min;
    private Run max;

    // custom fields
    private Integer runCount;
    private Integer requestCount;
    private String statusCode;
    private String statusText;
    private WptTestLabel label;



    public WptTestLabel getLabel() {
        return label;
    }

    public Integer getRunCount() {
        return runCount;
    }

    public void setRunCount(Integer runCount) {
        this.runCount = runCount;
    }

    public Integer getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(Integer requestCount) {
        this.requestCount = requestCount;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public void setLabel(WptTestLabel label) {
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTestUrl() {
        return testUrl;
    }

    public void setTestUrl(String testUrl) {
        this.testUrl = testUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getConnectivity() {
        return connectivity;
    }

    public void setConnectivity(String connectivity) {
        this.connectivity = connectivity;
    }

    public Integer getBwDown() {
        return bwDown;
    }

    public void setBwDown(Integer bwDown) {
        this.bwDown = bwDown;
    }

    public Integer getBwUp() {
        return bwUp;
    }

    public void setBwUp(Integer bwUp) {
        this.bwUp = bwUp;
    }

    public Integer getLatency() {
        return latency;
    }

    public void setLatency(Integer latency) {
        this.latency = latency;
    }

    public Integer getPlr() {
        return plr;
    }

    public void setPlr(Integer plr) {
        this.plr = plr;
    }


    public Long getCompleted() {
        return completed;
    }

    public void setCompleted(Long completed) {
        this.completed = completed;
    }

    public String getTesterDNS() {
        return testerDNS;
    }

    public void setTesterDNS(String testerDNS) {
        this.testerDNS = testerDNS;
    }

    public boolean isFvonly() {
        return fvonly;
    }

    public void setFvonly(boolean fvonly) {
        this.fvonly = fvonly;
    }

    public Integer getSuccessfulFVRuns() {
        return successfulFVRuns;
    }

    public void setSuccessfulFVRuns(Integer successfulFVRuns) {
        this.successfulFVRuns = successfulFVRuns;
    }

    public Integer getSuccessfulRVRuns() {
        return successfulRVRuns;
    }

    public void setSuccessfulRVRuns(Integer successfulRVRuns) {
        this.successfulRVRuns = successfulRVRuns;
    }

    public Run getAverage() {
        return average;
    }

    public void setAverage(Run average) {
        this.average = average;
    }

    public Run getMedian() {
        return median;
    }

    public void setMedian(Run median) {
        this.median = median;
    }

    public Run getMin() {
        return min;
    }

    public void setMin(Run min) {
        this.min = min;
    }

    public Run getMax() {
        return max;
    }

    public void setMax(Run max) {
        this.max = max;
    }

    public static class Run {
        View firstView;
        View repeatView;

        public View getFirstView() {
            return firstView;
        }

        public void setFirstView(View firstView) {
            this.firstView = firstView;
        }

        public View getRepeatView() {
            return repeatView;
        }

        public void setRepeatView(View repeatView) {
            this.repeatView = repeatView;
        }
    }


    public static class View {
        private String URL;
        private Integer loadTime;
        private Integer TTFB;
        private Integer fullyLoaded;
        private Integer responses_200;
        private Integer responses_404;
        private Integer responses_other;
        private Integer result;
        private Integer render;
        private Integer docTime;
        private Integer domTime;
        private Integer atf;
        private Integer domElements;
        private String title;
        private Integer lastVisualChange;
        private String browser_name;
        private String browser_version;
        private HashMap<String, Long> userTimes;
        private Integer userTime;
        private Integer SpeedIndex;
        private Integer visualComplete;
        private Integer run;
        private Pages pages;
        private Thumbnails thumbnails;
        private Images images;

        public String getURL() {
            return URL;
        }

        public void setURL(String URL) {
            this.URL = URL;
        }

        public Integer getLoadTime() {
            return loadTime;
        }

        public void setLoadTime(Integer loadTime) {
            this.loadTime = loadTime;
        }

        public Integer getTTFB() {
            return TTFB;
        }

        public void setTTFB(Integer TTFB) {
            this.TTFB = TTFB;
        }

        public Integer getFullyLoaded() {
            return fullyLoaded;
        }

        public void setFullyLoaded(Integer fullyLoaded) {
            this.fullyLoaded = fullyLoaded;
        }

        public Integer getResponses_200() {
            return responses_200;
        }

        public void setResponses_200(Integer responses_200) {
            this.responses_200 = responses_200;
        }

        public Integer getResponses_404() {
            return responses_404;
        }

        public void setResponses_404(Integer responses_404) {
            this.responses_404 = responses_404;
        }

        public Integer getResponses_other() {
            return responses_other;
        }

        public void setResponses_other(Integer responses_other) {
            this.responses_other = responses_other;
        }

        public Integer getResult() {
            return result;
        }

        public void setResult(Integer result) {
            this.result = result;
        }

        public Integer getRender() {
            return render;
        }

        public void setRender(Integer render) {
            this.render = render;
        }

        public Integer getDocTime() {
            return docTime;
        }

        public void setDocTime(Integer docTime) {
            this.docTime = docTime;
        }

        public Integer getDomTime() {
            return domTime;
        }

        public void setDomTime(Integer domTime) {
            this.domTime = domTime;
        }

        public Integer getAtf() {
            return atf;
        }

        public void setAtf(Integer atf) {
            this.atf = atf;
        }

        public Integer getDomElements() {
            return domElements;
        }

        public void setDomElements(Integer domElements) {
            this.domElements = domElements;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Integer getLastVisualChange() {
            return lastVisualChange;
        }

        public void setLastVisualChange(Integer lastVisualChange) {
            this.lastVisualChange = lastVisualChange;
        }

        public String getBrowser_name() {
            return browser_name;
        }

        public void setBrowser_name(String browser_name) {
            this.browser_name = browser_name;
        }

        public String getBrowser_version() {
            return browser_version;
        }

        public void setBrowser_version(String browser_version) {
            this.browser_version = browser_version;
        }

        public HashMap<String, Long> getUserTimes() {
            return userTimes;
        }

        public void setUserTimes(HashMap<String, Long> userTimes) {
            this.userTimes = userTimes;
        }

        public Integer getUserTime() {
            return userTime;
        }

        public void setUserTime(Integer userTime) {
            this.userTime = userTime;
        }

        public Integer getSpeedIndex() {
            return SpeedIndex;
        }

        public void setSpeedIndex(Integer speedIndex) {
            SpeedIndex = speedIndex;
        }

        public Integer getVisualComplete() {
            return visualComplete;
        }

        public void setVisualComplete(Integer visualComplete) {
            this.visualComplete = visualComplete;
        }

        public Integer getRun() {
            return run;
        }

        public void setRun(Integer run) {
            this.run = run;
        }

        public Pages getPages() {
            return pages;
        }

        public void setPages(Pages pages) {
            this.pages = pages;
        }

        public Thumbnails getThumbnails() {
            return thumbnails;
        }

        public void setThumbnails(Thumbnails thumbnails) {
            this.thumbnails = thumbnails;
        }

        public Images getImages() {
            return images;
        }

        public void setImages(Images images) {
            this.images = images;
        }


        public static class Pages
        {
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
        public static class Thumbnails {
            private String waterfall;
            private String checklist;
            private String screenShot;

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
        }

        public static class Images extends Thumbnails {
            private String connectionView;

            public String getConnectionView() {
                return connectionView;
            }

            public void setConnectionView(String connectionView) {
                this.connectionView = connectionView;
            }
        }



        public static class Breakdown {

            private RequestsBreakdown html;
            private RequestsBreakdown css;
            private RequestsBreakdown js;
            private RequestsBreakdown flash;
            private RequestsBreakdown image;
            private RequestsBreakdown font;
            private RequestsBreakdown other;

            public static class RequestsBreakdown {
                private Integer bytes;
                private Integer requests;

                public Integer getBytes() {
                    return bytes;
                }

                public void setBytes(Integer bytes) {
                    this.bytes = bytes;
                }

                public Integer getRequests() {
                    return requests;
                }

                public void setRequests(Integer requests) {
                    this.requests = requests;
                }
            }


        }
    }

}

