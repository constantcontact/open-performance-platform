
Ext.define('OppUI.view.loadTestDashboard.loadtestreport.LoadTestReport',{
    extend: 'Ext.panel.Panel',

    requires: [
        'OppUI.view.loadTestDashboard.loadtestreport.LoadTestReportController',
        'OppUI.view.loadTestDashboard.loadtestreport.LoadTestReportModel'
    ],

    controller: 'loadtestreport-loadtestreport',
    viewModel: {
        type: 'loadtestreport-loadtestreport'
    },

    html: 'Hello, World!!'
});
