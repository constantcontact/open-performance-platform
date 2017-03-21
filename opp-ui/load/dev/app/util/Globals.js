Ext.define('CCPerf.util.Globals', {
    statics: {
        reports: {
           reportSettings:{
                       val: "",
                       type: "compare",
                       activeTab: 0
                       
           },
           loadTest: [],
           loadtest: [],
           current: {
               loadtest: "",
               aggregates: []
           },
           sla: {
               dirty: false
           }
        },
        forms:{
            required: '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>'
        }
    }
});

