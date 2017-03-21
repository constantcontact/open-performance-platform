// setup lightbox
$(document).delegate('*[data-toggle="lightbox"]', 'click', function (event) {
    event.preventDefault();
    $(this).ekkoLightbox();
});

// setup tablesorter bootstrap theme
$(document).ready(function () {
    $(function () {
        $.extend($.tablesorter.themes.bootstrap, {
            // these classes are added to the table. To see other table classes available,
            // look here: http://twitter.github.com/bootstrap/base-css.html#tables
            table: 'table table-bordered',
            caption: 'caption',
            header: 'bootstrap-header', // give the header a gradient background
            footerRow: '',
            footerCells: '',
            icons: '', // add "icon-white" to make them white; this icon class is added to the <i> in the header
            sortNone: 'bootstrap-icon-unsorted',
            sortAsc: 'icon-chevron-up glyphicon glyphicon-chevron-up',     // includes classes for Bootstrap v2 & v3
            sortDesc: 'icon-chevron-down glyphicon glyphicon-chevron-down', // includes classes for Bootstrap v2 & v3
            active: '', // applied when column is sorted
            hover: '', // use custom css here - bootstrap class may not override it
            filterRow: '', // filter row class
            even: '', // odd row zebra striping
            odd: ''  // even row zebra striping
        });
    });

    var pagerOptions = {

        // target the pager markup - see the HTML block below
        container: $(".pager"),

        // use this url format "http:/mydatabase.com?page={page}&size={size}&{sortList:col}"
        ajaxUrl: null,

        // modify the url after all processing has been applied
        customAjaxUrl: function (table, url) {
            return url;
        },

        // process ajax so that the data object is returned along with the total number of rows
        // example: { "data" : [{ "ID": 1, "Name": "Foo", "Last": "Bar" }], "total_rows" : 100 }
        ajaxProcessing: function (ajax) {
            if (ajax && ajax.hasOwnProperty('data')) {
                // return [ "data", "total_rows" ];
                return [ajax.total_rows, ajax.data];
            }
        },

        // output string - default is '{page}/{totalPages}'
        // possible variables: {page}, {totalPages}, {filteredPages}, {startRow}, {endRow}, {filteredRows} and {totalRows}
        // also {page:input} & {startRow:input} will add a modifiable input in place of the value
        output: '{startRow} to {endRow} ({totalRows})',

        // apply disabled classname to the pager arrows when the rows at either extreme is visible - default is true
        updateArrows: true,

        // starting page of the pager (zero based index)
        page: 0,

        // Number of visible rows - default is 10
        size: 10,

        // Save pager page & size if the storage script is loaded (requires $.tablesorter.storage in jquery.tablesorter.widgets.js)
        savePages: true,

        //defines custom storage key
        storageKey: 'tablesorter-pager',

        // if true, the table will remain the same height no matter how many records are displayed. The space is made up by an empty
        // table row set to a height to compensate; default is false
        fixedHeight: true,

        // remove rows from the table to speed up the sort of large tables.
        // setting this to false, only hides the non-visible rows; needed if you plan to add/remove rows with the pager enabled.
        removeRows: false,

        // css class names of pager arrows
        cssNext: '.next', // next page arrow
        cssPrev: '.prev', // previous page arrow
        cssFirst: '.first', // go to first page arrow
        cssLast: '.last', // go to last page arrow
        cssGoto: '.gotoPage', // select dropdown to allow choosing a page

        cssPageDisplay: '.pagedisplay', // location of where the "output" is displayed
        cssPageSize: '.pagesize', // page size selector - select dropdown that sets the "size" option

        // class added to arrows when at the extremes (i.e. prev/first arrows are "disabled" when on the first page)
        cssDisabled: 'disabled', // Note there is no period "." in front of this class name
        cssErrorRow: 'tablesorter-errorRow' // ajax error information row

    };


    $("#tblUXTests").tablesorter({
        theme: "bootstrap",
        widthFixed: true,
        headerTemplate: '{content} {icon}',
        headers: {0: {sortInitialOrder: 'desc'}},
        widgets: ["uitheme", "zebra"],
        widgetOptions: {
            zebra: ["even", "odd"]
        },
        sortList: [[0, 1]]

    })
        // bind to pager events
        // *********************
        .bind('pagerChange pagerComplete pagerInitialized pageMoved', function (e, c) {
            var msg = '"</span> event triggered, ' + (e.type === 'pagerChange' ? 'going to' : 'now on') +
                ' page <span class="typ">' + (c.page + 1) + '/' + c.totalPages + '</span>';
            $('#display')
                .append('<li><span class="str">"' + e.type + msg + '</li>')
                .find('li:first').remove();
        })

        // initialize the pager plugin
        // ****************************
        .tablesorterPager(pagerOptions);

});


function populatePages() {
    // clear #on select drop down
    $('#on option[value!=""]').remove();
    // build based on json array for the selected category
    var cat = $('#cat').val(); // get selected value
    var defaultSelected = false, selectedNow = false;
    for (var i = 0; i < json[cat].length; i++) {
        defaultSelected = false;
        selectedNow = (getQueryVar('on') == json[cat][i].full) ? true : false;
        $('#on').append(new Option(json[cat][i].display, json[cat][i].full, defaultSelected, selectedNow));
    }
}

function getQueryVar(variable) {
    var query = window.location.search.substring(1);
    var vars = query.split('&');
    for (var i = 0; i < vars.length; i++) {
        var pair = vars[i].split('=');
        if (decodeURIComponent(pair[0]) === variable) {
            return decodeURIComponent(pair[1]);
        }
    }
    return "";
}


$(function () {


    $('#chart_div').highcharts({
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
            data: ttfbLine,
            zIndex: 1,
            marker: {
                fillColor: 'white',
                lineWidth: 2,
                lineColor: Highcharts.getOptions().colors[0]
            }
        }, {
            name: 'Time to First Byte (Range)',
            data: ttfbRange,
            type: 'arearange',
            lineWidth: 0,
            linkedTo: ':previous',
            color: Highcharts.getOptions().colors[0],
            fillOpacity: 0.3,
            zIndex: 0
        }, {
            name: 'Speed Index',
            data: siLine,
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
            data: siRange,
            type: 'arearange',
            lineWidth: 0,
            linkedTo: ':previous',
            color: Highcharts.getOptions().colors[1],
            fillOpacity: 0.3,
            zIndex: 0
        },
            {
                name: 'Visually Complete',
                data: vcLine,
                zIndex: 1,
                marker: {
                    fillColor: 'white',
                    lineWidth: 2,
                    lineColor: Highcharts.getOptions().colors[2]
                }
            }, {
                name: 'Visually Complete (Range)',
                data: vcRange,
                type: 'arearange',
                lineWidth: 0,
                linkedTo: ':previous',
                color: Highcharts.getOptions().colors[2],
                fillOpacity: 0.3,
                zIndex: 0
            }]
    });

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

    if(userTimings !== null) {
        // show all user timings
        $.each(userTimings, function (name, data) {
            userTimingsChart.highcharts().addSeries({
                "name": name,
                "data": data
            });
        });
    } else {
        $('#userTimings-chart').hide();
    }


});
