var rrux = {
    init: function(){
        rrux.navigation.setStateFromUrl();
        rrux.data.getNavigationData();
    },
    strings: {
        ucFirst: function(text){
            return text.substr(0,1).toUpperCase() + text.substr(1);
        }
    },
    dataTables: {
        tests: {
            dt: null,
            reload: function(data){ // data is optional
                if (data !== undefined) rrux.data.tests = data;
                if(rrux.dataTables.tests.dt !== null) rrux.dataTables.tests.dt.destroy();
                rrux.dataTables.tests.loadData();
            },
            page: function(){
                $('#perfTestListTable tr').dblclick(function(ev) {
                    var rowData = rrux.dataTables.tests.dt.row(ev.currentTarget._DT_RowIndex).data();
                    var statesToSet = ['name', 'app', 'page', 'env', 'browser', 'connection', 'other'];
                    $.each(statesToSet, function(idx, val){
                        rrux.navigation.state[val] = rowData[val];
                    });
                    rrux.navigation.go();
                });
            },
            loadData: function(){
                rrux.dataTables.tests.dt = $('#perfTestListTable').DataTable({
                    responsive: true,
                    data: rrux.data.tests,
                    columns: [
                        {data: 'app'},
                        {data: 'page'},
                        {data: 'location'},
                        {data: 'env'},
                        {data: 'browser'},
                        {data: 'connection'},
                        {data: 'other'}
                    ]
                });

                // when the table first gets loaded, set the double
                // click event.
                rrux.dataTables.tests.setDoubleClickEventOnRows();

                //when the user clicks on a different page, apply the double click event.
                $('#perfTestListTable').on('draw.dt', function() {
                    rrux.dataTables.tests.setDoubleClickEventOnRows();
                });
            },
            setDoubleClickEventOnRows: function() {
                // unbind the double click event if it already has it.
                $('#perfTestListTable tr').off('dblclick');

                // apply the double click event.
                $('#perfTestListTable tr').dblclick(function(ev) {
                    var rowData = rrux.dataTables.tests.dt.row(ev.currentTarget._DT_RowIndex).data();
                    var statesToSet = ['name', 'app', 'page', 'env', 'browser', 'connection', 'other'];
                    $.each(statesToSet, function(idx, val){
                        rrux.navigation.state[val] = rowData[val];
                    });
                    rrux.navigation.go();
                });
            }
        },
        wptRuns: {
            dt: null,
            loadData: function () {
                var isAdmin = (rrux.navigation.getQueryParam('user') === 'admin');
                rrux.dataTables.wptRuns.dt = $('#perfSummaryTable').DataTable({
                    responsive: true,
                    data: rrux.data.report.dataTable,
                    columns: [
                        {data: 'timestamp'},
                        {data: 'page'},
                        {data: 'viewData.TTFB'},
                        {data: 'viewData.visualComplete'},
                        {data: 'viewData.SpeedIndex'},
                        {data: 'connectivity'},
                        {
                            data: 'viewData.images', render: function (data, type, rowData, meta) {
                            var html = 'n/a';
                            if (rowData.viewData.images !== undefined) {
                                html = '<a href="#" data-featherlight="' + rowData.viewData.images.screenShot + '">' +
                                    '<img height="25" width="25" src="' + rowData.viewData.images.screenShot + '"/>' +
                                    '</a> | ' +
                                    '<a href="#" data-featherlight="' + rowData.viewData.images.waterfall + '">' +
                                    '<img height="25" width="25" src="' + rowData.viewData.images.waterfall + '"/>' +
                                    '</a>';
                            }
                            return html;
                        }
                        },
                        {
                            data: 'office', render: function (data, type, rowData, meta) {
                                var html = '<a href="/ux/preparehar?wptid=' + rowData.id + '" target="_blank">Waterfall Analysis</a> <br/>';
                                if (rowData.viewData.pages !== undefined) {
                                    html += '<a href="' + rowData.viewData.pages.details + '" target="_blank">WebPageTest</a>';
                                }
                                return html;
                            }
                        },
                        { visible: isAdmin, render: function ( data, type, full, meta ) {
                            if(isAdmin){
                                return '<a href="#none" onclick="rrux.data.deleteTestRun(this, \''+full.id+'\', \''+full._id+'\', \''+full.rev+'\'); return false;"><img src="/images/ux/delete-32.gif" /></a>';
                            } else {
                                return '';
                            }

                        }}
                    ]
                });
            }
        }
    },
    navigation: {
        data: [],
        state: {
            app: "",
            page: "",
            name: "",
            cached: "false", // defaults
            run: "median",
            utBaseline: false,
            env: "",
            browser: "",
            location: "",
            connection: "",
            other: ""
        },
        showLoading: function(){
            $('.LoadingIndicator').show();
        },
        hideLoading: function(){
            $('.LoadingIndicator').hide();
        },
        showTestList: function(){
            $("#report").hide("slide", { direction: "left" }, 300, function(){
                $("#reportList").show("slide", { direction: "left" }, 300);
            });
        },
        dropdowns: {
            setAllFromState: function(){
                if(rrux.navigation.state.app !== "") {
                    $("#select-app").val(rrux.navigation.state.app);
                    if($("#select-page").find('option').length === 0){
                        $("#select-app").trigger('change'); // sets the page lists
                    }
                    $("#select-page").val(rrux.navigation.state.page);
                    $("#select-cache").val(rrux.navigation.state.cached);
                    $("#select-run").val(rrux.navigation.state.run);
                    $("#select-utBaseline").val(rrux.navigation.state.utBaseline);
                    $("#select-location").val(rrux.navigation.state.location);
                    $("#select-browser").val(rrux.navigation.state.browser);
                    $("#select-connection").val(rrux.navigation.state.connection);
                    $("#select-env").val(rrux.navigation.state.env);
                    $("#select-other").val(rrux.navigation.state.other);
                }
            },
            addApplications: function(data){
                var apps = [];
                $.each(rrux.navigation.data, function(k, v){
                    apps.push(k);
                });
                apps.sort();
                $.each(apps, function(idx, val) {
                    $('#select-app').append($("<option></option>") .attr("value", val).text(val));
                });
            },
            addPages: function(elSelect){
                $("#perfTestListTable").show();
                rrux.navigation.dropdowns.showExtended(false); // hide all extended attributes
                rrux.navigation.state.app = elSelect.value; // set the app state
                rrux.navigation.dropdowns.populate('page', []);
            },
            setPage: function(elSelect){
                rrux.navigation.dropdowns.showExtended(false); // hide all extended attributes
                rrux.navigation.state.page = elSelect.value;
                rrux.navigation.state.name = $(elSelect).find('option:selected').attr('data-test-name');
                var arrName = rrux.navigation.state.name.split('.');
                rrux.navigation.dropdowns.populate('env', ['page']);

            },
            populate: function(attrib, criteria){
                $('#select-'+attrib).empty().append($("<option></option>").attr("value", "").text("Select " + rrux.strings.ucFirst(attrib))); // clear it out
                // create filtered list that removes dups
                var dupCheck = [];
                var filteredData = rrux.navigation.data[rrux.navigation.state.app].filter(
                    function(val, idx, self){
                        if (val.page === undefined) {
                            val.page = val.other; // set page to other if page is not set
                            val.other = '';
                        }
                        var match;
                        if($.inArray(val[attrib], dupCheck) < 0) {  // dup check
                            // loop through all the filters and check to see if any don't match the object i'm looking for
                            match = true;
                            $.each(criteria, function (idx, stateName) {
                                if (stateName !== "" && val[stateName] !== rrux.navigation.state[stateName]) {
                                    match = false;
                                }
                            });
                            if(match === true) dupCheck.push(val[attrib]); // add to array for dup check since this is a matching value now for all criteria
                        } else {
                            match = false; // we already have this attribute, no need to continue searching array element
                        }
                        return match;
                    }
                );

                // sorting the mapped array containing the reduced values
                filteredData.sort(function(a, b) {
                    return +(a[attrib] > b[attrib]) || +(a[attrib] === b[attrib]) - 1;
                });

                var filteredDataTable = rrux.navigation.data[rrux.navigation.state.app].filter(
                    function(val, idx, self) {
                        var match = true;
                        $.each(criteria, function (idx, stateName) {
                            if (val[stateName] !== rrux.navigation.state[stateName]) {
                                match = false;
                            }
                        });
                        return match;
                    }
                );

                rrux.dataTables.tests.reload(filteredDataTable); // set list for test data

                // populate the drop down for matched criteria
                $.each(filteredData, function(idx, val) {
                    $('#select-' + attrib).append($("<option></option>")
                        .attr("value", val.attrib)
                        .attr("data-test-name", val.name)
                        .text(val[attrib]));
                });

                // show the dropdown
                $("#select-"+attrib+"-wrapper").show();
            },
            showExtended: function(boolShow){
                var extendedIds = ['env', 'location', 'browser', 'connection', 'other'];
                if(boolShow) {
                    $.each(extendedIds, function(index, value){
                        $("#select-"+value+"-wrapper").show();
                    });
                } else {
                    $.each(extendedIds, function(index, value){
                        $("#select-"+value+"-wrapper").hide();
                    });
                }
            }

        },
        setMetaDataFromTestName: function(){
            var state = rrux.navigation.state;
            var arrName = state.name.split('.');
            if(arrName.length >= 6){
                state.env = arrName[0];
                state.app = arrName[1];
                state.page = arrName[2];
                state.location = arrName[3];
                state.browser = arrName[4];
                state.connection = arrName[5];
                state.other = (arrName.length > 6) ? arrName[6] : '';
                rrux.navigation.dropdowns.showExtended(true);
            } else {
                // handle legacy stuff
                var arrLegacy = state.name.split('-');
                state.app = arrLegacy[0];
                arrLegacy.splice(0, 1);
                state.page = arrLegacy.join('-');
                state.location = '';
                state.env = '';
                state.browser = '';
                state.connection = '';
                state.other = '';
                rrux.navigation.dropdowns.showExtended(false);
            }
        },
        setStateFromUrl: function(){
            var state = rrux.navigation.state;
            var nav = rrux.navigation;
            if(nav.getQueryParam("name") !== false) {
                // todo: i may only need the primary ones here and not the extra attributes
                state.name = nav.getQueryParam("name");
                nav.setMetaDataFromTestName();
                if (nav.getQueryParam("cached") !== false) state.cached = nav.getQueryParam("cached");
                if (nav.getQueryParam("run") !== false) state.run = nav.getQueryParam("run");
                if (nav.getQueryParam("utBaseline") !== false) state.utBaseline = nav.getQueryParam("utBaseline");
                if (nav.getQueryParam("location") !== false) state.location = nav.getQueryParam("location");
                if (nav.getQueryParam("browser") !== false) state.browser = nav.getQueryParam("browser");
                if (nav.getQueryParam("env") !== false) state.env = nav.getQueryParam("env");
                if (nav.getQueryParam("connection") !== false) state.connection = nav.getQueryParam("connection");
                if (nav.getQueryParam("other") !== false) state.other = nav.getQueryParam("other");
                nav.go();
            }
        },
        updateUrl: function(){
            if(!window.history.pushState) return; // html 5 only
            var url = "?name=" + rrux.navigation.state.name +
                        "&cached=" + rrux.navigation.state.cached +
                        "&run=" + rrux.navigation.state.run +
                        "&utBaseline=" + rrux.navigation.state.utBaseline;
            var user = rrux.navigation.getQueryParam('user');
            if(user !== false) url = url + "&user=" + user;
            window.history.pushState({}, '', url);
        },
        go: function(){
            $("#reportList").hide("slide", { direction: "left" }, 300, function(){
                $("#report").show("slide", { direction: "left" }, 300, function(){
                    $("#reportTitle").html(rrux.navigation.state.name);
                    rrux.data.getWptData();
                    rrux.navigation.updateUrl();
                });
            });

            //rrux.data.getWptData($('#select-page option:selected').attr('data-test-name'), $('#select-cache').val(), $('#select-run').val(), false);
        },
        getQueryParam: function(variable) {
            var query = window.location.search.substring(1);
            var vars = query.split('&');
            for (var i = 0; i < vars.length; i++) {
                var pair = vars[i].split('=');
                if (decodeURIComponent(pair[0]) == variable) {
                    return decodeURIComponent(pair[1]);
                }
            }
            return false;
        }

        /*$.each(value1, function (index2, value2) {
         if($.inArray(value2.app, rrux.navigation.uniqueAppNames) === -1) {
         rrux.navigation.uniqueAppNames.push(value2.app);
         $('#select-full-name')
         .append($("<option></option>")
         .attr("value", value2.app)
         .attr("onchange", "rrux.navigation.addPages(this)")
         .text(value2.app));
         }
         });*/
    },
    data: {
        tests: [],
        report: [],
        deleteTestRun: function(btn, wptId, couchId, couchRevId){
            $.ajax({
                url: '/uxsvc/v2/rrux/uxwpt',
                type: 'DELETE',
                data: { wptid: wptId, couchid: couchId, revid: couchRevId },
                context: btn,
                statusCode: {
                    200: function(){
                        var tr = $(this).parents('tr');
                        tr.children('td').css('background',"#FF6666");
                        tr.fadeOut(400, function(){
                            rrux.dataTables.wptRuns.dt
                                .row( tr )
                                .remove()
                                .draw();
                        });
                        return false;
                    },
                    404: function(){
                        alert('Test not found');
                    },
                    500: function(){
                        alert('Failed to delete test');
                    }
                }
            });
        },
        getNavigationData: function(){
            $.get( "/uxsvc/v1/wpt/categories", function(data) {
                rrux.navigation.data = data;
                rrux.navigation.dropdowns.addApplications();
                rrux.navigation.dropdowns.setAllFromState();
                rrux.navigation.hideLoading();
            });
        },
        getWptData: function() {
            rrux.navigation.showLoading();
            $('#perfSummaryTable').show();
            $('#chart-main-resp-time').show();
            $('#userTimings-chart').show();
            var url = "/uxsvc/v2/rrux/wptTrendData?name=" + rrux.navigation.state.name +
                    "&cached=" + rrux.navigation.state.cached +
                    "&run=" + rrux.navigation.state.run +
                    "&utBaseline=" + rrux.navigation.state.utBaseline;
            $.get(url, function (data) {
                rrux.data.report = data;
                rrux.charts.addWptChart();
                rrux.charts.addUserTimingsChart();
                if(rrux.dataTables.wptRuns.dt !== null) rrux.dataTables.wptRuns.dt.destroy();
                rrux.dataTables.wptRuns.loadData();
                rrux.navigation.hideLoading();
            });
        }
    },
    charts: {
        addWptChart: function () {
            $('#chart-main-resp-time').highcharts({
                credits: {
                    enabled: false
                },
                title: {
                    text: null
                },
                xAxis: {
                    type: 'datetime'
                },
                yAxis: {
                    title: {
                        text: null
                    },
                    min: 0
                },
                chart: {
                    backgroundColor: null
                },
                tooltip: {
                    crosshairs: true,
                    shared: true,
                    valueSuffix: 'ms'
                },

                legend: {
                    align: 'right',
                    layout: 'vertical',
                    verticalAlign: 'middle',
                    itemMarginTop: 10,
                    itemMarginBottom: 10
                },

                series: [{
                    name: 'Time to First Byte',
                    data: rrux.data.report.chart.ttfbLine,
                    zIndex: 1,
                    marker: {
                        fillColor: 'white',
                        lineWidth: 2,
                        lineColor: Highcharts.getOptions().colors[0]
                    }
                }, {
                    name: 'Time to First Byte (Range)',
                    data: rrux.data.report.chart.ttfbRange,
                    type: 'arearange',
                    lineWidth: 0,
                    linkedTo: ':previous',
                    color: Highcharts.getOptions().colors[0],
                    fillOpacity: 0.3,
                    zIndex: 0
                }, {
                    name: 'Speed Index',
                    data: rrux.data.report.chart.siLine,
                    zIndex: 1,
                    marker: {
                        fillColor: 'white',
                        lineWidth: 2,
                        lineColor: Highcharts.getOptions().colors[1]
                    },
                    tooltip: {
                        valueSuffix: ''
                    }
                }, {
                    name: 'Speed Index (Range)',
                    data: rrux.data.report.chart.siRange,
                    type: 'arearange',
                    lineWidth: 0,
                    linkedTo: ':previous',
                    color: Highcharts.getOptions().colors[1],
                    fillOpacity: 0.3,
                    zIndex: 0
                },
                {
                    name: 'Visually Complete',
                    data: rrux.data.report.chart.vcLine,
                    zIndex: 1,
                    marker: {
                        fillColor: 'white',
                        lineWidth: 2,
                        lineColor: Highcharts.getOptions().colors[2]
                    }
                }, {
                    name: 'Visually Complete (Range)',
                    data: rrux.data.report.chart.vcRange,
                    type: 'arearange',
                    lineWidth: 0,
                    linkedTo: ':previous',
                    color: Highcharts.getOptions().colors[2],
                    fillOpacity: 0.3,
                    zIndex: 0
                }]
            });
        },
        addUserTimingsChart: function () {
            var userTimingsChart = $('#userTimings-chart').highcharts({
                credits: {
                    enabled: false
                },
                title: {
                    text: null
                },
                xAxis: {
                    type: 'datetime'
                },
                yAxis: {
                    title: {
                        text: null
                    },
                    min: 0
                },
                chart: {
                    backgroundColor: null
                },
                tooltip: {
                    crosshairs: true,
                    shared: true,
                    valueSuffix: 'ms'
                },
                legend: {
                    align: 'right',
                    layout: 'vertical',
                    verticalAlign: 'middle',
                    itemMarginTop: 10,
                    itemMarginBottom: 10
                },
                series: []
            });

            if (rrux.data.report.chart.userTimings !== null) {
                // show all user timings
                $('#userTimings-chart').show();
                $.each(rrux.data.report.chart.userTimings, function (name, data) {
                    userTimingsChart.highcharts().addSeries({
                        name: name,
                        data: data
                    });
                });
               /* $.each(rrux.data.report.chart.userTimingsRange, function (name, data) {
                    userTimingsChart.highcharts().addSeries({
                        name: name,
                        data: data,
                        type: 'arearange',
                        lineWidth: 0,
                        linkedTo: ':previous',
                        fillOpacity: 0.3,
                        zIndex: 0
                    });
                });*/
            } else {

                $('#userTimings-chart').hide();
            }


        }
    }
};
