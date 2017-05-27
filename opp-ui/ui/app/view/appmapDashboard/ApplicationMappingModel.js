Ext.define('OppUI.view.appmapDashboard.ApplicationMappingModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.applicationmapping',

    stores: {
        remoteAppMapping: {
            model: 'OppUI.model.loadtestDashboard.ApplicationMapping',
            autoLoad: true,
            autoSync: true,
            proxy: {
                type: 'ajax',
                url: '/loadsvc/v1/applications',
                reader: {
                    type: 'json'
                },
                writer: {
                    type: 'json'
                }
            },
            listeners: {
                load: 'onRemoteAppMapping'
            }
        }
    }
});
