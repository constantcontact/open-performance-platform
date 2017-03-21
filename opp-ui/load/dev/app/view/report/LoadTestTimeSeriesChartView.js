Ext.define('CCPerf.view.report.LoadTestTimeSeriesChartView' ,{
    extend: 'Ext.Panel',
    xtype: 'line-crosszoom',
    requires: [
        'Ext.chart.CartesianChart',
        'Ext.chart.series.Line',
        'Ext.chart.axis.Numeric',
        'Ext.chart.axis.Time',
        'Ext.chart.interactions.CrossZoom'
    ],
    alias: 'widget.chart-load-test-timeseries',
    header:false,
    layout:'fit',
    loadTestId: '',
    yaxis:'',
    tbar: [
        '->',
        {
            xtype: 'combobox',
            store: {
                fields: [ "display","value" ],
                data: [
                  {"display":'Min', "value":'resp_min'},
                  {"display":'Average', "value":'resp_avg'},
                  {"display":'Median', "value":'resp_median'},
                  {"display":'Max', "value":'resp_max'},
                  {"display":'75th PCT', "value":'resp_pct75'},
                  {"display":'90th PCT', "value":'resp_pct90'},
                  {"display":'nth PCT', "value":'nPCT'},
                  {"display":'Transaction count', "value":'call_count'},
                  {"display":'Error count', "value":'error_count'},
                  {"display":'Std. Dev.', "value":'resp_stddev'},
                  {"display":'Total Bytes Rec.', "value":'total_bytes_received'},
                  {"display":'Total Bytes Sent', "value":'total_bytes_sent'},
                  {"display":'Total Bytes', "value":'total_bytes'}
                ]
            },
            width:200,
            editable:false,
            displayField: "display",
            valueField: "value",
            fieldLabel: 'Show:',
            labelWidth:35,
            anchor: '0',
            queryMode: 'local',
            selectOnTab: true,
            name: 'chartType',
            listeners: {
                change: function( field, newValue, oldValue, eOpts ){
                    var chartContainer = this.up('line-crosszoom');
                    if(newValue == "nPCT"){
                         Ext.MessageBox.prompt('Enter Percentile', 'Integer Percentile value:',  function (btn, pctValue){
                             pctValue = pctValue.trim();
                             if(/^[0-9]+$/.test(String(pctValue))){
                                chartContainer.loadChart('resp_pct'+pctValue, chartContainer.updateChart);
                             } else {
                                Ext.MessageBox.alert("Error","You must enter an integer value.");
                                field.setValue(oldValue);
                             }

                        });

                    } else {
                        chartContainer.loadChart(newValue, chartContainer.updateChart);
                    }
                }

            }
        },
        '-',
        {
            text: 'Undo Zoom',
            handler: function() {
                var chart = this.up('panel').down('cartesian'),
                    interaction = chart && Ext.ComponentQuery.query('interaction', chart)[0],
                    undoButton = interaction && interaction.getUndoButton(),
                    handler = undoButton && undoButton.handler;
                if (handler) {
                    handler();
                }
            }
        }
    ],
    items: [
        {
            xtype:'cartesian',
            width: '100%',
            height: 300,
            padding: '10 0 0 0',
            animate: true,
            shadow: false,
            insetPadding: 10,
            style: 'background: #fff;',
            legend: {
                docked: 'right'
            },
            interactions: {
                type: 'crosszoom',
                zoomOnPanGesture: false
            },
            axes: [
                {
                    type: 'numeric',
                    minimum: 0,
                    position: 'left',
                    title: 'Response Time (msec)', // gets overridden with yaxisTitle
                    grid: true
                }, {
                    type: 'time',
                    position: 'bottom',
                    fields: ['startTime'],
                    renderer:function(v){
                        if(v.length > 20){
                            this.font = "10px Arial, Helvetica, sans-serif";
                        }
                        return Ext.Date.format(new Date(v*1000),'m/d H:i');
                    }
                }
            ]

        }
    ],
    initComponent: function() {

        this.initChartTypeComboValue();
        this.loadChart(null, this.createChart);

       /*
       // can't get this to work
       this.getChart().store = {
            fields: 'startTime',
            proxy: {
                type: 'ajax',
                url: '/loadsvc/v1/charts/timeseries/loadtests/' + this.loadTestId + "?yaxis=" + this.yaxis,
                reader: {
                    type: 'json'
                }
            },
            autoLoad: false,
            listeners:{
                load:function(store, records, x, y, z){
                    chartContainer.setSeries(records[0].data.chart.series);
                    store.model.addFields(records[0].data.chart.modelFields);
                    store.loadData(records[0].data.chart.data);

                    //chartContainer.setStore(records[0].data.chart.modelFields, records[0].data.chart.data);

                }
            }
        };*/

        this.callParent(arguments);

    },
    initChartTypeComboValue: function(){
        // set combo box initial value
        for (var i = 0; i < this.tbar.length; i++) {
            if (this.tbar[i].xtype === 'combobox') {
                this.tbar[i].value = this.yaxis;
            }
        }
    },
    updateChart: function(chart, chartContainer, yaxis, json){
        chart.store.loadData(json.chart.data);
        chartContainer.setYAxisTitle(chart, yaxis, null);
        chartContainer.setXAxisTitle(chart, null);
        chartContainer.up('cstpanel').setTitle(json.chart.title);
    },
    createChart: function(chart, chartContainer, yaxis, json){
        chartContainer.setSeries(chart, json.chart.series);
        chartContainer.setStore(chart, json.chart.modelFields, json.chart.data);
        chartContainer.setYAxisTitle(chart,yaxis, null);
        //chartContainer.setXAxisTitle(chart, null);
        chartContainer.removeAll();
        chartContainer.add(chart);
    },
    loadChart: function(yaxis, callback){
        var chartContainer = this;
        var chart = chartContainer.getChart();

        // figure out yaxis
        if(yaxis === null || yaxis === undefined){
            yaxis = this.yaxis;
        }
        this.yaxis = yaxis;

        // TODO: make this async.  This is harder than you think.  It becomes a race condition with multiple charts.  I guess this logic would need to move to the controller.
        // TODO: it's like EXTJS can't differentiate between different instances of a view making ajax calls or i'm probably just doing something wrong
        Ext.Ajax.request({
            url: '/loadsvc/v1/charts/timeseries/loadtests/' + this.loadTestId + "?yaxis=" + yaxis,
            async: true,
            scope: chartContainer,
            disableCaching: false,
            success: function (response) {
                var json = Ext.decode(response.responseText, false);
                // callback to either update or create method
                callback(chart, chartContainer, this.yaxis, json);
            }
        });
    },
    setSeries: function(chart, chartSeriesJson) {
        var seriesStyle = {lineWidth: 4};
        var seriesMarker = {radius: 4};
        var seriesHighlight = {fillStyle: '#000', radius: 5, lineWidth: 2, strokeStyle: '#fff'};
        var seriesTooltip = {
            trackMouse: true, style: 'background: #fff', showDelay: 0, dismissDelay: 0, hideDelay: 0,
            renderer: function (storeItem, item) {
                var title = item.series.getYField();
                this.setHtml('<b>' + title + ':</b> ' + storeItem.get(title) + "ms");
            }
        };

        // append markers, highlighting, and style to the series
        var len = chartSeriesJson.length;
        for(var i=0; i<len; i++){
            chartSeriesJson[i].style=seriesStyle;
            chartSeriesJson[i].highlight=seriesHighlight;
            chartSeriesJson[i].marker=seriesMarker;
            chartSeriesJson[i].tooltip=seriesTooltip;

        }
        // this.items.items[0].series.push(chartSeriesJson);
        // apply changes to chart
        Ext.apply(chart, { series: chartSeriesJson});
    },
    setXAxisTitle: function(chart, title) {
        if(!title) {
            if (this.xaxisTitle !== null && this.xaxisTitle !== undefined) {
                // is was passed in to this object
                title = this.xaxisTitle;
            } else {
                if (this.up().xaxisTitle !== null) {
                    // it was passed into the parent container (e.g. - CSTPanel)
                    title = this.up().xaxisTitle;
                }
            }
            chart.axes[1].title = title;
        }
    },
    setYAxisTitle: function(chart, yaxis, title) {
        if(!title){
            // set all resp times
            if(yaxis.indexOf("resp") > -1) {
                title = 'Response Time (ms)';
            } else {
                // handle all non response times
                switch (yaxis) {
                    case 'call_count':
                        title = 'Transactions per Minute';
                        break;
                    case 'total_bytes_received':
                        title = 'Total Bytes Received per Minute';
                        break;
                    case 'total_bytes_sent':
                        title = 'Total Bytes Sent per Minute';
                        break;
                    case 'total_bytes':
                        title = 'Total Bytes per Minute';
                        break;
                    default:
                        title = '';
                        break;
                }
            }
        }

        chart.axes[0].title=title;
    },
    getChart: function() {
        if(this.items[0] !== undefined){
            return this.items[0];
        } else {
            return this.down('chart');
        }
    },
    setStore: function(chart, modelFieldsJson, storeDataJson) {
        var store = Ext.create('Ext.data.JsonStore', {
            fields: modelFieldsJson,
            data: storeDataJson
        });
        // apply changes to chart
        Ext.apply(chart, { store: store});
    },
    setMockSeries:function() {
        Ext.apply(this.getChart(), { series: [{"type":"line","axis":"left","xField":"startTime","yField":"1 - ReportingSvc - All Data Types"},{"type":"line","axis":"left","xField":"startTime","yField":"2 - ReportingSvc - Donations"},{"type":"line","axis":"left","xField":"startTime","yField":"3 - ReportingSvc - Email"},{"type":"line","axis":"left","xField":"startTime","yField":"4 - ReportingSvc - Survey"},{"type":"line","axis":"left","xField":"startTime","yField":"5 - ReportingSvc - SaveLocal"},{"type":"line","axis":"left","xField":"startTime","yField":"6 - ReportingSvc - Events"},{"type":"line","axis":"left","xField":"startTime","yField":"7 - ReportingSvc - Social"},{"type":"line","axis":"left","xField":"startTime","yField":"8 - ReportingSvc - Coupon"}]});
    },
    setMockStore: function(){
        // todo: update this data as its using primarily the old _ format
        // Ext.apply(this.getChart(), { store: {
        //     fields: ["startTime","1 - ReportingSvc - All Data Types","2 - ReportingSvc - Donations","3 - ReportingSvc - Email","4 - ReportingSvc - Survey","5 - ReportingSvc - SaveLocal","6 - ReportingSvc - Events","7 - ReportingSvc - Social","8 - ReportingSvc - Coupon"]
        //     ,data: [{"loadTestId":"729","startTime":"1424380133","transactionName":"1 - ReportingSvc - All Data Types","resp_min":"109","resp_max":"146","resp_avg":"125","resp_median":"126","resp_pct75":"141","resp_pct90":"146","resp_stddev":"12.69","call_count":"7","total_bytes_received":"114276","total_bytes_sent":"17479"},{"loadTestId":"729","startTime":"1424380178","transactionName":"1 - ReportingSvc - All Data Types","resp_min":"98","resp_max":"140","resp_avg":"119","resp_median":"116","resp_pct75":"130","resp_pct90":"140","resp_stddev":"11.47","call_count":"13","total_bytes_received":"204715","total_bytes_sent":"31610"},{"loadTestId":"729","startTime":"1424380223","transactionName":"1 - ReportingSvc - All Data Types","resp_min":"93","resp_max":"192","resp_avg":"119","resp_median":"113","resp_pct75":"138","resp_pct90":"192","resp_stddev":"26.86","call_count":"10","total_bytes_received":"147182","total_bytes_sent":"22824"},{"loadTestId":"729","startTime":"1424380268","transactionName":"1 - ReportingSvc - All Data Types","resp_min":"95","resp_max":"220","resp_avg":"118","resp_median":"111","resp_pct75":"120","resp_pct90":"164","resp_stddev":"29.69","call_count":"17","total_bytes_received":"259218","total_bytes_sent":"40044"},{"loadTestId":"729","startTime":"1424380320","transactionName":"1 - ReportingSvc - All Data Types","resp_min":"91","resp_max":"123","resp_avg":"104","resp_median":"103","resp_pct75":"114","resp_pct90":"123","resp_stddev":"9.67","call_count":"13","total_bytes_received":"203389","total_bytes_sent":"31314"},{"loadTestId":"729","startTime":"1424380412","transactionName":"1 - ReportingSvc - All Data Types","resp_min":"86","resp_max":"194","resp_avg":"117","resp_median":"107","resp_pct75":"165","resp_pct90":"194","resp_stddev":"33.48","call_count":"10","total_bytes_received":"157056","total_bytes_sent":"24230"},{"loadTestId":"729","startTime":"1424380451","transactionName":"1 - ReportingSvc - All Data Types","resp_min":"91","resp_max":"192","resp_avg":"107","resp_median":"99","resp_pct75":"104","resp_pct90":"192","resp_stddev":"28.50","call_count":"10","total_bytes_received":"152414","total_bytes_sent":"23527"},{"loadTestId":"729","startTime":"1424380133","transactionName":"2 - ReportingSvc - Donations","resp_min":"14","resp_max":"20","resp_avg":"17","resp_median":"18","resp_pct75":"18","resp_pct90":"20","resp_stddev":"1.75","call_count":"7","total_bytes_received":"11847","total_bytes_sent":"3234"},{"loadTestId":"729","startTime":"1424380178","transactionName":"2 - ReportingSvc - Donations","resp_min":"14","resp_max":"42","resp_avg":"19","resp_median":"16","resp_pct75":"18","resp_pct90":"42","resp_stddev":"7.83","call_count":"13","total_bytes_received":"22007","total_bytes_sent":"6006"},{"loadTestId":"729","startTime":"1424380223","transactionName":"2 - ReportingSvc - Donations","resp_min":"14","resp_max":"31","resp_avg":"18","resp_median":"15","resp_pct75":"24","resp_pct90":"31","resp_stddev":"5.39","call_count":"10","total_bytes_received":"16941","total_bytes_sent":"4620"},{"loadTestId":"729","startTime":"1424380268","transactionName":"2 - ReportingSvc - Donations","resp_min":"13","resp_max":"31","resp_avg":"17","resp_median":"15","resp_pct75":"18","resp_pct90":"26","resp_stddev":"4.64","call_count":"17","total_bytes_received":"28783","total_bytes_sent":"7854"},{"loadTestId":"729","startTime":"1424380320","transactionName":"2 - ReportingSvc - Donations","resp_min":"12","resp_max":"19","resp_avg":"15","resp_median":"15","resp_pct75":"17","resp_pct90":"19","resp_stddev":"1.86","call_count":"13","total_bytes_received":"22005","total_bytes_sent":"6006"},{"loadTestId":"729","startTime":"1424380412","transactionName":"2 - ReportingSvc - Donations","resp_min":"12","resp_max":"18","resp_avg":"14","resp_median":"14","resp_pct75":"16","resp_pct90":"18","resp_stddev":"1.85","call_count":"10","total_bytes_received":"16942","total_bytes_sent":"4620"},{"loadTestId":"729","startTime":"1424380452","transactionName":"2 - ReportingSvc - Donations","resp_min":"12","resp_max":"15","resp_avg":"14","resp_median":"15","resp_pct75":"15","resp_pct90":"15","resp_stddev":"0.92","call_count":"10","total_bytes_received":"16936","total_bytes_sent":"4620"},{"loadTestId":"729","startTime":"1424380133","transactionName":"3 - ReportingSvc - Email","resp_min":"19","resp_max":"33","resp_avg":"23","resp_median":"23","resp_pct75":"23","resp_pct90":"33","resp_stddev":"4.36","call_count":"7","total_bytes_received":"13489","total_bytes_sent":"3308"},{"loadTestId":"729","startTime":"1424380178","transactionName":"3 - ReportingSvc - Email","resp_min":"21","resp_max":"28","resp_avg":"25","resp_median":"25","resp_pct75":"27","resp_pct90":"28","resp_stddev":"2.08","call_count":"13","total_bytes_received":"24083","total_bytes_sent":"6006"},{"loadTestId":"729","startTime":"1424380223","transactionName":"3 - ReportingSvc - Email","resp_min":"20","resp_max":"46","resp_avg":"25","resp_median":"23","resp_pct75":"27","resp_pct90":"46","resp_stddev":"7.31","call_count":"10","total_bytes_received":"18531","total_bytes_sent":"4620"},{"loadTestId":"729","startTime":"1424380268","transactionName":"3 - ReportingSvc - Email","resp_min":"19","resp_max":"34","resp_avg":"24","resp_median":"25","resp_pct75":"29","resp_pct90":"34","resp_stddev":"5.13","call_count":"17","total_bytes_received":"31753","total_bytes_sent":"7891"},{"loadTestId":"729","startTime":"1424380320","transactionName":"3 - ReportingSvc - Email","resp_min":"20","resp_max":"26","resp_avg":"23","resp_median":"23","resp_pct75":"26","resp_pct90":"26","resp_stddev":"1.96","call_count":"13","total_bytes_received":"24608","total_bytes_sent":"6080"},{"loadTestId":"729","startTime":"1424380412","transactionName":"3 - ReportingSvc - Email","resp_min":"16","resp_max":"28","resp_avg":"20","resp_median":"19","resp_pct75":"22","resp_pct90":"28","resp_stddev":"3.41","call_count":"10","total_bytes_received":"18543","total_bytes_sent":"4620"},{"loadTestId":"729","startTime":"1424380451","transactionName":"3 - ReportingSvc - Email","resp_min":"18","resp_max":"27","resp_avg":"22","resp_median":"23","resp_pct75":"25","resp_pct90":"27","resp_stddev":"2.93","call_count":"10","total_bytes_received":"18791","total_bytes_sent":"4657"},{"loadTestId":"729","startTime":"1424380133","transactionName":"4 - ReportingSvc - Survey","resp_min":"14","resp_max":"24","resp_avg":"17","resp_median":"17","resp_pct75":"20","resp_pct90":"24","resp_stddev":"3.40","call_count":"7","total_bytes_received":"15012","total_bytes_sent":"3234"},{"loadTestId":"729","startTime":"1424380140","transactionName":"4 - ReportingSvc - Survey","resp_min":"13","resp_max":"26","resp_avg":"17","resp_median":"16","resp_pct75":"19","resp_pct90":"26","resp_stddev":"3.75","call_count":"13","total_bytes_received":"27857","total_bytes_sent":"6006"},{"loadTestId":"729","startTime":"1424380223","transactionName":"4 - ReportingSvc - Survey","resp_min":"14","resp_max":"28","resp_avg":"17","resp_median":"15","resp_pct75":"21","resp_pct90":"28","resp_stddev":"4.24","call_count":"10","total_bytes_received":"21439","total_bytes_sent":"4620"},{"loadTestId":"729","startTime":"1424380268","transactionName":"4 - ReportingSvc - Survey","resp_min":"13","resp_max":"28","resp_avg":"16","resp_median":"16","resp_pct75":"17","resp_pct90":"23","resp_stddev":"3.67","call_count":"17","total_bytes_received":"36439","total_bytes_sent":"7854"},{"loadTestId":"729","startTime":"1424380320","transactionName":"4 - ReportingSvc - Survey","resp_min":"12","resp_max":"21","resp_avg":"15","resp_median":"15","resp_pct75":"16","resp_pct90":"21","resp_stddev":"2.13","call_count":"13","total_bytes_received":"27864","total_bytes_sent":"6006"},{"loadTestId":"729","startTime":"1424380412","transactionName":"4 - ReportingSvc - Survey","resp_min":"11","resp_max":"19","resp_avg":"14","resp_median":"13","resp_pct75":"16","resp_pct90":"19","resp_stddev":"2.33","call_count":"10","total_bytes_received":"21437","total_bytes_sent":"4620"},{"loadTestId":"729","startTime":"1424380451","transactionName":"4 - ReportingSvc - Survey","resp_min":"12","resp_max":"34","resp_avg":"16","resp_median":"14","resp_pct75":"16","resp_pct90":"34","resp_stddev":"6.10","call_count":"10","total_bytes_received":"21436","total_bytes_sent":"4620"},{"loadTestId":"729","startTime":"1424380133","transactionName":"5 - ReportingSvc - SaveLocal","resp_min":"14","resp_max":"16","resp_avg":"15","resp_median":"16","resp_pct75":"16","resp_pct90":"16","resp_stddev":"0.70","call_count":"7","total_bytes_received":"21984","total_bytes_sent":"4233"},{"loadTestId":"729","startTime":"1424380140","transactionName":"5 - ReportingSvc - SaveLocal","resp_min":"13","resp_max":"18","resp_avg":"15","resp_median":"16","resp_pct75":"17","resp_pct90":"18","resp_stddev":"1.64","call_count":"13","total_bytes_received":"28960","total_bytes_sent":"6339"},{"loadTestId":"729","startTime":"1424380223","transactionName":"5 - ReportingSvc - SaveLocal","resp_min":"13","resp_max":"20","resp_avg":"15","resp_median":"15","resp_pct75":"17","resp_pct90":"20","resp_stddev":"1.95","call_count":"10","total_bytes_received":"20284","total_bytes_sent":"4620"},{"loadTestId":"729","startTime":"1424380268","transactionName":"5 - ReportingSvc - SaveLocal","resp_min":"13","resp_max":"28","resp_avg":"17","resp_median":"16","resp_pct75":"22","resp_pct90":"24","resp_stddev":"4.14","call_count":"17","total_bytes_received":"38792","total_bytes_sent":"8409"},{"loadTestId":"729","startTime":"1424380320","transactionName":"5 - ReportingSvc - SaveLocal","resp_min":"13","resp_max":"38","resp_avg":"17","resp_median":"15","resp_pct75":"17","resp_pct90":"38","resp_stddev":"6.53","call_count":"13","total_bytes_received":"33296","total_bytes_sent":"6894"},{"loadTestId":"729","startTime":"1424380412","transactionName":"5 - ReportingSvc - SaveLocal","resp_min":"11","resp_max":"15","resp_avg":"13","resp_median":"13","resp_pct75":"15","resp_pct90":"15","resp_stddev":"1.30","call_count":"10","total_bytes_received":"22878","total_bytes_sent":"4953"},{"loadTestId":"729","startTime":"1424380451","transactionName":"5 - ReportingSvc - SaveLocal","resp_min":"11","resp_max":"21","resp_avg":"16","resp_median":"15","resp_pct75":"20","resp_pct90":"21","resp_stddev":"3.07","call_count":"10","total_bytes_received":"23742","total_bytes_sent":"5064"},{"loadTestId":"729","startTime":"1424380133","transactionName":"6 - ReportingSvc - Events","resp_min":"12","resp_max":"16","resp_avg":"14","resp_median":"14","resp_pct75":"14","resp_pct90":"16","resp_stddev":"1.16","call_count":"7","total_bytes_received":"13868","total_bytes_sent":"3234"},{"loadTestId":"729","startTime":"1424380178","transactionName":"6 - ReportingSvc - Events","resp_min":"12","resp_max":"17","resp_avg":"14","resp_median":"15","resp_pct75":"15","resp_pct90":"17","resp_stddev":"1.22","call_count":"13","total_bytes_received":"25714","total_bytes_sent":"6006"},{"loadTestId":"729","startTime":"1424380223","transactionName":"6 - ReportingSvc - Events","resp_min":"13","resp_max":"37","resp_avg":"18","resp_median":"15","resp_pct75":"19","resp_pct90":"37","resp_stddev":"6.82","call_count":"10","total_bytes_received":"19768","total_bytes_sent":"4620"},{"loadTestId":"729","startTime":"1424380268","transactionName":"6 - ReportingSvc - Events","resp_min":"13","resp_max":"31","resp_avg":"17","resp_median":"16","resp_pct75":"18","resp_pct90":"26","resp_stddev":"4.53","call_count":"17","total_bytes_received":"33637","total_bytes_sent":"7854"},{"loadTestId":"729","startTime":"1424380320","transactionName":"6 - ReportingSvc - Events","resp_min":"13","resp_max":"47","resp_avg":"18","resp_median":"15","resp_pct75":"19","resp_pct90":"47","resp_stddev":"8.97","call_count":"13","total_bytes_received":"25722","total_bytes_sent":"6006"},{"loadTestId":"729","startTime":"1424380412","transactionName":"6 - ReportingSvc - Events","resp_min":"12","resp_max":"17","resp_avg":"14","resp_median":"14","resp_pct75":"15","resp_pct90":"17","resp_stddev":"1.48","call_count":"10","total_bytes_received":"19800","total_bytes_sent":"4620"},{"loadTestId":"729","startTime":"1424380451","transactionName":"6 - ReportingSvc - Events","resp_min":"12","resp_max":"19","resp_avg":"15","resp_median":"13","resp_pct75":"18","resp_pct90":"19","resp_stddev":"2.41","call_count":"10","total_bytes_received":"19777","total_bytes_sent":"4620"},{"loadTestId":"729","startTime":"1424380133","transactionName":"7 - ReportingSvc - Social","resp_min":"15","resp_max":"23","resp_avg":"18","resp_median":"17","resp_pct75":"22","resp_pct90":"23","resp_stddev":"3.00","call_count":"7","total_bytes_received":"38275","total_bytes_sent":"7082"},{"loadTestId":"729","startTime":"1424380141","transactionName":"7 - ReportingSvc - Social","resp_min":"16","resp_max":"26","resp_avg":"18","resp_median":"18","resp_pct75":"19","resp_pct90":"26","resp_stddev":"2.56","call_count":"13","total_bytes_received":"76523","total_bytes_sent":"13961"},{"loadTestId":"729","startTime":"1424380223","transactionName":"7 - ReportingSvc - Social","resp_min":"15","resp_max":"26","resp_avg":"19","resp_median":"19","resp_pct75":"24","resp_pct90":"26","resp_stddev":"3.56","call_count":"10","total_bytes_received":"50542","total_bytes_sent":"9504"},{"loadTestId":"729","startTime":"1424380268","transactionName":"7 - ReportingSvc - Social","resp_min":"15","resp_max":"20","resp_avg":"18","resp_median":"18","resp_pct75":"19","resp_pct90":"20","resp_stddev":"1.48","call_count":"17","total_bytes_received":"90342","total_bytes_sent":"16808"},{"loadTestId":"729","startTime":"1424380320","transactionName":"7 - ReportingSvc - Social","resp_min":"16","resp_max":"33","resp_avg":"19","resp_median":"18","resp_pct75":"21","resp_pct90":"33","resp_stddev":"4.30","call_count":"13","total_bytes_received":"70291","total_bytes_sent":"13036"},{"loadTestId":"729","startTime":"1424380412","transactionName":"7 - ReportingSvc - Social","resp_min":"13","resp_max":"21","resp_avg":"17","resp_median":"16","resp_pct75":"21","resp_pct90":"21","resp_stddev":"2.80","call_count":"10","total_bytes_received":"57780","total_bytes_sent":"10577"},{"loadTestId":"729","startTime":"1424380451","transactionName":"7 - ReportingSvc - Social","resp_min":"15","resp_max":"38","resp_avg":"20","resp_median":"17","resp_pct75":"26","resp_pct90":"38","resp_stddev":"6.86","call_count":"10","total_bytes_received":"52044","total_bytes_sent":"9726"},{"loadTestId":"729","startTime":"1424380133","transactionName":"8 - ReportingSvc - Coupon","resp_min":"26","resp_max":"111","resp_avg":"41","resp_median":"31","resp_pct75":"33","resp_pct90":"111","resp_stddev":"28.72","call_count":"7","total_bytes_received":"12107","total_bytes_sent":"3234"},{"loadTestId":"729","startTime":"1424380178","transactionName":"8 - ReportingSvc - Coupon","resp_min":"24","resp_max":"46","resp_avg":"34","resp_median":"34","resp_pct75":"38","resp_pct90":"46","resp_stddev":"6.28","call_count":"13","total_bytes_received":"22476","total_bytes_sent":"6006"},{"loadTestId":"729","startTime":"1424380232","transactionName":"8 - ReportingSvc - Coupon","resp_min":"25","resp_max":"104","resp_avg":"43","resp_median":"33","resp_pct75":"62","resp_pct90":"104","resp_stddev":"22.81","call_count":"10","total_bytes_received":"17287","total_bytes_sent":"4620"},{"loadTestId":"729","startTime":"1424380269","transactionName":"8 - ReportingSvc - Coupon","resp_min":"25","resp_max":"43","resp_avg":"34","resp_median":"34","resp_pct75":"41","resp_pct90":"41","resp_stddev":"4.85","call_count":"17","total_bytes_received":"29395","total_bytes_sent":"7854"},{"loadTestId":"729","startTime":"1424380320","transactionName":"8 - ReportingSvc - Coupon","resp_min":"27","resp_max":"43","resp_avg":"34","resp_median":"34","resp_pct75":"43","resp_pct90":"43","resp_stddev":"5.71","call_count":"13","total_bytes_received":"22482","total_bytes_sent":"6006"},{"loadTestId":"729","startTime":"1424380412","transactionName":"8 - ReportingSvc - Coupon","resp_min":"21","resp_max":"41","resp_avg":"31","resp_median":"29","resp_pct75":"39","resp_pct90":"41","resp_stddev":"5.55","call_count":"10","total_bytes_received":"17298","total_bytes_sent":"4620"},{"loadTestId":"729","startTime":"1424380451","transactionName":"8 - ReportingSvc - Coupon","resp_min":"27","resp_max":"66","resp_avg":"35","resp_median":"32","resp_pct75":"36","resp_pct90":"66","resp_stddev":"10.76","call_count":"10","total_bytes_received":"17291","total_bytes_sent":"4620"}]
        //
        // }});
    }

});
