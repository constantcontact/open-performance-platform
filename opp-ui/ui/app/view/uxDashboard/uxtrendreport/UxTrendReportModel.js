Ext.define('OppUI.view.uxDashboard.uxtrendreport.UxTrendReportModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.uxtrendreport',

    stores: {
        histogramData: {
            fields: [{
                    name: 'completedDate',
                    convert: function(value, record) {
                        return value * 1000;
                    }
                },
                { name: 'TTFB', type: 'auto' },
                { name: 'VisuallyComplete', type: 'auto' },
                { name: 'SpeedIndex', type: 'auto' }
            ],
            autoLoad: true,
            proxy: {
                type: 'ajax',
                url: '/uxsvc/v1/wpt/trend/histogram',
                reader: {
                    type: 'json',
                    keepRawData: true
                }
            },
            listeners: {
                load: 'onHistogramDataLoaded'
            }
        },
        wptTrendTable: {
            fields: [
                { name: 'id', mapping: 'id', type: 'auto' },
                {
                    name: 'date',
                    mapping: 'date',
                    type: 'auto',
                    convert: function(value, record) {
                        return new Date(value * 1000);
                    }
                },
                { name: 'TTFB', mapping: 'ttfb', type: 'auto' },
                { name: 'VisuallyComplete', mapping: 'visuallyComplete', type: 'auto' },
                { name: 'SpeedIndex', mapping: 'speedIndex', type: 'auto' },
                { name: 'Page', mapping: 'label.page', type: 'auto' },
                { name: 'Connection', mapping: 'label.connection', type: 'auto' },
                { name: 'SummaryURL', mapping: 'summaryUrl', type: 'auto' }
            ],
            autoLoad: true,
            proxy: {
                type: 'ajax',
                url: '/uxsvc/v1/wpt/trend/table',
                reader: {
                    type: 'json'
                }
            }
        },
        customTimings: {
            fields: [{
                    name: 'timePeriod',
                    type: 'auto',
                    convert: function(value, record) {
                        return value * 1000;
                    }
                },
                { name: 'min', type: 'int' },
                { name: 'max', type: 'int' },
                { name: 'median', type: 'int' },
                { name: 'average', type: 'int' },
                'name'
            ],
            proxy: {
                type: 'ajax',
                url: '/uxsvc/v1/wpt/trend/histogram_usertimings',
                reader: {
                    type: 'json',
                    keepRawData: true
                } //,
                // extraParams: {
                //     // avaiable extra params to send
                //     // name: '',
                //     // run: '',
                //     // view: '',
                //     // utBaseline: '',
                //     // interval: '1d'
                // }
            },
            autoLoad: false,
            listeners: {
                load: 'onHistogramUserTimingDataLoaded'
            }
        }
    }
});