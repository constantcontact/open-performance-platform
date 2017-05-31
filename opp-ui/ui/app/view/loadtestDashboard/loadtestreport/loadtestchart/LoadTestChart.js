
Ext.define('OppUI.view.loadTestDashboard.loadtestreport.loadtestchart.LoadTestChart',{
    extend: 'Ext.chart.CartesianChart',
    alias: 'widget.loadtestchart',

    requires: [
        'OppUI.view.loadTestDashboard.loadtestreport.loadtestchart.LoadTestChartController',
        'OppUI.view.loadTestDashboard.loadtestreport.loadtestchart.LoadTestChartModel',
        'Ext.chart.plugin.ItemEvents'
    ],

    controller: 'loadtestchart',
    viewModel: {
        type: 'loadtestchart'
    },

    config: {
        seriesStyle: { lineWidth: 4 },
        seriesMarker: { radius: 4 },
        seriesHighlight: {
            fillStyle: '#000', 
            radius: 5, 
            lineWidth: 2, 
            strokeStyle: '#fff'
        },
        seriesTooltip: {
            trackMouse: true, 
            renderer: function (tooltip, record, item) {
                var startTime;
                if(item && record) {
                    // determine if the tooltip is for a timeseries chart
                    // or aggregation chart.
                    startTime = record.data.start_time;
                    if(!startTime) {
                        startTime = window.parseInt(record.data.xaxis);
                    }
                    tooltip.setHtml(item.field + ' on ' + new Date(startTime * 1000) + ': ' +
                        record.get(item.series.getYField()) + ' (ms)');
                }
            }
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
                            // Ext.tip.QuickTipManager.register({
                            //     target: me.getId(),
                            //     title: 'Switch to Table View',
                            //     text: 'View this graph as a trend table'
                            // });
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
                            Ext.create('OppUI.view.report.LoadTestRawDataChart', {
                                loadTestId:cstPanel.loadTestId,
                                yaxis:cstPanel.yaxis,
                                dataType: cstPanel.dataType
                            })
                        ],
                        tools: [ {
                            type:'print',
                            listeners: {
                                afterrender: function(me) {
                                    // Ext.tip.QuickTipManager.register({
                                    //     target: me.getId(),
                                    //     title: 'View as Wiki',
                                    //     text: 'View Wiki Markup for this table'
                                    // });
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

                    // Ext.tip.QuickTipManager.register({
                    //     target: window.tools[2].id,
                    //     title: 'Toggle Maximize',
                    //     text: 'Toggle size of this window'
                    // });
                    // Ext.tip.QuickTipManager.register({
                    //     target: window.tools[3].id,
                    //     title: 'Close',
                    //     text: 'Close this window'
                    // });

                } else {
                    alert("This chart has no raw data");
                }
            }
        },
        {
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
                view.up('loadtestchart').download();
            }
        },
        {
            type:'maximize',
            handler: function(event, element, view){

                var window = Ext.create('Ext.window.Window', {
                    layout: 'fit',
                    height: Ext.getBody().getViewSize().height - 100,
                    width: Ext.getBody().getViewSize().width - 100,
                    maximizable: true,
                    items: view.up('loadtestchart').getItems(),

                    tools: [{
                        type:'print',
                        handler: function(event, element, view){
                            view.up('loadtestchart').download();
                        }
                    }]
                    // listeners: {
                    //     beforeclose: function(panel, opts){
                    //         view.up('loadtestreport').add(this.items);
                    //     }
                    // }
                });

                // add the window to the parent loadTestReport
                // so the window will share the viewmodel.
                view.up('loadtestreport').add(window).showAt();
            }
        }] 
    },

    plugins: {
        ptype: 'chartitemevents'
        // moveEvents: true
    },

    listeners: {
        itemdblclick: function(series, item, event, eOpts ) {
            // only create a tab if the user is dblclicking
            // on aggregation load test chart.
            if(this.getItemId().indexOf('trend-') >= 0) {
                this.up('loadtest')
                    .getController()
                    .updateUrlTabState(item.record.id, true)
            }
        }
    },

    legend: {
        docked: 'right'
    },

    axes: [
        {
            type: 'numeric',
            minimum: 0,
            position: 'left',
            title: 'Response Time (msec)', // gets overridden with yaxisTitle
            grid: true
        }, {
            type: 'category',
            position: 'bottom',
            title: 'Run Date', // gets overridden with xaxisTitle
            renderer: function (o, value) {
                if (value.length > 20) {
                    this.font = "10px Arial, Helvetica, sans-serif";
                }
                //return parseInt(value) * 1000;
                return Ext.Date.format(new Date(parseInt(value) * 1000), 'm/d/y');
            }
        }
    ]
});
