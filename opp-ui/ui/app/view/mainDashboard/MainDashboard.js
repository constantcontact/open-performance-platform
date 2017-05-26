
Ext.define('OppUI.view.mainDashboard.MainDashboard',{
    //extend: 'Ext.panel.Panel',
    extend: 'Ext.container.Container',
    alias: 'widget.maindashboard',
    xtype: 'maindashboard',
    itemId: 'maindashboard',

    requires: [
        'OppUI.view.mainDashboard.MainDashboardController',
        'OppUI.view.mainDashboard.MainDashboardModel',
        'Ext.toolbar.Paging'
    ],

    controller: 'maindashboard',
    viewModel: {
        type: 'maindashboard'
    },

    config: {
        activeState: null
    },

    layout: 'responsivecolumn',
    height: Ext.getBody().getViewSize().height,

    items: [
        {
            xtype: 'uxstatswidget',
            containerColor: 'green',
            userCls: 'big-50 small-100'
        },
        {
            xtype: 'loadteststatswidget',
            containerColor: 'magenta',
            userCls: 'big-50 small-100'
        },{
            xtype: 'grid',
            title: 'Last 10 Ux Tests',
            userCls: 'big-50 small-100',
            bind: {
                store: '{uxApplicationFilter}'
            },
            columnLines: true,
            columns: [
                { xtype: 'rownumberer',width: 50, sortable: false }, 
                { text: "Test Date", dataIndex: 'testDate', flex: 1,width: 150,sortable: false, renderer: Ext.util.Format.dateRenderer('n/j/Y g:i A') },
                {
                    tdCls: 'x-grid-cell-topic',
                    text: "Application",
                    dataIndex: 'application',
                    flex: 1,
                    width: 150,
                    sortable: false
                }, {
                    text: "Env",
                    dataIndex: 'environment',
                    flex: 1,
                    width: 70,
                    sortable: false
                }, {
                    text: "Page",
                    dataIndex: 'page',
                    flex: 1,
                    width: 150,
                    sortable: false
                }, {
                    text: "Location",
                    dataIndex: 'location',
                    hidden: true,
                    flex: 1,
                    width: 150,
                    sortable: false
                }, {
                    text: "Browser",
                    dataIndex: 'browser',
                    hidden: true,
                    flex: 1,
                    width: 150,
                    sortable: false
                }, {
                    text: "Connection",
                    dataIndex: 'connection',
                    hidden: true,
                    flex: 1,
                    width: 150,
                    sortable: false
                }, {
                    text: "Misc",
                    dataIndex: 'misc',
                    hidden: true,
                    sortable: false,
                    flex: 1
                }
            ],
            listeners: {
                itemdblclick: 'uxItemSelected'
            },
            //,
            // bbar: {
            //     xtype: 'pagingtoolbar',
            //     displayInfo: true,
            //     displayMsg: 'Displaying topics {0} - {1} of {2}',
            //     emptyMsg: "No topics to display",
            //     pageSize: 5,
            //     store: this.store
            // }
        },{
            xtype: 'grid',
            userCls: 'big-50 small-100',
            title: 'Last 10 Load Tests',
            bind: {
                store: '{loadTestFilter}'
            },
            columnLines: true,
            columns: [{ xtype: 'rownumberer', width: 50, sortable: false },
                {text: 'TestId', dataIndex: "loadTestId", hidden:true, flex: 1},
                {text: 'Test Name', dataIndex:"testName", flex: 1},
                {text: 'Sub Name', dataIndex:"testSubName", hidden:true, flex: 1},
                {text: 'Application', dataIndex: "appUnderTest", flex: 1},
                {text: 'App Version', dataIndex: "appUnderTestVersion", hidden:true, flex: 1},
                {text: 'Comments', dataIndex: "comments", hidden:true, flex: 1},
                {text: 'Description', dataIndex: "description", hidden:true, flex: 1},
                {text: 'Environment', dataIndex: "environment", flex: 1},
                {text: 'Start Time', dataIndex: "startTime", flex: 1, renderer: function(v) { return Ext.Date.format(new Date(v),'m/d/Y H:i a')}},
                {text: 'End Time', dataIndex: "endTime", hidden:true, flex: 1, renderer: function(v) { return Ext.Date.format(new Date(v),'m/d/Y H:i a')}}
            ],
            listeners: {
                itemdblclick: 'loadtestItemSelected'
            }
            //,
            // bbar: {
            //     xtype: 'pagingtoolbar',
            //     displayInfo: true,
            //     displayMsg: 'Displaying topics {0} - {1} of {2}',
            //     emptyMsg: "No topics to display",
            //     pageSize: 5,
            //     store: this.store
            // }
        }
    ]
    
});
