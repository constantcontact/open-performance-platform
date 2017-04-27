
Ext.define('OppUI.view.loadtestDashboard.loadtestreport.loadtestreportsummary.LoadTestReportSummary',{
    extend: 'Ext.grid.Panel',
    alias: 'widget.loadtestreportsummary',

    requires: [
        'OppUI.view.loadtestDashboard.loadtestreport.loadtestreportsummary.LoadTestReportSummaryController',
        'OppUI.view.loadtestDashboard.loadtestreport.loadtestreportsummary.LoadTestReportSummaryModel'
    ],

    controller: 'loadtestreportsummary',
    viewModel: {
        type: 'loadtestreportsummary'
    },
    bind: {
        store: '{remoteAggData}'
    },
    title: 'Load Test Summary',

    config: {
        tools: [{
            type:'print',
            listeners: {
                afterrender: function(me) {
                    // Ext.tip.QuickTipManager.register({
                    //     target: me.getId(),
                    //     title: 'Print',
                    //     text: 'Print this graph'
                    // });
                }
            },
            handler: function(event, element, view){
                var chart, grid, markup;

                grid = view.up('loadtestreportsummary');
                markup = grid.getWikiMarkup();
                Ext.Msg.alert('Wiki Markup', markup);
            }
        },{
            type:'maximize',
            handler: function(event, element, view){

                var window = Ext.create('Ext.window.Window', {
                    layout: 'fit',
                    height: Ext.getBody().getViewSize().height - 100,
                    width: Ext.getBody().getViewSize().width - 100,
                    maximizable: true,
                    items: [
                        {
                            xtype: 'loadtestreportsummary',      
                            header: false,
                            collapsible: false
                        }
                    ],
                    tools: [{
                        type:'print',
                        handler: function(event, element, view){
                            var chart, grid, markup;

                            grid = view.up('loadtestreportsummary');
                            markup = grid.getWikiMarkup();
                            Ext.Msg.alert('Wiki Markup', markup);
                        }
                    }]
                });
                
                // add the window to the parent loadTestReport
                // so the window will share the view viewmodel.
                view.up('loadtestreport').add(window).showAt();
            }
        }]
    },

    columns:[
        {text: 'Transaction', dataIndex: "transaction_name", width: 180, flex: 1 },
            {text: 'Min (s)', dataIndex: "resp_min", width: 75, 
                renderer: function(v, meta, rec, rowIndex, colIndex, store){ 
                    var sla = rec.data.sla_min;
                    var val = this.convertToSec(v);
                    var slaData = this.processSLA(v, sla, rec.data.sla_margin_of_error);
                    //return "<div>"+val+"</div>";
                   // meta.tdAttr = 'data-qtip="' + slaData['slaTooltip'] + '" class="' + slaData['slaClass'] + '"';
                    return "<div class='" + slaData['slaClass'] + "' data-qtip='"+slaData['slaTooltip']+"'>"+val+"</div>";
                }, flex: 1 
            },
            {text: 'Max (s)', dataIndex: "resp_max", width: 75, 
                renderer: function(v, meta, rec, rowIndex, colIndex, store){ 
                    var sla = rec.data.sla_max;
                    var val = this.convertToSec(v);
                    var slaData = this.processSLA(v, sla, rec.data.sla_margin_of_error);
                    //return "<div>"+val+"</div>";
                    return "<div class='" + slaData['slaClass'] + "' data-qtip='"+slaData['slaTooltip']+"'>"+val+"</div>";
                }, flex: 1 
            },
            {text: 'Avg (s)', dataIndex: "resp_avg", width: 75, 
                renderer: function(v, meta, rec, rowIndex, colIndex, store){ 
                    var sla = rec.data.sla_avg;
                    var val = this.convertToSec(v);
                    var slaData = this.processSLA(v, sla, rec.data.sla_margin_of_error);
                    //return "<div>"+val+"</div>";
                    return "<div class='" + slaData['slaClass'] + "' data-qtip='"+slaData['slaTooltip']+"'>"+val+"</div>";
                }, flex: 1 
            },
            {text: 'Median (s)', dataIndex: "resp_median",  
                renderer: function(v, meta, rec, rowIndex, colIndex, store){ 
                    var sla = rec.data.sla_median;
                    var val = this.convertToSec(v);
                    var slaData = this.processSLA(v, sla, rec.data.sla_margin_of_error);
                    //return "<div>"+val+"</div>";
                    return "<div class='" + slaData['slaClass'] + "' data-qtip='"+slaData['slaTooltip']+"'>"+val+"</div>";
                }, flex: 1 
            },
            {text: '75th PCT (s)', dataIndex: "resp_pct75", 
                renderer: function(v, meta, rec, rowIndex, colIndex, store){ 
                    var sla = rec.data.sla_pct75;
                    var val = this.convertToSec(v);
                    var slaData = this.processSLA(v, sla, rec.data.sla_margin_of_error);
                    //return "<div>"+val+"</div>";
                    return "<div class='" + slaData['slaClass'] + "' data-qtip='"+slaData['slaTooltip']+"'>"+val+"</div>";
                }, flex: 1 
            },
            {text: '90th PCT (s)', dataIndex: "resp_pct90", 
                renderer: function(v, meta, rec, rowIndex, colIndex, store){ 
                    var sla = rec.data.sla_pct90;
                    var val = this.convertToSec(v);
                    var slaData = this.processSLA(v, sla, rec.data.sla_margin_of_error);
                    //return "<div>"+val+"</div>";
                    return "<div class='" + slaData['slaClass'] + "' data-qtip='"+slaData['slaTooltip']+"'>"+val+"</div>";
                }, flex: 1 
            },
            {text: 'Std Dev (s)', dataIndex: "resp_stddev",
                renderer: function(v, meta, rec, rowIndex, colIndex, store){ 
                    var avg = Number(rec.data.resp_avg);
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
                }, flex: 1 
            },
            {text: 'Call Count', dataIndex: "call_count", flex: 1 },
            {text: 'MB Received', dataIndex: "total_bytes_received", renderer: function(v){ return this.convertToMb(v)}, flex: 1  },
            {text: 'MB Sent', dataIndex: "total_bytes_sent", renderer: function(v){ return this.convertToMb(v)}, flex: 1  },
            {text: 'TPS (Median)', dataIndex: "tps_median", renderer: function(v){ return v; }, flex: 1  },
            {text: 'TPS (Max)', dataIndex: "tps_max", renderer: function(v){ return v; }, flex: 1  },
            {text: 'Errors', dataIndex: "error_count", renderer: function(v, meta, rec){
                // set sla to .05% of total number of calls
                var slaData = this.processSLA(v, rec.data.call_count *.005, 0);
                var tooltip = slaData['slaTooltip'].replace('sec ', ' ').replace('the SLA.', 'the SLA error rate of 0.5%'); // quick hack so I can reuse sla function
                return "<div class='" + slaData['slaClass'] + "' data-qtip='"+tooltip+"'>"+v+"</div>";
            }, flex: 1  
        }
    ],

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
        // get all column data
    	colData = this.columns;
	    cols = '||';
	    for(i=0; i<colData.length; i++) {
            cols += colData[i].text.replace(' <br />', '<br />').replace('<br /> ', '<br />').replace('<br />', '\\\\') + '||';
	    }
	    cols += '<br />';
	    
	    // get all row data
	    data = this.store.data.items;
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
