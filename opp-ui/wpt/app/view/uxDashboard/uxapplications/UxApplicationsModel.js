Ext.define('OppUI.view.uxDashboard.uxapplications.UxApplicationsModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.uxapplications',
    data: {
        total: 10,
        totalApps: 20,
        activeTestsPerPage: 30,
        failures: 40,
        passing: 50
    },
    stores: {
        remoteUxApplications: {
            model: 'OppUI.model.uxDashboard.UxApplications',
            autoLoad: true,

            proxy: {
                type: 'ajax',
                //url: 'http://roadrunner.roving.com/uxsvc/v2/rrux/wptNav',
                url: 'https://localhost/uxsvc/v1/wpt/navigation',
                cors: true,
                useDefaultXhrHeader: false,
                reader: {
                    type: 'json'
                }
            },

            listeners: {
                load: 'onRemoteUxApplicationsLoad'
            }
        }
    }
});
