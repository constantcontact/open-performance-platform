Ext.define('OppUI.view.uxDashboard.uxtabpanel.UxTabPanelController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.uxtabpanel',

    updateUrlTabState: function(pageName, add) {
        var view, activeState, queryParams, params, paren, pagest, newTabState, initialTabState;
        console.log('updateUrlTabState ==> pageName' + pageName + ' add: ' + add);

        view = this.getView();
        parent = view.up();
        activeState = parent.getActiveState();

        queryParams = activeState.split('?');
        queryParams = queryParams.length > 1 ? queryParams[1] : undefined; 

        console.log('queryParams: ' + queryParams);

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

                    if(pages && pages.length === 1) {
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

        console.log('UpdateUrlTabState ===> activeState: ' + activeState);
        parent.getController().updateActiveState(activeState);
    }

});
