Ext.define('OppUI.view.loadtestDashboard.LoadTestDashboard',{
    extend: 'Ext.tab.Panel',
    xtype: 'loadtest',
    itemId: 'loadtest',

    // width: 600,
    // height: 400,

    controller: 'loadtest',
    viewModel: {
        type: 'loadtest'
    },

    defaults: {
        bodyPadding: 10,
        scrollable: true
    },

    items: [{
        title: 'Application Mapping',
        //html: "My content was added during construction."
        xtype: 'applicationmapping'
    },
    {
        title: 'Load Tests',
        xtype: 'loadtestsummary'
    }
    // , 
    // {
    //     title: 'Ajax Tab 2',
    //     loader: {
    //         url: 'data/tab/ajax2.htm',
    //         contentType: 'html',
    //         loadMask: true,
    //         loadOnRender: true
    //     }
    // }
    
    ],

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