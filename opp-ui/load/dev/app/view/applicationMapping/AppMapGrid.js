Ext.define('CCPerf.view.applicationMapping.AppMapGrid', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.app-map-grid',
    frame:true,
    scrollable:'y',
    store: {
        model: 'CCPerf.model.ApplicationMapping',
        autoLoad: true,
        autoSync: true,
        proxy: {
            type: 'rest',
            url: '/loadsvc/v1/applications',
            reader: {
                type: 'json'
            },
            writer: {
                type: 'json'
            }
        },
        write: function(store, operation){
            var record = operation.getRecords()[0],
                name = Ext.String.capitalize(operation.action),
                verb;


            if (name == 'Destroy') {
                verb = 'Destroyed';
            } else {
                verb = name + 'd';
            }
            Ext.example.msg(name, Ext.String.format("{0} user: {1}", verb, record.getId()));

        }
    },
    /*selModel: {
        selType: 'rowmodel'
    },*/
    tbar: [{
        text: 'Add Application',
        itemId: 'btnAddApplication',
        scope: this,
        handler: function (btn) {
            // Create a model instance
            var rec = new CCPerf.model.ApplicationMapping({});
            var grid = btn.up('grid');
            //grid.editingPlugin.cancelEdit();
            grid.store.insert(0, new CCPerf.model.ApplicationMapping());
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
            {text: 'ID', dataIndex: 'id', hidden: true, renderer: function (v, meta, rec) { return rec.phantom ? '' : v; } },
            {text: 'Application Id', dataIndex: 'appKey', allowBlank: false},
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
                        store.remove(context.record);
                    }
                }
            }
        });
        this.plugins = [rowEditing];
        this.height = Ext.getBody().getViewSize().height - 30;
        this.callParent(arguments);

    }
});
