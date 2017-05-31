Ext.define('OppUI.view.loadtestDashboard.loadtestsummary.LoadTestSummaryController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.loadtestsummary',

    showGroupReportForm: function() {
        var window, store;

         store = this.getView()
                    .up('loadtest')
                    .getViewModel()
                    .getStore('remoteSummaryTrendFilter');

        window = Ext.create('Ext.window.Window', {
            title: 'Grouped Report Creator',
            layout: 'vbox',
            itemId: 'createGroupReportForm',
            height:500,
            autoScroll:true,
            autoDestroy: true,
            closeAction: 'destroy',
            items: {  xtype:'loadtestgroupreportform' }
        });

       

        // clear the grid when the build group
        // report button is clicked. 
        store.filterBy(function(record){
            return undefined;
        });

        //this.getView().up('loadtest').add(window);
        //this.getView().up('loadtestsummarytab').add(window);
        window.show();
    },

    search: function() {},
    specialkey: function(field, e) {
        if(e.getKey() === window.parseInt(e.ENTER)) {
            var searchString, store, view;

            searchString = field.getValue();
            view = this.getView();

            store = view.up('loadtest')
                        .getViewModel()
                        .getStore('remoteSummaryTrend');
            
            if (!searchString || searchString.length === 0) {
                //this.up('grid').getController().reBuildGridByCurrentFilterState();
                store.clearFilter();
            } else {
                store.filterBy(function(record) {
                    var app, env; 

                    app = record.get('appUnderTest');
                    env = record.get('environment');
 
                    if (app.indexOf(searchString) >= 0 ||
                        env.indexOf(searchString) >= 0) {
                        return true;
                    }

                    return false;
                });
            }
        }
    },
    showTrend: function(v, rec, trendName, conversion, trendType){
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
        var cls = ""; var color = ""; var red = '#e15757'; var green = '#3dae3d';

        if(trendType == "time" || trendType == "errors") {
        	// lower is better
	        if(trendVal<0) { cls = "arrow-ug"; color=green;}
	        if (trendVal>0) { cls = "arrow-dr"; color=red; }
            
            if(trendType == "errors"){
                if(v > 0) valColor = 'DarkOrange'; // if any errors, make orange
                if(v > rec.data.callCount * 0.005) valColor = red; // if error rate is greater than 0.5% make red
            }
        } else {
        	// higher is better
        	if(trendVal>0) { cls = "arrow-ug"; color=green;}
	        if (trendVal<0) { cls = "arrow-dr"; color=red; }
        }
        var quickTip = "<span style='padding-left:16px; color:"+color+"' class='"+cls+"'>" + trendVal + " ("+trendPct+"%)</span>";
        var colorCss = (color === '') ? color : 'color:'+color;
        // returning html markup
        return v + '<span ext:qwidth="150" ext:qtip="'+quickTip+'" style="margin-left: 10px; padding-left:16px; '+colorCss+'" class="'+cls+'">'+trendPct+'%</span>';
    },

    calculateDuration: function(start, end){
        var duration = "";
        if(end !== null) {
            var durationSec = end/1000 - start/1000; // get the diff in sec
            if(durationSec > 86400){
                duration = Math.floor(durationSec/86400) + ' d ' + Math.round((durationSec % 86400)/60/60) + ' h'; // calculate hours and minutes
            }
            else if(durationSec > 3600){
                duration = Math.floor(durationSec/3600) + ' h ' + Math.round((durationSec % 3600)/60) + ' m'; // calculate hours and minutes
            } else {
                duration = Math.floor(durationSec/60) + ' m ' + durationSec % 60 + ' s'; // calculate minutes and seconds
            }
        }
        return duration;
    },

    loadTestSelected: function(grid, record, item, index) {
        this.getView().up('loadtest').down('loadtestsummarytab').getController().createTab(grid, record, item, index);
    },

    deleteButtonClicked: function(button) {
        var grid, i, ids;

        function _handleInput(btn) {
            if(btn == 'yes') {
                grid.deleteRecords(ids);
            }
        }

        grid = button.up('grid');
        if(grid.getSelectionModel().selected.items.length > 0){
            ids = new Array(); 
            for(i=0; i<grid.getSelectionModel().selected.items.length; i++){
                ids.push(grid.getSelectionModel().selected.items[i].data.load_test_id);
            }
            Ext.MessageBox.confirm('Confirm', 'Are you sure you want to delete ' + ids.length + ' record(s)', _handleInput);
        } else {
            Ext.Msg.alert("Warning...", "You must make a selection first");
        }
    },

    deleteRecords: function(ids) {
        Ext.Ajax.request({
            url: '/loadsvc/v1/loadtests/' + ids.join(),
            method:'delete',
            scope: this,
            success: function(response){
                //var json = Ext.decode(response.responseText, false);
                this.getView().up('loadtest').getViewModel().getStore('remoteSummaryTrend').reload();
            },
            failure: function(response) {
                Ext.Msg.alert("Error...", "Error processing deletion. Please Try again Later.");
            }
        });
    },
    
});
