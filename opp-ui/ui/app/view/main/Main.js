/**
 * This class is the main view for the application. It is specified in app.js as the
 * "autoCreateViewport" property. That setting automatically applies the "viewport"
 * plugin to promote that instance of this class to the body element.
 */
Ext.define('OppUI.view.main.Main', {
    extend: 'Ext.tab.Panel',
    xtype: 'app-main',

    requires: [
        'OppUI.view.*'
    ],

    controller: 'main',

    viewModel: {
        type: 'main'
    },

    ui: 'navigation',

    tabBarHeaderPosition: 1,
    titleRotation: 0,
    tabRotation: 0,

    header: {
        layout: {
            align: 'stretchmax'
        },
        iconCls: 'fa-bar-chart',
        title: {
            text: 'OPP',
            textAlign: 'center',
            flex: 0,
            minWidth: 160
        },
        tools: [{
            type: 'gear',
            plugins: 'responsive',
            width: 120,
            margin: '0 0 0 0',
            handler: 'onSwitchTool',
            responsiveConfig: {
                'width < 768 && tall': {
                    visible: true
                },
                'width >= 768': {
                    visible: false
                }
            }
        }]
    },

    tabBar: {
        flex: 1,
        layout: {
            align: 'stretch',
            overflowHandler: 'none'
        }
    },

    responsiveConfig: {
        tall: {
            headerPosition: 'top'
        },
        wide: {
            headerPosition: 'left'
        }
    },

    listeners: {
        tabchange: 'onTabChange'
    },

    defaults: {
        tabConfig: {
            plugins: 'responsive',
            responsiveConfig: {
                wide: {
                    iconAlign: 'left',
                    textAlign: 'left',
                    flex: 0
                },
                tall: {
                    iconAlign: 'top',
                    textAlign: 'center',
                    flex: 1
                },
                'width < 768 && tall': {
                    visible: false
                },
                'width >= 768': {
                    visible: true
                }
            }
        }
    },

    items: [{
            // This page has a hidden tab so we can only get here during initialization. This
            // allows us to avoid rendering an initial activeTab only to change it immediately
            // by routing
            xtype: 'component',
            tabConfig: {
                hidden: true
            }
        },
        {
            xtype: 'container',
            title: 'Dashboard',
            iconCls: 'fa-home'
        },
        {
            xtype: 'ux',
            title: 'UX Tests',
            iconCls: 'fa-line-chart'
        }, {
            xtype: 'loadtest',
            title: 'Load Tests',
            iconCls: 'fa-tachometer'
        }, {
            xtype: 'applicationmapping',
            title: 'Application Mapping',
            iconCls: 'fa-list'
        }
    ],

    // This object is a config for the popup menu we present on very small form factors.
    // It is used by our controller (MainController).
    assistiveMenu: {
        items: [{
                height: 50,
                text: 'Dashboard',
                iconCls: 'fa-home'
            },
            {
                height: 50,
                text: 'UX Tests',
                iconCls: 'fa-line-chart'
            }, {
                height: 50,
                text: 'Load Tests',
                iconCls: 'fa-tachometer'
            }, {
                height: 50,
                text: 'Application Mapping',
                iconCls: 'fa-list'
            }
        ],
        listeners: {
            click: 'onMenuClick'
        }
    }
});