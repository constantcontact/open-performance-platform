Ext.define('OppUI.view.loadTestDashboard.loadtestreport.loadtestsla.LoadTestSla', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.loadtestsla',

    requires: [
        'OppUI.view.loadTestDashboard.loadtestreport.loadtestsla.LoadTestSlaController',
        'OppUI.view.loadTestDashboard.loadtestreport.loadtestsla.LoadTestSlaModel',
        'Ext.window.Toast'
    ],

    controller: 'loadtestsla',
    viewModel: {
        type: 'loadtestsla'
    },

    bind: {
        store: '{remoteSlas}'
    },

    plugins: {
        ptype: 'cellediting',
        clicksToEdit: 1
    },
    selModel: 'cellmodel',

    tbar: [{
            xtype: 'button',
            iconCls: 'x-fa fa-download',
            itemId: 'btnImportMissingTransactions',
            text: 'Import Missing Transactions',
            tooltip: 'Adds the current transaction(s) to the SLA list',
            listeners: {
                click: 'importMissingTransactions'
            }
        },
        {
            xtype: 'button',
            iconCls: 'x-fa fa-floppy-o',
            text: 'Save All Changes',
            tooltip: 'Saves all SLA changes',
            handler: function(btn) {
                btn.up('loadtestreport').getViewModel().getStore('remoteSlas').sync({
                    scope: btn,
                    success: function(batch, opts) {
                        this.up('loadtestreport').getViewModel().getStore('remoteSlas').reload(); // need to do this to load all the correct ids that are missing for new transactions
                    },
                    failure: function(batch, opts) {
                        Ext.Msg.alert('Error:', 'Failed Saving All Changes.');
                    }
                });
            }
        },
        {
            xtype: 'button',
            iconCls: 'x-fa fa-refresh',
            text: 'Refresh SLAs',
            tooltip: 'Gets the current saved SLAs',
            handler: function(btn) {
                btn.up('loadtestreport')
                    .getViewModel()
                    .getStore('remoteSlas')
                    .reload();
            }
        },
        {
            xtype: 'button',
            iconCls: 'x-fa fa-check-square-o',
            text: 'Apply to Similar Tests',
            tooltip: 'Will apply this SLA group to all transactions',
            handler: function(btn) {
                Ext.MessageBox.confirm('Confirm', 'You are about to apply this SLA grouping to all similar tests.  Are you sure you want to continue?',
                    function(btn) {
                        if (btn === "yes") {
                            Ext.MessageBox.alert('Coming soon...', 'This feature is coming soon.');
                        }
                    });

            }
        },
        {
            xtype: 'button',
            iconCls: 'x-fa fa-close',
            text: 'Close SLAs',
            tooltip: 'Closes the SLA Panel',
            handler: function(btn) {
                btn.up('loadtestsla').collapse();
            }
        },
        '-'
    ],
    initComponent: function() {
        // TDOO: For some reason I could't get this to work
        // with the columns. Commenting out for now.
        // this.defaultEditor = {
        //     xtype: 'numberfield',
        //     allowBlank: true,
        //     minValue: 0,
        //     maxValue: 99999999
        // }

        this.callParent(arguments);

        var loadTestId = this.up('loadtestreport').getLoadTestId();

        this.up('loadtestreport').getViewModel().getStore('remoteSlas').proxy.api.read = '/loadsvc/v1/loadtests/' + loadTestId + '/slas';
        this.up('loadtestreport').getViewModel().getStore('remoteSlas').proxy.api.create = '/loadsvc/v1/slas_bulk_import/' + loadTestId;
        this.up('loadtestreport').getViewModel().getStore('remoteSlas').proxy.api.update = '/loadsvc/v1/slas_bulk_update/' + loadTestId;
    },

    columns: [
        { header: 'Transaction Name', dataIndex: 'name', flex: 2 },
        { header: 'Min (ms)', dataIndex: 'min', flex: 1, editor: 'numberfield' },
        { header: 'Max (ms)', dataIndex: 'max', flex: 1, editor: 'numberfield' },
        { header: 'Avg (ms)', dataIndex: 'avg', flex: 1, editor: 'numberfield' },
        { header: 'Median (ms)', dataIndex: 'median', flex: 1, editor: 'numberfield' },
        { header: '75th PCT (ms)', dataIndex: 'pct75', flex: 1, editor: 'numberfield' },
        { header: '90th PCT (ms)', dataIndex: 'pct90', flex: 1, editor: 'numberfield' },
        { header: 'Margin of Error (%)', dataIndex: 'margin_of_error', flex: 1, editor: 'numberfield' }
    ],

    importMissingTransactions: 'importMissingTransactions'
});