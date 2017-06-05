Ext.define('OppUI.view.uxDashboard.uxtabpanel.UxTabPanelController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.uxtabpanel',

    updateUrlTabState: function(pageName, add) {
        var view, activeState, queryParams, params, paren, pagest, newTabState, initialTabState;

        view = this.getView();
        parent = view.up();
        activeState = parent.getActiveState();

        queryParams = activeState.split('?');
        queryParams = queryParams.length > 1 ? queryParams[1] : undefined; 

        if (!queryParams) {
            activeState = activeState.concat('/?pages='+pageName);
        } else if(queryParams.indexOf('&') >= 0) { 
            params = queryParams.split('&');
            
            // need to find where the pages query param is. 
            for(var i = 0; i < params.length; i++) {
                if(params[i].indexOf('pages=') >= 0) {
                    initialTabState = params[i];
                    if(add) {
                        newTabState = params[i].concat(','+pageName);
                    } else {
                        // ie, pages=l1.campaign-ui.campaigns-morecampaigns.aws-us-east.chrome.cable
                        // the first split will split on the '=', the second
                        // split will get the page name.
                        pages = params[i].split('=')[1].split(',');

                        if(pages.length === 1) {
                            newTabState = '';
                        } else {
                            if(pages[0] === pageName) {
                                newTabState = params[i].replace(pageName+',', '');
                            } else {
                                newTabState = params[i].replace(','+pageName, '');
                            }
                        }
                    }
                    activeState = activeState.replace(initialTabState, newTabState);

                    if(pages && pages.length === 1 && !add) {
                        // also remove the &
                        activeState = activeState.replace('&', '');
                    }
                    break;
                }
            }
        } else {
            if(add) {
                if(queryParams.indexOf('pages=') >= 0) {
                    initialTabState = queryParams;
                    newTabState = queryParams.concat(','+pageName);
                    activeState = activeState.replace(initialTabState, newTabState);
                } else {
                    // at this point there's only a tab query param.
                    activeState = activeState.concat('&pages='+pageName);
                }
            } else {
                if(queryParams.indexOf('pages=') >= 0) {
                    initialTabState = queryParams;
                    pages = queryParams.split('=')[1].split(',');

                    if(pages.length === 1) {
                        newTabState = '';
                    } else {
                        // replace if it's the first tab
                        if(pages[0] === pageName) {
                            newTabState = queryParams.replace(pageName+',', '');
                        } else {
                            newTabState = queryParams.replace(','+pageName, '');
                        }
                    }
                    
                    activeState = activeState.replace(initialTabState, newTabState);

                    if(pages && pages.length === 1) {
                        // also remove the /?
                        activeState = activeState.replace('/?', '');
                    }
                }
            }
        }

        if(!add) {
            // need to set this in order for the routing
            // to work for deleted tabs.
            parent.setActiveState(activeState);
        }
        parent.getController().updateActiveState(activeState);
    },

    createTab: function(pageName) {
        this.updateUrlTabState(pageName, true);
    }, 

    createTabs: function(params) {
        var queryParams, i, j, ages, pageTrendReport, pageIdentifier, duplicateEntries = {}, view = this.getView();

        queryParams = params.split('&');

        if(queryParams.length > 0) {
            for(i = 0; i < queryParams.length; i++) {
                if(queryParams[i].indexOf('pages=') >= 0) {
                    // ie, pages=l1.campaign-ui.campaigns-morecampaigns.aws-us-east.chrome.cable
                    // the first split will split on the '=', the second
                    // split will get the pages.
                    pages = queryParams[i].split('=')[1].split(',');

                    for(j = 0; j < pages.length; j++) {
                        if(duplicateEntries[pages[j]]) {
                            // user tried loading the same report
                            // so just make that report tab active.
                            pageIdentifier = pages[j].split('.').join('');
                            view.setActiveTab(view.down('#pagetrendreport-'+pageIdentifier));
                        } else {
                            duplicateEntries[pages[j]] = 1;

                            pageIdentifier = pages[j].split('.').join('');
                            pageTrendReport = view.down('#pagetrendreport-'+pageIdentifier);

                            if(!pageTrendReport) {
                                this.createPageTrendReport(pages[j]);
                            }
                        }
                    }
                    break;
                }
            }
        }
    },

    createPageTrendReport: function(pageName) {
        var tab, pageIdentifier, connection, view;

        view = this.getView();
        connection = pageName.split('.')[5];
        pageIdentifier = pageName.split('.').join('');

        tab = view.add({
                closable: true,
                xtype: 'uxtrendreport',
                itemId: 'pagetrendreport-' + pageIdentifier,
                iconCls: 'x-fa fa-line-chart',
                title: pageName,
                pageName: pageName
            }
        );

        view.setActiveTab(tab);
    },

    processAdmin: function(params) {
        if(params.indexOf('user=admin') >= 0) {
            this.getView().setAdmin(true);
        }
    }

});
