Ext.define('OppUI.store.NavigationTree', {
    extend: 'Ext.data.TreeStore',

    storeId: 'NavigationTree',

    fields: [{
        name: 'text'
    }],

    root: {
        expanded: true,
        children: [{
                text: 'Dashboard',
                iconCls: 'x-fa fa-home',
                rowCls: 'nav-tree-badge nav-tree-badge-new',
                viewType: 'dashboard',
                leaf: true
            },
            {
                text: 'UX Tests',
                iconCls: 'x-fa fa-bar-chart',
                rowCls: 'nav-tree-badge nav-tree-badge-new',
                viewType: 'ux',
                leaf: true
            },
            {
                text: 'Load Tests',
                iconCls: 'x-fa fa-tachometer',
                rowCls: 'nav-tree-badge nav-tree-badge-hot',
                viewType: 'loadtest',
                leaf: true
            },
            {
                text: 'Application Mapping',
                iconCls: 'x-fa fa-list',
                viewType: 'applicationmapping',
                leaf: true
            }
        ]
    }
});