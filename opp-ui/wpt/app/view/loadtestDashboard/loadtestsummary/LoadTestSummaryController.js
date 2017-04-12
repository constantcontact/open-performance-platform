Ext.define('OppUI.view.loadtestDashboard.loadtestsummary.LoadTestSummaryController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.loadtestsummary-loadtestsummary',

    onRemoteSummaryTrend: function() {
        console.log("Remote Summary Trend Store loaded");
    }
    
});
