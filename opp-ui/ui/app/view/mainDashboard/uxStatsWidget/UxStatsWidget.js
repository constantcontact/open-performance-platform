
Ext.define('OppUI.view.mainDashboard.uxStatsWidget.UxStatsWidget',{
    extend: 'Ext.container.Container',
    alias: 'widget.uxstatswidget',

    requires: [
        'OppUI.view.mainDashboard.uxStatsWidget.UxStatsWidgetController',
        'OppUI.view.mainDashboard.uxStatsWidget.UxStatsWidgetModel'
    ],

    controller: 'uxstatswidget',
    viewModel: {
        type: 'uxstatswidget'
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
            '<h2>{totalUxTests}</h2>'+
            '<div class="infodiv">{description}</div>'+
         '</div>',
    bind: {
        data: {
            totalUxTests: '{totalUxTests}',
            description: 'Total Ux Tests',
            icon: 'bar-chart'
        }   
    }
});
