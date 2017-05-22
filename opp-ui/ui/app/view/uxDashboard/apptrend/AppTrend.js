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
        activeState: null
    },

    layout: {
        type: 'vbox',
        align: 'stretch'
    },

    title: 'App Trend',
    scrollable: 'y',

    minWidth: 600,

    items: [
    {
        xtype: 'wpttrendchart',
        title: 'WPT Trend'
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

    listeners: {
        beforeclose: function(tab) {
            console.log('tab closing ' + tab.getTitle());
            this.up('uxtabpanel').getController().updateUrlTabState(tab.getTitle(), false);
        }
    },

     // needed for routing.
    config: {
        activeState: null
    },

    validStates: {
        dashboard: 1
    },

    isValidState: function(state) {
        return state in this.validStates;
    }
});
