Ext.define('OppUI.view.loadtestDashboard.loadtestsummary.groupreportfilter.LoadTestGroupReportFilter', {
    extend: 'Ext.form.FieldContainer',
    alias: 'widget.groupreportfilter',

    requires: [
        'OppUI.view.loadtestDashboard.loadtestsummary.groupreportfilter.LoadTestGroupReportFilterController',
        'OppUI.view.loadtestDashboard.loadtestsummary.groupreportfilter.LoadTestGroupReportFilterModel'
    ],

    controller: 'loadtestgroupreportfilter',
    viewModel: {
        type: 'loadtestgroupreportfilter'
    },

    layout: {
        type: 'hbox',
        align: 'middle' // Child items are stretched to full width
    },
    fieldDefaults: {
        padding: 5
    },
    items: [{
        xtype: 'combobox',
        itemId: 'filterCombobox',
        store: {
            type: 'array',
            fields: ['column_name'],
            data: [
                ['appUnderTest'],
                ['appUnderTestVersion'],
                ['comments'],
                ['description'],
                ['environment'],
                ['startTime'],
                ['testName'],
                ['testSubName'],
                ['testTool'],
                ['testToolVersion'],
                ['vuserCount'],
                ['slaGroupId']
            ]
        },
        displayField: 'column_name',
        fieldLabel: 'Filter',
        labelWidth: 35,
        anchor: '0',
        queryMode: 'local',
        selectOnTab: false,
        name: 'columnName'
    }, {
        xtype: 'textfield',
        itemId: 'filterField',
        name: 'criteria',
        width: 200
    }, {
        xtype: 'button',
        text: 'Add more',
        itemId: 'btnGroupedFilterFieldAdd',
        padding: 5,
        handler: function(button) {
            button.hide();
            button.up('panel').add({ xtype: 'groupreportfilter' });
            button.up('fieldcontainer').down('#deleteFieldContainerBtn').show();
        }
    }, {
        xtype: 'button',
        itemId: 'deleteFieldContainerBtn',
        text: 'X',
        padding: 5,
        hidden: true,
        handler: function() {
            this.up('panel').remove(this.up('fieldcontainer'), true);
        }
    }]
});