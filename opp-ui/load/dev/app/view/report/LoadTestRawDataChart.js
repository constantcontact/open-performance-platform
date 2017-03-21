Ext.define('CCPerf.view.report.LoadTestRawDataChart', {
	extend: 'Ext.grid.Panel',
	alias: 'widget.chart-load-test-data-grid',
	header: false,
	layout: 'fit',
	loadTestId: '',
	yaxis: '',
	dataType: 'time',
	multiColumnSort: true,
	data: [],
	columns: [
		{text: 'Name', dataIndex:'name', width:300}
	],
	csv: [],
	setColumns: function(data) {
		var index, date, seconds, dateString;
		var stop = data.length;

		// reset CSV data
		this.csv = [['Name']];

		for(index = stop-1; index >= 0; index--) {
			seconds = parseInt(data[index].xaxis);
			date = new Date(seconds * 1000);
			dateString = (date.getMonth()+1) + "/" + date.getDate() + "/" + date.getFullYear();
			this.columns.push({text: dateString, dataIndex:seconds, sortable:false, width:200});
			this.csv[0].push(dateString);
			this.csv[0].push(dateString + " trend");
		}
	},
	formatData: function(fields, raw_data) {
		var formattedData = {};
		var index, xData, prev, curr, putIndex;
		var stop = fields.length;
		var fieldNames = ['name'];
		var fieldName;
		for(index = 1; index<stop; index++) {
			fieldName = fields[index];
			formattedData[fieldName] = {'name':fieldName};
			this.csv.push([fieldName]);
		}

		stop = raw_data.length;

		for(index = 0; index<stop; index++) {
			xData = raw_data[index].xaxis;
			fieldNames.push(xData);
			putIndex = 1;

			for(var field in formattedData) {
				curr = raw_data[index][field];
				if(curr === undefined) { curr = "N/A"; }

				if(index === 0) {
					prev = null;
				} else {
					prev = raw_data[index-1][field];
				}
				formattedData[field][xData] = this.showTrend(prev, curr, putIndex);
				this.csv[putIndex].push(curr);
				putIndex++;
			}
		}

		this.csvReorder();

		var dataArray = [];
		for(var key in formattedData) {
			dataArray.push(formattedData[key]);
		}

		this.setData(fieldNames, dataArray);

	},
	setData: function(fieldNames, dataArray) {
		var storeName = this.loadTestId + "-" + this.yaxis + "-dataStore";
		Ext.create('Ext.data.Store', {
			storeId:storeName,
			fields: fieldNames,
			data: dataArray
		});
		this.store = Ext.data.StoreManager.lookup(storeName);
	},
	showTrend: function(prev, curr, putIndex) {
        // set CSS
        var trendColor; var valColor = "black";
        var trendPct, trendVal;
        if(prev !== null && prev !== undefined && curr !== "N/A") {
		    trendVal = Math.round((curr-prev)/prev * 1000)/1000;
		    trendPct = Math.round(trendVal*10000)/100;
		    this.csv[putIndex].push(trendPct);
			// get trending color and image
		    trendColor = this.getTrendColor(trendPct);
		    // set quick tip
	        var quickTip = "<span style='padding-left:16px; color:"+trendColor.color+"' class='"+trendColor.imageCls+"'>("+trendPct+"%)</span>";
	        // returning html markup
	        return '<span style="color:'+valColor+'">'+curr+'</span>' + '<span ext:qwidth="150" ext:qtip="'+quickTip+'" style="margin-left: 10px; padding-left:16px; color:'+trendColor.color+'" class="'+trendColor.imageCls+'">'+trendPct+'%</span>';
    	} else {
    		trendPct = 0;
    		this.csv[putIndex].push(trendPct);

    		return '<span style="color:'+valColor+'">'+curr+'</span>';
    	}
	},
	getCsv: function() {
		var output = "";
		for(var rowIndex = 0; rowIndex < this.csv.length; rowIndex++) {
			var row = this.csv[rowIndex];
			for(var column = 0; column < row.length; column++) {
				output += row[column] + ",";
			}
			output = output.substring(0, output.length-1) + "\n";
		}
		return output;
	},
	getWikiFormat: function() {
		var output = "||";
		var delimiter = "||";
		var trendPct;
		var increment;
		var trendColor;
		for(var rowIndex = 0; rowIndex < this.csv.length; rowIndex++) {
			var row = this.csv[rowIndex];

			if(rowIndex == 1) {
				delimiter = "|";
				increment = 2;
			}

			output += row[0] + delimiter;

			for(var column = 1; column < row.length; column+=2) {
				output += row[column];

				// get trend data
				if(delimiter == "|") {
					trendPct = parseFloat(row[column+1]);
					console.log(this.getTrendColor(trendPct));
					trendColor = this.getTrendColor(trendPct);
					output += " <span style='color:" + trendColor.color + ";'>(" + trendPct + "%)</span>";
				}

				output += delimiter;
			}

			output += "<br />|";
		}

		return output.substring(0, output.length-1);
	},
	getTrendColor: function(trendPct) {
		var color = 'black';
		var imgCls = '';

		if(trendPct > 0) {
			if(this.dataType === 'time') {
				color = "red"; // time is the opposite.
				imgCls = 'arrow-dr';
			} else {
				color = "green";
				imgCls = 'arrow-ug';
			}
		} else {
			if(this.dataType === 'time') {
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
	csvReorder: function() {
		var csv = this.csv;
		var temp = [];
		for(var index = 1; index < csv.length; index++) {
			temp = csv[index].slice(1, csv[index].length);
			temp.reverse();
			this.csv[index] = [csv[index][0]].concat(temp);
		}
	},
	initComponent: function() {
		this.columns = [{text: 'Name', dataIndex:'name', width:300}];
        Ext.Ajax.request({
            url: '/loadsvc/v1/charts/aggregate/loadtests/' + this.loadTestId + "?yaxis=" + this.yaxis,
            async: false,
            scope:this,
            disableCaching:false,
            success: function(response){
                var json = Ext.decode(response.responseText, false);
                // SET COLUMNS AND DATA STORE HERE
                this.setColumns(json.chart.data);
                this.formatData(json.chart.modelFields, json.chart.data);
            }
        });
		this.callParent();
	}
});
