Ext.define('OppUI.view.uxDashboard.customtimingchart.CustomTimingChartController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.customtimingchart',

    buttonMetricClicked: function(button) {
        var view = this.getView(), store;

        if(button.getText() === 'median') {
            store = view.up('uxtrendreport').getViewModel().getStore('customUserTimingsMedian');
        } else {
            store = view.up('uxtrendreport').getViewModel().getStore('customUserTimingsAverage');
        }

        store.load();
        view.setStore(store);
        view.setTitle('Custom Timings - ' + button.getText());
    }

});
