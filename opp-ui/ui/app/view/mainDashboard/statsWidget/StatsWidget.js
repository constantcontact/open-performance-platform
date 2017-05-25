
Ext.define('OppUI.view.mainDashboard.statsWidget.StatsWidget',{
    extend: 'Ext.container.Container',
    alias: 'widget.statswidget',

    requires: [
        'OppUI.view.mainDashboard.statsWidget.StatsWidgetController',
        'OppUI.view.mainDashboard.statsWidget.StatsWidgetModel'
    ],

    controller: 'statswidget',
    viewModel: {
        type: 'statswidget'
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
            '<h2>{amount}</h2>'+
            '<div class="infodiv">{type}</div>'+
         '</div>'

});
