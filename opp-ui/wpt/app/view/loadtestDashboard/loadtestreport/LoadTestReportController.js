Ext.define('OppUI.view.loadTestDashboard.loadtestreport.LoadTestReportController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.loadtestreport',

    remoteAggDataLoaded: function(aggData) {
        var view, curTime, startTime, endTime, duration, cloudTestLink, externalTestId;
        console.log("AGG DATA LOADED ");
        viewModel = this.getViewModel();

        startTime = aggData.getData().items[0].getData().start_time;
        endTime = aggData.getData().items[0].getData().end_time;
        curTime = (typeof startTime === 'number' && startTime % 1 === 0) ? 
                    Ext.Date.format(new Date(startTime *1000),'m/d/Y h:i a') : startTime;
        duration = this.calculateDuration(startTime, endTime);
        externalTestId = aggData.getData().items[0].getData().external_test_id;

        if(externalTestId && externalTestId !== "") {
            cloudTestLink = 'http://cloudtest.roving.com/concerto/?initResultsTab=' + externalTestId;
        } else {
            cloudTestLink = "N/A";
        }
        
        // Set all the bindings for the load test report header.
        viewModel.set('testName',  aggData.getData().items[0].getData().test_name);
        viewModel.set('curTime',  curTime ? curTime : "");
        viewModel.set('duration',  duration);
        viewModel.set('vuserCount',  aggData.getData().items[0].getData().vuser_count);
        viewModel.set('environment',  aggData.getData().items[0].getData().environment);
        viewModel.set('description',  aggData.getData().items[0].getData().description);
        viewModel.set('cloudTestLink',  cloudTestLink);
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
