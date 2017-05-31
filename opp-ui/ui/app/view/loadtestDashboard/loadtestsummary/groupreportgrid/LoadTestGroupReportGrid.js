
Ext.define('OppUI.view.loadtestDashboard.loadtestsummary.groupreportgrid.LoadTestGroupReportGrid',{
    extend: 'Ext.grid.Panel',
    alias: 'widget.loadtestgroupreportgrid',

    requires: [
        'OppUI.view.loadtestDashboard.loadtestsummary.groupreportgrid.LoadTestGroupReportGridController',
        'OppUI.view.loadtestDashboard.loadtestsummary.groupreportgrid.LoadTestGroupReportGridModel'
    ],

    controller: 'loadtestgroupreportgrid',
    viewModel: {
        type: 'loadtestgroupreportgrid'
    },

    bind: {
        store: '{groupReport}'
    },

    title: 'Latest Trends',

    columns: [
        {xtype: 'widgetcolumn', text: '90th PCT Trend', width: 200, dataIndex:'sparkline90', widget: { xtype: 'sparklineline', fillColor: '#ddf', width: 200, height:60 } },
        {xtype: 'widgetcolumn', text: 'Median Trend', width: 200, dataIndex:'sparkline50', widget: { xtype: 'sparklineline', fillColor: '#ddf', width: 200, height:60 } },
        {text: 'Test Id', width: 50, hidden:true, dataIndex: "loadTestId"},
        {text: 'Test Name', width: 200, dataIndex: "testName"},
        {text: 'Sub Name', dataIndex: "testSubName", hidden: true},
        {text: 'Application', dataIndex: "appUnderTest"},
        {text: 'App Version', dataIndex: "appUnderTestVersion", hidden: true},
        {text: 'Comments', dataIndex: "comments", hidden: true},
        {text: 'Description', dataIndex: "description", hidden: true},
        {text: 'Env', width: 50, dataIndex: "environment"},
        {text: 'Start Time', dataIndex: "startTime", renderer: function (v) { return Ext.Date.format(new Date(v), 'm/d/Y H:i a') } },
        {text: 'Test Tool', dataIndex: "testTool", hidden: true},
        {text: 'Tool Version', dataIndex: "testToolVersion", hidden: true},
        {text: '# Users', width: 70, dataIndex: "vuserCount"},
        {text: 'Average (sec)', width:150, dataIndex: "respAvg", renderer: function(val, meta, rec) { return this.showTrend(val, rec, "respAvgTrend", "sec", "time")} },
        {text: 'Median (sec)', width:150, dataIndex: "respMedian", renderer: function(val, meta, rec) { return this.showTrend(val, rec, "respMedianTrend", "sec", "time")} },
        {text: '90th PCT (sec)', width:150, dataIndex: "respPct90", renderer: function(val, meta, rec) { return this.showTrend(val, rec, "respPct90Trend", "sec", "time")} },
        {text: 'Total Calls', width:150, dataIndex:"totalCallCount", renderer: function(val, meta, rec) { return this.showTrend(val, rec, "totalCallCountTrend", "", "count")} },
        {text: 'TPS (median)', width:150, dataIndex: "tpsMedian", renderer: function(val, meta, rec) { return this.showTrend(val, rec, "tpsMedianTrend", "", "count")} },
        {text: 'TPS (max)', width:150, dataIndex: "tpsMax", renderer: function(val, meta, rec) { return this.showTrend(val, rec, "tpsMaxTrend", "", "count")} },
        {text: 'Total MBytes', width:150, dataIndex: "totalBytes", renderer: function(val, meta, rec) { return this.showTrend(val, rec, "totalBytesTrend", "mb", "count")} }
    ],
    
    showTrend: function(v, rec, trendName, conversion, trendType){
       return this.getController().showTrend(v, rec, trendName, conversion, trendType);
    }
});
