Ext.define('CCPerf.controller.TestsController', {
    extend: 'Ext.app.Controller',
    requires:['CCPerf.util.Globals', 'Ext.form.FieldContainer', 'Ext.grid.column.Widget', 'Ext.sparkline.Line'],
    stores: [
        //'TestsUxStore',
        'TestsLoadStore',
        'LoadTestSummaryTrendDynamic'
    ],
    models: [
        'TestLoadModel',
        'TestLoadGroupModel'
    ],
    views: [
        //'test.UxListView',
        'test.LoadListView',
        'test.EditView',
        'test.AdvancedReportView',
        'test.CreateGroupedReportView',
        'test.components.CreateGroupedFilterField'
    ],
    init: function() {
        this.control({
          '#tabPanelMain': {
              afterrender: function(){
                  // let the page render first for a faster user perception.  Settings the timeout of 100ms allows this to happen before loading all the tabs
                  var testController = this;
                  setTimeout(function(){
                    testController.loadTabs();
                  }, 100);

              }
          },
          'testloadlist #btnViewReport':{
                click: function(button) {
                     var grid = button.up('grid');
                     if(grid.getSelectionModel().selected.items.length > 0){
                         this.setGlobalReportVars(grid, "autotrend", true);
                         this.createLoadReport(null, null, null, grid.getSelectionModel().selected.items[0].data.loadTestId);
                     } else {
                         Ext.Msg.alert("Warning...", "You must make a selection first");
                     }
                 }
            },
            'testloadlist #btnDelete':{
                click: function(button) {
                     var grid = button.up('grid');
                     if(grid.getSelectionModel().selected.items.length > 0){
                        var ids = [];
                        for(var i=0; i<grid.getSelectionModel().selected.items.length; i++){
                             ids.push(grid.getSelectionModel().selected.items[i].data.loadTestId);
                         }
                         if(window.confirm("Are you sure you want to delete " + ids.length + " record(s)")){
                             this.deleteRecords(ids);
                         }
                     } else {
                         Ext.Msg.alert("Warning...", "You must make a selection first");
                     }
                 }
            },
          'testloadlist #btnFilterLoadGrid': {
              click: function(button) {
                  var grid = button.up('grid');
                  // Ext.getCmp('txtFilterLoadGrid').value()
                    grid.store.load( { params: { filter: Ext.getCmp('txtFilterLoadGrid').getValue() } } );
              }

          },
          'testAdvancedReport #btnCreateAdvancedReport':{
                click: function(btn) {
                        f = btn.up('window').down('form').getForm();
                        if(f.isValid()){
                            fFields = f.getFields();
                            queryStr = "?";
                            for(i=0;i<fFields.length; i++){
                                field = fFields.items[i];
                                if(field.value) {
                                    if(queryStr !== '?') queryStr += '&';
                                    queryStr += field.name + '=' + f.getFields().items[i].value;
                                    if(field.name === "val") queryStr += '&id=' + f.getFields().items[i].value; // for run.php
                                }
                            }
                           // alert(queryStr);
                           this.addReportTab('Advanced Report', 'icon-advanced-report', true, queryStr);
                        }
                    }
          },
          'create-grouped-report #btnCreateGroupedReport': {
              click: function(btn) {
                  var form = btn.up('form');
                  var queryStr = this.buildGroupedReportQueryStr(form);
                  var formVals = form.getValues();
                  var reportName = (formVals['report-name'] === '') ? 'Grouped Report' : formVals['report-name'];
                  this.createGroupLoadReport(reportName, 'icon-trend', true, queryStr);

              }
          },
         'create-grouped-report #btnShowGroupedSampleData': {
            click: function (btn) {
                this.loadGroupedReportSampleData(btn.up('form'));
            }
         },
         'create-grouped-report #btnGroupedFilterFieldAdd': {
            click: function(btn) {
                btn.hide();
                btn.up('panel').add({ xtype: 'create-grouped-filter-field'});
                btn.up('fieldcontainer').down('#deleteFieldContainerBtn').show();
            }
        }

        });

    },
    locationHashAdd: function(str){
        if(str === undefined){
            // do nothing
        }
        else if(location.hash === ''){
            location.hash = str;
        } else {
            location.hash = location.hash + '||' + str;
        }
    },
    setGlobalReportVars: function(grid, rptType, isLoad){
        var dataId = (isLoad) ? "loadTestId" : "run";
        if(rptType === "compare") {
            var ids = "";
            var i=0;
            for(i=0; i<grid.getSelectionModel().selected.items.length; i++){
                if(ids !== "") ids = ids + ',';
                ids = ids + grid.getSelectionModel().selected.items[i].data[dataId];
            }
    		    CCPerf.util.Globals.reports.reportSettings.val = ids;
            CCPerf.util.Globals.reports.reportSettings.type = rptType;
            CCPerf.util.Globals.reports.reportSettings.activeTab = 0;
        }
        if(rptType === "autotrend"){
            CCPerf.util.Globals.reports.reportSettings.val = grid.getSelectionModel().selected.items[0].data[dataId];
            CCPerf.util.Globals.reports.reportSettings.type = rptType;
            CCPerf.util.Globals.reports.reportSettings.activeTab = 0;
        }
        if(rptType === "group") {
          CCPerf.util.Globals.reports.reportSettings.val = grid;
          CCPerf.util.Globals.reports.reportSettings.type = rptType;
          CCPerf.util.Globals.reports.reportSettings.activeTab = 0;
        }

    },/*
    createReport: function(rptType, testId){
        if(CCPerf.util.Globals.reports.reportSettings.type === "compare") {
            title = 'Custom Report: ' + testId;
            icon = 'icon-compare';
        }
        else if(CCPerf.util.Globals.reports.reportSettings.type === "autotrend"){
            title = 'Report: ' + testId;
            icon = 'icon-trend';
        }
        else {
            title = 'Report: ' + testId;
            icon = 'icon-trend';
        }
        if(rptType === "ux"){
            this.addUxReportTab(title, icon, true, null);
        }
        if(rptType === "load"){
          //  CCPerf.util.Globals.reports.loadTest[]

            this.addLoadReportTab(title, icon, true, null, testId);
        }
    },*/
    advancedReportWindow: function(){
        var view = Ext.widget('testAdvancedReport');
    },
    editTest: function(grid, record) {
        var view = Ext.widget('testedit');
        view.down('form').loadRecord(record);
    },
    updateUser: function(button) {
        var win    = button.up('window'),
        form   = win.down('form'),
        record = form.getRecord(),
        values = form.getValues();

        record.set(values);
        win.close();
        // synchronize the store after editing the record
        this.getUsersStore().sync();
    },
    rebuildQueryStr: function(omitKey){
        var self = this;
        // Get filter fields
        var query = self.getAllQueryVars();
        // Form API query string
        var str = "";
        for (var key in query) {
            if(key == omitKey) continue; // skip this key
            str += key + "=" + query[key] + "&";
        }
        str = str.substring(0, str.length - 1);
        return str;
    },
    createGroupLoadReport: function (title, icon, closable, reportExpression) {
        // set defaults
        if(title === null) title = 'Group Report: ' + reportExpression;
        if(icon === null) icon = 'icon-trend';
        if(closable ===null) closable = true;

        var rptLink = 'name:'+title+'|'+'group:' + reportExpression;
        this.addNewTab({ xtype: "report-grouploadtest", closable: !!closable, iconCls: icon, title: title, reportExp: reportExpression, reportLink:rptLink },
        true, rptLink);
    },
    createLoadReport: function (title, icon, closable, loadTestId) {
        // set defaults
        if(title === null) title = 'Test Run #' + loadTestId;
        if(icon === null) icon = 'icon-trend';
        if(closable ===null) closable = true;

        // EXTJS 5.1 hack to fix another EXTJS bug
        // get tabpanel
        var mainTabs = Ext.ComponentQuery.query('#tabPanelMain')[0];
        // extjs 5.1 work around - create hidden tab and activate it
        // activating it is key as it seems to somehow allow the next tab to be activated
        var hiddenTab = this.addNewTab({
            xtype: "panel",
            hidden:true
        }, true);

        // load tab i wanted
        var reportTab = this.addNewTab({ xtype:"report-loadtest", closable: !!closable, iconCls: icon, title: title, loadTestId: loadTestId}, true, loadTestId);

        // remove hidden tab
        mainTabs.remove(hiddenTab);
    },
    addNewTab: function(obj, setActive, urlUpdate){
        this.locationHashAdd(urlUpdate);
        var mainTabs = Ext.ComponentQuery.query('#tabPanelMain')[0];

        var instance = mainTabs.add(obj); // add to new tab

        // TODO: EXTJS 5.1 bug causes an issue with this.  It's not a race condition either.
        if(setActive) mainTabs.setActiveTab(instance);

        return instance;
    },
    deleteRecords: function(ids){
        Ext.Ajax.request({
            url: '/loadsvc/v1/loadtests/' + ids.join(),
            method:'delete',
            async: false,
            disableCaching:false,
            success: function(response){
                //var json = Ext.decode(response.responseText, false);
                Ext.getCmp('gridAllTestRunsLoad').store.reload();
            }
        });
    },
    getQueryVar: function(variable) {
        var query = window.location.search.substring(1);
        var vars = query.split('&');
        for (var i = 0; i < vars.length; i++) {
            var pair = vars[i].split('=');
            if (decodeURIComponent(pair[0]) === variable) {
                return decodeURIComponent(pair[1]);
            }
        }
        return "";
    },
    getAllQueryVars: function() {
      var query = window.location.search.substring(1);
      var pairs = query.split("&");
      var params = {};
      for (var i = 0; i < pairs.length; i++) {
        var keyval = pairs[i].split("=");
        params[keyval[0]] = keyval[1];
      }
      return params;
    },
    buildGroupedReportQueryStr: function(form)
    {
        var data = form.getValues();
        var qStr = '';
        if(typeof data.columnName === 'string'){
            // process non-array
            qStr = data.columnName + '=' + data.criteria;
        } else {
            // I have more than one criteria, so this is an array... loop through it
            for(var i=0; i<data.columnName.length; i++){
                if(data.columnName[i].trim() !== ''){
                    if(qStr !== '') qStr += '&';
                    qStr += data.columnName[i] + '=' + data.criteria[i];
                }
            }
        }
        return qStr;
    },
    loadGroupedReportSampleData: function(form) {
        var qStr = this.buildGroupedReportQueryStr(form.getForm());
        var store = form.down('gridpanel').getStore();
        store.getProxy().api.read = '/loadsvc/v1/loadtests/all/summaryTrendByGroup/?' + qStr;
        store.load();
    },
    loadTabs: function(){
        // parse #

        var hash = location.hash.replace('#', '');
        hash = decodeURI(hash);
        var arrHash, isGroupTest, arrKeyVal, arrParams, optKey, optVal, reportExpression, reportName, hasParams, testId;
        if(hash !== '') {
           // Ext.getBody().mask('Loading reports...');
            // split to separate reports with ||
            arrHash = hash.split('||');
            location.hash='';
            arrHash.forEach(function (reportParams) {
                // set defaults
                isGroupTest = false;
                hasParams = false;
                reportName = null;
                testId = null;

                // split to separate report params with |
                arrParams = reportParams.split('|');
                // loop through report params
                arrParams.forEach(function (param) {
                    // loop through various test key value pairs of report params
                    arrKeyVal = param.split(':');
                    if (arrKeyVal.length > 1) {
                        optKey = arrKeyVal[0];
                        optVal = arrKeyVal[1];

                        // ifs for various report params
                        if(optKey == 'group'){
                            isGroupTest = true;
                            reportExpression = optVal;
                        }
                        if(optKey == 'name'){
                            reportName = optVal; // set report name
                        }
                        if(optKey == 'id'){
                            testId = optVal;
                        }
                    }
                });


                if(isGroupTest) {
                    // create a grouped report
                   this.createGroupLoadReport(reportName, null, null, reportExpression);
                } else {
                    if(hasParams){
                        this.createLoadReport(reportName, null, null, testId);
                    } else {
                        // no report params, assume arrParam[0] is a test id
                        var loadTestId = parseInt(arrParams[0]);
                        if(!isNaN(loadTestId)) {
                            this.createLoadReport(reportName, null, null, loadTestId);
                        } else {
                            Ext.Msg.alert("'" + arrParams[0] + "' is not a valid test id");
                        }
                    }
                }

            }, this);

          //  Ext.getBody().unmask();
        }

    }
});
