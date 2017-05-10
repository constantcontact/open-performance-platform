
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
        itemId: 'stats',
        cls: 'kpi-main kpi-tiles',
        shrinkWrap: false,
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
            description: 'Total Apps',
            statistic: 96
        },{
            description: 'Server Side Apps',
            statistic: 56
        },{
            description: 'Front End Apps',
            statistic: 40
        },{
            description: 'Pipeline Apps',
            statistic: 25
        },{
            description: 'Awesome Apps',
            statistic: 69
        }]
    },{
        xtype: 'panel',
        layout: 'fit',
        items:[
            { xtype: 'applicationmappinggrid' }
        ]
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
