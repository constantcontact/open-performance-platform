Ext.define('OppUI.view.mainDashboard.MainDashboardModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.maindashboard',
    data: {
        name: 'OppUI',
        totalUxTests: 0,
        totalLostTests: 0
    },

    stores: {
        uxapplications: {
            model: 'OppUI.model.uxDashboard.UxApplications',
            autoLoad: true,

            proxy: {
                type: 'ajax',
                url: 'https://localhost/uxsvc/v1/wpt/navigation',
                reader: {
                    type: 'json'
                }
            },
            listeners: {
                load: 'uxApplicationsLoaded'
            }
        },
        loadtests: {
            model: 'OppUI.model.loadtestDashboard.LoadTestSummary',
            autoLoad: true,

            proxy: {
                type: 'ajax',
                url: 'http://roadrunner.roving.com/loadsvc/v1/loadtests/all/summarytrend',
                reader: {
                    type: 'json'
                }
            },
            listeners: {
                load: 'loadTestsLoaded'
            }
        },

        uxApplicationFilter: {
            model: 'OppUI.model.uxDashboard.UxApplications',
            autoLoad: false,
            pageSize: 10,

            proxy: {
                type: 'memory',
                enablePaging: true,
                reader: {
                    type: 'json'
                }
            }
        },
        loadTestFilter: {
            model: 'OppUI.model.loadtestDashboard.LoadTestSummary',
            autoLoad: false,
            pageSize: 10,
            proxy: {
                type: 'memory',
                enablePaging: true,
                reader: {
                    type: 'json'
                }
            }
        }
    }
});
