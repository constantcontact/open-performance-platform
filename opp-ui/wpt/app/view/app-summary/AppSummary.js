Ext.define('OppUI.view.app-summary.AppSummary',{
    extend: Ext.grid.Panel,
    alias: 'widget.appsummary',
    requires: [
        'OppUI.store.AppSummary'
    ],
    itemId: 'appsummary',
    controller: 'appsummary',
    viewModel: {
        type: 'appsummary'
    },
    title: 'App Summary List',
    //xtype: 'grid',
    iconCls: 'x-fa fa-users',
    store: {
        type:'appsummary'
    },
    columns: [{
        text: 'First Name',
        dataIndex: 'nm',
        flex: 1
    }, {
        text: 'City',
        dataIndex: 'cty',
        flex: 1
    }, {
        text: 'Home',
        dataIndex: 'hse',
        flex: 1
    }]
});
