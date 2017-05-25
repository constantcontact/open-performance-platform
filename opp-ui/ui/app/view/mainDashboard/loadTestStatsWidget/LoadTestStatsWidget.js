
Ext.define('OppUI.view.mainDashboard.loadTestStatsWidget.LoadTestStatsWidget',{
    extend: 'Ext.container.Container',
    alias: 'widget.loadteststatswidget',

    requires: [
        'OppUI.view.mainDashboard.loadTestStatsWidget.LoadTestStatsWidgetController',
        'OppUI.view.mainDashboard.loadTestStatsWidget.LoadTestStatsWidgetModel'
    ],

    controller: 'loadteststatswidget',
    viewModel: {
        type: 'loadteststatswidget'
    },

    data: {
        amount: 0,
        type: '',
        icon: ''
    },

    initComponent: function(){
        var me = this;

        Ext.apply(me, {
            cls: me.config.containerColor
        });

        me.callParent(arguments);
    },

    cls:'admin-widget info-card-item info-card-large-wrap shadow',

    height: 280,

    tpl: '<div>'+
            '<span class="x-fa fa-{icon}"></span>'+
            '<h2>{totalLoadTests}</h2>'+
            '<div class="infodiv">{description}</div>'+
         '</div>',
    bind: {
        data: {
            totalLoadTests: '{totalLoadTests}',
            description: 'Total Load Tests',
            icon: 'tachometer'
        }   
    }
});
