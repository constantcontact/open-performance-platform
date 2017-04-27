Ext.define('OppUI.view.uxDashboard.UxDashboard',{
    extend: 'Ext.panel.Panel',
    xtype: 'ux',
    itemId: 'ux',
    requires: [
        'Ext.grid.feature.Grouping'
    ],

    layout: 'card',
    items: [
        {
            xtype: 'uxapplications'
        },
        {
            xtype: 'apptrend'
        }
    ],

    listeners: {
        beforerender: function(panel) {
            panel.getLayout().setActiveItem(0);
        }
    },
    back: function() {
        //this.remove(this.getLayout().activeItem);
        this.getLayout().setActiveItem(0);
    },
    wptName: function(wptName) {
        Ext.ComponentQuery.query("#wptsByPageGrid")[0].loadViewModelStore(wptName);
        this.getLayout().setActiveItem(1);
        
        //this.up('panel').getLayout().setActiveItem(1);
    },

    config: {
        activeState: null,
        defaultActiveState: 'dashboard'
    },

    validStates: {
        dashboard: 1
    },

    isValidState: function(state) {
        return state in this.validStates;
    }

});
