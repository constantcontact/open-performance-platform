Ext.define('OppUI.view.loadTestDashboard.loadtestreport.loadtestsla.LoadTestSlaController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.loadtestsla',

    importMissingTransactions: function() {
        var view, aggregateData, i, transName, slaStore, slaCounter, msg;

        /**
         * Check if the transaction name exists.
         * @param {String} transName 
         */
        function _transactionNameExists(transName) {
            var i;
            for(i = 0; i < aggregateData.length; i++) {
                if(transName === aggregateData[i].data.name) {
                    return true;
                }
            }
            return false;
        }

        slaCounter = 0;
        view = this.getView();
        aggregateData = view.up('loadtestreport')
            .getViewModel()
            .getStore('remoteAggData').data.items;
        slaStore = view.up('loadtestreport').getViewModel().getStore('remoteSlas');

        for(i = 0; i < aggregateData.length; i++) {
            transName = aggregateData[i].data.transaction_name;
            if(!_transactionNameExists(transName)) {
                slaStore.add({
                    loadTestId: view.up('loadtestreport').getLoadTestId(),
                    name: transName,
                    min: '',
                    max: '',
                    avg: '',
                    median: '',
                    pct75: '',
                    pct90: '',
                    margin_of_error: '0'
                });

                slaCounter++;
           }
        }

        msg = (slaCounter > 0) ? 'Found ' + slaCounter + ' new SLAs' : 'No new SLAs found';
		Ext.toast({ html: msg, width: 200, align: 't', closable:false, header:false });

    }
});
