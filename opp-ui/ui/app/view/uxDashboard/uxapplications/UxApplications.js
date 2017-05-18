Ext.define('OppUI.view.uxDashboard.uxapplications.UxApplications', {
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
    title: 'User Experience Tests',

    minWidth: 600,

    items: [{
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
        }, {
            text: 'Pages',
            width: 150,
            textAlign: 'left',
            reference: 'pagesButton',
            menu: {
                cls: 'pl-option-menu',
                items: []
            }
        }, {
            text: 'Env',
            width: 150,
            textAlign: 'left',
            reference: 'envsButton',
            menu: {
                cls: 'pl-option-menu',
                items: []
            }
        }, '->', {
            width: 400,
            fieldLabel: 'Search',
            labelWidth: 50,
            xtype: 'textfield',
            listeners: {
                specialkey: 'search'
            }
        }],
        columns: [

            {
                xtype: 'rownumberer',
                width: 50,
                sortable: false
            }, {
                text: "Test Date",
                dataIndex: 'testDate',
                flex: 1,
                width: 150,
                sortable: false,
                renderer: Ext.util.Format.dateRenderer('n/j/Y g:i A')
            }, {
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
                flex: 1,
                width: 150,
                sortable: false
            }, {
                text: "Browser",
                dataIndex: 'browser',
                flex: 1,
                width: 150,
                sortable: false
            }, {
                text: "Connection",
                dataIndex: 'connection',
                flex: 1,
                width: 150,
                sortable: false
            }, {
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
        },

        menuItemDefaults: {
            checked: false,
            hideOnClick: true
        },

        listeners: {
            itemdblclick: function(grid, record, item, index) {
                this.up('ux').wptName(record.getData().full);
            }
        }
    }],

    menuItemDefaults: {
        checked: false,
        hideOnClick: true
    },


    listeners: {
        itemdblclick: function(grid, record, item, index) {
            this.up('ux').test();
        }
    }
});