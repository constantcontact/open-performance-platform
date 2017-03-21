Ext.define('CCPerf.view.test.UxListView' ,{
    extend: 'Ext.grid.Panel',
    alias: 'widget.testuxlist',
    title: 'UX Tests',
    id:'gridAllTestRunsUx',
    store:'TestsUxStore',
    selModel: Ext.create('Ext.selection.CheckboxModel', { checkOnly: true, mode: 'MULTI' }),
    columnLines: true,
    animCollapse: false,
    iconCls: 'icon-grid',
    margin: '0 0 20 0',
    listeners: {
         "cellclick": function (sender, td, cellIndex, record, tr, rowIndex, e, eOpts) {
             clickedColumnIndex = cellIndex;
         },
         "beforedeselect": function (rowmodel, record, index, eOpts) {
             return (clickedColumnIndex === 0); //prevents deselecting all rows when editing other cells
         }
     },
    tbar: [
         {
             id:'btnCompareTrendUx',
             xtype:'button',
             iconCls: 'icon-compare',
             text: 'Compare &amp; Trend Selected',
             tooltip:'Provides custom trending and comparison of selected test runs.'

         },{
             id:'btnAutoTrendUx',
             xtype:'button',
             iconCls: 'icon-trend',
             text: 'Auto Trend Single Run',
             tooltip:'Performs automatic trending. Displays trending for the last 5 similar test runs'
         },
         {
             id:'btnAdvancedReportUx',
             xtype:'button',
             iconCls: 'icon-advanced-report',
             text: 'Advanced Report',
             tooltip:'Provides the ability to create a fully customizable report the way you want it.'
         }],
    initComponent: function() {


        this.columns = [
            {text: 'Run', dataIndex:'run', hidden:true, width:100},
            {text: 'Test Name', dataIndex:'test'},
            {text: 'Trend', dataIndex:'trend', renderer: function(v) { return this.showTrend(v); }, width:100},
            {text: 'Total Time (s)', dataIndex:'totalTime'},
            {text: 'Step Count', dataIndex:'stepCount', width:70},
            {text: 'Env', dataIndex:'env', width:65},
            {text: 'URL', dataIndex:'url'},
            {text: 'User', dataIndex:'user'},
            {text: 'Description', dataIndex:'description', width:200},
            {text: 'Browser', dataIndex:'browser'},
            {text: 'Date', dataIndex:'date'}
        ];
      //  this.enableLocking=true;

        this.callParent(arguments);
    },
    showTrend: function(v){
        var cls = ""; var color = "black";
        if(v>0) { cls = "arrow-ug"; color="green";}
        else if (v<0) { cls = "arrow-dr"; color='red'; }
        else { cls = ""; color="black"; }
        return '<span style="padding-left:16px; color:'+color+'" class="'+cls+'">'+v+'%</span>';
    }
});
