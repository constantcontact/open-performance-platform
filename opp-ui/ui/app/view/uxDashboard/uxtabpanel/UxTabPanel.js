
Ext.define('OppUI.view.uxDashboard.uxtabpanel.UxTabPanel',{
    extend: 'Ext.tab.Panel',
    alias: 'widget.uxtabpanel',

    requires: [
        'OppUI.view.uxDashboard.uxtabpanel.UxTabPanelController',
        'OppUI.view.uxDashboard.uxtabpanel.UxTabPanelModel',
        'Ext.ux.TabReorderer'
    ],

    controller: 'uxtabpanel',
    viewModel: {
        type: 'uxtabpanel'
    },

    plugins: 'tabreorderer',

    items: [{
        title: 'Ux Applications',
        xtype: 'uxapplications',
        iconCls: 'x-fa fa-table',
        reorderable: false,
        closable: false,
        scrollable: true,
        layout: 'fit'
    }],

});
