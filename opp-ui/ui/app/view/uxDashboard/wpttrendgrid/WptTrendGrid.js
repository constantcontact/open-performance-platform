
Ext.define('OppUI.view.uxDashboard.wpttrendgrid.WptTrendGrid',{
    extend: 'Ext.grid.Panel',
    alias: 'widget.wpttrendgrid',

    requires: [
        'OppUI.view.uxDashboard.wpttrendgrid.WptTrendGridController',
        'OppUI.view.uxDashboard.wpttrendgrid.WptTrendGridModel'
    ],

    controller: 'wpttrendgrid',
    viewModel: {
        type: 'wpttrendgrid'
    },
    width: 700,
    height: 470,
    

    margin: '0 20 20 20',
    bind: {
        store: '{histogramData}'
    },

    loadMask: true,
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
        dataIndex: 'date',
        renderer: Ext.util.Format.dateRenderer('n/j/Y g:i A'),
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
        dataIndex: 'Connection',
        sortable: false,
        flex: 1
    },
    {
        text: "Result Details",
        dataIndex: 'Pages',
        sortable: false,
        renderer: function renderTopic(value, p, record) {
            var href = 'http://wpt.roving.com/result/'+ record.getData().id;
            return Ext.String.format('<a href="{0}" target="_blank">WebPageTest</a>', href);
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

});
