
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
        console.log('Creating tab for pagename: ' + pageName);
        this.getController().updateUrlTabState(pageName, true);
    },


    createTabs: function(params) {
        var queryParams, i, j, ages, pageTrendReport, pageIdentifier;
        
        console.log("Creating tabs for UX Applications: " + params);

        queryParams = params.split('&');
        console.log('queryParams count: ' + i);

        if(queryParams.length >= 1) {
            for(i = 0; i < queryParams.length; i++) {
                if(queryParams[i].indexOf('pages=') >= 0) {
                    // ie, pages=l1.campaign-ui.campaigns-morecampaigns.aws-us-east.chrome.cable
                    // the first split will split on the '=', the second
                    // split will get the pages.
                    pages = queryParams[i].split('=')[1].split(',');

                    for(j = 0; j < pages.length; j++) {
                        pageIdentifier = pages[j].split('.').join('');
                        console.log('Page Identifier: ' + pageIdentifier);
                        pageTrendReport = this.down('#pagetrendreport-'+pageIdentifier);

                        if(!pageTrendReport) {
                            console.log('Creating new Page Trending Report for ' + pages[j]);
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
                connection: connection,
                scrollable: false
            }
        );

        this.setActiveTab(tab);
    },

    processAdmin: function(params) {
        console.log("Processing Admin for UX Applications: " + params); 
    }
});
