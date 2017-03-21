Ext.define('CCPerf.view.graphiteMetric.GraphiteMetricGrid', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.graphite-metric-grid',
    frame:true,
    scrollable:'y',
    store: {
        model: 'CCPerf.model.GraphiteMetricModel',
        autoLoad: true,
        autoSync: true,
        proxy: {
            type: 'rest',
            url: '/loadsvc/v1/graphitemetrics',
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
        text: 'Add Metric',
        itemId: 'btnAddMetric',
        scope: this,
        handler: function (btn) {
            // Create a model instance
            var rec = new CCPerf.model.GraphiteMetricModel({});
            var grid = btn.up('grid');
            //grid.editingPlugin.cancelEdit();
            grid.store.insert(0, new CCPerf.model.GraphiteMetricModel());
            grid.editingPlugin.startEdit(0, 0);
        }
    },
        {
            text: 'Refresh',
            itemId: 'btnRefresh',
            scope: this,
            handler: function (btn) {
                var grid = btn.up('grid').getStore().reload();
            }
        }],
    initComponent: function () {
        var rowEditing = Ext.create('Ext.grid.plugin.RowEditing', {
            clicksToEdit: 1,
            autoCancel: false
        });
        this.plugins = [rowEditing];
        this.height = Ext.getBody().getViewSize().height - 30;


        var applicationsStore = Ext.create('Ext.data.Store', {
            model:'CCPerf.model.ApplicationMapping',
            proxy: {
                type: 'ajax',
                url: '/loadsvc/v1/applications',
                reader: {
                    type: 'json'
                }
            },
            autoLoad: true
        });


        this.columns = {
            defaults: {
                editor: {allowBlank: false}
            },
            items: [
                {text: 'ID', dataIndex: 'graphiteId', hidden: true, renderer: function (v, meta, rec) { return rec.phantom ? '' : v; }, editor: { allowBlank: true } },
                {text: 'App Key', flex:1, dataIndex: 'appKey', editor: { xtype:'combobox', store: applicationsStore, queryMode: 'local', displayField: 'appKey', valueField: 'appKey',
                    listeners: {
                        select: function(combo, recs, opts){
                            // set the value of application id field
                            Ext.ComponentQuery.query("#col-appl-id")[0].getEditor().setValue(recs.getData().id);
                        }
                    }
                }},
                {text: 'Application Id', dataIndex: 'applicationId', width:150, id:"col-appl-id", allowBlank: false, editor: { readOnly:true }},
                {text: 'Name', dataIndex: 'name', flex:2},
                {text: 'Path', dataIndex: 'graphitePath', flex:2},
                {
                    xtype: 'actioncolumn',
                    width: 60,
                    sortable: false,
                    menuDisabled: true,
                    items: [{
                        getClass: function () {
                            return 'icon-delete'
                        },
                        tooltip: 'Delete graphite metric',
                        scope: this,
                        handler: function (grid, rowIndex) {
                            grid.getStore().removeAt(rowIndex);
                        }
                    }]
                }
            ]
        };




        this.callParent(arguments);
    }
});
