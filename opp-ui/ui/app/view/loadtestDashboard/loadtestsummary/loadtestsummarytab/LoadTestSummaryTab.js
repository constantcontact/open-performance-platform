
Ext.define('OppUI.view.loadtestDashboard.loadtestsummary.loadtestsummarytab.LoadTestSummaryTab',{
    extend: 'Ext.tab.Panel',
    alias: 'widget.loadtestsummarytab',
    cls: 'loadtest-summary-tab',

    requires: [
        'OppUI.view.loadtestDashboard.loadtestsummary.loadtestsummarytab.LoadTestSummaryTabController',
        'OppUI.view.loadtestDashboard.loadtestsummary.loadtestsummarytab.LoadTestSummaryTabModel',
        'Ext.ux.TabReorderer'
    ],

    controller: 'loadtestsummarytab',
    viewModel: {
        type: 'loadtestsummarytab'
    },

    plugins: 'tabreorderer',
    height: Ext.getBody().getViewSize().height,

    items: [{
        title: 'Load Tests',
        xtype: 'loadtestsummary',
        iconCls: 'x-fa fa-table',
        reorderable: false,
        closable: false
    }],

    createTab: function(grid, record, item, index) {
        this.up('loadtest')
            .getController()
            .updateUrlTabState(record.getData().loadTestId, true);
    },

    createGroupReport: function(groupReport) {
        this.up('loadtest')
            .getController()
            .updateUrlGroupTabState(groupReport, true);
    },

    createGroupReportTab: function(groupReportName, filters) {
        var tab, reportLink, queryParam;

        // build the query param
        queryParam = groupReportName + ':';
        for(var prop in filters) {
            if(filters.hasOwnProperty(prop)) {
                queryParam += (prop + '+' + filters[prop] + ',')
            }
        }
        // remove the last comma.
        queryParam = queryParam.slice(0, -1);
        reportLink = window.location.origin+'/#!loadtest/?groupTab='+queryParam;

        tab = this.add({
            closable: true,
            xtype: 'loadtestgroupreport',
            itemId: 'loadtestgroupreport-'+ groupReportName,
            iconCls: 'x-fa fa-line-chart',
            title: groupReportName,
            filters: filters,
            reportLink: reportLink
        });

        this.setActiveTab(tab);
    },

    createLoadTestReport: function(loadTestId) {
        var tab;

        tab = this.add({
                closable: true,
                xtype: 'loadtestreport',
                itemId: 'loadtestreport-' + loadTestId,
                iconCls: 'x-fa fa-line-chart',
                loadTestId: loadTestId,
                title: 'Test Run #' + loadTestId,
                width: Ext.getBody().getViewSize().width
            }
        );

        this.setActiveTab(tab);
    },


    createTabs: function(params) {
        var tabIndex, tabGroupIndex, i, j, k, key, value, both, queryParams, 
            loadTestIds, loadTestReport, groupReports, groupReport, groupFilters, 
            filters, filterList, existingGroupReport;

        // check if both query params are set.
        both = params.indexOf('&') >= 0 ? true : false;

        if(both) {
            queryParams = params.split('&');

            for(i = 0; i < queryParams.length; i++) {
                if(queryParams[i].indexOf('tab=') >= 0) {
                    // ie, tab=1234,5678
                    // the first split will split on the '=', the second
                    // split will get the loadtestids.
                    loadTestIds = queryParams[i].split('=')[1].split(',');

                    for(j = 0; j < loadTestIds.length; j++) {
                        loadTestReport = this.down('#loadtestreport-'+loadTestIds[j]);

                        if(!loadTestReport) {
                            this.createLoadTestReport(loadTestIds[j])
                        }
                    }
                } else if (queryParams[i].indexOf('groupTab=') >= 0) {
                    // it must be a groupTab query param
                    groupReports = queryParams[i].split('=')[1].split('::');

                    for(j = 0; j < groupReports.length; j++) {
                        filters = {};
                        groupReport = groupReports[j].split(':');
                        groupFilters = groupReport[1].split(',');
                        existingGroupReport = this.down('#loadtestgroupreport-' + groupReport[0]);

                        if(!existingGroupReport) {
                            for(k = 0; k < groupFilters.length; k++) {
                                filtersList = groupFilters[k].split('+');
                                filters[filtersList[0]] = filtersList[1];
                            }
                            this.createGroupReportTab(groupReport[0], filters);
                        }
                    }
                }
            }
        } else {
            tabIndex = params.indexOf('tab=');
            tabGroupIndex = params.indexOf('groupTab=');

            if(tabIndex >= 0) {
                queryParams = params.split('=');
                loadTestIds = queryParams[1].split(',');

                for(j = 0; j < loadTestIds.length; j++) {
                    loadTestReport = this.down('#loadtestreport-'+loadTestIds[j]);

                    if(!loadTestReport) {
                        this.createLoadTestReport(loadTestIds[j])
                    }
                }                    
            }

            if(tabGroupIndex >= 0) {
                // it must be a groupTab query param
                groupReports = params.split('=')[1].split('::');
                
                for(j = 0; j < groupReports.length; j++) {
                    filters = {};
                    groupReport = groupReports[j].split(':');
                    groupFilters = groupReport[1].split(',');
                    existingGroupReport = this.down('#loadtestgroupreport-' + groupReport[0]);

                    if(!existingGroupReport) {
                        for(i = 0; i < groupFilters.length; i++) {
                            filtersList = groupFilters[i].split('+');
                            filters[filtersList[0]] = filtersList[1];
                        }

                        this.createGroupReportTab(groupReport[0], filters);
                    }
                }
            }
        }
    },

    _hashCode: function(str) {
        var hash = 0;
            if (str.length == 0) return hash;
            for (i = 0; i < str.length; i++) {
                char = str.charCodeAt(i);
                hash = ((hash<<5)-hash)+char;
                hash = hash & hash; // Convert to 32bit integer
            }
            return hash;
    },

    processAdmin: function(params) {
        if(params.indexOf('user=admin') >= 0) {
            this.down('#btnDelete').show();
        }
    }
});
