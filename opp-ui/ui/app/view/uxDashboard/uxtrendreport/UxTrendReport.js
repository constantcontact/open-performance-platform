Ext.define('OppUI.view.uxDashboard.uxtrendreport.UxTrendReport',{
    extend: 'Ext.panel.Panel',
    alias: 'widget.uxtrendreport',
    xtype: 'uxtrendreport',

    requires: [
        'OppUI.view.uxDashboard.uxtrendreport.UxTrendReportController',
        'OppUI.view.uxDashboard.uxtrendreport.UxTrendReportModel'
    ],

    controller: 'uxtrendreport',
    viewModel: {
        type: 'uxtrendreport'
    },

    config: {
        activeState: null,
        pageName: null
    },

    initComponent: function() {

        
        this.callParent(arguments);

        this.getViewModel().getStore('histogramData').proxy.extraParams = {
            name: this.getPageName(),
            interval: '1d'
        };

        this.getViewModel().getStore('wptTrendTable').proxy.extraParams = {
            name: this.getPageName()
        };

        Ext.Ajax.request({
            url: '/uxsvc/v1/wpt/trend/customtiming/histogram?name=' + this.getPageName(),
            scope: this,
            success: 'customTimingChartData',
            failure: function(response) {
                //Ext.Msg.alert("Error...", "Error processing deletion. Please Try again Later.");

                // mock out some data for now....
                var resp = this.mockData();

                this.customTimingChartData(resp);
            }
        });
        
    },

    layout: {
        type: 'vbox',
        align: 'stretch'
    },

    scrollable: 'y',

    minWidth: 600,

    items: [{
        xtype: 'wpttrendchart',
        title: 'WPT Trend - median'
    },{
        xtype: 'customtimingchart',
        hidden: true
    },{
        xtype: 'wpttrendgrid'
    }],

    listeners: {
        beforeclose: 'updateUrlTabState'
    },

    customTimingChartData: function(response, options) {
        var json, chart, title, item, series;

        json = Ext.decode(response.responseText, false);
        series = json.chart.series;
        chart = this.down('customtimingchart');

        if(json && chart) {
            for(var i=0; i<series.length; i++){
                series[i].style=chart.getSeriesStyle();
                series[i].highlight=chart.getSeriesHighlight();
                series[i].marker=chart.getSeriesMarker();
                series[i].tooltip=chart.getSeriesTooltip();
            }

            chart.axes[0].fields = json.chart.modelFields.slice(1);
            chart.setSeries(series);
            chart.setStore(Ext.create('Ext.data.JsonStore', {
                fields: json.chart.modelFields,
                data: json.chart.data
            }));
            chart.redraw();
            chart.show();
        } 
    },

    mockData: function() {
        return {responseText: JSON.stringify({
                    "chart":{
                        "data":[
                            {
                                "start_time":"1495788660",
                                "Custom Timing Name 1":"701"
                            },
                            {
                                "start_time":"1495788840",
                                "Custom Timing Name 1":"415"
                            },
                            {
                                "start_time":"1495789020",
                                "Custom Timing Name 1":"1748"
                            },
                            {
                                "start_time":"1495789200",
                                "Custom Timing Name 1":"673"
                            },
                            {
                                "start_time":"1495789380",
                                "Custom Timing Name 1":"607"
                            },
                            {
                                "start_time":"1495789560",
                                "Custom Timing Name 1":"1957"
                            },
                            {
                                "start_time":"1495789740",
                                "Custom Timing Name 1":"993"
                            },
                            {
                                "start_time":"1495789920",
                                "Custom Timing Name 1":"1866"
                            },
                            {
                                "start_time":"1495790100",
                                "Custom Timing Name 1":"236"
                            },
                            {
                                "start_time":"1495790280",
                                "Custom Timing Name 1":"105"
                            },
                            {
                                "start_time":"1495790460",
                                "Custom Timing Name 1":"969"
                            },
                            {
                                "start_time":"1495790640",
                                "Custom Timing Name 1":"114"
                            },
                            {
                                "start_time":"1495790820",
                                "Custom Timing Name 1":"365"
                            },
                            {
                                "start_time":"1495791000",
                                "Custom Timing Name 1":"746"
                            },
                            {
                                "start_time":"1495791180",
                                "Custom Timing Name 1":"723"
                            },
                            {
                                "start_time":"1495791360",
                                "Custom Timing Name 1":"1237"
                            },
                            {
                                "start_time":"1495791540",
                                "Custom Timing Name 1":"1713"
                            },
                            {
                                "start_time":"1495791720",
                                "Custom Timing Name 1":"289"
                            },
                            {
                                "start_time":"1495791900",
                                "Custom Timing Name 1":"860"
                            },
                            {
                                "start_time":"1495792080",
                                "Custom Timing Name 1":"1407"
                            },
                            {
                                "start_time":"1495788696",
                                "Custom Timing Name 2":"779"
                            },
                            {
                                "start_time":"1495788876",
                                "Custom Timing Name 2":"870"
                            },
                            {
                                "start_time":"1495789056",
                                "Custom Timing Name 2":"369"
                            },
                            {
                                "start_time":"1495789236",
                                "Custom Timing Name 2":"349"
                            },
                            {
                                "start_time":"1495789416",
                                "Custom Timing Name 2":"1474"
                            },
                            {
                                "start_time":"1495789596",
                                "Custom Timing Name 2":"1474"
                            },
                            {
                                "start_time":"1495789776",
                                "Custom Timing Name 2":"451"
                            },
                            {
                                "start_time":"1495789956",
                                "Custom Timing Name 2":"504"
                            },
                            {
                                "start_time":"1495790136",
                                "Custom Timing Name 2":"21"
                            },
                            {
                                "start_time":"1495790316",
                                "Custom Timing Name 2":"803"
                            },
                            {
                                "start_time":"1495790496",
                                "Custom Timing Name 2":"636"
                            },
                            {
                                "start_time":"1495790676",
                                "Custom Timing Name 2":"958"
                            },
                            {
                                "start_time":"1495790856",
                                "Custom Timing Name 2":"1751"
                            },
                            {
                                "start_time":"1495791036",
                                "Custom Timing Name 2":"1054"
                            },
                            {
                                "start_time":"1495791216",
                                "Custom Timing Name 2":"1818"
                            },
                            {
                                "start_time":"1495791396",
                                "Custom Timing Name 2":"1228"
                            },
                            {
                                "start_time":"1495791576",
                                "Custom Timing Name 2":"331"
                            },
                            {
                                "start_time":"1495791756",
                                "Custom Timing Name 2":"1550"
                            },
                            {
                                "start_time":"1495791936",
                                "Custom Timing Name 2":"382"
                            },
                            {
                                "start_time":"1495792116",
                                "Custom Timing Name 2":"1343"
                            },
                            {
                                "start_time":"1495788732",
                                "Custom Timing Name 3":"1373"
                            },
                            {
                                "start_time":"1495788912",
                                "Custom Timing Name 3":"1074"
                            },
                            {
                                "start_time":"1495789092",
                                "Custom Timing Name 3":"895"
                            },
                            {
                                "start_time":"1495789272",
                                "Custom Timing Name 3":"507"
                            },
                            {
                                "start_time":"1495789452",
                                "Custom Timing Name 3":"1980"
                            },
                            {
                                "start_time":"1495789632",
                                "Custom Timing Name 3":"1737"
                            },
                            {
                                "start_time":"1495789812",
                                "Custom Timing Name 3":"1040"
                            },
                            {
                                "start_time":"1495789992",
                                "Custom Timing Name 3":"982"
                            },
                            {
                                "start_time":"1495790172",
                                "Custom Timing Name 3":"308"
                            },
                            {
                                "start_time":"1495790352",
                                "Custom Timing Name 3":"1856"
                            },
                            {
                                "start_time":"1495790532",
                                "Custom Timing Name 3":"734"
                            },
                            {
                                "start_time":"1495790712",
                                "Custom Timing Name 3":"674"
                            },
                            {
                                "start_time":"1495790892",
                                "Custom Timing Name 3":"1623"
                            },
                            {
                                "start_time":"1495791072",
                                "Custom Timing Name 3":"140"
                            },
                            {
                                "start_time":"1495791252",
                                "Custom Timing Name 3":"689"
                            },
                            {
                                "start_time":"1495791432",
                                "Custom Timing Name 3":"37"
                            },
                            {
                                "start_time":"1495791612",
                                "Custom Timing Name 3":"946"
                            },
                            {
                                "start_time":"1495791792",
                                "Custom Timing Name 3":"1578"
                            },
                            {
                                "start_time":"1495791972",
                                "Custom Timing Name 3":"1416"
                            },
                            {
                                "start_time":"1495792152",
                                "Custom Timing Name 3":"305"
                            },
                            {
                                "start_time":"1495788768",
                                "Custom Timing Name 4":"627"
                            },
                            {
                                "start_time":"1495788948",
                                "Custom Timing Name 4":"1206"
                            },
                            {
                                "start_time":"1495789128",
                                "Custom Timing Name 4":"1390"
                            },
                            {
                                "start_time":"1495789308",
                                "Custom Timing Name 4":"74"
                            },
                            {
                                "start_time":"1495789488",
                                "Custom Timing Name 4":"952"
                            },
                            {
                                "start_time":"1495789668",
                                "Custom Timing Name 4":"1677"
                            },
                            {
                                "start_time":"1495789848",
                                "Custom Timing Name 4":"1149"
                            },
                            {
                                "start_time":"1495790028",
                                "Custom Timing Name 4":"613"
                            },
                            {
                                "start_time":"1495790208",
                                "Custom Timing Name 4":"1359"
                            },
                            {
                                "start_time":"1495790388",
                                "Custom Timing Name 4":"1075"
                            },
                            {
                                "start_time":"1495790568",
                                "Custom Timing Name 4":"1691"
                            },
                            {
                                "start_time":"1495790748",
                                "Custom Timing Name 4":"142"
                            },
                            {
                                "start_time":"1495790928",
                                "Custom Timing Name 4":"1564"
                            },
                            {
                                "start_time":"1495791108",
                                "Custom Timing Name 4":"1292"
                            },
                            {
                                "start_time":"1495791288",
                                "Custom Timing Name 4":"304"
                            },
                            {
                                "start_time":"1495791468",
                                "Custom Timing Name 4":"1794"
                            },
                            {
                                "start_time":"1495791648",
                                "Custom Timing Name 4":"637"
                            },
                            {
                                "start_time":"1495791828",
                                "Custom Timing Name 4":"1335"
                            },
                            {
                                "start_time":"1495792008",
                                "Custom Timing Name 4":"36"
                            },
                            {
                                "start_time":"1495792188",
                                "Custom Timing Name 4":"60"
                            },
                            {
                                "start_time":"1495788804",
                                "Custom Timing Name 5":"897"
                            },
                            {
                                "start_time":"1495788984",
                                "Custom Timing Name 5":"668"
                            },
                            {
                                "start_time":"1495789164",
                                "Custom Timing Name 5":"1950"
                            },
                            {
                                "start_time":"1495789344",
                                "Custom Timing Name 5":"1262"
                            },
                            {
                                "start_time":"1495789524",
                                "Custom Timing Name 5":"995"
                            },
                            {
                                "start_time":"1495789704",
                                "Custom Timing Name 5":"212"
                            },
                            {
                                "start_time":"1495789884",
                                "Custom Timing Name 5":"1846"
                            },
                            {
                                "start_time":"1495790064",
                                "Custom Timing Name 5":"1801"
                            },
                            {
                                "start_time":"1495790244",
                                "Custom Timing Name 5":"1959"
                            },
                            {
                                "start_time":"1495790424",
                                "Custom Timing Name 5":"771"
                            },
                            {
                                "start_time":"1495790604",
                                "Custom Timing Name 5":"802"
                            },
                            {
                                "start_time":"1495790784",
                                "Custom Timing Name 5":"469"
                            },
                            {
                                "start_time":"1495790964",
                                "Custom Timing Name 5":"641"
                            },
                            {
                                "start_time":"1495791144",
                                "Custom Timing Name 5":"1031"
                            },
                            {
                                "start_time":"1495791324",
                                "Custom Timing Name 5":"293"
                            },
                            {
                                "start_time":"1495791504",
                                "Custom Timing Name 5":"555"
                            },
                            {
                                "start_time":"1495791684",
                                "Custom Timing Name 5":"826"
                            },
                            {
                                "start_time":"1495791864",
                                "Custom Timing Name 5":"1729"
                            },
                            {
                                "start_time":"1495792044",
                                "Custom Timing Name 5":"827"
                            },
                            {
                                "start_time":"1495792224",
                                "Custom Timing Name 5":"582"
                            }
                        ],
                        "modelFields":[
                            "start_time",
                            "Custom Timing Name 1",
                            "Custom Timing Name 2",
                            "Custom Timing Name 3",
                            "Custom Timing Name 4",
                            "Custom Timing Name 5"
                        ],
                        "series":[
                            {
                                "type":"line",
                                "axis":"left",
                                "xField":"start_time",
                                "yField":"Custom Timing Name 1"
                            },
                            {
                                "type":"line",
                                "axis":"left",
                                "xField":"start_time",
                                "yField":"Custom Timing Name 2"
                            },
                            {
                                "type":"line",
                                "axis":"left",
                                "xField":"start_time",
                                "yField":"Custom Timing Name 3"
                            },
                            {
                                "type":"line",
                                "axis":"left",
                                "xField":"start_time",
                                "yField":"Custom Timing Name 4"
                            },
                            {
                                "type":"line",
                                "axis":"left",
                                "xField":"start_time",
                                "yField":"Custom Timing Name 5"
                            }
                        ]
                    }
                })
            };
    }
});
