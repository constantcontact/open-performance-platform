Ext.define('CCPerf.view.report.LoadTestSla', {
    extend: 'Ext.grid.Panel',
    alias:'widget.load-test-sla',
    itemdId:'slaGrid',
    //store:'ReportLoadSlaStore',
    xtype: 'cell-editing',
    height:'300',
    autoScroll:true,
    loadTestId:'',
    requires:['Ext.window.Toast'],
    tbar: [ {
             xtype:'button',
             iconCls: 'icon-download',
             itemId: 'btnImportMissingTransactions',
             text: 'Import Missing Transactions',
             tooltip:'Adds the current transaction(s) to the SLA list'
         },
         {
             xtype:'button',
             iconCls: 'icon-save',
             text: 'Save All Changes',
             tooltip:'Saves all SLA changes',
             scope:this,
             handler: function(btn){
                 btn.up('gridpanel').getStore().sync({
                     scope:btn,
                     success: function(batch, opts) {
                         this.up('gridpanel').getStore().reload(); // need to do this to load all the correct ids that are missing for new transactions
                         CCPerf.util.Globals.reports.sla.dirty = true;
                     },
                     failure: function(batch, opts) {
                         alert('failed');
                     }
                 });
             }
         },
         {
             xtype:'button',
             iconCls: 'icon-refresh',
             text: 'Refresh SLAs',
             tooltip:'Gets the current saved SLAs',
             scope:this,
             handler: function(btn){
                 btn.up('gridpanel').getStore().reload();
             }
         },
         {
             xtype:'button',
             iconCls: 'icon-refresh',
             text: 'Apply to Similar Tests',
             tooltip:'Will apply this SLA group to all transactions',
             scope:this,
             handler: function(btn){
                 Ext.MessageBox.confirm('Confirm', 'You are about to apply this SLA grouping to all similar tests.  Are you sure you want to continue?',
                 function(btn){
                     if(btn === "yes") {
                        Ext.MessageBox.alert('Coming soon...', 'This feature is coming soon.');
                    }
                 });

             }
         },
         {
             xtype:'button',
             iconCls: 'icon-close',
             text: 'Close SLAs',
             tooltip:'Closes the SLA Panel',
             scope:this,
             handler: function(btn){
                 btn.up('gridpanel').up('panel').collapse();
             }
         },
         '-'
    ],
    initComponent: function() {
        this.cellEditing = new Ext.grid.plugin.CellEditing({ clicksToEdit: 1 });
        this.defaultEditor = {
            xtype: 'numberfield',
            allowBlank: true,
            minValue: 0,
            maxValue: 99999999
        };

        this.plugins = [this.cellEditing];



        this.store = new Ext.data.Store( {
            extend: 'Ext.data.Store',
            autoLoad: true, // TODO: need to figure out why when i remove this, the sla popup doesn't work.  The store is getting loading twice.  The afterlayout event is loading it as well.
          //  require: ['CCPerf.model.slaModel'],
            model: 'CCPerf.model.SlaModel',
            sortOnLoad: true,
            sorters: { property: 'name', direction : 'ASC' }
        });

        this.store.model.proxy.api.read = '/loadsvc/v1/loadtests/' + this.loadTestId + '/slas/';
        this.store.model.proxy.api.create = '/loadsvc/v1/loadtests/' + this.loadTestId + '/slas/';
        this.store.model.proxy.api.update = '/loadsvc/v1/slas/';


	    this.columns = [
            { header: 'Transaction Name', dataIndex: 'name', flex: 2 },
            { header: 'Min (ms)', dataIndex: 'min', flex: 1, editor: this.defaultEditor },
            { header: 'Max (ms)', dataIndex: 'max', flex: 1, editor: this.defaultEditor },
            { header: 'Avg (ms)', dataIndex: 'avg', flex: 1, editor: this.defaultEditor },
            { header: 'Median (ms)', dataIndex: 'median', flex: 1, editor: this.defaultEditor },
            { header: '75th PCT (ms)', dataIndex: 'pct75', flex: 1, editor: this.defaultEditor },
            { header: '90th PCT (ms)', dataIndex: 'pct90', flex: 1, editor: this.defaultEditor },
            { header: 'Margin of Error (%)', dataIndex: 'marginOfError', flex: 1, editor: this.defaultEditor }
        ];

        // wait till after layout before loading the store
        this.on('afterlayout', this.loadStore, this, {
            delay: 1,
            single: true
        });

        this.callParent(arguments);
    },

   loadStore: function() {
        this.getStore().load({
            // store loading is asynchronous, use a load listener or callback to handle results
            callback: this.onStoreLoad,
            scope:this
        });
    },

    onStoreLoad: function(){
      /*  Ext.Msg.show({
            title: 'Store Load Callback',
            msg: 'store was loaded, data available for processing',
            icon: Ext.Msg.INFO,
            buttons: Ext.Msg.OK
        });
         */
        var missingTransBtn = this.down('button');
        missingTransBtn.setVisible(true);
        missingTransBtn.on('click', this.addSLAs, this);

        if(this.getStore().data.length === 0){
            var slaPanel = this.up('panel');
            if(slaPanel.collapsed) {
                 var slaAlertWin = Ext.create('Ext.window.Window', {
                    title: 'SLAs Not Set', height: 120, width: 300,
                    layout: { align: 'middle', pack: 'center', type: 'vbox' },
                    items: [
                        { padding: 10, html: 'You have no SLAs currently set for this test.' },
                        { itemId: 'btnSetSLAs', xtype: 'button', text: 'Set SLAs',
                            handler: function(){
                                var curWindow = this.up('window');
                                slaPanel.expand();
                                // wait for expand to finish to rows will be imported properly
                                // TODO: i hate doing this, but 500ms seems to be enough.  It would be great if the expand function had an easy callback i could use here
                                setTimeout(function() {
                                    var btnImport = slaPanel.down('#btnImportMissingTransactions');
                                    btnImport.fireEvent('click', btnImport);
                                    curWindow.close();
                                }, 500);
                        }
                    }],
                    listeners: {
                        afterrender: function(win) {
                            setTimeout(function() {
                                if(win) {
                                    win.close();
                                }
                            }, 10000);
                        }
                    }
                });

               // slaAlertWin.alignTo(Ext.getBody(), "tr-tr?", [-10, 10]);
                //slaAlertWin.show();

            }
        } else {
            // populate the group id if it doesn't already exist
            if(this.down('toolbar').down('[xtype=tbtext]') === null) {
                this.down('toolbar').add({xtype:'tbtext', text:'SLA Group Id: ' + this.getStore().getAt(0).data.groupId });
           }
        }

    },
    transactionNameExists: function(transactionName){
    	var inStore = false;
    	var len = this.getStore().data.items.length;
    	for(var i=0; i<len; i++){
    		if(transactionName == this.getStore().data.items[i].data.name){
    			return true;
    		}
    	}
		return false; // not found in store
    },
    addSLAs: function(btn){
    	var slaCounter = 0;
    	// TODO: fix this... this might not work with more than 1 tab.  the tabpanel could be any tab.  I need to go up to the tab and not the panel
	    var aggregateData = this.up('report-loadtest').down('grid-loadtest-aggregates').getStore().data.items;


        for(i=0; i<aggregateData.length; i++){
            var transName = aggregateData[i].data.transactionName;

			// if it doesn't already exist in the SLA store, add it
			if(!this.transactionNameExists(transName)){
                this.getStore().add({
                    loadTestId: this.loadTestId,
                    name: transName,
                    min: '',
                    max: '',
                    avg: '',
                    median: '',
                    pct75: '',
                    pct90: '',
                    marginOfError: '0'
                });
                slaCounter++;
            }
		}
		var msg = (slaCounter > 0) ? 'Found ' + slaCounter + ' new SLAs' : 'No new SLAs found';
		Ext.toast({ html: msg, width: 200, align: 't', closable:false, header:false });
        //this.cellEditing.startEditByPosition({  row: 0, column: 1 });

       // btn.hide();
    },
    onRemoveClick: function(grid, rowIndex){
        this.getStore().removeAt(rowIndex);
    }
});
