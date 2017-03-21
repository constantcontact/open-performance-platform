Ext.define('CCPerf.view.report.LoadTestAggregatesGrid' ,{
    extend: 'Ext.grid.Panel',
    alias: 'widget.grid-loadtest-aggregates',
    columnLines: true,
    itemId: 'loadtest-summary-grid',
    header:false,
    animCollapse: false,
    margin: '0 0 20 0',
    loadTestId: '',
    store: {
      fields: [ 'transactionName', 'callCount', 'respAvg', 'respMedian',
        'respMin', 'respMax', 'respPct75', 'respPct90',  'tpsMedian',  'tpsMax',
        'totalBytesReceived', 'totalBytesSent', 'respStddev', 'errorCount'
      ],
      data:[] // passed in
    },
    columns: [
        //{text: 'Trend', dataIndex:'trend', renderer: function(v) { return this.showTrend(v); }, width:100},
        {text: 'Transaction', dataIndex: "transactionName", width: 180 },
        {text: 'Min (s)', dataIndex: "respMin", width: 75,
            renderer: function(v, meta, rec, rowIndex, colIndex, store){
                var sla = rec.data.slaMin;
                var val = this.convertToSec(v);
                var slaData = this.processSLA(v, sla, rec.data.slaMarginOfError);
                //return "<div>"+val+"</div>";
               // meta.tdAttr = 'data-qtip="' + slaData['slaTooltip'] + '" class="' + slaData['slaClass'] + '"';
                return "<div class='" + slaData.slaClass + "' data-qtip='"+slaData.slaTooltip+"'>"+val+"</div>";
            }
        },
        {text: 'Max (s)', dataIndex: "respMax", width: 75,
            renderer: function(v, meta, rec, rowIndex, colIndex, store){
                var sla = rec.data.slaMax;
                var val = this.convertToSec(v);
                var slaData = this.processSLA(v, sla, rec.data.slaMarginOfError);
                //return "<div>"+val+"</div>";
                return "<div class='" + slaData.slaClass + "' data-qtip='"+slaData.slaTooltip+"'>"+val+"</div>";
            }
        },
        {text: 'Avg (s)', dataIndex: "respAvg", width: 75,
            renderer: function(v, meta, rec, rowIndex, colIndex, store){
                var sla = rec.data.slaAvg;
                var val = this.convertToSec(v);
                var slaData = this.processSLA(v, sla, rec.data.slaMarginOfError);
                //return "<div>"+val+"</div>";
                return "<div class='" + slaData.slaClass + "' data-qtip='"+slaData.slaTooltip+"'>"+val+"</div>";
            }
        },
        {text: 'Median (s)', dataIndex: "respMedian",
            renderer: function(v, meta, rec, rowIndex, colIndex, store){
                var sla = rec.data.slaMedian;
                var val = this.convertToSec(v);
                var slaData = this.processSLA(v, sla, rec.data.slaMarginOfError);
                //return "<div>"+val+"</div>";
                return "<div class='" + slaData.slaClass + "' data-qtip='"+slaData.slaTooltip+"'>"+val+"</div>";
            }
        },
        {text: '75th PCT (s)', dataIndex: "respPct75",
            renderer: function(v, meta, rec, rowIndex, colIndex, store){
                var sla = rec.data.slaPct75;
                var val = this.convertToSec(v);
                var slaData = this.processSLA(v, sla, rec.data.slaMarginOfError);
                //return "<div>"+val+"</div>";
                return "<div class='" + slaData.slaClass + "' data-qtip='"+slaData.slaTooltip+"'>"+val+"</div>";
            }
        },
        {text: '90th PCT (s)', dataIndex: "respPct90",
            renderer: function(v, meta, rec, rowIndex, colIndex, store){
                var sla = rec.data.slaPct90;
                var val = this.convertToSec(v);
                var slaData = this.processSLA(v, sla, rec.data.slaMarginOfError);
                //return "<div>"+val+"</div>";
                return "<div class='" + slaData.slaClass + "' data-qtip='"+slaData.slaTooltip+"'>"+val+"</div>";
            }
        },
        {text: 'Std Dev (s)', dataIndex: "respStddev",
            renderer: function(v, meta, rec, rowIndex, colIndex, store){
                var avg = Number(rec.data.respAvg);
                v = Number(v);
                var stdClass = "sla-pass";
                var stdToolTip = "Standard deviation is within normal range";
                // if greater than 1/2 the mean, we could have an issue
                if(v >= (avg/2)) {
                    stdClass = "sla-warning";
                    stdToolTip = "Standard deviation is greater than 1/2 the mean. This might be suspect.";
                }
                // if greater than the mean, things are probably pretty bad
                if(v >= avg) {
                    stdClass = "sla-fail";
                    stdToolTip = "Standard deviation is greater than the mean. This might be statistically invalid.";
                }

                return "<div class='" + stdClass + "' data-qtip='"+stdToolTip+"'>"+this.convertToSec(v)+"</div>";
            }
        },
        {text: 'Call Count', dataIndex: "callCount"},
        {text: 'MB Received', dataIndex: "totalBytesReceived", renderer: function(v){ return this.convertToMb(v); } },
        {text: 'MB Sent', dataIndex: "totalBytesSent", renderer: function(v){ return this.convertToMb(v); } },
        {text: 'TPS (Median)', dataIndex: "tpsMedian", renderer: function(v){ return v; } },
        {text: 'TPS (Max)', dataIndex: "tpsMax", renderer: function(v){ return v; } },
        {text: 'Errors', dataIndex: "errorCount", renderer: function(v, meta, rec){
            // set sla to .05% of total number of calls
            var slaData = this.processSLA(v, rec.data.callCount * 0.005, 0);
            var tooltip = slaData.slaTooltip.replace('sec ', ' ').replace('the SLA.', 'the SLA error rate of 0.5%'); // quick hack so I can reuse sla function
            return "<div class='" + slaData.slaClass + "' data-qtip='"+tooltip+"'>"+v+"</div>";
        } }
   ],
    initComponent: function() {
        var grid = this;
      //  this.enableLocking=true;

        this.callParent(arguments);
    },
    convertToMb: function(bytes){
        return Math.round((bytes/1048576)*1000)/1000;
    },
    convertToSec: function(ms){
        return Math.round((ms/1000)*1000)/1000;
    },
    buildSlaToolTip: function(sla, valToSla, valToSlaPerc, errMargin, slaClass) {
        var status = "";
        var aboveBelow = "";
        switch(slaClass){
            case "sla-pass":
                status = "passed";
                aboveBelow = "below";
                break;
            case "sla-warning":
                status = " was exceeded but the response time stayed within the " + errMargin + "% margin of error";
                aboveBelow = "above";
                break;
            case "sla-fail":
                status = "failed";
                aboveBelow = "above";
                break;

        }
        return "The " + this.convertToSec(sla) + "sec SLA "+status+". The transaction was " +
                this.convertToSec(valToSla) + "s ("+ valToSlaPerc + "%) "+aboveBelow+" the SLA.";
    },
    processSLA: function(val, sla, errMargin){
        var arrRes = [];
        var slaClass = "";
        var valToSlaPerc = 0;
        var valToSla = 0;
        var slaTooltip = "";
        if(errMargin === undefined || errMargin === null) errMargin = 10; // default 10%
        if(sla !== null) {
            valToSlaPerc = Math.round((((val/sla)-1) * 100)*100)/100; // value percentage of SLA
            if(valToSlaPerc < 0) valToSlaPerc = valToSlaPerc * -1; // make percentage positive
            valToSla = val - sla; // below 0 is below SLA (good)
            if(valToSla <= 0){
                slaClass = "sla-pass";
                slaTooltip = this.buildSlaToolTip(sla, valToSla, valToSlaPerc, errMargin, slaClass);
            } else {
                // if the val is less than or equal to 110% of the SLA make orange
                if(valToSlaPerc < errMargin){
                    slaClass = "sla-warning";
                    slaTooltip = this.buildSlaToolTip(sla, valToSla, valToSlaPerc, errMargin, slaClass);
                } else {
                    // val is greater than the 10% margin of error
                    slaClass = "sla-fail";
                    slaTooltip = this.buildSlaToolTip(sla, valToSla, valToSlaPerc, errMargin, slaClass);
                }
            }
        } else {
            slaClass = "sla-missing";
            slaTooltip = "No SLA set. Click on the SLA bar above this grid to set your SLAs.";
        }
        arrRes.slaClass = slaClass;
        arrRes.slaTooltip = slaTooltip;
        arrRes.valToSlaPerc = valToSlaPerc;
        arrRes.valToSla = valToSla;
        return arrRes;

    },
    showTrend: function(v, rec, trendName, conversion){
        // getting pipe delimited trending value (val and percentage)
        var trendValArr = rec.data[trendName].split("|");
        var trendVal = trendValArr[0].trim();
        var trendPct = trendValArr[1].trim().replace('%', '');
        // convert if necessary
        if(conversion === "sec"){
            v = Math.round((v/1000)*1000)/1000;
            trendVal = Math.round((trendVal/1000)*1000)/1000;
        }
        if(conversion === "mb"){
            v = Math.round((v/1048576)*1000)/1000;
            trendVal = Math.round((trendVal/1048576)*1000)/1000;
        }
        // round the percent
        trendPct = Math.round(trendPct*100)/100;
        // set CSS
        var cls = ""; var color = "black";
        if(trendVal<0) { cls = "arrow-ug"; color="green";}
        else if (trendVal>0) { cls = "arrow-dr"; color='red'; }
        else { cls = ""; color="black"; }
        var quickTip = "<span style='padding-left:16px; color:"+color+"' class='"+cls+"'>" + trendVal + " ("+trendPct+"%)</span>";
        // returning html markup
        return v + '<span ext:qwidth="150" ext:qtip="'+quickTip+'" style="margin-left: 10px; padding-left:16px; color:'+color+'" class="'+cls+'">'+trendPct+'%</span>';
    }
});
