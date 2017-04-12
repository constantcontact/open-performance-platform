
Ext.define('OppUI.view.loadtestDashboard.loadtestsummary.LoadTestSummary',{
    extend: 'Ext.grid.Panel',
    xtype: 'loadtestsummary',
    alias: 'widget.loadtestsummary',

    requires: [
        'OppUI.view.loadtestDashboard.loadtestsummary.LoadTestSummaryController',
        'OppUI.view.loadtestDashboard.loadtestsummary.LoadTestSummaryModel'
    ],

    controller: 'loadtestsummary-loadtestsummary',
    viewModel: {
        type: 'loadtestsummary-loadtestsummary'
    },

    bind: {
        store: '{remoteSummaryTrend}'
    },

    tbar: [
         {
             itemId:'btnViewReport',
             xtype:'button',
             iconCls: 'icon-advanced-report',
             text: 'View Report',
             tooltip:'Provides custom trending and comparison of selected test runs.'
         },
        {
            itemId:'btnCreateGroupedReport',
            xtype:'button',
            iconCls: 'icon-advanced-report',
            text: 'Build Grouped Report',
            tooltip:'Enables you to build a report for several different test runs.',
            listeners:{
                click: function(){
                    Ext.create('Ext.window.Window', {
                        title: 'Grouped Report Creator',
                        layout: 'vbox',
                        height:500,
                        autoScroll:true,
                        autoDestroy: true,
                        closeAction: 'destroy',
                        items: {  xtype:'create-grouped-report' }
                    }).show();
                }
            }
        },'-',
         'Filter',
         {
            id:'txtFilterLoadGrid',
            xtype: 'textfield',
            name: 'searchField',
            hideLabel: true,
            width: 125,
            listeners: {
                specialkey: function(field, e){
                    if (e.getKey() === e.ENTER) {
                        btnFilterLoadGrid.click();
                    }
                }
            }
        }, {
            id:'btnFilterLoadGrid',
            xtype: 'button',
            text: '&gt;',
            tooltip: 'Filter the test runs'
        }, '-'
        
    ],

    columns: {
        items: [
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

    showTrend: function(v, rec, trendName, conversion, trendType){
        // getting pipe delimited trending value (val and percentage)
        var trendValArr = rec.data[trendName].split("|");
        var trendVal = trendValArr[0].trim();
        var trendPct = trendValArr[1].trim().replace('%', '');
        // convert if necessary
        if(conversion === "sec"){
            v = Math.round((v/1000)*1000)/1000;
            trendVal = Math.round((trendVal/1000)*1000)/1000;
        }
        if(conversion === "mb"){
            v = Math.round((v/1048576)*1000)/1000;
            trendVal = Math.round((trendVal/1048576)*1000)/1000;
        }
        // round the percent
        trendPct = Math.round(trendPct*100)/100;
        // set CSS
        var cls = ""; var color = "black"; var valColor = "black";

        if(trendType == "time" || trendType == "errors") {
        	// lower is better
	        if(trendVal<0) { cls = "arrow-ug"; color="green";}
	        else if (trendVal>0) { cls = "arrow-dr"; color='red'; }
	        else { cls = ""; color="black"; }
            if(trendType == "errors"){
                if(v > 0) valColor = 'DarkOrange'; // if any errors, make orange
                if(v > rec.data.callCount * 0.005) valColor = 'red'; // if error rate is greater than 0.5% make red
            }
        } else {
        	// higher is better
        	if(trendVal>0) { cls = "arrow-ug"; color="green";}
	        else if (trendVal<0) { cls = "arrow-dr"; color='red'; }
	        else { cls = ""; color="black"; }
        }
        var quickTip = "<span style='padding-left:16px; color:"+color+"' class='"+cls+"'>" + trendVal + " ("+trendPct+"%)</span>";
        // returning html markup
        return '<span style="color:'+valColor+'">'+v+'</span>' + '<span ext:qwidth="150" ext:qtip="'+quickTip+'" style="margin-left: 10px; padding-left:16px; color:'+color+'" class="'+cls+'">'+trendPct+'%</span>';
    },
    loadAdmin: function(){
       if(this.getQueryVar('user') === 'admin') {
            this.getDockedItems()[0].add({
                 id:'btnDelete',
                 xtype:'button',
                 iconCls: 'icon-delete',
                 text: 'Delete Selected',
                 tooltip:'Admin Only Feature.  Delete custom runs.'
             });
         }

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
    },
    getQueryVar: function(variable) {
        var query = window.location.search.substring(1);
        var vars = query.split('&');
        for (var i = 0; i < vars.length; i++) {
            var pair = vars[i].split('=');
            if (decodeURIComponent(pair[0]) === variable) {
                return decodeURIComponent(pair[1]);
            }
        }
        return "";
    }


});
