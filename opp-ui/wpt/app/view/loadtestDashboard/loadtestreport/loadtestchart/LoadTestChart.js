
Ext.define('OppUI.view.loadTestDashboard.loadtestreport.loadtestchart.LoadTestChart',{
    extend: 'Ext.chart.CartesianChart',
    alias: 'widget.loadtestchart',

    requires: [
        'OppUI.view.loadTestDashboard.loadtestreport.loadtestchart.LoadTestChartController',
        'OppUI.view.loadTestDashboard.loadtestreport.loadtestchart.LoadTestChartModel'
    ],

    config: {
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
        }] 
    },

    controller: 'loadtestchart',
    viewModel: {
        type: 'loadtestchart'
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
            fields: ['xaxis'],
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
