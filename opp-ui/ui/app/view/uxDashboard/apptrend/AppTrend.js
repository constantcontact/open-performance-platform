Ext.define('OppUI.view.uxDashboard.apptrend.AppTrend',{
    extend: 'Ext.panel.Panel',
    xtype: 'apptrend',
    itemId: 'apptrend',

    requires: [
        'OppUI.store.uxDashboard.AppTrend',
        'OppUI.store.uxDashboard.WptTrendData',
        'Ext.chart.axis.Numeric',
        'Ext.chart.axis.Category',
        'Ext.chart.series.Area',
        'Ext.chart.series.Pie',
        'Ext.chart.interactions.PanZoom',
        'Ext.chart.interactions.Rotate',
        'Ext.toolbar.Paging'
    ],

    config: {
        activeState: null,
        defaultActiveState: 'clicks'
    },

    layout: {
        type: 'vbox',
        align: 'stretch'
    },

    title: 'App Trend',
    scrollable: 'y',

    minWidth: 600,

    items: [{
        xtype: 'container',
        margin: '20px 20px 0px 20px',
        items: [{
            xtype: 'button',
            text: 'Back',
            iconCls: 'x-fa fa-arrow-left',
            handler: function() {
                var wptGridStore;
                wptGridStore = Ext.ComponentQuery.query("#wptsByPageGrid")[0].getStore();
                wptGridStore.proxy.data = [];
                wptGridStore.load();

                this.up('ux').back();
            }
        }]
        
    },{
        xtype: 'cartesian',
        alias: 'wptTrendChart',
        itemId: 'wptTrendChart',
        reference: 'chart',
        width: '100%',
        height: 500,

        margin: '20px 20px 0 20px',

        legend: {
            docked: 'right'
        },
        insetPadding: 40,
        store: {
            type: 'wpttrenddata'
        },
        listeners: {
             itemclick: function(o) {
                 //THIS SERIESINDEX POINTS TO WHICH FIELD WAS SELECTED
                 //IF YOUR FIELDS ARE IN SOME ARRAY THIS WILL WORK NICELY -- I JUST WISH THERE WAS GOOD DOCUMENTATION ON THIS
                 alert ( "Selected: " + o.seriesIndex );   
                var rec = store.getAt(o.index);
             }

        },

        // sprites: [{
        //     type: 'text',
        //     text: 'WPT Trend - Campaign UI',
        //     fontSize: 22,
        //     width: 100,
        //     height: 30,
        //     x: 75, // the sprite x position
        //     y: 20  // the sprite y position
        // }],
        axes: [{
            type: 'numeric',
            fields: ['TTFB', 'VisuallyComplete', 'SpeedIndex'],
            position: 'left',
            grid: true,
            minimum: 0,
            // renderer: function (axis, label, layoutContext) {
            //     return label.toFixed(label < 10 ? 1: 0);
            // },
            title: "Milliseconds"
        }, {
            type: 'time',
            fields: 'wptTimestamp',
            position: 'bottom',
            label: {
                rotate: {
                    degrees: -45
                }
            },
            title: 'Run Date'
        }],
        series: [{
            type: 'line',
            title: 'TTFB',
            xField: 'wptTimestamp',
            yField: 'TTFB',
            marker: {
                type: 'square',
                fx: {
                    duration: 200,
                    easing: 'backOut'
                }
            },
            highlightCfg: {
                scaling: 2
            },
            tooltip: {
                trackMouse: true,
                renderer: function (tooltip, record, item) {
                    var title = item.series.getTitle();

                    if(record) {
                        tooltip.setHtml(title + ' on ' + record.get('timestamp') + ': ' +
                            record.get(item.series.getYField()) + ' (ms)');
                    }
                }
            }
        },{
            type: 'line',
            title: 'Visually Complete',
            xField: 'wptTimestamp',
            yField: 'VisuallyComplete',
            marker: {
                type: 'triangle',
                fx: {
                    duration: 200,
                    easing: 'backOut'
                }
            },
            highlightCfg: {
                scaling: 2
            },
            tooltip: {
                trackMouse: true,
                renderer: function (tooltip, record, item) {
                    var title = item.series.getTitle();

                    if (record) {
                        tooltip.setHtml(title + ' on ' + record.get('timestamp') + ': ' +
                            record.get(item.series.getYField()) + ' (ms)');
                    }
                }
            }
            
        },{
            type: 'line',
            title: 'Speed Index',
            xField: 'wptTimestamp',
            yField: 'SpeedIndex',
            marker: {
                type: 'cross',
                fx: {
                    duration: 200,
                    easing: 'backOut'
                }
            },
            highlightCfg: {
                scaling: 2
            },
            tooltip: {
                trackMouse: true,
                renderer: function (tooltip, record, item) {
                    var title = item.series.getTitle();

                    if (record) {
                        tooltip.setHtml(title + ' on ' + record.get('timestamp') + ': ' +
                            record.get(item.series.getYField()) + ' (ms)');
                    }
                }
            }
        }]
    },{
        xtype: 'container',
        height: 30
    },{
        xtype: 'grid',
        width: 700,
        height: 470,
        alias: 'wptsByPageGrid',
        itemId: 'wptsByPageGrid',

        margin: '0 20 20 20',

        controller: 'apptrend',
        viewModel: {
            type: 'apptrend'
        },
        store: {
            type: 'apptrend'
        },
        title: 'WPT Summary',
        loadMask: true,
        loadViewModelStore: function(wptName) {
            console.log("Loading View Model Store");
            var wptStore = this.getViewModel().getStore('remoteAppTrend');
            wptStore.clearData();
            wptStore.removeAll();
            wptStore.proxy.data = [];
            wptStore.load();

            wptStore.proxy.extraParams = {name : wptName};
            wptStore.load();

            Ext.ComponentQuery.query("#wptTrendChart")[0].setTitle(wptName);
            
        },
        selModel: {
            pruneRemoved: false
        },
        multiSelect: true,
        viewConfig: {
            trackOver: false,
            emptyText: '<h1 style="margin:20px">No matching results</h1>'
        },
        // grid columns
        columns:[
        {
            xtype: 'rownumberer',
            width: 50,
            sortable: false
        },{
            text: "Date",
            dataIndex: 'timestamp',
            width: 175,
            sortable: false
        },{
            text: "Page",
            dataIndex: 'page',
            width: 70,
            sortable: false,
            flex: 1
        },
        {
            text: "TTFB",
            dataIndex: 'TTFB',
            width: 75,
            sortable: false
        },
        {
            text: "Visually Complete (ms)",
            dataIndex: 'VisuallyComplete',
            sortable: false,
            width: 200
        },
        {
            text: "SpeedIndex",
            dataIndex: 'SpeedIndex',
            width: 125,
            sortable: false
        },
        {
            text: "WPT Id",
            dataIndex: 'id',
            sortable: false,
            flex: 1,
            hidden: true
        },
        {
            text: "Connectivity",
            dataIndex: 'connectivity',
            sortable: false,
            flex: 1
        },
        {
            text: "Result Details",
            dataIndex: 'Pages',
            sortable: false,
            renderer: function renderTopic(value, p, record) {
                return Ext.String.format('<a href="{0}" target="_blank">WebPageTest</a>', value);
            },
            flex: 1
        }],
        bbar: {
            xtype: 'pagingtoolbar',
            displayInfo: true,
            displayMsg: 'Displaying topics {0} - {1} of {2}',
            emptyMsg: "No topics to display",
            pageSize: 15,
            store: this.store
        },
        listeners: {
            itemdblclick: function(grid, record, item, index) {
                var win = window.open('http://wpt.roving.com/result/' + record.getData().id, '_blank');
                win.focus();
            }
        }
    }],

     // needed for routing.
    config: {
        activeState: null,
        defaultActiveState: ''
    },

    validStates: {
        dashboard: 1
    },

    isValidState: function(state) {
        return state in this.validStates;
    }
});
