Ext.define('OppUI.view.ux-dashboard.UxDashboardController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.uxdashboard',

    init: function(view) {
        view.updateActiveState = this.updateActiveState.bind(this);
    },

    updateActiveState: function(activeState) {
        var refs = this.getReferences();
        var viewModel = this.getViewModel();

        this.fireEvent('changeroute', this, 'ux/' + activeState);
    }
});
