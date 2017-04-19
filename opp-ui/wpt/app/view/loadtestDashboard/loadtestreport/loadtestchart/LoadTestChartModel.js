Ext.define('OppUI.view.loadTestDashboard.loadtestreport.loadtestchart.LoadTestChartModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.loadtestchart',
    data: {
        name: 'OppUI'
    },

    stores: {
        remoteChart: {
            model: 'OppUI.model.loadtestDashboard.LoadTestChart',
            autoLoad: true,
            proxy: {
                type: 'ajax',
                url: '',
                reader: {
                    type: 'json',
                    rootProperty: 'chart'
                }
            },
            listeners: {
                load: 'onRemoteChartLoaded'
            }
        }
    }
});
