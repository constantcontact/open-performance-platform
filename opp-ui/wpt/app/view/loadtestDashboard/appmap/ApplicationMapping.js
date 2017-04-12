
Ext.define('OppUI.view.loadtestDashboard.appmap.ApplicationMapping',{
    extend: 'Ext.grid.Panel',
    alias: 'widget.application-mapping-grid',
    xtype: 'applicationmapping',

    requires: [
        'OppUI.view.loadtestDashboard.appmap.ApplicationMappingController',
        'OppUI.view.loadtestDashboard.appmap.ApplicationMappingModel'
    ],

    controller: 'applicationmapping',
    viewModel: {
        type: 'applicationmapping'
    },

    //store: {},
    bind: {
        store : '{remoteAppMapping}'
    },

    columns: {
        defaults: {
            flex: 1,
            editor: {allowBlank: true},
            field: { xtype: 'textfield' }
        },
        items: [
            {text: 'ID', dataIndex: 'id', hidden: true, renderer: function (v, meta, rec) { return rec.phantom ? '' : v; } },
            {text: 'Application Name', dataIndex: 'appKey', allowBlank: false},
            // {text: 'Team Name', dataIndex: 'teamName'},
            // {text: 'Repo', dataIndex: 'repo'},
            {text: 'NewRelic', dataIndex: 'newrelic'},
            {text: 'AppDynamics', dataIndex: 'appdynamics'},
            {text: 'WebPageTest', dataIndex: 'webpagetest'},
            {text: 'CodeCoverage', dataIndex: 'codeCoverageId'},
            {text: 'SecurityId', dataIndex: 'securityId'},
            {text: 'RegressionResultsIndex', dataIndex: 'regressionResultsId'},
            {text: 'kqiAppName', dataIndex: 'kqiAppName'},
            {text: 'Splunk', dataIndex: 'splunk'},
            {text: 'Dynatrace', dataIndex: 'dynatrace'},
            {text: 'TeamName', dataIndex: 'teamName'},
            {text: 'Client Side?', dataIndex: 'isClientSide', xtype: 'checkcolumn', editor: { xtype: 'checkbox', cls: 'x-grid-checkheader-editor' }},
            {text: 'Server Side?', dataIndex: 'isServerSide', xtype: 'checkcolumn', editor: { xtype: 'checkbox', cls: 'x-grid-checkheader-editor' }},
            {text: 'CD Pipeline Client?', dataIndex: 'inCdPipelineClient', xtype: 'checkcolumn', editor: { xtype: 'checkbox', cls: 'x-grid-checkheader-editor' }},
            {text: 'CD Pipeline Server?', dataIndex: 'inCdPipelineServer', xtype: 'checkcolumn', editor: { xtype: 'checkbox', cls: 'x-grid-checkheader-editor' }},
            {
                xtype: 'actioncolumn',
                width: 30,
                sortable: false,
                menuDisabled: true,
                items: [{
                    getClass: function () {
                        return 'icon-delete'
                    },
                    tooltip: 'Delete application mapping',
                    scope: this,
                    handler: function (grid, rowIndex) {
                        grid.getStore().removeAt(rowIndex);
                    }
                }]
            }
        ]
    }
});
