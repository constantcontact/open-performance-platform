
Ext.define('OppUI.view.appmapDashboard.ApplicationMapping',{
    extend: 'Ext.panel.Panel',
    alias: 'widget.applicationmapping',
    xtype: 'applicationmapping',
    itemId: 'applicationmapping',

    requires: [
        'OppUI.view.appmapDashboard.ApplicationMappingController',
        'OppUI.view.appmapDashboard.ApplicationMappingModel'
    ],

    controller: 'applicationmapping',
    viewModel: {
        type: 'applicationmapping'
    },
    items: [{
        xtype: 'panel',
        items:[{ 
            xtype: 'applicationmappinggrid',
            layout: 'fit'
        }],
        title: 'Application Mapping'
    }],
   
    config: {
        activeState: null
        //defaultActiveState: 'dashboard'
    },

    validStates: {
        dashboard: 1
    },

    isValidState: function(state) {
        return state in this.validStates;
    },

    processQueryParams: function() {
        
    }
});
