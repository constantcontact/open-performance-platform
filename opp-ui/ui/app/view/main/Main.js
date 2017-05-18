Ext.define('OppUI.view.main.Main', {
    extend: 'Ext.container.Viewport',

    requires: [
        'OppUI.view.main.MainController',
        'OppUI.view.main.MainModel',
        'Ext.button.Segmented',
        'Ext.list.Tree'
    ],

    controller: 'main',
    viewModel: 'main',

    cls: 'sencha-dash-viewport',
    itemId: 'mainView',

    layout: {
        type: 'vbox',
        align: 'stretch'
    },

    listeners: {
        render: 'onMainViewRender'
    },

    items: [{
            xtype: 'toolbar',
            cls: 'opp-dash-dash-headerbar shadow',
            height: 64,
            itemId: 'headerBar',
            items: [{
                    xtype: 'component',
                    reference: 'oppLogo',
                    cls: 'opp-logo',
                    html: '<div class="main-logo"><img src="resources/images/opp-logo.png">OPP</div>',
                    width: 250
                },
                {
                    margin: '0 0 0 8',
                    ui: 'header',
                    iconCls: 'x-fa fa-navicon',
                    id: 'main-navigation-btn',
                    handler: 'onToggleNavigationSize',
                    tooltip: 'Toggle Navigation'
                },
                '->',
                {
                    iconCls: 'x-fa fa-home',
                    ui: 'header',
                    href: '#dashboard',
                    hrefTarget: '_self',
                    tooltip: 'Latest Runs'
                },
                {
                    iconCls: 'x-fa fa-bar-chart',
                    ui: 'header',
                    href: '#dashboard',
                    hrefTarget: '_self',
                    tooltip: 'UX Tests'
                },
                {
                    iconCls: 'x-fa fa-tachometer',
                    ui: 'header',
                    href: '#loadtest',
                    hrefTarget: '_self',
                    tooltip: 'Load Tests'
                },
                {
                    iconCls: 'x-fa fa-question',
                    ui: 'header',
                    href: 'https://github.com/constantcontact/open-performance-platform/wiki',
                    hrefTarget: '_blank',
                    tooltip: 'Help / FAQ\'s'
                },
                {
                    xtype: 'tbtext',
                    text: 'Standard User',
                    cls: 'top-user-name'
                },
                {
                    iconCls: 'x-fa fa-user',
                    ui: 'header'
                }
                // show image of person instead --- cool feature
                // {
                //     xtype: 'image',
                //     cls: 'header-right-profile-image',
                //     height: 35,
                //     width: 35,
                //     alt: 'current user image',
                //     src: 'resources/images/user-profile/2.png'
                // }
            ]
        },
        {
            xtype: 'maincontainerwrap',
            id: 'main-view-detail-wrap',
            reference: 'mainContainerWrap',
            flex: 1,
            items: [{
                    xtype: 'treelist',
                    reference: 'navigationTreeList',
                    itemId: 'navigationTreeList',
                    ui: 'navigation',
                    store: 'NavigationTree',
                    width: 250,
                    expanderFirst: false,
                    expanderOnly: false,
                    listeners: {
                        selectionchange: 'onNavigationTreeSelectionChange'
                    }
                },
                {
                    xtype: 'container',
                    flex: 1,
                    reference: 'mainCardPanel',
                    cls: 'sencha-dash-right-main-container',
                    itemId: 'contentPanel',
                    padding: 20,
                    layout: {
                        type: 'card',
                        anchor: '100%'
                    }
                }
            ]
        }
    ]
});