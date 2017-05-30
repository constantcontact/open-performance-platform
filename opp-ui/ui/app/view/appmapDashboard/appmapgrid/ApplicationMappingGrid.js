
Ext.define('OppUI.view.appmapDashboard.appmapgrid.ApplicationMappingGrid',{
    extend: 'Ext.grid.Panel',
    alias: 'widget.applicationmappinggrid',

    requires: [
        'OppUI.view.appmapDashboard.appmapgrid.ApplicationMappingGridController',
        'OppUI.view.appmapDashboard.appmapgrid.ApplicationMappingGridModel'
    ],

    controller: 'applicationmappinggrid',
    viewModel: {
        type: 'applicationmappinggrid'
    },

    bind: {
        store : '{remoteAppMapping}'
    },
    initComponent: function () {
        var rowEditing = Ext.create('Ext.grid.plugin.RowEditing', {
            clicksToEdit: 1,
            autoCancel: false,
            listeners: {
                beforeedit: function(editor, context, eOpts){
                    // fix issue where checkbox columns were popping up row editor on edit.  This should not happen.
                    if(context.column.config.xtype === 'checkcolumn'){
                        return false;
                    }
                },
                cancelEdit: function (rowEditing, context) {
                    // Canceling editing of a locally added, unsaved record: remove it
                    if (context.record.phantom) {
                        context.view.up('grid').getStore().remove(context.record);
                    }
                }
            }
        });
        this.plugins = [rowEditing];
        this.height = Ext.getBody().getViewSize().height;
        this.callParent(arguments);
    },

    tbar: [{
        text: 'Add Application',
        itemId: 'btnAddApplication',
        scope: this,
        handler: function (btn) {
            var grid = btn.up('grid');
            grid.getStore().insert(0, new OppUI.model.loadtestDashboard.ApplicationMapping())
            grid.editingPlugin.startEdit(0, 0);
        }
    }],

    columns: {
        defaults: {
            flex: 1,
            editor: {allowBlank: true},
            field: { xtype: 'textfield' }
        },
        items: [
            {text: '#', xtype: 'rownumberer', width: 50, sortable: false },
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
                        return 'x-fa fa-trash'
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
