
Ext.define('OppUI.view.loadTestDashboard.loadtestreport.loadtestreportmain.LoadTestReportMain',{
    extend: 'Ext.panel.Panel',
    alias: 'widget.loadtestreportmain',

    requires: [
        'OppUI.view.loadTestDashboard.loadtestreport.loadtestreportmain.LoadTestReportMainController',
        'OppUI.view.loadTestDashboard.loadtestreport.loadtestreportmain.LoadTestReportMainModel'
    ],

    controller: 'loadtestreportmain',
    viewModel: {
        type: 'loadtestreportmain'
    },

    items:[
        {
            xtype: 'panel',
            height: 100,
            margin: '0px 0px 20px 0px',
            tpl: [
                '<table class=\'tbl-load-test-details\'>',
                    '<tr>',
                        '<th>Test Name:</th>',
                        '<td>{testName}</td>',
                    '</tr>',
                '</table>'
            ],

            //,viewModel: Ext.ComponentQuery.query('loadtestreport')[0].getViewModel()
            data:{
                testName: "TEST NAME!!!"
            }
        },
        { 
            xtype: 'loadtestreportsummary',
            height: 500,
            margin: '0px 0px 20px 0px'
        },
        {
            xtype: 'loadtestchart',
            height: 400,
            margin: '0px 0px 20px 0px'

        }
    ]
});
