Ext.define('OppUI.view.loadtestDashboard.loadtestreport.chartdatagrid.ChartDataGridController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.chartdatagrid',

    formatData: function(fields, raw_data) {
		var formattedData = {};
		var index, xData, prev, curr, putIndex;
		var stop = fields.length;
		var fieldNames = ['name'];
		var fieldName;
        var view = this.getView();

		for(index = 1; index<stop; index++) {
			fieldName = fields[index];
			formattedData[fieldName] = {'name':fieldName};
			view.csv.push([fieldName]);
		}

		stop = raw_data.length;

		for(index = 0; index<stop; index++) {
			xData = raw_data[index].xaxis;
			fieldNames.push(xData);
			putIndex = 1;

			for(var field in formattedData) {
				curr = raw_data[index][field];
				if(curr == undefined) { curr = "N/A"; }

				if(index == 0) {
					prev = null;
				} else {
					prev = raw_data[index-1][field];
				}
				formattedData[field][xData] = this.showTrend(prev, curr, putIndex);
				view.csv[putIndex].push(curr);
				putIndex++;
			}
		}

		this.csvReorder();

		var dataArray = [];
		for(var key in formattedData) {
			dataArray.push(formattedData[key]);
		}

		view.setData(fieldNames, dataArray);

	},
	setData: function(fieldNames, dataArray) {
        var view = this.getView();
		var storeName = view.getLoadTestId() + "-" + view.getYAxis() + "-dataStore";
		Ext.create('Ext.data.Store', {
			storeId:storeName,
			fields: fieldNames,
			data: dataArray
		});
		view.store = Ext.data.StoreManager.lookup(storeName);
	},

    csvReorder: function() {
        var view = this.getView();
		var csv = view.csv;
		var temp = [];
		for(var index = 1; index < csv.length; index++) {
			temp = csv[index].slice(1, csv[index].length);
			temp.reverse();
			csv[index] = [csv[index][0]].concat(temp);
		}
	},
    getTrendColor: function(trendPct) {
        var view = this.getView();
		var color = 'black';
		var imgCls = '';

		if(trendPct > 0) {
			if(view.dataType === 'time') {
				color = "red"; // time is the opposite.
				imgCls = 'arrow-dr';
			} else {
				color = "green";
				imgCls = 'arrow-ug';
			}
		} else {
			if(view.dataType === 'time') {
				color = "green"; // time is the opposite.
				imgCls = 'arrow-ug';
			} else {
				color = "red";
				imgCls = 'arrow-dr';
			}
		}
		return {
			color: color,
			imageCls: imgCls
		};
	},

    showTrend: function(prev, curr, putIndex) {
        // set CSS
        var trendColor; var valColor = "black";
        var trendPct, trendVal;
        var view = this.getView();
        if(prev !== null && prev !== undefined && curr !== "N/A") {
		    trendVal = Math.round((curr-prev)/prev * 1000)/1000;
		    trendPct = Math.round(trendVal*10000)/100;
		    view.csv[putIndex].push(trendPct);
			// get trending color and image
		    trendColor = this.getTrendColor(trendPct);
		    // set quick tip
	        var quickTip = "<span style='padding-left:16px; color:"+trendColor.color+"' class='"+trendColor.imageCls+"'>("+trendPct+"%)</span>";
	        // returning html markup
	        return '<span style="color:'+valColor+'">'+curr+'</span>' + '<span ext:qwidth="150" ext:qtip="'+quickTip+'" style="margin-left: 10px; padding-left:16px; color:'+trendColor.color+'" class="'+trendColor.imageCls+'">'+trendPct+'%</span>';
    	} else {
    		trendPct = 0;
    		view.csv[putIndex].push(trendPct);

    		return '<span style="color:'+valColor+'">'+curr+'</span>';
    	}
	},

});
