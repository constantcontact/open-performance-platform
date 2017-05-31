Ext.define('OppUI.view.loadtestDashboard.loadtestreport.loadtestreportsummarygrid.LoadTestReportSummaryGridController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.loadtestreportsummarygrid',

    onRemoteAggDataLoad: function() {
        
    },

    processSLA: function(val, sla, errMargin){
        var arrRes = new Array();
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
        arrRes['slaClass'] = slaClass;
        arrRes['slaTooltip'] = slaTooltip;
        arrRes['valToSlaPerc'] = valToSlaPerc;
        arrRes['valToSla'] = valToSla;
        return arrRes;
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
    convertToMb: function(bytes){
        return Math.round((bytes/1048576)*1000)/1000;
    },
    convertToSec: function(ms){
        return Math.round((ms/1000)*1000)/1000;
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
    },
    getWikiMarkup: function() {
        var view = this.getView();
        // get all column data
    	colData = view.columns;
	    cols = '||';
	    for(i=0; i<colData.length; i++) {
            cols += colData[i].text.replace(' <br />', '<br />').replace('<br /> ', '<br />').replace('<br />', '\\\\') + '||';
	    }
	    cols += '<br />';
	    
	    // get all row data
	    data = view.store.data.items;
	    rows = "";
	    // loop through rows
	    for(i=0; i<data.length; i++) {
	    	//rows += '|' + data[i].raw.join('|') + '|<br />';
	    	// loop through data in each row
	    	 for (var p in data[i].data) {
		        if (data[i].data.hasOwnProperty(p)) {
		            activeColumn = false; // flag for active columns only
		            // loop through colunns to only export data that is mapped to a column
		            for(j=0; j<colData.length; j++){
	    	 		   if(colData[j].dataIndex == p) {
	    	 		   		activeColumn = true;
	    	 		   		break;
	    	 		   }
	    	 		}
	    	 		// if data is in a column, export the data
		            if(activeColumn) {
			            val = data[i].data[p];
			            if(val === null) val = '';
			            rows += '|' + val;
		            }
		        }
		    }
		    rows += '|<br />'; // end row
	    }
	    return cols + rows; // merge columns and rows
    }
});
