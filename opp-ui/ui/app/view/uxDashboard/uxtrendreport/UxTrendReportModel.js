Ext.define('OppUI.view.uxDashboard.uxtrendreport.UxTrendReportModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.uxtrendreport',

    stores: {
        remoteAppTrend: {
            model: 'OppUI.model.uxDashboard.AppTrend',
            autoLoad: false,

            proxy: {
                type: 'ajax',
                url: 'http://roadrunner.roving.com/uxsvc/v2/rrux/wptTrendData', 
                reader: {
                    type: 'json',
                    rootProperty: 'dataTable'
                },
                data: []
            },
            listeners: {
                load: 'onRemoteAppTrendLoad'
            }
        },
        histogramData: {
            fields: [
                { name: 'wptTimestamp', mapping: 'completedDate', type: 'auto',
                    convert: function(value, record) {
                        return value * 1000;                
                    }
                }, 
                { name: 'TTFB', mapping: 'ttfb.median', type: 'auto' },
                { name: 'VisuallyComplete', mapping: 'visuallyComplete.median', type: 'auto' },
                { name: 'SpeedIndex', mapping: 'speedIndex.median', type: 'auto' },
                { name: 'Page', mapping: 'page', type: 'auto'},
                { name: 'Connection', mapping: 'connection', type: 'auto'}
            ],
            autoLoad: true,
            proxy: {
                type: 'ajax',
                url:  'http://opp-svc.mydomain.com/uxsvc/v1/wpt/trend/histogram',
                reader: {
                    type: 'json',
                    keepRawData: true
                }
            },
            listeners: {
                load: 'onHistogramDataLoaded'
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
        },



        uxPageTrendGrid: {
            source: '{histogramData}'
        }
    }
});
