Ext.define('OppUI.view.loadTestDashboard.loadtestreport.LoadTestReportModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.loadtestreport',
    data: {
        name: 'OppUI',
        testName: 'THIS IS A TEST NAME!!'
    },

    stores: {
        remoteAggData: {
            model: 'OppUI.model.loadTestDashboard.LoadTestReportSummary',
            autoLoad: true,
            url: '',
            proxy: {
                type: 'ajax',
                reader: {
                    type: 'json'
                }
            }
        }
    }
    // ,
    // stores: {
    //     aggData: {
    //         model: '',
    //         autoLoad: true,
    //         proxy: {
    //             type: 'ajax',
    //             url: 'http://roadrunner.roving.com/loadsvc/v1/loadtests/6483/aggdata',
    //             reader: {
    //                 type: 'json'
    //             }
    //         },
    //         listeners: {
    //             load: function() {
    //                 console.log("Agg Data Loaded");
    //             }
    //         }
    //     },
    //     slas: {
    //         model: '',
    //         autoLoad: true,
    //         proxy: {
    //             type: 'ajax',
    //             url: 'http://roadrunner.roving.com/loadsvc/v1/loadtests/6483/slas',
    //             reader: {
    //                 type: 'json'
    //             }
    //         },
    //         listeners: {
    //             load: function() {
    //                 console.log("SLAs Loaded");
    //             }
    //         }
    //     },
    //     respPct90: {
    //         model: '',
    //         autoLoad: true,
    //         proxy: {
    //             type: 'ajax',
    //             url: 'http://roadrunner.roving.com/loadsvc/v1/charts/timeseries/loadtests/6483',
    //             reader: {
    //                 type: 'json'
    //             },
    //             extraParams: {
    //                 yaxis: 'resp_pct90'
    //             }
    //         },
    //         listeners: {
    //             load: function() {
    //                 console.log("Resp Pct90 Loaded");
    //             }
    //         }
    //     },
    //     respPct75: {
    //         model: '',
    //         autoLoad: true,
    //         proxy: {
    //             type: 'ajax',
    //             url: 'http://roadrunner.roving.com/loadsvc/v1/charts/timeseries/loadtests/6483',
    //             reader: {
    //                 type: 'json'
    //             },
    //             extraParams: {
    //                 yaxis: 'resp_pct75'
    //             }
    //         },
    //         listeners: {
    //             load: function() {
    //                 console.log("Resp Pct75 Loaded");
    //             }
    //         }
    //     },
    //     respAvg: {
    //         model: '',
    //         autoLoad: true,
    //         proxy: {
    //             type: 'ajax',
    //             url: 'http://roadrunner.roving.com/loadsvc/v1/charts/timeseries/loadtests/6483',
    //             reader: {
    //                 type: 'json'
    //             },
    //             extraParams: {
    //                 yaxis: 'resp_avg'
    //             }
    //         },
    //         listeners: {
    //             load: function() {
    //                 console.log("Resp Avg Loaded");
    //             }
    //         }
    //     },
    //     respMedian: {
    //         model: '',
    //         autoLoad: true,
    //         proxy: {
    //             type: 'ajax',
    //             url: 'http://roadrunner.roving.com/loadsvc/v1/charts/timeseries/loadtests/6483',
    //             reader: {
    //                 type: 'json'
    //             },
    //             extraParams: {
    //                 yaxis: 'resp_median'
    //             }
    //         },
    //         listeners: {
    //             load: function() {
    //                 console.log("Resp Median Loaded");
    //             }
    //         }
    //     },
    //     tpsMedian: {
    //         model: '',
    //         autoLoad: true,
    //         proxy: {
    //             type: 'ajax',
    //             url: 'http://roadrunner.roving.com/loadsvc/v1/charts/timeseries/loadtests/6483',
    //             reader: {
    //                 type: 'json'
    //             },
    //             extraParams: {
    //                 yaxis: 'tps_median'
    //             }
    //         },
    //         listeners: {
    //             load: function() {
    //                 console.log("TPS Medain Loaded");
    //             }
    //         }
    //     },
    //     tpsMax: {
    //         model: '',
    //         autoLoad: true,
    //         proxy: {
    //             type: 'ajax',
    //             url: 'http://roadrunner.roving.com/loadsvc/v1/charts/timeseries/loadtests/6483',
    //             reader: {
    //                 type: 'json'
    //             },
    //             extraParams: {
    //                 yaxis: 'tps_max'
    //             }
    //         },
    //         listeners: {
    //             load: function() {
    //                 console.log("TPS Max Loaded");
    //             }
    //         }
    //     }
    // }

});
