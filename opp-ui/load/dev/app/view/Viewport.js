Ext.define('CCPerf.view.Viewport', {
    extend: 'Ext.container.Viewport',
    requires:[
        'Ext.layout.container.Fit',
        'CCPerf.view.Main',
        'CCPerf.view.Jenkins',
        'CCPerf.view.GraphiteMetricView',
        'Ext.selection.CellModel',
        'Ext.grid.plugin.CellEditing'
    ],
    layout: {
        type: 'fit'
    },
    initComponent: function() {
        var view = this.getQueryVar("view");
        if(view === "jenkins" && this.getQueryVar("id") !== ""){
            this.items = [{ xtype: 'app-jenkins' }];
        } else if (view === "jobs") {
        	this.items = [{ xtype: 'app-jobs' }];
        } else if (view === "appmap"){
            this.items = [{ xtype: 'app-map' }];
        } else if (view === "graphite-metrics"){
            this.items = [{ xtype: 'graphite-metric-view' }];
        } else {
            this.items = [{ xtype: 'app-main' }];
        }

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
