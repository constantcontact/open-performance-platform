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
                type: 'memory',
                reader: {
                    type: 'json'
                },
                data: [
                    {completedDate: 1453420800, ttfb:{min: 72,max: 66,median: 73,average: 92}, visuallyComplete:{min: 172,max: 166,median: 173,average: 192}, speedIndex:{min: 272,max: 266,median: 273,average: 292} },
                    {completedDate: 1453421800, ttfb:{min: 172,max: 166,median: 273,average: 392}, visuallyComplete:{min: 272,max: 266,median: 273,average: 292}, speedIndex:{min: 372,max: 366,median: 373,average: 392} },
                    {completedDate: 1453422800, ttfb:{min: 272,max: 366,median: 573,average: 392}, visuallyComplete:{min: 1272,max: 1266,median: 4273,average: 2292}, speedIndex:{min: 1372,max: 1366,median: 1373,average: 392} },
                    {completedDate: 1453423800, ttfb:{min: 572,max: 666,median: 873,average: 692}, visuallyComplete:{min: 4272,max: 466,median: 6273,average: 5292}, speedIndex:{min: 1372,max: 1366,median: 3173,average: 1392} },
                    {completedDate: 1453424800, ttfb:{min: 972,max: 466,median: 373,average: 392}, visuallyComplete:{min: 5272,max: 666,median: 3273,average: 3292}, speedIndex:{min: 1372,max: 1366,median: 1373,average: 1392} },   
                    {completedDate: 1453425800, ttfb:{min: 772,max: 866,median: 473,average: 892}, visuallyComplete:{min: 1272,max: 966,median: 1273,average: 2292}, speedIndex:{min: 1372,max: 1366,median: 1373,average: 1392} }   
                ]
            },

            listeners: {
                load: 'onHistogramDataLoaded'
            }
        },
        min: {
            fields: [
                { name: 'wptTimestamp', mapping: 'completedDate', type: 'auto',
                    convert: function(value, record) {
                        return value * 1000;                
                    }
                }, 
                { name: 'TTFB', mapping: 'ttfb.min', type: 'auto' },
                { name: 'VisuallyComplete', mapping: 'visuallyComplete.min', type: 'auto' },
                { name: 'SpeedIndex', mapping: 'speedIndex.min', type: 'auto' },
                { name: 'Page', mapping: 'page', type: 'auto'},
                { name: 'Connection', mapping: 'connection', type: 'auto'}
            ],
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
            fields: [
                { name: 'wptTimestamp', mapping: 'completedDate', type: 'auto',
                    convert: function(value, record) {
                        return value * 1000;                
                    }
                }, 
                { name: 'TTFB', mapping: 'ttfb.max', type: 'auto' },
                { name: 'VisuallyComplete', mapping: 'visuallyComplete.max', type: 'auto' },
                { name: 'SpeedIndex', mapping: 'speedIndex.max', type: 'auto' },
                { name: 'Page', mapping: 'page', type: 'auto'},
                { name: 'Connection', mapping: 'connection', type: 'auto'}
            ],
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
            fields: [
                { name: 'wptTimestamp', mapping: 'completedDate', type: 'auto',
                    convert: function(value, record) {
                        return value * 1000;                
                    }
                }, 
                { name: 'TTFB', mapping: 'ttfb.average', type: 'auto' },
                { name: 'VisuallyComplete', mapping: 'visuallyComplete.average', type: 'auto' },
                { name: 'SpeedIndex', mapping: 'speedIndex.average', type: 'auto' },
                { name: 'Page', mapping: 'page', type: 'auto'},
                { name: 'Connection', mapping: 'connection', type: 'auto'}
            ],
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
