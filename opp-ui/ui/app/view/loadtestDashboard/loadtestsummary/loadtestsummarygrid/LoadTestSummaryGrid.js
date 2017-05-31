
Ext.define('OppUI.view.loadtestDashboard.loadtestsummary.loadtestsummarygrid.LoadTestSummaryGrid',{
    extend: 'Ext.grid.Panel',
    xtype: 'loadtestsummarygrid',
    alias: 'widget.loadtestsummarygrid',

    requires: [
        'OppUI.view.loadtestDashboard.loadtestsummary.loadtestsummarygrid.LoadTestSummaryGridController',
        'OppUI.view.loadtestDashboard.loadtestsummary.loadtestsummarygrid.LoadTestSummaryGridModel'
    ],

    controller: 'loadtestsummarygrid',
    viewModel: {
        type: 'loadtestsummarygrid'
    },

    bind: {
        store: '{remoteSummaryTrend}'
    },
    tbar: [{
            itemId:'btnCreateGroupedReport',
            xtype:'button',
            iconCls: 'x-fa fa-list-alt',
            text: 'Build Grouped Report',
            tooltip:'Enables you to build a report for several different test runs.',
            listeners:{
                click: 'showGroupReportForm'
            }
        },'-',
        'Search',
         {
            xtype: 'textfield',
            name: 'searchField',
            hideLabel: true,
            width: 250,
            listeners: {
                specialkey: 'specialkey'
            }
        },
        {
            xtype: 'button',
            iconCls: 'x-fa fa-search',
            tooltip: 'Filter the test runs',
            listeners: {
                click: 'search'
            }
        },'-',
        {
            itemId:'btnDelete',
            xtype: 'button',
            iconCls: 'x-fa fa-times',
            text: 'Delete Selected',
            hidden: true,
            tooltip: 'Admin Only Feature.  Delete custom runs.',
            listeners: {
                click: 'deleteButtonClicked'
        }
    }],

    selModel: {
       selType: 'rowmodel', // rowmodel is the default selection model
       mode: 'MULTI' // Allows selection of multiple rows
    },

    columns: {
        items: [
            {text: '#', xtype: 'rownumberer', width: 50, sortable: false },
            {text: 'TestId', dataIndex: "loadTestId", hidden:true},
            {text: 'Test Name', dataIndex:"testName"},
            {text: 'Sub Name', dataIndex:"testSubName", hidden:true},
            {text: 'Application', dataIndex: "appUnderTest"},
            {text: 'App Version', dataIndex: "appUnderTestVersion", hidden:true},
            {text: 'Comments', dataIndex: "comments", hidden:true},
            {text: 'Description', dataIndex: "description", hidden:true},
            {text: 'Environment', dataIndex: "environment"},
            {text: 'Start Time', dataIndex: "startTime", renderer: function(v) { return Ext.Date.format(new Date(v),'m/d/Y H:i a')}},
            {text: 'End Time', dataIndex: "endTime", hidden:true, renderer: function(v) { return Ext.Date.format(new Date(v),'m/d/Y H:i a')}},
            {text: 'Duration', dataIndex: "endTime", renderer: function(v, meta, rec) { return this.calculateDuration(rec.data.startTime, v) }},
            {text: 'Test Tool', dataIndex:"testTool", hidden:true},
            {text: 'Tool Version', dataIndex: "testToolVersion", hidden:true},
            {text: '# Users', dataIndex:"vuserCount"},
            {text: 'Average (sec)', width:150, dataIndex: "respAvg", renderer: function(val, meta, rec) { return this.showTrend(val, rec, "respAvgTrend", "sec", "time")} },
            {text: 'Median (sec)', width:150, dataIndex: "respMedian", renderer: function(val, meta, rec) { return this.showTrend(val, rec, "respMedianTrend", "sec", "time")} },
            {text: '90th PCT (sec)', width:150, dataIndex: "respPct90", renderer: function(val, meta, rec) { return this.showTrend(val, rec, "respPct90Trend", "sec", "time")} },
            {text: 'Total Calls', width:150, dataIndex:"totalCallCount", renderer: function(val, meta, rec) { return this.showTrend(val, rec, "totalCallCountTrend", "", "count")} },
            {text: 'Total Errors', width:130, dataIndex: "errorCount", renderer: function(val, meta, rec) { return this.showTrend(val, rec, "errorCountTrend", "", "errors"); } },
            {text: 'TPS (median)', width:130, dataIndex: "tpsMedian", renderer: function(val, meta, rec) { return this.showTrend(val, rec, "tpsMedianTrend", "", "count")} },
            {text: 'TPS (max)', width:130, dataIndex: "tpsMax", renderer: function(val, meta, rec) { return this.showTrend(val, rec, "tpsMaxTrend", "", "count")} },
            {text: 'Total MBytes', width:160, dataIndex: "totalBytes", renderer: function(val, meta, rec) { return this.showTrend(val, rec, "totalBytesTrend", "mb", "count")} }
        ]
    },

    listeners: {
        itemdblclick: 'loadTestSelected'
    },

    showTrend: function(v, rec, trendName, conversion, trendType){
       return this.getController().showTrend(v, rec, trendName, conversion, trendType);
    },
    calculateDuration: function(start, end){
        return this.getController().calculateDuration(start, end);
    }
});
