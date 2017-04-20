Ext.define('OppUI.view.loadTestDashboard.loadtestreport.LoadTestReportModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.loadtestreport',
    data: {
        name: 'OppUI'
    },

    stores: {
        remoteAggData: {
            model: 'OppUI.model.loadTestDashboard.LoadTestReportSummary',
            autoLoad: true,
            url: '',
            proxy: {
                type: 'ajax',
                reader: {
                    type: 'json'
                }
            },
            listeners: {
                load: 'remoteAggDataLoaded'
            }
        }
        // remoteSla: {
        //     //model: 'OppUI.model.loadTestDashboard.LoadTestReportSummary',
        //     autoLoad: true,
        //     url: '',
        //     proxy: {
        //         type: 'ajax',
        //         reader: {
        //             type: 'json'
        //         }
        //     }
        // }
    }
});
