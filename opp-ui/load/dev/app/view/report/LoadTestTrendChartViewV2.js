Ext.define('CCPerf.view.report.LoadTestTrendChartViewV2' ,{
   	extend: 'Ext.Panel',
    xtype: 'basic-line',
    alias: 'widget.chart-load-test-trend-v2',
    header:false,
    layout:'fit',
    requires: ['Ext.chart.plugin.ItemEvents'],
    loadTestId: '',
    items: [],
    // initComponent: function() {
    //     this.callParent(arguments);
    // },
    buildChart: function(chartData){
      var chart = {
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
                  title: this.getYAxisTitle(),
                  //title: 'Response Time (msec)', // gets overridden with yaxisTitle
                  // minorTickSteps: 1,
                  grid: true
              }, {
                  type: 'category',
                  position: 'bottom',
                  fields: ['xaxis'],
                  //majorTickSteps: 10,
                  title: this.getXAxisTitle(),
                  renderer: function (v) {
                      if (v.length > 20) {
                          this.font = "10px Arial, Helvetica, sans-serif";
                      }
                      return Ext.Date.format(new Date(v * 1000), 'm/d/y');
                  }
              }
          ],
          series: this.getChartSeries(chartData.series),
          store: this.getChartStore(chartData.modelFields, chartData.data)
      };
      this.add(Ext.create(chart));
    },
    getChartSeries: function(seriesData) {
        //var seriesData = this.getChartSeriesData();
        // set the styling
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
        var len = seriesData.length;
        for(var i=0; i<len; i++){
            seriesData[i].style=seriesStyle;
            seriesData[i].highlight=seriesHighlight;
            seriesData[i].marker=seriesMarker;
            seriesData[i].tooltip=seriesTooltip;
        }
        return seriesData;
    },
    getAttributeValue: function(attributeName, defaultVal){
      var val = defaultVal;
      if (this[attributeName] !== null && this[attributeName] !== undefined) {
          // is was passed in to this object
          val = this[attributeName];
      } else {
          var containerObj = this.up();
          if (containerObj !== undefined && containerObj[attributeName] !== null) {
              // it was passed into the parent container (e.g. - CSTPanel)
              val = containerObj[attributeName];
          }
      }
      return val;
    },
    getXAxisTitle: function() {
        return this.getAttributeValue('xaxisTitle', '');
    },
    getYAxisTitle: function() {
        return this.getAttributeValue('yaxisTitle', '');
    },
    getChart: function() {
        return this.items.items[0];
    },
    getChartStore: function(modelFields, storeData) {
        return Ext.create('Ext.data.Store', {
            fields: modelFields,
            data: storeData
        });
    }
});
