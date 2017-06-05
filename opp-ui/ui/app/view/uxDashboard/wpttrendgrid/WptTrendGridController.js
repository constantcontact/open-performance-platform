Ext.define('OppUI.view.uxDashboard.wpttrendgrid.WptTrendGridController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.wpttrendgrid',

    deleteWptTest: function(grid, rowIndex) {
        var store = grid.getStore(),
            wptId = store.getAt(rowIndex).getId();

        this.deleteRecord(wptId);
    },

    deleteRecord: function(wptId) {
        Ext.Ajax.request({
            url: '/uxsvc/v1/wpt/tests/' + wptId,
            method:'delete',
            scope: this,
            success: function(response){
                this.getView().getStore().reload();
            },
            failure: function(response) {
                Ext.Msg.alert("Error...", "Error processing deletion. Please Try again Later.");
            }
        });
    }
});
