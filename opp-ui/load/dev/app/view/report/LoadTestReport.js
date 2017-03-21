Ext.define('CCPerf.view.report.LoadTestReport' ,{
    extend: 'Ext.panel.Panel',
    alias: 'widget.report-loadtest',
    requires: [
        'Ext.chart.CartesianChart',
        'Ext.chart.axis.Numeric',
        'Ext.chart.axis.Category',
        'Ext.chart.series.Line',
        'Ext.chart.interactions.ItemHighlight',
        'CCPerf.view.report.LoadTestRawDataChart'
    ],
    title: 'Load Tests',
    layout:'border',
    loadTestId:'',
    defaults: {
        collapsible: true,
        split: true,
        bodyStyle: 'padding:15px'
    },
    initComponent: function() {
        var loadTestId = this.loadTestId;
        var loadTestReport = this;
        var trendReportStore = Ext.create('Ext.data.Store', {
            model: 'CCPerf.model.ReportLoadTestModel',
            autoLoad: true,
            proxy: {
                type: 'ajax',
                api: {
                    read: '/loadsvc/v1/loadtests/'+this.loadTestId+'/trendreport'
                },
                reader: {
                    type: 'json'
                  //  rootProperty: 'aggregateData'
                }
            },
            listeners: {
                load: function(store){
                    var data = store.data.items[0].data;
                    loadTestReport.down('load-test-details').setContent(data.loadTest);
                    loadTestReport.down('load-test-report-header').setContent(data.loadTest);
                    loadTestReport.down('grid-loadtest-aggregates').getStore().loadData(data.aggregateData);

                    var chartsToBuild = [
                      { id: 'resp-pct-90', data: data.charts.respPct90.chart, chartXType:'chart-load-test-trend-v2' },
                      { id: 'resp-pct-75', data: data.charts.respPct90.chart, chartXType:'chart-load-test-trend-v2' },
                      { id: 'resp-avg', data: data.charts.respPct90.chart, chartXType:'chart-load-test-trend-v2' },
                      { id: 'resp-median', data: data.charts.respPct90.chart, chartXType:'chart-load-test-trend-v2' },
                      { id: 'tps-median', data: data.charts.respPct90.chart, chartXType:'chart-load-test-trend-v2' },
                      { id: 'tps-max', data: data.charts.respPct90.chart, chartXType:'chart-load-test-trend-v2' }
                    ];
                    chartsToBuild.forEach(function(chart){
                      var chartWrapper = loadTestReport.down('#' + chart.id).down(chart.chartXType);
                      chartWrapper.buildChart(chart.data);
                    });
                }
            }
        });

        this.items =  [{
            title: 'SLAs',
            region: 'north',
            collapsed:true,
            header:true,
            minSize: 100,
            maxSize: 250,
            margins: '5 0 0 0',
            cmargins: '5 5 0 0',
            autoScroll: true,
            layout:'fit',
            items: [ { xtype: 'load-test-sla', loadTestId:loadTestId }],
            listeners: {
                "float": function(){
                    this.expand();
                },
                collapse: function(e){
                    if(CCPerf.util.Globals.reports.sla.dirty){
                        this.up('panel').down('gridpanel[xtype=grid-loadtest-aggregates]').getStore().reload();
                        CCPerf.util.Globals.reports.sla.dirty = false;
                    }
                }
            }
        } ,{
            title: 'Load Test Details',
            region:'west',
            collapsed:true,
            margins: '5 0 0 0',
            cmargins: '5 5 0 0',
            width: 300,
            minSize: 100,
            maxSize: 500,
            items: [ { xtype: 'load-test-details', loadTestId:loadTestId }]
        }, {
            //   title: 'Main Content',
            collapsible: false,
            region: 'center',
            autoScroll: true,
            scope: this,
            layout: {
                type: 'table',
                columns: 2,
                tableAttrs: {
                    style: {
                        width: '100%'
                    }
                }
            },
            style: {
                overflow: 'auto'
            },
            defaults: {
                padding: 7,
                height: 300
            },
            margins: '5 0 0 0',
            items: [
                { xtype: 'load-test-report-header', height: 'auto', colspan: 2,  loadTestId: loadTestId},
                { xtype: 'cstpanel', colspan: 2, childxtype: 'grid-loadtest-aggregates', title: 'Load Test Summary', loadTestId: loadTestId },
                { xtype: 'cstpanel', colspan: 2, childxtype: 'chart-load-test-timeseries', yaxis: 'resp_pct90', xaxisTitle:'', yaxisTitle:'Response Time (msec)', title: '90th Percentile Response Time During Test', loadTestId: loadTestId, dataType: 'time' },
                { xtype: 'cstpanel', colspan: 2, childxtype: 'chart-load-test-timeseries', yaxis: 'call_count', xaxisTitle:'', yaxisTitle:'Transactions per Minute', title: 'Transactions per Minute During Test', loadTestId: loadTestId,  dataType: 'numeric' },
                { xtype: 'cstpanel', itemId:'resp-pct-90', colspan: 1, childxtype: 'chart-load-test-trend-v2', yaxis: 'resp_pct90', xaxisTitle:'', yaxisTitle:'Response Time (msec)', title: 'Trend: 90th Percentile Response Time', loadTestId: loadTestId, dataType: 'time' },
                { xtype: 'cstpanel', itemId:'resp-pct-75', colspan: 1, childxtype: 'chart-load-test-trend-v2', yaxis: 'resp_pct75', xaxisTitle:'', yaxisTitle:'Response Time (msec)', title: 'Trend: 75th Percentile Response Time', loadTestId: loadTestId, dataType: 'time' },
                { xtype: 'cstpanel', itemId:'resp-avg', colspan: 1, childxtype: 'chart-load-test-trend-v2', yaxis: 'resp_avg', xaxisTitle:'', yaxisTitle:'Response Time (msec)', title: 'Trend: Average Response Time', loadTestId: loadTestId, dataType: 'time' },
                { xtype: 'cstpanel', itemId:'resp-median', colspan: 1, childxtype: 'chart-load-test-trend-v2', yaxis: 'resp_median', xaxisTitle:'', yaxisTitle:'Response Time (msec)', title: 'Trend: Median Response Time', loadTestId: loadTestId, dataType: 'time' },
                { xtype: 'cstpanel', itemId:'tps-median', colspan: 1, childxtype: 'chart-load-test-trend-v2', yaxis: 'tps_median', xaxisTitle:'', yaxisTitle:'Transactions per Second', title: 'Trend: TPS Median', loadTestId: loadTestId, dataType: 'numeric' },
                { xtype: 'cstpanel', itemId:'tps-max', colspan: 1, childxtype: 'chart-load-test-trend-v2', yaxis: 'tps_max', xaxisTitle:'', yaxisTitle:'Transactions per Second', title: 'Trend: TPS Max', loadTestId: loadTestId, dataType: 'numeric' }
                ]
            }
        ];

        // unmask if masked
        Ext.getBody().unmask();

        this.callParent(arguments);
    }
});
