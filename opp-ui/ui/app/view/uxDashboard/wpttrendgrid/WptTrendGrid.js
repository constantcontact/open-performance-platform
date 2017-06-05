
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

    initComponent: function() {
        this.callParent(arguments);
        var admin = this.up('uxtabpanel').getAdmin();
        
        if(admin) {
            this.columns[9].hidden = false;
        }
    },
    width: 700,
    height: 470,
    
    margin: '0 20 20 20',
    bind: {
        store: '{wptTrendTable}'
    },

    selModel: {
        pruneRemoved: false
    },
    multiSelect: true,
    viewConfig: {
        trackOver: false,
        emptyText: '<h1 style="margin:20px">No matching results</h1>'
    },
    title: 'WPT Summary',
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
        dataIndex: 'Page',
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
        width: 75,
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
        dataIndex: 'SummaryURL',
        sortable: false,
        renderer: function renderTopic(value, p, record) {
            return Ext.String.format('<a href="{0}" target="_blank">WebPageTest</a>', record.getData().summaryUrl);
        },
        flex: 1
    },
    {
        xtype: 'actioncolumn',
        width: 30,
        sortable: false,
        menuDisabled: true,
        hidden: true,
        items: [{
            getClass: function () {
                return 'x-fa fa-trash'
            },
            tooltip: 'Delete WPT Data',
            scope: this,
            handler: function (grid, rowIndex) {
                grid.up('wpttrendgrid').getController().deleteWptTest(grid, rowIndex);
            }
        }]
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
