Ext.define('OppUI.view.loadtestDashboard.loadtestsummary.groupreport.LoadTestGroupReportModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.loadtestgroupreport',
    data: {
        name: 'OppUI'
    },

    stores: {
        groupReport: {
            model: 'OppUI.model.loadtestDashboard.LoadTestGroup',
            autoLoad: true,
            url: '',
            proxy: {
                type: 'ajax',
                reader: {
                    type: 'json'
                }
            }
        }
    }

});
