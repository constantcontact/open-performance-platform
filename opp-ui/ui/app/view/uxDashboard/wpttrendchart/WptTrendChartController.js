Ext.define('OppUI.view.uxDashboard.wpttrendchart.WptTrendChartController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.wpttrendchart',

    buttonMetricClicked: function(button) {
        console.log('Button Metric clicked ==>'+ button.getText());

        this.getView().setStore(this.getView().getViewModel().getStore(button.getText()));
        //this.getView().redraw();
    },

    onHistogramDataLoaded : function(histogramData) {
        var view;

        view = this.getView();

        items = histogramData.getData().items;

        

    }

});
