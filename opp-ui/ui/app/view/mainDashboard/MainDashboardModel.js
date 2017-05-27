Ext.define('OppUI.view.mainDashboard.MainDashboardModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.maindashboard',
    data: {
        name: 'OppUI',
        totalUxTests: 0,
        totalLoadTests: 0
    },

    stores: {
        uxapplications: {
            model: 'OppUI.model.uxDashboard.UxApplications',
            autoLoad: true,

            proxy: {
                type: 'ajax',
                url: '/uxsvc/v1/wpt/navigation',
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
                url: '/loadsvc/v1/loadtesttrends/summarytrend',
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
