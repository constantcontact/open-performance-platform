Ext.define('CCPerf.view.report.LoadTestReportHeader', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.load-test-report-header',
    loadTestId:'',
    // html: 'Loading...',
    initComponent: function() {
       // this.loadTestId = CCPerf.util.Globals.reports.reportSettings.val;
        this.html = this.getContent(null);
        this.callParent(arguments);
    },
    setContent: function(lt) {
        this.update(this.getContent(lt));
    },
    getContent: function(lt) {
        if(lt === undefined || lt === null) lt = '';

	   if (lt === "") {
            return "Loading...";
        } else {
            var curTime = (typeof lt.startTime === 'number' && lt.startTime % 1 === 0) ? Ext.Date.format(new Date(lt.startTime *1000),'m/d/Y h:i a') : lt.startTime;
            var endTime = (typeof lt.endTime === 'number' && lt.endTime % 1 === 0) ? Ext.Date.format(new Date(lt.endTime *1000),'m/d/Y h:i a') : lt.endTime;
            var duration = this.calculateDuration(lt.startTime, lt.endTime);
            var testName = (lt.testSubName === "" || lt.testSubName === null) ? lt.testName : lt.testName + " | " + lt.testSubName;
            var html = "<table class='tbl-load-test-details'>\n\
                    <tr>\n\
                        <th>Test Name:</th>\n\
                        <td>" + testName + "</td>\n\
                        <th class='col2'>Date:</th>\n\
                        <td class='col2'>" + curTime + "</td>\n\
                        <th class='col2'>Duration:</th>\n\
                        <td class='col2'>" + duration + "</td>\n\
                    </tr>\n\
                    <tr>\n\
                        <th>Vusers:</th>\n\
                        <td>" + lt.vuserCount + "</td>\n\
                        <th class='col2'>Env.:</th>\n\
                        <td class='col2' colspan='2'>" + lt.environment + "</td>\n\
                    </tr>\n\
                    <tr>\n\
                        <th>Description:</td>\n\
                        <td colspan='4'>" + lt.description + "</td>\n\
                    </tr>\n";

           // add cloudtest link if it's a soasta test
           if((lt.testTool.indexOf("SOASTA") > -1) && lt.externalTestId !== null & lt.externalTestId !== 0 & lt.externalTestId !== '') {
               var cloudTestLink = CLOUD_TEST_BASE_URL + "/concerto/?initResultsTab=" + lt.externalTestId;
               html += "<tr>\n\
                            <th>CloudTest Result:</td>\n\
                            <td colspan='3'><a href='" + cloudTestLink + "' target='_blank'>" + cloudTestLink + "</a></td>\n\
                        </tr>\n";
           }

            html += "</table>";

            return html;

        }
    },
    calculateDuration: function(start, end){
        var duration = "";
        if(end !== null) {
            var durationSec = end/1000 - start/1000; // get the diff in sec
            if(durationSec > 86400){
                duration = Math.floor(durationSec/86400) + ' d ' + Math.round((durationSec % 86400)/60/60) + ' h'; // calculate hours and minutes
            }
            else if(durationSec > 3600){
                duration = Math.floor(durationSec/3600) + ' h ' + Math.round((durationSec % 3600)/60) + ' m'; // calculate hours and minutes
            } else {
                duration = Math.floor(durationSec/60) + ' m ' + durationSec % 60 + ' s'; // calculate minutes and seconds
            }
        }
        return duration;
    }
});
