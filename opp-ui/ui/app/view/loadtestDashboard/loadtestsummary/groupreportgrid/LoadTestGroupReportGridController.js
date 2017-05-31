Ext.define('OppUI.view.loadtestDashboard.loadtestsummary.groupreportgrid.LoadTestGroupReportGridController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.loadtestgroupreportgrid',

    showTrend: function(v, rec, trendName, conversion, trendType) {
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

        if(trendType == "time") {
            // lower is better
            if(trendVal<0) { cls = "arrow-ug"; color="green";}
            else if (trendVal>0) { cls = "arrow-dr"; color='red'; }
            else { cls = ""; color="black"; }
        } else {
            // higher is better
            if(trendVal>0) { cls = "arrow-ug"; color="green";}
            else if (trendVal<0) { cls = "arrow-dr"; color='red'; }
            else { cls = ""; color="black"; }
        }
        // don't need decimal places for counts
        if(trendType == "count") v = Math.round(v);

        var quickTip = "<span style='padding-left:16px; color:"+color+"' class='"+cls+"'>" + trendVal + " ("+trendPct+"%)</span>";
        // returning html markup
        return v + '<span ext:qwidth="150" ext:qtip="'+quickTip+'" style="margin-left: 10px; padding-left:16px; color:'+color+'" class="'+cls+'">'+trendPct+'%</span>';
    }
    
});
