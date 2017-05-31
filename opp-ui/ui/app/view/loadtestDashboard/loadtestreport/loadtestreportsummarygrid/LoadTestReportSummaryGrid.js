
Ext.define('OppUI.view.loadtestDashboard.loadtestreport.loadtestreportsummarygrid.LoadTestReportSummaryGrid',{
    extend: 'Ext.grid.Panel',
    alias: 'widget.loadtestreportsummarygrid',

    requires: [
        'OppUI.view.loadtestDashboard.loadtestreport.loadtestreportsummarygrid.LoadTestReportSummaryGridController',
        'OppUI.view.loadtestDashboard.loadtestreport.loadtestreportsummarygrid.LoadTestReportSummaryGridModel'
    ],

    controller: 'loadtestreportsummarygrid',
    viewModel: {
        type: 'loadtestreportsummarygrid'
    },
    bind: {
        store: '{remoteAggData}'
    },
    title: 'Load Test Summary',

    config: {
        tools: [{
            type:'print',
            listeners: {
                afterrender: function(me) {
                    // Ext.tip.QuickTipManager.register({
                    //     target: me.getId(),
                    //     title: 'Print',
                    //     text: 'Print this graph'
                    // });
                }
            },
            handler: function(event, element, view){
                var chart, grid, markup;

                grid = view.up('loadtestreportsummarygrid');
                markup = grid.getController().getWikiMarkup();
                Ext.Msg.alert('Wiki Markup', markup);
            }
        },{
            type:'maximize',
            handler: function(event, element, view){

                var window = Ext.create('Ext.window.Window', {
                    layout: 'fit',
                    height: Ext.getBody().getViewSize().height - 100,
                    width: Ext.getBody().getViewSize().width - 100,
                    maximizable: true,
                    items: [
                        {
                            xtype: 'loadtestreportsummarygrid',      
                            header: false,
                            collapsible: false
                        }
                    ],
                    tools: [{
                        type:'print',
                        handler: function(event, element, view){
                            var chart, grid, markup;

                            grid = view.up('loadtestreportsummarygrid');
                            markup = grid.getController().getWikiMarkup();
                            Ext.Msg.alert('Wiki Markup', markup);
                        }
                    }]
                });
                
                // add the window to the parent loadTestReport
                // so the window will share the viewmodel.
                view.up('loadtestreport').add(window).showAt();
            }
        }]
    },

    columns:[
        {text: 'Transaction', dataIndex: "transactionName", width: 180, flex: 1 },
            {text: 'Min (s)', dataIndex: "respMin", width: 75, 
                renderer: function(v, meta, rec, rowIndex, colIndex, store){ 
                    var sla = rec.data.slaMin;
                    var val = this.convertToSec(v);
                    var slaData = this.processSLA(v, sla, rec.data.slaMarginOfError);
                    //return "<div>"+val+"</div>";
                   // meta.tdAttr = 'data-qtip="' + slaData['slaTooltip'] + '" class="' + slaData['slaClass'] + '"';
                    return "<div class='" + slaData['slaClass'] + "' data-qtip='"+slaData['slaTooltip']+"'>"+val+"</div>";
                }, flex: 1 
            },
            {text: 'Max (s)', dataIndex: "respMax", width: 75, 
                renderer: function(v, meta, rec, rowIndex, colIndex, store){ 
                    var sla = rec.data.slaMax;
                    var val = this.convertToSec(v);
                    var slaData = this.processSLA(v, sla, rec.data.slaMarginOfError);
                    //return "<div>"+val+"</div>";
                    return "<div class='" + slaData['slaClass'] + "' data-qtip='"+slaData['slaTooltip']+"'>"+val+"</div>";
                }, flex: 1 
            },
            {text: 'Avg (s)', dataIndex: "respAvg", width: 75, 
                renderer: function(v, meta, rec, rowIndex, colIndex, store){ 
                    var sla = rec.data.slaAvg;
                    var val = this.convertToSec(v);
                    var slaData = this.processSLA(v, sla, rec.data.slaMarginOfError);
                    //return "<div>"+val+"</div>";
                    return "<div class='" + slaData['slaClass'] + "' data-qtip='"+slaData['slaTooltip']+"'>"+val+"</div>";
                }, flex: 1 
            },
            {text: 'Median (s)', dataIndex: "respMedian",  
                renderer: function(v, meta, rec, rowIndex, colIndex, store){ 
                    var sla = rec.data.slaMedian;
                    var val = this.convertToSec(v);
                    var slaData = this.processSLA(v, sla, rec.data.slaMarginOfError);
                    //return "<div>"+val+"</div>";
                    return "<div class='" + slaData['slaClass'] + "' data-qtip='"+slaData['slaTooltip']+"'>"+val+"</div>";
                }, flex: 1 
            },
            {text: '75th PCT (s)', dataIndex: "respPct75", 
                renderer: function(v, meta, rec, rowIndex, colIndex, store){ 
                    var sla = rec.data.slaPct75;
                    var val = this.convertToSec(v);
                    var slaData = this.processSLA(v, sla, rec.data.slaMarginOfError);
                    //return "<div>"+val+"</div>";
                    return "<div class='" + slaData['slaClass'] + "' data-qtip='"+slaData['slaTooltip']+"'>"+val+"</div>";
                }, flex: 1 
            },
            {text: '90th PCT (s)', dataIndex: "respPct90", 
                renderer: function(v, meta, rec, rowIndex, colIndex, store){ 
                    var sla = rec.data.slaPct90;
                    var val = this.convertToSec(v);
                    var slaData = this.processSLA(v, sla, rec.data.slaMarginOfError);
                    //return "<div>"+val+"</div>";
                    return "<div class='" + slaData['slaClass'] + "' data-qtip='"+slaData['slaTooltip']+"'>"+val+"</div>";
                }, flex: 1 
            },
            {text: 'Std Dev (s)', dataIndex: "respStddev",
                renderer: function(v, meta, rec, rowIndex, colIndex, store){ 
                    var avg = Number(rec.data.respAvg);
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
                }, flex: 1 
            },
            {text: 'Call Count', dataIndex: "callCount", flex: 1 },
            {text: 'MB Received', dataIndex: "totalBytesReceived", renderer: function(v){ return this.convertToMb(v)}, flex: 1  },
            {text: 'MB Sent', dataIndex: "totalBytesSent", renderer: function(v){ return this.convertToMb(v)}, flex: 1  },
            {text: 'TPS (Median)', dataIndex: "tpsMedian", renderer: function(v){ return v; }, flex: 1  },
            {text: 'TPS (Max)', dataIndex: "tpsMax", renderer: function(v){ return v; }, flex: 1  },
            {text: 'Errors', dataIndex: "errorCount", renderer: function(v, meta, rec){
                // set sla to .05% of total number of calls
                var slaData = this.processSLA(v, rec.data.callCount *.005, 0);
                var tooltip = slaData['slaTooltip'].replace('sec ', ' ').replace('the SLA.', 'the SLA error rate of 0.5%'); // quick hack so I can reuse sla function
                return "<div class='" + slaData['slaClass'] + "' data-qtip='"+tooltip+"'>"+v+"</div>";
            }, flex: 1  
        }
    ],

    convertToMb: function(bytes){
        return this.getController().convertToMb(bytes);
    },
    convertToSec: function(ms){
        return this.getController().convertToSec(ms);
    },
    
    processSLA: function(val, sla, errMargin){
        return this.getController().processSLA(val, sla, errMargin);
        
    }

});
