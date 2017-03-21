Ext.define('CCPerf.view.test.AdvancedReportView', {
    extend: 'Ext.window.Window',
    alias: 'widget.testAdvancedReport',
    title: 'Create Advanced Report',
    layout: 'fit',
    autoHeight:true,
    autoShow: true,

    initComponent: function() {
        this.items = [
            {
                xtype: 'form',
                id: 'formAdvancedReporting',
                frame: true,
                bodyPadding: '5 5 0',
                fieldDefaults: {
                    msgTarget: 'side',
                    labelWidth: 100
                },
                defaultType: 'textfield',
                items: [
                    {
                        fieldLabel: 'Test Run Ids',
                        afterLabelTextTpl: CCPerf.util.Globals.forms.required,
                        name: 'val',
                        allowBlank: false,
                        tooltip: 'Enter the test run ids here',
                        listeners: {
                            afterrender: function(elem) {
                                val = "";
                                grid = Ext.ComponentQuery.query('gridpanel')[0];
                                for(i=0; i<grid.getSelectionModel().selected.items.length; i++){
                                    if(val !== "") val = val + ",";
                                    val = val + grid.getSelectionModel().selected.items[i].data.run;
                                }
                                this.setValue(val);
                            }
                        }
                    },{
                        xtype: 'combobox',
                        name: 'type',
                        fieldLabel: 'Report Type',
                        store: Ext.create('Ext.data.Store',{
                            fields: ['name', 'val'],
                            //data : [{"name":"compare", "value":"compare"}, {"name":"Auto Trend", "value":"autotrend"}]
                            data : [{"name":"compare"},{"name":"autotrend"}]
                        }),
                        value:'compare',
                        displayField: 'name',
                        valueField: 'name',
                        forceSelection:true,
                        typeAhead: true,
                        mode: 'local',
                        triggerAction: 'all',
                        selectOnFocus:true
                        
                    }, {
                        fieldLabel: 'X-Axis',
                        name: 'xaxis',
                        tooltip: 'Enter the column name you would like to use for the x-axis in the chart'
                    },{
                        fieldLabel: 'X-Axis Title',
                        name: 'xaxisTitle',
                        tooltip: "Enter your title for the x-axis"
                    },{
                        xtype: 'combobox',
                        name: 'sort',
                        fieldLabel: 'Sort',
                        store: Ext.create('Ext.data.Store',{
                            fields: ['name'],
                            data : [{"name":"ASC"}, {"name":"DESC"}] 
                        }),
                        value:'ASC',
                        displayField: 'name',
                        valueField: 'name',
                        forceSelection:true,
                        typeAhead: true,
                        mode: 'local',
                        triggerAction: 'all',
                        selectOnFocus:true
                    }, {
                        fieldLabel: 'Order By',
                        name: 'orderby',
                        tooltip: 'Enter the field you would like to order the data by'
                    }
                ]
            }
        ];
        this.buttons =  [{
                    id:'btnCreateAdvancedReport',
                    text: 'Create Report'
                },{
                    text: 'Cancel',
                    handler: function() {
                        this.up('window').close();
                    }
                }];

        

        this.callParent(arguments);
    }
});