Ext.define('OppUI.view.loadtestDashboard.LoadTestDashboardController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.loadtest',

    init: function (view) {
        view.updateActiveState = this.updateActiveState.bind(this);
    },

    updateActiveState: function(activeState) {
        var refs = this.getReferences();
        var viewModel = this.getViewModel();

        // refs[activeState].setPressed(true);
        // viewModel.set('loadTestData', activeState);

        this.fireEvent('changeroute', this, 'loadtest/' + activeState);
    }
    
});
