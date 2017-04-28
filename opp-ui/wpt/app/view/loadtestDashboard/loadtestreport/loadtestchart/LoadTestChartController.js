Ext.define('OppUI.view.loadTestDashboard.loadtestreport.loadtestchart.LoadTestChartController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.loadtestchart',

    onRemoteChartLoaded: function(remoteData) {
        var view, items;

        view = this.getView();
        view.setTitle('90th Percentile Response Time During Test');

        view.setSeries(remoteData.getData().items[0].getData().series);
        // Ext.apply(view, {series: remoteData.getData().items[0].getData().series});

        view.setStore(Ext.create('Ext.data.JsonStore', {
            fields: remoteData.getData().items[0].getData().modelFields,
            data: remoteData.getData().items[0].getData().data
        }));

        // Ext.apply(view, { store: Ext.create('Ext.data.JsonStore', {
        //         fields: remoteData.getData().items[0].getData().modelFields,
        //         data: remoteData.getData().items[0].getData().data
        //     })
        // });

    }
});
