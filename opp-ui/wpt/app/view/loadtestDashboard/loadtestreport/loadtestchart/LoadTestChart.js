
Ext.define('OppUI.view.loadTestDashboard.loadtestreport.loadtestchart.LoadTestChart',{
    extend: 'Ext.chart.CartesianChart',
    alias: 'widget.loadtestchart',

    requires: [
        'OppUI.view.loadTestDashboard.loadtestreport.loadtestchart.LoadTestChartController',
        'OppUI.view.loadTestDashboard.loadtestreport.loadtestchart.LoadTestChartModel'
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
            trackMouse: true, style: 'background: #fff', showDelay: 0, dismissDelay: 0, hideDelay: 0,
            renderer: function (tooltip, record, item) {
                var title;
                if(record && item.series) {
                    title = item.series.config.chart.getTitle();
                    tooltip.setHtml('<b>' + title + ':</b>' + record.get(item.series.config.yField) + '(ms)');
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
    ],

    updateChart: function(container, yaxis, json){    
        var series = json.chart.series;
    
        for(var i=0; i<series.length; i++){
            series[i].style = container.getSeriesStyle();
            series[i].highlight = container.getSeriesHighlight();
            series[i].marker = container.getSeriesMarker();
            series[i].tooltip = container.getSeriesTooltip();
        }

        container.axes[0].fields = json.chart.modelFields.slice(1);
        container.setTitle(json.chart.title);
        container.setSeries(series);
        container.setStore(Ext.create('Ext.data.JsonStore', {
            fields: json.chart.modelFields,
            data: json.chart.data
        }));
    },

    loadChart: function(yaxis, container) {
        var loadTestReport = container.up('loadtestreport');
        
        Ext.Ajax.request({
            url: 'http://roadrunner.roving.com/loadsvc/v1/charts/timeseries/loadtests/'+ loadTestReport.getLoadTestId() + "?yaxis=" + yaxis,
            scope: container,
            disableCaching: false,
            success: function (response) {
                var json = Ext.decode(response.responseText, false);
                // callback to either update or create method
                this.updateChart(container, this.yaxis, json);
            }
        });
    }
});
