Ext.define('OppUI.view.uxDashboard.uxtrendreport.UxTrendReportModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.uxtrendreport',

    stores: {
        histogramData: {
            fields: [
                { name: 'wptTimestamp', mapping: 'completedDate', type: 'auto',
                    convert: function(value, record) {
                        return value * 1000;                
                    }
                }, 
                { name: 'TTFB', mapping: 'ttfb.median', type: 'auto' },
                { name: 'VisuallyComplete', mapping: 'visuallyComplete.median', type: 'auto' },
                { name: 'SpeedIndex', mapping: 'speedIndex.median', type: 'auto' }
            ],
            autoLoad: true,
            proxy: {
                type: 'ajax',
                url:  '/uxsvc/v1/wpt/trend/histogram',
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
                { name: 'date', mapping: 'date', type: 'auto',
                    convert: function(value, record) {
                        return new Date(value * 1000);
                    }
                }, 
                { name: 'TTFB', mapping: 'ttfb', type: 'auto' },
                { name: 'VisuallyComplete', mapping: 'visuallyComplete', type: 'auto' },
                { name: 'SpeedIndex', mapping: 'speedIndex', type: 'auto' },
                { name: 'Page', mapping: 'label.page', type: 'auto'},
                { name: 'Connection', mapping: 'label.connection', type: 'auto'},
                { name: 'SummaryURL', mapping: 'summaryUrl', type: 'auto'}
            ],
            autoLoad: true,
            proxy: {
                type: 'ajax',
                url:  '/uxsvc/v1/wpt/trend/table',
                reader: {
                    type: 'json'
                }
            },
            listeners: {
                load: function() {
                    console.log('wptTrendTable data loaded!');
                }
            }
        },

        min: {
            model: 'OppUI.model.uxDashboard.HistogramMin',
            autoLoad: false,
            proxy: {
                type: 'memory',
                reader: {
                    type: 'json'
                }
            },
            listeners: {
                load: function() {
                    console.log('Min histogram data loaded!');
                }
            }
        },
            max: {
            model: 'OppUI.model.uxDashboard.HistogramMax',
            autoLoad: false,
            proxy: {
                type: 'memory',
                reader: {
                    type: 'json'
                }
            },
            listeners: {
                load: function() {
                    console.log('Max histogram data loaded!');
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
            },
            listeners: {
                load: function() {
                    console.log('Median histogram data loaded!');
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
            },
            listeners: {
                load: function() {
                    console.log('Average histogram data loaded!');
                }
            }
        }
    }
});
