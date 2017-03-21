Ext.define('CCPerf.view.Jenkins', {
    extend: 'Ext.container.Container',
    requires:[
        'Ext.tab.Panel',
        'Ext.layout.container.Border'
    ],
    xtype: 'app-jenkins',
    layout: 'fit',
    initComponent: function() {
        this.items = [{ xtype:'report-loadtest', header:false, loadTestId: this.getQueryVar("id") }];
        this.callParent(arguments);
    },
    getQueryVar: function(variable) {
        var query = window.location.search.substring(1);
        var vars = query.split('&');
        for (var i = 0; i < vars.length; i++) {
            var pair = vars[i].split('=');
            if (decodeURIComponent(pair[0]) === variable) {
                return decodeURIComponent(pair[1]);
            }
        }
        return "";
    }
    
});