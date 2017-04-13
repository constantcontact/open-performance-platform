Ext.define('OppUI.view.appmapDashboard.ApplicationMappingModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.applicationmapping',

    stores: {
        remoteAppMapping: {
            model: 'OppUI.model.loadtestDashboard.ApplicationMapping',
            autoLoad: true,
            proxy: {
                type: 'ajax',
                //url: 'http://localhost:8888/loadsvc/v1/applications',
                url: 'https://appmap-svc.ctct.net/v1/applications',
                reader: {
                    type: 'json'
                }
            },
            listeners: {
                load: 'onRemoteAppMapping'
            }
        }
    }
});
