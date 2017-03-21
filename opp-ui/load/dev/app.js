/*
 * This file is generated and updated by Sencha Cmd. You can edit this file as
 * needed for your application, but these edits will have to be merged by
 * Sencha Cmd when upgrading.
 *
Ext.application({
    name: 'CCPerf',

    extend: 'CCPerf.Application',

    autoCreateViewport: 'CCPerf.view.main.Main'

    //-------------------------------------------------------------------------
    // Most customizations should be made to CCPerf.Application. If you need to
    // customize this file, doing so below this section reduces the likelihood
    // of merge conflicts when upgrading to new versions of Sencha Cmd.
    //-------------------------------------------------------------------------
});
*/

Ext.application({
    name: 'CCPerf',
    // requires:['ext-charts'],
    controllers: [
        'TestsController',
        'ReportsController',
        'ApplicationMappingController',
        'GraphiteMetricController'
    ],
    autoCreateViewport: true
});
