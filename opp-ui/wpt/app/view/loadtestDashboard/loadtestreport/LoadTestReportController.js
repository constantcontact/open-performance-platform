Ext.define('OppUI.view.loadTestDashboard.loadtestreport.LoadTestReportController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.loadtestreport',

    remoteAggDataLoaded: function(aggData) {
        var viewModel, urTime, startTime, endTime, duration, cloudTestLink, externalTestId;
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
        
        // Set all the bindings for the load test report.
        viewModel.set('testName',  aggData.getData().items[0].getData().test_name);
        viewModel.set('curTime',  curTime ? curTime : "");
        viewModel.set('duration',  duration);
        viewModel.set('vuserCount',  aggData.getData().items[0].getData().vuser_count);
        viewModel.set('environment',  aggData.getData().items[0].getData().environment);
        viewModel.set('description',  aggData.getData().items[0].getData().description);
        viewModel.set('cloudTestLink',  cloudTestLink);
        viewModel.set('loadTestId', aggData.getData().items[0].getData().load_test_id);
        viewModel.set('testSubName', aggData.getData().items[0].getData().test_sub_name);
        viewModel.set('appUnderTest', aggData.getData().items[0].getData().app_under_test);
        viewModel.set('appUnderTestVersion', aggData.getData().items[0].getData().app_under_test_version);
        viewModel.set('startTime', Ext.Date.format(new Date(aggData.getData().items[0].getData().start_time *1000),'m/d/Y h:i a'));
        viewModel.set('endTime', Ext.Date.format(new Date(aggData.getData().items[0].getData().end_time *1000),'m/d/Y h:i a'));
        viewModel.set('testTool', aggData.getData().items[0].getData().test_tool);
        viewModel.set('testToolVersion', aggData.getData().items[0].getData().test_tool_version);
        viewModel.set('comments', aggData.getData().items[0].getData().comments);
    },

    remoteSlasLoaded: function() {

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
