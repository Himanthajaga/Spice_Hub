$(document).ready(function() {
    $('#loginBtn').click(function (){
        let email = $('#email').val();
        let password = $('#password').val();

        $.ajax({
            url: 'http://localhost:8080/api/v1/auth/authenticate',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                "email": email,
                "password": password
            }),
            success: function(response) {
                console.log(response);
                alert('User logged in successfully');
                window.localStorage.setItem('token', response.data.token);
                if (response.data.role === 'ADMIN') {
                    window.location.href = 'admin_index.html';
                } else {
                    window.location.href = 'user_index.html';
                }
            },
            error: function (error) {
                console.log(error);
                alert('Something went wrong!');
            }
        });
    });
});