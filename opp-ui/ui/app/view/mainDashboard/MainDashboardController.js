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
        var uxApplicationsFilterStore, data, totalUxTests;
        
        data = [];
        uxApplicationsFilterStore = this.getView().getViewModel().getStore('uxApplicationFilter');
        totalUxTests = uxApplicationData.getData().items.length;

        this.getViewModel().set('totalUxTests', totalUxTests);

        for(var i = 0; i < totalUxTests; i++) {
            data.push(uxApplicationData.getData().items[i].getData());
        }

        uxApplicationsFilterStore.getProxy().setData(data);
        uxApplicationsFilterStore.load();
    }, 

    loadTestsLoaded: function(loadTestData) {
        var loadTestFilterStore, data, totalLoadTests;
        
        data = [];
        loadTestFilterStore = this.getView().getViewModel().getStore('loadTestFilter');
        totalLoadTests = loadTestData.getData().items.length;

        this.getViewModel().set('totalLoadTests', totalLoadTests);

        for(var i = 0; i < loadTestData.getData().items.length; i++) {
            data.push(loadTestData.getData().items[i].getData());
        }

        loadTestFilterStore.getProxy().setData(data);
        loadTestFilterStore.load();
    },

    uxItemSelected: function(grid, record, domElement, index) {
        this.fireEvent('changeroute', this, 'ux/?pages=' + record.getData().full);
    },

    loadtestItemSelected: function(grid, record, domElement, index) {
        console.log(record.getData().loadTestId);
        this.fireEvent('changeroute', this, 'loadtest/?tab=' + record.getData().loadTestId);
    }
});
