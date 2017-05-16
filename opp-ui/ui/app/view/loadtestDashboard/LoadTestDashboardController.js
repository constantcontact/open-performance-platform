Ext.define('OppUI.view.loadtestDashboard.LoadTestDashboardController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.loadtest',

    init: function (view) {
        console.log('LoadTestDashboard Init viewController!');
        view.updateActiveState = this.updateActiveState.bind(this);
    },

    updateActiveState: function(activeState) {
        var refs = this.getReferences();
        var viewModel = this.getViewModel();

        console.log('LoadTestDashboard Update Active State: ' + activeState);

        this.fireEvent('changeroute', this, 'loadtest/' + activeState);
    },

    updateUrlTabState: function(testId, add) {
        var loadTestIds, queryParams, params, initialTabState, newTabState, activeState;

        activeState = this.getView().getActiveState();
        queryParams = activeState.split('?');
        queryParams = queryParams.length > 1 ? queryParams[1] : undefined; 

        console.log('queryParams: ' + queryParams);

        if (!queryParams) {
            activeState = activeState.concat('/?tab='+testId);
        } else if(queryParams.indexOf('&') >= 0) {
            params = queryParams.split('&');
            
            // need to find where the tab query param is. 
            for(var i = 0; i < params.length; i++) {
                if(params[i].indexOf('tab=') >= 0) {
                    initialTabState = params[i];
                    if(add) {
                        newTabState = params[i].concat(','+testId);
                    } else {
                        // ie, tab=1234,5678
                        // the first split will split on the '=', the second
                        // split will get the loadtestids.
                        loadTestIds = params[i].split('=')[1].split(',');

                        if(loadTestIds.length === 1) {
                            newTabState = '';
                        } else {
                            if(loadTestIds[0] === testId) {
                                newTabState = params[i].replace(testId+',', '');
                            } else {
                                newTabState = params[i].replace(','+testId, '');
                            }
                            
                        }
                    }
                    activeState = activeState.replace(initialTabState, newTabState);

                    if(loadTestIds && loadTestIds.length === 1) {
                        // also remove the &
                        activeState = activeState.replace('&', '');
                    }
                    break;
                }
            }
        } else {
            if(add) {
                if(queryParams.indexOf('tab=') >= 0) {
                    initialTabState = queryParams;
                    newTabState = queryParams.concat(','+testId);
                    activeState = activeState.replace(initialTabState, newTabState);
                } else {
                    // at this point there's only a tabGroup query param.
                    activeState = activeState.concat('&tab='+testId);
                }
            } else {
                if(queryParams.indexOf('tab=') >= 0) {
                    initialTabState = queryParams;
                    loadTestIds = queryParams.split('=')[1].split(',');

                    if(loadTestIds.length === 1) {
                        newTabState = '';
                    } else {
                        // replace if it's the first tab
                        if(loadTestIds[0] === testId) {
                            newTabState = queryParams.replace(testId+',', '');
                        } else {
                            newTabState = queryParams.replace(','+testId, '');
                        }
                    }
                    
                    activeState = activeState.replace(initialTabState, newTabState);

                    if(loadTestIds && loadTestIds.length === 1) {
                        // also remove the /?
                        activeState = activeState.replace('/?', '');
                    }
                }
            }
        }
        console.log('initialTabState: '+initialTabState);
        console.log('newTabState: '+newTabState);
        console.log('activeState: '+activeState);

        this.updateActiveState(activeState);
    },

    updateUrlGroupTabState: function(groupTab, add) {
        var activeState, queryParams, groupTabs;

        activeState = this.getView().getActiveState();
        queryParams = activeState.split('?');
        queryParams = queryParams.length > 1 ? queryParams[1] : undefined; 
    }
    
});
