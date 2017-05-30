
Ext.define('OppUI.view.loadtestDashboard.loadtestsummary.LoadTestSummary',{
    extend: 'Ext.grid.Panel',
    xtype: 'loadtestsummary',
    alias: 'widget.loadtestsummary',

    requires: [
        'OppUI.view.loadtestDashboard.loadtestsummary.LoadTestSummaryController',
        'OppUI.view.loadtestDashboard.loadtestsummary.LoadTestSummaryModel'
    ],

    controller: 'loadtestsummary',
    viewModel: {
        type: 'loadtestsummary'
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
                click: function(button) {
                    var grid, i, ids;

                    function _handleInput(btn) {
                        if(btn == 'yes') {
                            grid.deleteRecords(ids);
                        }
                    }

                    grid = button.up('grid');
                    if(grid.getSelectionModel().selected.items.length > 0){
                        ids = new Array(); 
                        for(i=0; i<grid.getSelectionModel().selected.items.length; i++){
                            ids.push(grid.getSelectionModel().selected.items[i].data.load_test_id);
                        }
                        Ext.MessageBox.confirm('Confirm', 'Are you sure you want to delete ' + ids.length + ' record(s)', _handleInput);
                    } else {
                        Ext.Msg.alert("Warning...", "You must make a selection first");
                    }
                }
            }
        }
        
    ],

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
        itemdblclick: function(grid, record, item, index) {
            this.up('loadtest').down('loadtestsummarytab').createTab(grid, record, item, index);
        }
    },

    deleteRecords: function(ids) {
        var me = this;

        Ext.Ajax.request({
            url: '/loadsvc/v1/loadtests/' + ids.join(),
            method:'delete',
            success: function(response){
                //var json = Ext.decode(response.responseText, false);
                me.up('loadtest').getViewModel().getStore('remoteSummaryTrend').reload();
            },
            failure: function(response) {
                Ext.Msg.alert("Error...", "Error processing deletion. Please Try again Later.");
            }
        });
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
        var cls = ""; var color = ""; var red = '#e15757'; var green = '#3dae3d';

        if(trendType == "time" || trendType == "errors") {
        	// lower is better
	        if(trendVal<0) { cls = "arrow-ug"; color=green;}
	        if (trendVal>0) { cls = "arrow-dr"; color=red; }
            
            if(trendType == "errors"){
                if(v > 0) valColor = 'DarkOrange'; // if any errors, make orange
                if(v > rec.data.callCount * 0.005) valColor = red; // if error rate is greater than 0.5% make red
            }
        } else {
        	// higher is better
        	if(trendVal>0) { cls = "arrow-ug"; color=green;}
	        if (trendVal<0) { cls = "arrow-dr"; color=red; }
        }
        var quickTip = "<span style='padding-left:16px; color:"+color+"' class='"+cls+"'>" + trendVal + " ("+trendPct+"%)</span>";
        var colorCss = (color === '') ? color : 'color:'+color;
        // returning html markup
        return v + '<span ext:qwidth="150" ext:qtip="'+quickTip+'" style="margin-left: 10px; padding-left:16px; '+colorCss+'" class="'+cls+'">'+trendPct+'%</span>';
    },
    loadAdmin: function(){
       if(this.getQueryVar('user') === 'admin') {
            this.getDockedItems()[0].add({
                // id:'btnDelete',
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
