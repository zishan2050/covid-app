var labels = [];
var active = [];
var recovered = [];
var deaths = [];
var confirmed = [];

$(document).ready(function () {
    $('#logout').prop('disabled', true);
    $.ajax({
        url: '/covid/api/me',
        type: 'get',
        dataType: 'json',
        contentType: 'application/json',
        success: function (data) {
            $('#logout').prop('disabled', false);
            fetchCovidData();
        },
        error: function (error) {
            $(location).attr('href', '/covid/app/login.html');
        }
    });
    $("#state_selector").change(function () {
        loadPieChart();
    });
});

function fetchCovidData() {
    $.ajax({
        url: '/covid/api/data',
        type: 'get',
        dataType: 'json',
        contentType: 'application/json',
        success: function (data) {
            console.log(data);
            if (data.length > 0) {
                $('#total_count').text(data[0].confirmed);
                $('#active_count').text(data[0].active);
                $('#recovered_count').text(data[0].recovered);
                $('#death_count').text(data[0].deaths);
            }

            for (var i = 1; i < data.length; i++) {
                labels.push(data[i].state);
                confirmed.push(data[i].confirmed);
                active.push(data[i].active);
                recovered.push(data[i].recovered);
                deaths.push(data[i].deaths);

                $("#state_selector").append($("<option />").val(i - 1).text(data[i].state));
            }

            //===========================================
            new Chartist.Bar('.chart', {
                labels: labels,
                series: [
                    confirmed,
                    active,
                    recovered,
                    deaths
                ]
            }, {
                height: labels.length * 50,
                seriesBarDistance: 10,
                reverseData: true,
                horizontalBars: true,
                fullWidth: true,
                axisY: {
                    offset: 100
                },
                chartPadding: {
                    right: 50
                }
            });
            //===========================================
            loadPieChart();
        },
        error: function (error) {
            console.log(error);
        }
    });
}

function logout() {
    $('#logout').prop('disabled', true);
    $.ajax({
        url: '/covid/api/logout',
        type: 'post',
        contentType: 'application/json',
        success: function (data) {
            $(location).attr('href', '/covid/app/login.html');
        },
        error: function (error) {
            console.log(error);
            $('#logout').prop('disabled', false);
        }
    });
}

function loadPieChart() {
    var dataIndex = $("#state_selector").val();
    $("#state_total_count").text(confirmed[dataIndex]);
    $("#state_active_count").text(active[dataIndex]);
    $("#state_recovered_count").text(recovered[dataIndex]);
    $("#state_death_count").text(deaths[dataIndex]);

    var data = {
        series: [active[dataIndex], recovered[dataIndex], deaths[dataIndex]]
    };

    var options = {
        labelInterpolationFnc: function (value) {
            return value[0]
        },
        showLabel: true
    };

    var responsiveOptions = [
        ['screen and (min-width: 640px)', {
            chartPadding: 0,
            labelOffset: 100,
            labelDirection: 'explode',
            labelInterpolationFnc: function (value) {
                return value;
            }
        }],
        ['screen and (min-width: 1024px)', {
            labelOffset: 80,
            chartPadding: 0
        }]
    ];

    var pieChart = new Chartist.Pie('.pie', data, options, responsiveOptions);
}
