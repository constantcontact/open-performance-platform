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
                    // at this point there's only a tab query param.
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

    updateUrlGroupTabState: function(groupReportMetaData, add) {
        var i, activeState, queryParams, queryParam, initialGroupTabQueryParams, 
            groupReportQueryParams, groupReport, groupReportName, groupReportFilters;

        activeState = this.getView().getActiveState();
        queryParams = activeState.split('?');
        initialQueryParams = queryParams.length > 1 ? queryParams[1] : undefined;

        groupReportName = groupReportMetaData.name;
        groupReportFilters = groupReportMetaData.filters;

        // build the query param
        queryParam = groupReportName + ':';
        for(var prop in groupReportFilters) {
            if(groupReportFilters.hasOwnProperty(prop)) {
                queryParam += (prop + '+' + groupReportFilters[prop] + ',')
            }
        }
        // remove the last comma.
        queryParam = queryParam.slice(0, -1);

        if(!initialQueryParams) {
            activeState = activeState.concat('/?groupTab='+queryParam);
        } else if(initialQueryParams.indexOf('&') >= 0) {
             queryParams = initialQueryParams.split('&');

            for(i = 0; i < queryParams.length; i++) {
                if(queryParams[i].indexOf('groupTab=') >= 0) {
                    initialGroupTabQueryParams = queryParams[i];
                    break;
                }
            }

            if(initialGroupTabQueryParams) {
                if(add) {
                    groupReportQueryParams = initialGroupTabQueryParams.concat('::'+queryParam);
                    activeState = activeState.replace(initialGroupTabQueryParams, groupReportQueryParams);
                } else {
                    // ie groupTab=visitor:app_under_test+visitor,environment+s1::visitor2:app_under_test+visitor,environment+s1
                    groupReportQueryParams = initialGroupTabQueryParams.split('=')[1].split('::');

                    for(i = 0; i < groupReportQueryParams.length; i++) {
                        if(groupReportQueryParams[i].indexOf(groupReportName+ ':') >= 0) {
                            console.log('Removing ==> name:' + groupReportName + ' queryParamValue: ' + groupReportQueryParams[i]);

                            groupReport = groupReportQueryParams[i];
                            console.log('Removing tab: ' + groupReport);
                            break;
                        }
                    }

                    if(groupReport) {
                        // if its the first
                        if(groupReportQueryParams[0].indexOf(groupReportName + ':') >= 0) {

                            if(groupReportQueryParams.length > 1) {
                                activeState = activeState.replace(groupReport+'::', '');
                            } else {
                                activeState = activeState.replace(groupReport, '');
                            }
                        } else {
                            activeState = activeState.replace('::'+groupReport, '');
                        }

                        if(groupReportQueryParams.length === 1) {
                            // if there's only one group report then remove the query param
                            console.log('Remove the whole thing')

                            if(initialQueryParams.split('&')[0].indexOf('groupTab=') >= 0) {
                                activeState = activeState.replace('groupTab=&', '');
                            } else {
                                activeState = activeState.replace('&groupTab=', '');
                            }
                        }
                        console.log('Active State: ' + activeState);
                    }
                }
            } else {
                queryParam = groupReportName + ':';

                // build the query param
                for(var prop in groupReportFilters) {
                    if(groupReportFilters.hasOwnProperty(prop)) {
                        queryParam += (prop + '+' + groupReportFilters[prop] + ',')
                    }
                }
                // remove the last comma.
                queryParam = queryParam.slice(0, -1);

                activeState = activeState.concat('&groupTab='+queryParam);
            }
        } else {
            // at this point there's a query param, need to find out what it is.
            if(add) {
                if(initialQueryParams.indexOf('groupTab=') >= 0) {
                    queryParams = initialQueryParams.concat('::'+queryParam);
                    activeState = activeState.replace(initialQueryParams, queryParams);
                } else{
                    activeState = activeState.concat('&groupTab='+queryParam);
                }
            } else {
                if(initialQueryParams.indexOf('groupTab=') >= 0) {
                    queryParams = initialQueryParams.split('=')[1].split('::');

                    if(queryParams.length === 1) {
                        activeState = activeState.replace('/?'+initialQueryParams, '');
                    } else {
                        // check if it's the first group report
                        if(queryParams[0].indexOf(groupReportName+':') >= 0) {
                            activeState = activeState.replace(queryParam+'::', '');
                        } else {
                            activeState = activeState.replace('::'+queryParam, ''); 
                        }
                    }
                }
            }
        }


        this.updateActiveState(activeState);
    }
    
});
