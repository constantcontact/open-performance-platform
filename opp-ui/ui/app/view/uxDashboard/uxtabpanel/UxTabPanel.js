
Ext.define('OppUI.view.uxDashboard.uxtabpanel.UxTabPanel',{
    extend: 'Ext.tab.Panel',
    alias: 'widget.uxtabpanel',
    itemId: 'uxtabpanel',

    requires: [
        'OppUI.view.uxDashboard.uxtabpanel.UxTabPanelController',
        'OppUI.view.uxDashboard.uxtabpanel.UxTabPanelModel',
        'Ext.ux.TabReorderer'
    ],

    controller: 'uxtabpanel',
    viewModel: {
        type: 'uxtabpanel'
    },
    config: {
        admin: false
    },

    plugins: 'tabreorderer',

    items: [{
        title: 'Ux Applications',
        xtype: 'uxapplications',
        iconCls: 'x-fa fa-table',
        reorderable: false,
        closable: false,
        scrollable: true,
        layout: 'fit'
    }],

    createTab: function(pageName) {
        this.getController().updateUrlTabState(pageName, true);
    },


    createTabs: function(params) {
        var queryParams, i, j, ages, pageTrendReport, pageIdentifier;

        queryParams = params.split('&');

        if(queryParams.length >= 1) {
            for(i = 0; i < queryParams.length; i++) {
                if(queryParams[i].indexOf('pages=') >= 0) {
                    // ie, pages=l1.campaign-ui.campaigns-morecampaigns.aws-us-east.chrome.cable
                    // the first split will split on the '=', the second
                    // split will get the pages.
                    pages = queryParams[i].split('=')[1].split(',');

                    for(j = 0; j < pages.length; j++) {
                        pageIdentifier = pages[j].split('.').join('');
                        pageTrendReport = this.down('#pagetrendreport-'+pageIdentifier);

                        if(!pageTrendReport) {
                            this.createPageTrendReport(pages[j]);
                        }
                    }

                    break;
                }
            }
        }
    },

    createPageTrendReport: function(pageName) {
        var tab, pageIdentifier, connection;

        connection = pageName.split('.')[5];
        pageIdentifier = pageName.split('.').join('');

        tab = this.add({
                closable: true,
                xtype: 'uxtrendreport',
                itemId: 'pagetrendreport-' + pageIdentifier,
                iconCls: 'x-fa fa-line-chart',
                title: pageName,
                pageName: pageName,
                connection: connection
            }
        );

        this.setActiveTab(tab);
    },

    processAdmin: function(params) {
        if(params.indexOf('user=admin') >= 0) {
            this.setAdmin(true);
        }
    }
});
