
Ext.define('OppUI.view.uxDashboard.uxapplications.UxApplications',{
    //extend: 'Ext.grid.Panel',
    extend: 'Ext.panel.Panel',
    xtype: 'uxapplications',
    itemId: 'uxapplications',

    cls: 'kpi-main',

    requires: [
        'OppUI.store.uxDashboard.UxApplications',
        'Ext.grid.filters.Filters',
        'Ext.toolbar.Paging'
    ],

    layout: {
        type: 'vbox',
        align: 'stretch'
    },

    scrollable: 'y',

    minWidth: 600,

    items: [{
        xtype: 'component',
        itemId: 'stats',
        cls: 'kpi-tiles',
        height: 100,

        // bind: {
        //     data: {
        //         total: '{total}',
        //         totalApps: '{totalApps}',
        //         activeTestsPerPage: '{activeTestsPerPage}',
        //         failures: '{failures}',
        //         passing: '{passing}'
        //     }
        // },

        // tpl: [
        //     '<div class="kpi-meta">',
        //         '<span>',
        //             '<div>{total}</div> Total WPTs',
        //         '</span>',
        //     '</div>'
        // ],

        tpl: [
            '<div class="kpi-meta">',
                '<tpl for=".">',
                    '<span>',
                        '<div>{statistic}</div> {description}',
                    '</span>',
                '</tpl>',
            '</div>'
        ],

        data: [{
            description: 'Total WPT Runs',
            statistic: 546
        },{
            description: 'Number of Apps',
            statistic: 12
        },{
            description: 'Active Tests Per Page',
            statistic: 35
        },{
            description: 'Failures',
            statistic: 15
        },{
            description: 'Passing',
            statistic: 434
        }]
    },{
        xtype: 'grid',
        layout: 'fit',
        itemId: 'appGrid',
        controller: 'uxapplications',

        viewModel: {
            type: 'uxapplications'
        },

        store: {
            type: 'uxapplications'
        },
        //bind: '{remoteUxApplications}',
        loadMask: true,

        tbar: [{
            text: 'Applications',
            width: 150,
            textAlign: 'left',
            reference: 'applicationsButton',
            menu: {
                cls: 'pl-option-menu',
                items: []
            }
        },{
            text: 'Pages',
            width: 150,
            textAlign: 'left',
            reference: 'pagesButton',
            menu: {
                cls: 'pl-option-menu',
                items: []
            }
        },{
            text: 'Env',
            width: 150,
            textAlign: 'left',
            reference: 'envsButton',
            menu: {
                cls: 'pl-option-menu',
                items: []
            }
        },'->',{
            width: 400,
            fieldLabel: 'Search',
            labelWidth: 50,
            xtype: 'textfield',
            listeners: {
                specialkey: 'search'
            }
        }],
        columns:[
            
        {
            xtype: 'rownumberer',
            width: 50,
            sortable: false
        },
        
        // {
        //     tdCls: 'x-grid-cell-topic',
        //     text: "Application",
        //     dataIndex: 'app',
        //     flex: 1,
        //     width: 150,
        //     sortable: false
        // },{
        //     text: "Env",
        //     dataIndex: 'env',
        //     flex: 1,
        //     width: 70,
        //     sortable: false
        // },{
        //     text: "Page",
        //     dataIndex: 'page',
        //     flex: 1,
        //     width: 150,
        //     sortable: false
        // },{
        //     text: "Location",
        //     dataIndex: 'location',
        //     flex: 1,
        //     width: 150,
        //     sortable: false
        // },{
        //     text: "Browser",
        //     dataIndex: 'browser',
        //     flex: 1,
        //     width: 150,
        //     sortable: false
        // },{
        //     text: "Connection",
        //     dataIndex: 'connection',
        //     flex: 1,
        //     width: 150,
        //     sortable: false
        // },{
        //     text: "Custom",
        //     dataIndex: 'other',
        //     sortable: false,
        //     flex: 1
        // }

        {
            text: "Test Date",
            dataIndex: 'testDate',
            flex: 1,
            width: 150,
            sortable: false,
            renderer: function(val) {
                return val / 1000;
            }
        },
         {
            tdCls: 'x-grid-cell-topic',
            text: "Application",
            dataIndex: 'application',
            flex: 1,
            width: 150,
            sortable: false
        },{
            text: "Env",
            dataIndex: 'environment',
            flex: 1,
            width: 70,
            sortable: false
        },{
            text: "Page",
            dataIndex: 'page',
            flex: 1,
            width: 150,
            sortable: false
        },{
            text: "Location",
            dataIndex: 'location',
            flex: 1,
            width: 150,
            sortable: false
        },{
            text: "Browser",
            dataIndex: 'browser',
            flex: 1,
            width: 150,
            sortable: false
        },{
            text: "Connection",
            dataIndex: 'connection',
            flex: 1,
            width: 150,
            sortable: false
        },{
            text: "Misc",
            dataIndex: 'misc',
            sortable: false,
            flex: 1
        }
        
        ],

        bbar: {
            xtype: 'pagingtoolbar',
            displayInfo: true,
            displayMsg: 'Displaying topics {0} - {1} of {2}',
            emptyMsg: "No topics to display",
            pageSize: 15,
            store: this.store

            // items: ['-', {
            //     bind: '{expanded ? "Hide Preview" : "Show Preview"}',
            //     pressed: '{expanded}',
            //     enableToggle: true,
            //     toggleHandler: 'onToggleExpanded'
            // }]
        },

        // dockedItems: [{
        //     xtype: 'pagingtoolbar',
        //     // bind: '{localUxApplicationsBinding}',
        //     //store: this.store,
        //     dock: 'bottom',
        //     displayInfo: true
        // }],

        menuItemDefaults: {
            checked: false,
            hideOnClick: true
        },

        listeners: {
            itemdblclick: function(grid, record, item, index) {
                console.log("itemDoubleClick: " + record.getData().name);
                this.up('ux').wptName(record.getData().name);
            }
        }
    }],

    mytest: function() {
        console.log("My test!!!");
    },

    menuItemDefaults: {
        checked: false,
        hideOnClick: true
    },


    listeners: {
        itemdblclick: function(grid, record, item, index) {
            console.log("itemDoubleClick: " + record.getData().name);
            this.up('ux').test();
        }
    }
});
