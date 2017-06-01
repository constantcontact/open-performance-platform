
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
                    //     if(me.up().up().yaxis == undefined || me.up().up().yaxis == null) {
                    //         // remove table views for panels that have none
                    //         document.getElementById(me.getId()).style.display = "none";
                    //     } else {
                    //         // Tool tips to switch to table view
                    //         // Ext.tip.QuickTipManager.register({
                    //         //     target: me.getId(),
                    //         //     title: 'Switch to Table View',
                    //         //     text: 'View this graph as a trend table'
                    //         // });
                    //     }
                    }
            },
            handler: function(event, element, view){
                var chart = this.up('loadtestchart');
                var type = chart.getItemId().split('-');
                var yAxis = type[0].indexOf('timeseries') >= 0 ? 
                                chart.getDockedItems()[1].items.items[1].getValue() :
                                type[1];

                if(chart.getStore().getData().items.length > 0) {
                    var window = Ext.create('Ext.window.Window', {
                        layout: 'fit',
                        height: Ext.getBody().getViewSize().height - 100,
                        width: Ext.getBody().getViewSize().width - 100,
                        maximizable: true,
                        title: chart.title,
                        items: [{
                            xtype: 'chartdatagrid',
                            loadTestId: chart.up('loadtestreport').getLoadTestId(),
                            yAxis: yAxis
                        }],
                        // items:[
                        //     Ext.create('OppUI.view.report.LoadTestRawDataChart', {
                        //         loadTestId:chart.loadTestId,
                        //         yaxis:chart.yaxis,
                        //         dataType: chart.dataType
                        //     })
                        // ],
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
                            handler: function(event, element, view){
                                var grid = this.up('window').down('grid');
                                var wiki = grid.getWikiFormat();
                                Ext.Msg.alert('Wiki Markup', wiki);
                            }
                        }, {
                            type: 'save',
                            listeners: {
                                afterrender: function(me) {
                                    // Ext.tip.QuickTipManager.register({
                                    //     target: me.getId(),
                                    //     title: 'Save Data',
                                    //     text: 'Save Data as CSV'
                                    // });
                                }
                            },
                            handler: function(a,b,c) {
                                var grid = this.up('window').down('grid');
                                var csv = grid.getCsv();
                                var link = document.createElement("a");
                                link.download=chart.loadTestId + "_" + chart.yaxis + "_data.csv";
                                link.href='data:text/plain;charset=utf-8,' + encodeURIComponent(csv);
                                link.click();
                            }
                        }]
                    });
                    window.show();
                    //chart.add(window).show();

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
                    Ext.Msg.alert('Error', 'This chart has no raw data');
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
                var parentContainer = this.up().up().up();  // header --> chart --> container
                var chart = view.up('loadtestchart');
                chart.getHeader().hide();
                var window = Ext.create('Ext.window.Window', {
                    layout: 'fit',
                    parentContainer: parentContainer,
                    height: Ext.getBody().getViewSize().height - 100,
                    width: Ext.getBody().getViewSize().width - 100,
                    maximizable: true,
                    title: view.up('loadtestchart').getTitle(),
                    hideCollapseTool: true,
                    items: chart,

                    tools: [{
                        type:'print',
                        handler: function(event, element, view){
                            view.up('window').down('loadtestchart').download();
                        }
                    }],
                    listeners: {
                        beforeclose: function(chart, options) {
                            var child = this.items.items[0];
                            child.getHeader().show();
                            this.parentContainer.add(child);
                        }
                    }
                });

                window.show();
                //window.center();
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
