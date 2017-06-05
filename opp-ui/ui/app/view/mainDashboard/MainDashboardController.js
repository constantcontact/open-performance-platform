Ext.define('OppUI.view.mainDashboard.MainDashboardController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.maindashboard',

    init: function (view) {
        view.updateActiveState = this.updateActiveState.bind(this);
    },

    updateActiveState: function(activeState) {
        var refs = this.getReferences();
        var viewModel = this.getViewModel();

        // refs[activeState].setPressed(true);
        // viewModel.set('loadTestData', activeState);

        this.fireEvent('changeroute', this, activeState);
    },

    uxApplicationsLoaded: function(uxApplicationData) {
        var uxApplicationsFilterStore, data, totalUxTests;
        
        data = [];
        uxApplicationsFilterStore = this.getView().getViewModel().getStore('uxApplicationFilter');
        totalUxTests = uxApplicationData.getData().items.length;

        this.getViewModel().set('totalUxTests', totalUxTests);

        for(var i = 0; i < totalUxTests; i++) {
            data.push(uxApplicationData.getData().items[i].getData());
        }

        uxApplicationsFilterStore.getProxy().setData(data);
        uxApplicationsFilterStore.load();
    }, 

    loadTestsLoaded: function(loadTestData) {
        var loadTestFilterStore, data, totalLoadTests;
        
        data = [];
        loadTestFilterStore = this.getView().getViewModel().getStore('loadTestFilter');
        totalLoadTests = loadTestData.getData().items.length;

        this.getViewModel().set('totalLoadTests', totalLoadTests);

        for(var i = 0; i < loadTestData.getData().items.length; i++) {
            data.push(loadTestData.getData().items[i].getData());
        }

        loadTestFilterStore.getProxy().setData(data);
        loadTestFilterStore.load();
    },

    uxItemSelected: function(grid, record, domElement, index) {
        var uxDashboard = Ext.ComponentQuery.query('ux')[0],
            fullPageName = record.getData().full,
            activeState, queryParams, i, newState;

        if(uxDashboard) {
            activeState = uxDashboard.getActiveState();
            queryParams = activeState.split('?');

            if(queryParams.length > 1) {
                queryParams = queryParams[1];
                queryParams = activeState.split('&');

                for(i = 0; i < queryParams.length; i++) {
                    if(queryParams[i].indexOf('pages=') >= 0) {
                        newState = queryParams[i].concat(','+fullPageName);
                        activeState = activeState.replace(queryParams[i], newState);
                        break;
                    }
                }

                if(i === queryParams.length) {
                    if(queryParams.length > 0) {
                        // there is no pages query param
                        activeState = activeState.concat('&pages=' + fullPageName);
                    } else {
                        // there are no query params
                        activeState = 'ux/?pages=' + fullPageName;
                    }
                }
            }
        } else {
            activeState = 'ux/?pages=' + fullPageName;
        }

        this.fireEvent('changeroute', this, activeState);
    },

    loadtestItemSelected: function(grid, record, domElement, index) {
        var loadTestDashboard = Ext.ComponentQuery.query('loadtest')[0],
            loadTestId = record.getData().loadTestId,
            queryParams, newTabState, i, activeState;

        if(loadTestDashboard) {
            activeState = loadTestDashboard.getActiveState();
            queryParams = activeState.split('?');

            if(queryParams.length > 1) {
                queryParams = queryParams[1];
                queryParams = queryParams.split('&');

                // find the tab query param
                for(i = 0; i < queryParams.length; i++) {
                    if(queryParams[i].indexOf('tab=') >= 0) {
                        newTabState = queryParams[i].concat(','+loadTestId);
                        activeState = activeState.replace(queryParams[i], newTabState);
                        break;
                    }
                }

                if(i === queryParams.length) {
                    if(queryParams.length > 0) {
                        // there is no tab query param
                        activeState = activeState.concat('&tab='+loadTestId);
                    } else {
                        // there are no query params
                        activeState = 'loadtest/?tab=' + loadTestId;
                    }   
                } 
            } else {
                activeState = 'loadtest/?tab=' + loadTestId;
            }
        } else {
            activeState = 'loadtest/?tab=' + loadTestId;
        }
        this.fireEvent('changeroute', this, activeState);
    }
});
