Ext.define('OppUI.view.loadtestDashboard.LoadTestDashboard',{
    extend: 'Ext.panel.Panel',
    xtype: 'loadtest',
    itemId: 'loadtest',

    requires: [
        'OppUI.view.loadtestDashboard.LoadTestDashboardController',
        'OppUI.view.loadtestDashboard.LoadTestDashboardModel'
    ],

    controller: 'loadtest',
    viewModel: {
        type: 'loadtest'
    },

    items: [{
        xtype: 'component',
        itemId: 'stats',
        cls: 'kpi-main kpi-tiles',
        height: 100,

        tpl: [
            '<div class="kpi-meta">',
                '<tpl for=".">',
                    '<span>',
                        '<div>{statistic}</div> {description}',
                    '</span>',
                '</tpl>',
            '</div>'
        ],

        data: [{
            description: 'Total WPT Runs',
            statistic: 546
        },{
            description: 'Number of Apps',
            statistic: 12
        },{
            description: 'Active Tests Per Page',
            statistic: 35
        },{
            description: 'Failures',
            statistic: 15
        },{
            description: 'Passing',
            statistic: 434
        }]
    },{
        xtype: 'loadtestsummarytab',
        closable: false
    }],
    
    config: {
        scrollable: true,
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