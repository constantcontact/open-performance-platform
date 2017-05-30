Ext.define('OppUI.view.loadTestDashboard.loadtestreport.loadtestsla.LoadTestSlaController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.loadtestsla',

    importMissingTransactions: function() {
        var view, aggregateData, i, transactionName, slaStore, slaCounter, msg;

        /**
         * Check if the transaction name exists.
         * @param {String} transactionName 
         */
        function _transactionNameExists(transactionName) {
            var i;
            for(i = 0; i < aggregateData.length; i++) {
                if(transactionName === aggregateData[i].data.name) {
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
            transactionName = aggregateData[i].data.transactionName;
            if(!_transactionNameExists(transactionName)) {
                slaStore.add({
                    loadTestId: view.up('loadtestreport').getLoadTestId(),
                    name: transactionName,
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
