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
                url: '/loadsvc/v1/loadtesttrends/summarytrend',
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
