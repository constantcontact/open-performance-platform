Ext.define('OppUI.view.appmapDashboard.ApplicationMappingController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.applicationmapping',

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

    onRemoteAppMapping: function() {

    }
    
});
