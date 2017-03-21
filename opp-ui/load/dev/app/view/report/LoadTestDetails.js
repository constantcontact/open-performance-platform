Ext.define('CCPerf.view.report.LoadTestDetails' ,{
    extend: 'Ext.panel.Panel',
    alias: 'widget.load-test-details',
    loadTestId:'',
    initComponent: function() {
        this.html = this.getContent(null);
        this.callParent(arguments);
    },
    setContent: function(lt){
        this.update(this.getContent(lt));
    },
    getContent: function(lt){
        if(lt === null || lt === undefined) lt = '';

        if(lt !== '') {
          // set to blank if null or undefined
          Ext.iterate(lt, function(key, val){
            if(val === null || val === undefined) lt[key] = '';
          });

          var rows = "";
          rows += this.addRow('ID', this.loadTestId);
          rows += this.addRow('Test Name', lt.testName);
          rows += this.addRow('Sub Name', lt.testSubName);
          rows += this.addRow('VUser Count', lt.vuserCount);
          rows += this.addRow('Environment', lt.environment);
          rows += this.addRow('App Under Test', lt.appUnderTest);
          rows += this.addRow('App Under Test Ver.', lt.appUnderTestVersion);
          rows += this.addRow('Start Time', lt.startTime);
          rows += this.addRow('End Time', lt.endTime);
          rows += this.addRow('Test Tool', lt.testTool);
          rows += this.addRow('Test Tool Version', lt.testToolVersion);
          rows += this.addRow('Description', lt.description);
          rows += this.addRow('Comments', lt.comments);
          return "<table class='tbl-load-test-details'>\n" + rows + "</table>";
        } else {
            return "Loading...";
        }
     },
     addRow: function(name, val){
       return "<tr><th>"+name+"</th> <td>"+val+"</td></tr>\n";
     }
});
