Ext.define('CCPerf.controller.ReportsController', {
    extend: 'Ext.app.Controller',
    requires: ['Ext.layout.container.Table'],
    stores: [
        'LoadTestSummaryTrendDynamic'
    ],
    models: [
        'ReportLoadTestModel',
        'ReportGroupLoadTestModel',
        'SlaModel'
    ],
    views: [
        'report.LoadTestReport',
        'report.LoadTestTrendChartView',
        'report.LoadTestTimeSeriesChartView',
        'report.LoadTestTrendChartViewV2',
        'report.CSTPanel',
        'report.LoadTestAggregatesGrid',
        'report.LoadTestDetails',
        'report.LoadTestReportHeader',
        'report.LoadTestSla',
        'report.group.GroupLoadTestAggregatesGrid',
        'report.group.GroupLoadTestReportHeader',
        'report.group.GroupLoadTestReport'
    ],
    init: function() {
        this.control({
            // create a drill down report when row is clicked
            'grid-grouploadtest-aggregates': {
                itemdblclick: function(dv, record, item, index, e){
                    var ltId = record.data.loadTestId;
                    var tc = this.getController('TestsController');
                    tc.createLoadReport('Drill to #' + ltId, null, null, ltId);
                }
            },
            '#btnChartDrillDown': {
                click: function(btn){
                    var ltId = btn.iconCls;
                    var tc = this.getController('TestsController');
                    tc.createLoadReport('Drill to #' + ltId, null, null, ltId);
                }
            },
            'chart-load-test-trend cartesian': {
                itemdblclick: function( series, item, event, eOpts ){
                    var ltId = item.record.data.id;
                    var tc = this.getController('TestsController');
                    Ext.getBody().mask('Loading Report');
                    tc.createLoadReport('Drill to #' + ltId, null, null, ltId);
                }
            }
        });
    }
});
