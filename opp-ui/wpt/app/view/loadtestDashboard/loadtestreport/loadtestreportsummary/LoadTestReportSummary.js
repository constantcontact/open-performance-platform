
Ext.define('OppUI.view.loadtestDashboard.loadtestreport.loadtestreportsummary.LoadTestReportSummary',{
    extend: 'Ext.grid.Panel',
    alias: 'widget.loadtestreportsummary',

    requires: [
        'OppUI.view.loadtestDashboard.loadtestreport.loadtestreportsummary.LoadTestReportSummaryController',
        'OppUI.view.loadtestDashboard.loadtestreport.loadtestreportsummary.LoadTestReportSummaryModel'
    ],

    controller: 'loadtestreportsummary',
    viewModel: {
        type: 'loadtestreportsummary'
    },
    bind: {
        store: '{remoteAggData}'
    },

    title: 'Load Test Summary',

    columns:[
        {text: 'Transaction', dataIndex: "transaction_name", width: 180 },
            {text: 'Min (s)', dataIndex: "resp_min", width: 75, 
                renderer: function(v, meta, rec, rowIndex, colIndex, store){ 
                    var sla = rec.data.sla_min;
                    var val = this.convertToSec(v);
                    var slaData = this.processSLA(v, sla, rec.data.sla_margin_of_error);
                    //return "<div>"+val+"</div>";
                   // meta.tdAttr = 'data-qtip="' + slaData['slaTooltip'] + '" class="' + slaData['slaClass'] + '"';
                    return "<div class='" + slaData['slaClass'] + "' data-qtip='"+slaData['slaTooltip']+"'>"+val+"</div>";
                }
            },
            {text: 'Max (s)', dataIndex: "resp_max", width: 75, 
                renderer: function(v, meta, rec, rowIndex, colIndex, store){ 
                    var sla = rec.data.sla_max;
                    var val = this.convertToSec(v);
                    var slaData = this.processSLA(v, sla, rec.data.sla_margin_of_error);
                    //return "<div>"+val+"</div>";
                    return "<div class='" + slaData['slaClass'] + "' data-qtip='"+slaData['slaTooltip']+"'>"+val+"</div>";
                }
            },
            {text: 'Avg (s)', dataIndex: "resp_avg", width: 75, 
                renderer: function(v, meta, rec, rowIndex, colIndex, store){ 
                    var sla = rec.data.sla_avg;
                    var val = this.convertToSec(v);
                    var slaData = this.processSLA(v, sla, rec.data.sla_margin_of_error);
                    //return "<div>"+val+"</div>";
                    return "<div class='" + slaData['slaClass'] + "' data-qtip='"+slaData['slaTooltip']+"'>"+val+"</div>";
                }
            },
            {text: 'Median (s)', dataIndex: "resp_median",  
                renderer: function(v, meta, rec, rowIndex, colIndex, store){ 
                    var sla = rec.data.sla_median;
                    var val = this.convertToSec(v);
                    var slaData = this.processSLA(v, sla, rec.data.sla_margin_of_error);
                    //return "<div>"+val+"</div>";
                    return "<div class='" + slaData['slaClass'] + "' data-qtip='"+slaData['slaTooltip']+"'>"+val+"</div>";
                }
            },
            {text: '75th PCT (s)', dataIndex: "resp_pct75", 
                renderer: function(v, meta, rec, rowIndex, colIndex, store){ 
                    var sla = rec.data.sla_pct75;
                    var val = this.convertToSec(v);
                    var slaData = this.processSLA(v, sla, rec.data.sla_margin_of_error);
                    //return "<div>"+val+"</div>";
                    return "<div class='" + slaData['slaClass'] + "' data-qtip='"+slaData['slaTooltip']+"'>"+val+"</div>";
                }
            },
            {text: '90th PCT (s)', dataIndex: "resp_pct90", 
                renderer: function(v, meta, rec, rowIndex, colIndex, store){ 
                    var sla = rec.data.sla_pct90;
                    var val = this.convertToSec(v);
                    var slaData = this.processSLA(v, sla, rec.data.sla_margin_of_error);
                    //return "<div>"+val+"</div>";
                    return "<div class='" + slaData['slaClass'] + "' data-qtip='"+slaData['slaTooltip']+"'>"+val+"</div>";
                }
            },
            {text: 'Std Dev (s)', dataIndex: "resp_stddev",
                renderer: function(v, meta, rec, rowIndex, colIndex, store){ 
                    var avg = Number(rec.data.resp_avg);
                    v = Number(v);
                    var stdClass = "sla-pass";
                    var stdToolTip = "Standard deviation is within normal range";
                    // if greater than 1/2 the mean, we could have an issue
                    if(v >= (avg/2)) {
                        stdClass = "sla-warning";
                        stdToolTip = "Standard deviation is greater than 1/2 the mean. This might be suspect.";
                    }
                    // if greater than the mean, things are probably pretty bad
                    if(v >= avg) {
                        stdClass = "sla-fail";
                        stdToolTip = "Standard deviation is greater than the mean. This might be statistically invalid.";
                    }
                    
                    return "<div class='" + stdClass + "' data-qtip='"+stdToolTip+"'>"+this.convertToSec(v)+"</div>";
                }
            },
            {text: 'Call Count', dataIndex: "call_count"},
            {text: 'MB Received', dataIndex: "total_bytes_received", renderer: function(v){ return this.convertToMb(v)} },
            {text: 'MB Sent', dataIndex: "total_bytes_sent", renderer: function(v){ return this.convertToMb(v)} },
            {text: 'TPS (Median)', dataIndex: "tps_median", renderer: function(v){ return v; } },
            {text: 'TPS (Max)', dataIndex: "tps_max", renderer: function(v){ return v; } },
            {text: 'Errors', dataIndex: "error_count", renderer: function(v, meta, rec){
                // set sla to .05% of total number of calls
                var slaData = this.processSLA(v, rec.data.call_count *.005, 0);
                var tooltip = slaData['slaTooltip'].replace('sec ', ' ').replace('the SLA.', 'the SLA error rate of 0.5%'); // quick hack so I can reuse sla function
                return "<div class='" + slaData['slaClass'] + "' data-qtip='"+tooltip+"'>"+v+"</div>";
            } 
        }
    ],

    convertToMb: function(bytes){
        return Math.round((bytes/1048576)*1000)/1000;
    },
    convertToSec: function(ms){
        return Math.round((ms/1000)*1000)/1000;
    },
    buildSlaToolTip: function(sla, valToSla, valToSlaPerc, errMargin, slaClass) {
        var status = "";
        var aboveBelow = "";
        switch(slaClass){
            case "sla-pass":
                status = "passed";
                aboveBelow = "below";
                break;
            case "sla-warning":
                status = " was exceeded but the response time stayed within the " + errMargin + "% margin of error";
                aboveBelow = "above";
                break;
            case "sla-fail":
                status = "failed";
                aboveBelow = "above";
                break;
                
        }
        return "The " + this.convertToSec(sla) + "sec SLA "+status+". The transaction was " +
                this.convertToSec(valToSla) + "s ("+ valToSlaPerc + "%) "+aboveBelow+" the SLA.";
    },
    processSLA: function(val, sla, errMargin){
        var arrRes = new Array();
        var slaClass = "";
        var valToSlaPerc = 0;
        var valToSla = 0;
        var slaTooltip = "";
        if(errMargin === undefined || errMargin === null) errMargin = 10; // default 10%
        if(sla !== null) {
            valToSlaPerc = Math.round((((val/sla)-1) * 100)*100)/100; // value percentage of SLA
            if(valToSlaPerc < 0) valToSlaPerc = valToSlaPerc * -1; // make percentage positive 
            valToSla = val - sla; // below 0 is below SLA (good)
            if(valToSla <= 0){
                slaClass = "sla-pass";
                slaTooltip = this.buildSlaToolTip(sla, valToSla, valToSlaPerc, errMargin, slaClass);
            } else {
                // if the val is less than or equal to 110% of the SLA make orange
                if(valToSlaPerc < errMargin){
                    slaClass = "sla-warning";
                    slaTooltip = this.buildSlaToolTip(sla, valToSla, valToSlaPerc, errMargin, slaClass);
                } else {
                    // val is greater than the 10% margin of error
                    slaClass = "sla-fail";
                    slaTooltip = this.buildSlaToolTip(sla, valToSla, valToSlaPerc, errMargin, slaClass);
                }
            }
        } else {
            slaClass = "sla-missing";
            slaTooltip = "No SLA set. Click on the SLA bar above this grid to set your SLAs.";
        }
        arrRes['slaClass'] = slaClass;
        arrRes['slaTooltip'] = slaTooltip;
        arrRes['valToSlaPerc'] = valToSlaPerc;
        arrRes['valToSla'] = valToSla;
        return arrRes;
        
    },
    showTrend: function(v, rec, trendName, conversion){
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
        var cls = ""; var color = "black";
        if(trendVal<0) { cls = "arrow-ug"; color="green";}
        else if (trendVal>0) { cls = "arrow-dr"; color='red'; }
        else { cls = ""; color="black"; } 
        var quickTip = "<span style='padding-left:16px; color:"+color+"' class='"+cls+"'>" + trendVal + " ("+trendPct+"%)</span>";
        // returning html markup
        return v + '<span ext:qwidth="150" ext:qtip="'+quickTip+'" style="margin-left: 10px; padding-left:16px; color:'+color+'" class="'+cls+'">'+trendPct+'%</span>';
    },



     tools: [
    {
        type:'gear',
        listeners: {
            afterrender: function(me) {
                if(me.up().up().yaxis == undefined || me.up().up().yaxis == null) {
                    // remove table views for panels that have none
                    document.getElementById(me.getId()).style.display = "none";
                } else {
                    // Tool tips to switch to table view
                    Ext.tip.QuickTipManager.register({
                        target: me.getId(),
                        title: 'Switch to Table View',
                        text: 'View this graph as a trend table'
                    });
                }
            }
        },
        handler: function(a,b,c){
            var cstPanel = this.up('cstpanel');
            if(cstPanel.yaxis !== undefined  && cstPanel.yaxis !== null) {
                var window = Ext.create('Ext.window.Window', {
                    layout: 'fit',
                    height: Ext.getBody().getViewSize().height - 100,
                    width: Ext.getBody().getViewSize().width - 100,
                    maximizable: true,
                    title: cstPanel.down(cstPanel.childxtype).title,
                    items:[
                        Ext.create('CCPerf.view.report.LoadTestRawDataChart', {
                            loadTestId:cstPanel.loadTestId,
                            yaxis:cstPanel.yaxis,
                            dataType: cstPanel.dataType
                        })
                    ],
                    tools: [ {
                        type:'print',
                        listeners: {
                            afterrender: function(me) {
                                Ext.tip.QuickTipManager.register({
                                    target: me.getId(),
                                    title: 'View as Wiki',
                                    text: 'View Wiki Markup for this table'
                                });
                            }
                        },
                        handler: function(a,b,c){
                            var grid = this.up('window').down('grid');
                            var wiki = grid.getWikiFormat();
                            Ext.Msg.alert('Wiki Markup', wiki);
                        }
                    }, {
                        type: 'save',
                        listeners: {
                            afterrender: function(me) {
                                Ext.tip.QuickTipManager.register({
                                    target: me.getId(),
                                    title: 'Save Data',
                                    text: 'Save Data as CSV'
                                });
                            }
                        },
                        handler: function(a,b,c) {
                            var grid = this.up('window').down('grid');
                            var csv = grid.getCsv();
                            var link = document.createElement("a");
                            link.download=cstPanel.loadTestId + "_" + cstPanel.yaxis + "_data.csv";
                            link.href='data:text/plain;charset=utf-8,' + encodeURIComponent(csv);
                            link.click();
                        }
                    }]
                });
                window.show();

                Ext.tip.QuickTipManager.register({
                    target: window.tools[2].id,
                    title: 'Toggle Maximize',
                    text: 'Toggle size of this window'
                });
                Ext.tip.QuickTipManager.register({
                    target: window.tools[3].id,
                    title: 'Close',
                    text: 'Close this window'
                });

            } else {
                alert("This chart has no raw data");
            }
        }
    },
    {
        type:'print',
        listeners: {
            afterrender: function(me) {
                Ext.tip.QuickTipManager.register({
                    target: me.getId(),
                    title: 'Print',
                    text: 'Print this graph'
                });
            }
        },
        handler: function(a,b,c){
        	var chart, grid;
        	if(chart = this.up('cstpanel').down('chart')){
        		chart.download();
        	} else if(grid = this.up('cstpanel').down('grid')) {
        		var markup = getWikiMarkup(grid);
                Ext.Msg.alert('Wiki Markup', markup);
        	} else {
        		alert('No supported objects to save.');
        	}
        	//alert(this.up('cstpanel').down('chart').save({ type: 'image/png' }));
        }
    },
    {
        type:'maximize',
        listeners: {
            afterrender: function(me) {
                Ext.tip.QuickTipManager.register({
                    target: me.getId(),
                    title: 'Maximize',
                    text: 'Maximize this graph'
                });
            }
        },
        handler: function(a,b,c){
            var cstPanel = this.up('cstpanel');
            var childComponent = cstPanel.items.items[0];

            var window = Ext.create('Ext.window.Window', {
                layout: 'fit',
                childsOrigParent: cstPanel,
                height: Ext.getBody().getViewSize().height - 100,
                width: Ext.getBody().getViewSize().width - 100,
                maximizable: true,
                title: cstPanel.down(cstPanel.childxtype).title,
                items: childComponent,
                tools: [{
			        type:'print',
                    listeners: {
                        afterrender: function(me) {
                            Ext.tip.QuickTipManager.register({
                                target: me.getId(),
                                title: 'Print',
                                text: 'Print this graph'
                            });
                        }
                    },
			        handler: function(a,b,c){
			        	var chart, grid;
			        	if(chart = c.up('window').down('chart')){
			        		chart.download();
			        	} else if(grid = this.up('window').down('grid')) {
			        		var markup = getWikiMarkup(grid);
			                Ext.Msg.alert('Wiki Markup', markup);
			        	} else {
			        		alert('No supported objects to save.');
			        	}
			        	//alert(this.up('cstpanel').down('chart').save({ type: 'image/png' }));
			        }
			    }],
                listeners: {
                    beforeclose: function(panel, opts){
                        var child = this.items.items[0];
                        this.childsOrigParent.add(child);
                    }
                }
            });
            window.show();

            Ext.tip.QuickTipManager.register({
                target: window.tools[1].id,
                title: 'Toggle Maximize',
                text: 'Toggle size of this window'
            });
            Ext.tip.QuickTipManager.register({
                target: window.tools[2].id,
                title: 'Close',
                text: 'Close this window'
            });

            window.toggleMaximize();
        }
    }
    ]
});
