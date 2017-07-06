Ext.define('OppUI.view.uxDashboard.uxtrendreport.UxTrendReportModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.uxtrendreport',

    stores: {
        histogramData: {
            fields: [{
                    name: 'wptTimestamp',
                    mapping: 'completedDate',
                    type: 'auto',
                    convert: function(value, record) {
                        return value * 1000;
                    }
                },
                { name: 'TTFB', mapping: 'ttfb.median', type: 'auto' },
                { name: 'TTFB-min', mapping: 'ttfb.min', type: 'auto' },
                { name: 'TTFB-max', mapping: 'ttfb.max', type: 'auto' },
                { name: 'VisuallyComplete', mapping: 'visuallyComplete.median', type: 'auto' },
                { name: 'VisuallyComplete-min', mapping: 'visuallyComplete.min', type: 'auto' },
                { name: 'VisuallyComplete-max', mapping: 'visuallyComplete.max', type: 'auto' },
                { name: 'SpeedIndex', mapping: 'speedIndex.median', type: 'auto' },
                { name: 'SpeedIndex-min', mapping: 'speedIndex.min', type: 'auto' },
                { name: 'SpeedIndex-max', mapping: 'speedIndex.max', type: 'auto' }
                //,{ name: 'userTimings', mapping: 'userTimings', type: 'auto' }
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
        median: {
            model: 'OppUI.model.uxDashboard.HistogramMedian',
            autoLoad: false,
            proxy: {
                type: 'memory',
                reader: {
                    type: 'json'
                }
            }
        },
        average: {
            model: 'OppUI.model.uxDashboard.HistogramAverage',
            autoLoad: false,
            proxy: {
                type: 'memory',
                reader: {
                    type: 'json'
                }
            }
        },
        customTimings: {
            fields: ['timePeriod', { name: 'userTimings', type: 'auto' }],
            proxy: {
                type: 'ajax',
                url: '/uxsvc/v1/wpt/trend/histogram_usertimings',
                reader: {
                    type: 'json',
                    keepRawData: true
                }
            },
            listeners: {
                load: 'onHistogramUserTimingDataLoaded'
            }
        },

        customUserTimingsMedian: {
            autoLoad: false,
            proxy: {
                type: 'memory',
                reader: {
                    type: 'json'
                }
            }
        },
        customUserTimingsAverage: {
            autoLoad: false,
            proxy: {
                type: 'memory',
                reader: {
                    type: 'json'
                }
            }
        }
    }
});