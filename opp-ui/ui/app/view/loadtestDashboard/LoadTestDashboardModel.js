Ext.define('OppUI.view.loadtestDashboard.LoadTestDashboardModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.loadtest',
    data: {
        name: 'OppUI'
    },

    stores: {
        remoteSummaryTrend: {
            model: 'OppUI.model.loadtestDashboard.LoadTestSummary',
            autoLoad: true,
             proxy: {
                type: 'ajax',
                //url: 'http://localhost:8888/loadsvc/v1/loadtests/all/summarytrend',
                url: 'http://roadrunner.roving.com/loadsvc/v1/loadtests/all/summarytrend',
                reader: {
                    type: 'json',
                    keepRawData: true
                }
            }
        },
        remoteSummaryTrendFilter: {
            model: 'OppUI.model.loadTestDashboard.LoadTestReportSummary',
            source: '{remoteSummaryTrend}',
            autoLoad: false
        }
    }

});
