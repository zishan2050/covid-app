function send() {
    $('#submit').prop('disabled', true);
    var person = {
        first_name: $("#first_name").val(),
        last_name: $("#last_name").val(),
        email: $("#email").val(),
        password: $("#password").val()
    }

    $('#para').text('Sending..');
    $('#para').prop('hidden', false);

    $.ajax({
        url: '/covid/api/sign_up',
        type: 'post',
        dataType: 'json',
        contentType: 'application/json',
        success: function (data) {
            $('#para').text("Redirecting...");
            $(location).attr('href', '/covid/app/login.html')
        },
        error: function (error) {
            if (error.status == 400) {
                $('#para').text("");
                for (var i = 0; i < error.responseJSON.errors.length; i++) {
                    $('#para').append(error.responseJSON.errors[i]+"<br/>");
                }
            } else {
                $('#para').text(error.responseJSON.message);
            }
            $('#submit').prop('disabled', false);
        },
        data: JSON.stringify(person)
    });
}
