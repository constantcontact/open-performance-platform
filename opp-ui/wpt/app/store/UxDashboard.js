Ext.define('OppUI.store.UxDashboard', {
    extend: 'Ext.data.Store',
    //extend: 'Ext.data.BufferedStore',
    alias: 'store.uxdashboard',
    model: 'OppUI.model.UxDashboard',
    autoLoad: true,
    remoteFilter: false,

    proxy: {
        type: 'ajax',
        render: {
            type: 'json'
        },
        url: 'http://roadrunner.roving.com/uxsvc/v2/rrux/wptNav'
    },

    listeners: {
        load: function(store, records, success, operation) {
            var reader = store.getProxy().getReader();
            var response = operation.getResponse();

            //console.log("Response: " + response.responseText);


            
        },
        exception: function(proxy, response, operation) {
            Ext.MessageBox.show({
                title: 'Remote Exception',
                msg: operation.getError(),
                icon: Ext.MessageBox.ERROR,
                buttons: Ext.Msg.OK
            })
        }
    }

});