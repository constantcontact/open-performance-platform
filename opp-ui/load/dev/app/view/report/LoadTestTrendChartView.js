Ext.define('CCPerf.view.report.LoadTestTrendChartView' ,{
   	extend: 'Ext.Panel',
    xtype: 'basic-line',
    alias: 'widget.chart-load-test-trend',
    header:false,
    layout:'fit',
    requires: ['Ext.chart.plugin.ItemEvents'],
    loadTestId: '',
    items: [
        {
            xtype: 'cartesian',
            width: '100%',
            height: 410,
            padding: '10 0 0 0',
            animate: true,
            shadow: false,
            insetPadding: 10,
            style: 'background: #fff;',
            legend: {
                docked: 'right'
            },
            //interactions: 'itemhighlight',
            plugins: {
                ptype: 'chartitemevents',
                moveEvents: true
            },
            axes: [
                {
                    type: 'numeric',
                    minimum: 0,
                    //       maximum:500,
                    position: 'left',
                    title: 'Response Time (msec)', // gets overridden with yaxisTitle
                    // minorTickSteps: 1,
                    grid: true
                }, {
                    type: 'category',
                    position: 'bottom',
                    fields: ['xaxis'],
                    //majorTickSteps: 10,
                    title: 'Run Date', // gets overridden with xaxisTitle
                    renderer: function (v) {
                        if (v.length > 20) {
                            this.font = "10px Arial, Helvetica, sans-serif";
                        }
                        return Ext.Date.format(new Date(v * 1000), 'm/d/y');
                    }
                }
            ]
        }
    ],
    initComponent: function() {

        var loadTestId = this.loadTestId;
        var chartParams = this.items[0];
        var chartContainer = this;
        // TODO: make this async.  This is harder than you think.  It becomes a race condition with multiple charts.  I guess this logic would need to move to the controller.
        Ext.Ajax.request({
            url: '/loadsvc/v1/charts/aggregate/loadtests/' + loadTestId + "?yaxis=" + this.yaxis,
            async: true,
            scope: chartContainer,
            disableCaching:false,
            success: function(response){
                var json = Ext.decode(response.responseText, false);
                this.setSeries(chartParams, json.chart.series);
                this.setStore(chartParams, json.chart.modelFields, json.chart.data);
                this.setYAxisTitle(chartParams, null);
                this.setXAxisTitle(chartParams, null);
                this.removeAll();
                this.add(Ext.create(chartParams));            }
        });

        this.callParent(arguments);
    },
    setSeries: function(chart, chartSeriesJson) {
        var seriesStyle = {lineWidth: 4};
        var seriesMarker = {radius: 4};
        var seriesHighlight = {fillStyle: '#000', radius: 5, lineWidth: 2, strokeStyle: '#fff'};
        var seriesTooltip = {  trackMouse: true, height:135, width:200,  padding:5, style: 'background: #fff',
            items: [
              { html: '<p>Dummy Text</p>' },
              { html:'<span style="color:#ff8809">Tip: Double-click data point to drill-in</span>' }
            ],
            renderer: function(storeItem, item) {
                var title = item.series.getYField();
                var textContainer = this.items.items[0];
                textContainer.setHtml('<p><b>Test Date:</b> '+ Ext.Date.format(new Date(item.record.data.xaxis*1000), 'm-d-Y') + '<br />' +
                '<b>'+item.field +':</b> ' + storeItem.get(title) + '</p>');

            }};

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
                if (this.up() !== undefined && this.up().xaxisTitle !== null) {
                    // it was passed into the parent container (e.g. - CSTPanel)
                    title = this.up().xaxisTitle;
                }
            }
            chart.axes[1].title = title;
        }
    },
    setYAxisTitle: function(chart, title) {
        if(!title){
            if(this.yaxisTitle !== null && this.yaxisTitle !== undefined){
                // is was passed in to this object
                title = this.yaxisTitle;
            } else {
                if(this.up() !== undefined && this.up().yaxisTitle !== null){
                    // it was passed into the parent container (e.g. - CSTPanel)
                    title = this.up().yaxisTitle;
                }
            }
        }
        chart.axes[0].title=title;
    },
    getChart: function() {
        return this.items.items[0];
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
      // todo: redo mock data.  Its currently using old _ format
        // Ext.apply(this.getChart(), {
        //     series: [{"type":"line","axis":"left","xField":"xaxis","yField":"NC_AjxCmpDeals"},{"type":"line","axis":"left","xField":"xaxis","yField":"NC_AjxCmpDonation"},{"type":"line","axis":"left","xField":"xaxis","yField":"NC_AjxCmpEvents"},{"type":"line","axis":"left","xField":"xaxis","yField":"NC_AjxCmpName"},{"type":"line","axis":"left","xField":"xaxis","yField":"NC_AjxCmpSocial"},{"type":"line","axis":"left","xField":"xaxis","yField":"NC_AjxCmpStatus"},{"type":"line","axis":"left","xField":"xaxis","yField":"NC_AjxCmpSurvey"},{"type":"line","axis":"left","xField":"xaxis","yField":"NC_AjxCmpTracking"},{"type":"line","axis":"left","xField":"xaxis","yField":"NC_CampAll (Campaign Page)"},{"type":"line","axis":"left","xField":"xaxis","yField":"NC_HP ( Home Page)"},{"type":"line","axis":"left","xField":"xaxis","yField":"NC_HT ( Home Tab)"},{"type":"line","axis":"left","xField":"xaxis","yField":"NC_Reporting"},{"type":"line","axis":"left","xField":"xaxis","yField":"PingSSO Login"}]
        // });
    },
    setMockStore: function(){
        // todo: redo mock data.  Its currently using old _ format
        // Ext.apply(this.getChart(), { store: {
        //     fields: ["xaxis","NC_AjxCmpDeals","NC_AjxCmpDonation","NC_AjxCmpEvents","NC_AjxCmpName","NC_AjxCmpSocial","NC_AjxCmpStatus","NC_AjxCmpSurvey","NC_AjxCmpTracking","NC_CampAll (Campaign Page)","NC_HP ( Home Page)","NC_HT ( Home Tab)","NC_Reporting","PingSSO Login"]
        //     ,data: [{"xaxis":"1407424996","NC_AjxCmpDeals":"304","NC_AjxCmpDonation":"712","NC_AjxCmpEvents":"246","NC_AjxCmpName":"90","NC_AjxCmpSocial":"3973","NC_AjxCmpStatus":"98","NC_AjxCmpSurvey":"3465","NC_AjxCmpTracking":"375","NC_CampAll (Campaign Page)":"2100","NC_HP ( Home Page)":"4405","NC_HT ( Home Tab)":"795","NC_Reporting":"1861","PingSSO Login":"955"},{"xaxis":"1407786606","NC_AjxCmpDeals":"381","NC_AjxCmpDonation":"816","NC_AjxCmpEvents":"251","NC_AjxCmpName":"133","NC_AjxCmpSocial":"3657","NC_AjxCmpStatus":"171","NC_AjxCmpSurvey":"3654","NC_AjxCmpTracking":"352","NC_CampAll (Campaign Page)":"2207","NC_HP ( Home Page)":"4570","NC_HT ( Home Tab)":"767","NC_Reporting":"2033","PingSSO Login":"949"},{"xaxis":"1408564262","NC_AjxCmpDeals":"318","NC_AjxCmpDonation":"700","NC_AjxCmpEvents":"264","NC_AjxCmpName":"79","NC_AjxCmpSocial":"2639","NC_AjxCmpStatus":"97","NC_AjxCmpSurvey":"3694","NC_AjxCmpTracking":"413","NC_CampAll (Campaign Page)":"1619","NC_HP ( Home Page)":"3581","NC_HT ( Home Tab)":"464","NC_Reporting":"1605","PingSSO Login":"633"},{"xaxis":"1408996349","NC_AjxCmpDeals":"287","NC_AjxCmpDonation":"739","NC_AjxCmpEvents":"256","NC_AjxCmpName":"87","NC_AjxCmpSocial":"2849","NC_AjxCmpStatus":"104","NC_AjxCmpSurvey":"3653","NC_AjxCmpTracking":"416","NC_CampAll (Campaign Page)":"1529","NC_HP ( Home Page)":"3821","NC_HT ( Home Tab)":"454","NC_Reporting":"1486","PingSSO Login":"655"},{"xaxis":"1409078401","NC_AjxCmpDeals":"291","NC_AjxCmpDonation":"704","NC_AjxCmpEvents":"285","NC_AjxCmpName":"86","NC_AjxCmpSocial":"3675","NC_AjxCmpStatus":"103","NC_AjxCmpSurvey":"3625","NC_AjxCmpTracking":"422","NC_CampAll (Campaign Page)":"1713","NC_HP ( Home Page)":"4007","NC_HT ( Home Tab)":"460","NC_Reporting":"1607","PingSSO Login":"659"},{"xaxis":"1409142481","NC_AjxCmpDeals":"282","NC_AjxCmpDonation":"672","NC_AjxCmpEvents":"278","NC_AjxCmpName":"85","NC_AjxCmpSocial":"4263","NC_AjxCmpStatus":"102","NC_AjxCmpSurvey":"3544","NC_AjxCmpTracking":"367","NC_CampAll (Campaign Page)":"1572","NC_HP ( Home Page)":"3784","NC_HT ( Home Tab)":"479","NC_Reporting":"1489","PingSSO Login":"658"},{"xaxis":"1410097984","NC_AjxCmpDeals":"298","NC_AjxCmpDonation":"668","NC_AjxCmpEvents":"233","NC_AjxCmpName":"85","NC_AjxCmpSocial":"3054","NC_AjxCmpStatus":"114","NC_AjxCmpSurvey":"3500","NC_AjxCmpTracking":"95","NC_CampAll (Campaign Page)":"1642","NC_HP ( Home Page)":"3870","NC_HT ( Home Tab)":"485","NC_Reporting":"1635","PingSSO Login":"726"},{"xaxis":"1410702763","NC_AjxCmpDeals":"286","NC_AjxCmpDonation":"673","NC_AjxCmpEvents":"226","NC_AjxCmpName":"88","NC_AjxCmpSocial":"1752","NC_AjxCmpStatus":"105","NC_AjxCmpSurvey":"3415","NC_AjxCmpTracking":"96","NC_CampAll (Campaign Page)":"1563","NC_HP ( Home Page)":"3802","NC_HT ( Home Tab)":"498","NC_Reporting":"1558","PingSSO Login":"703"},{"xaxis":"1412792340","NC_AjxCmpDeals":"388","NC_AjxCmpDonation":"817","NC_AjxCmpEvents":"355","NC_AjxCmpName":"89","NC_AjxCmpSocial":"3935","NC_AjxCmpStatus":"144","NC_AjxCmpSurvey":"1654","NC_AjxCmpTracking":"99","NC_CampAll (Campaign Page)":"1872","NC_HP ( Home Page)":"4196","NC_HT ( Home Tab)":"465","NC_Reporting":"1751","PingSSO Login":"716"},{"xaxis":"1413726737","NC_AjxCmpDeals":"332","NC_AjxCmpDonation":"727","NC_AjxCmpEvents":"231","NC_AjxCmpName":"89","NC_AjxCmpSocial":"2948","NC_AjxCmpStatus":"142","NC_AjxCmpSurvey":"1730","NC_AjxCmpTracking":"94","NC_CampAll (Campaign Page)":"1653","NC_HP ( Home Page)":"4095","NC_HT ( Home Tab)":"430","NC_Reporting":"1580","PingSSO Login":"673"}]
        // }});
    }
});
