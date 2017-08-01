Ext.define('OppUI.view.uxDashboard.customtimingchart.CustomTimingChartController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.customtimingchart',
    buttonMetricClicked: function(button) {
        var view = this.getView(),
            store;

        // parent view is listening for this
        this.fireEvent('utMetricChange', button.getText());

    },



});