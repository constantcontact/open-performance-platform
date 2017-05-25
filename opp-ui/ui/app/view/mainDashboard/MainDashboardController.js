Ext.define('OppUI.view.mainDashboard.MainDashboardController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.maindashboard',

    init: function (view) {
        view.updateActiveState = this.updateActiveState.bind(this);
    },

    updateActiveState: function(activeState) {
        var refs = this.getReferences();
        var viewModel = this.getViewModel();

        // refs[activeState].setPressed(true);
        // viewModel.set('loadTestData', activeState);

        this.fireEvent('changeroute', this, activeState);
    },

    uxApplicationsLoaded: function(uxApplicationData) {
        var uxApplicationsFilterStore, data;
        
        data = [];
        uxApplicationsFilterStore = this.getView().getViewModel().getStore('uxApplicationFilter');

        for(var i = 0; i < uxApplicationData.getData().items.length; i++) {
            data.push(uxApplicationData.getData().items[i].getData());
        }

        uxApplicationsFilterStore.getProxy().setData(data);
        uxApplicationsFilterStore.load();
    }, 

    loadTestsLoaded: function(loadTestData) {
        var loadTestFilterStore, data;
        
        data = [];
        loadTestFilterStore = this.getView().getViewModel().getStore('loadTestFilter');

        for(var i = 0; i < loadTestData.getData().items.length; i++) {
            data.push(loadTestData.getData().items[i].getData());
        }

        loadTestFilterStore.getProxy().setData(data);
        loadTestFilterStore.load();
    }

});
