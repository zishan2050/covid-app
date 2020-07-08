$(document).ready(function () {
    $('#submit').prop('disabled', true);
    $.ajax({
        url: '/covid/api/me',
        type: 'get',
        dataType: 'json',
        contentType: 'application/json',
        success: function (data) {
            $('#error').text("Redirecting...");
            $(location).attr('href', '/covid/app/home.html')
        },
        error: function (error) {
            console.log(error);
            $('#submit').prop('disabled', false);
        }
    });
});
