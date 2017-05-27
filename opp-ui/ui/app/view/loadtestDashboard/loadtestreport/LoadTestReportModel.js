Ext.define('OppUI.view.loadTestDashboard.loadtestreport.LoadTestReportModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.loadtestreport',
    data: {
        loadTestId: 0,
        testName: '',
        testSubName: '',
        vuserCount: 0,
        environment: '',
        appUnderTest: '',
        appUnderTestVersion: '',
        startTime: 0,
        endTime: 0,
        testTool: '',
        testToolVersion: '',
        description: '',
        comments: ''
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
            }
        },
        remoteLoadTestInfo: {
            model: 'OppUI.model.loadtestDashboard.LoadTestInfo',
            autoLoad: true,
            url: '',
            proxy: {
                type: 'ajax',
                reader: {
                    type: 'json'
                }
            },
            listeners: {
                load: 'remoteLoadTestInfoLoaded'
            }
        },
        remoteSlas: {
            model: 'OppUI.model.loadtestDashboard.LoadTestSla',
            autoLoad: true,
            url: '',
            sortOnLoad: true,
            sorters: { property: 'name', direction : 'ASC' },
            proxy: {
                type: 'ajax',
                reader: {
                    type: 'json'
                }
            },
            listeners: {
                load: 'remoteSlasLoaded'
            }
        }
    }
});
