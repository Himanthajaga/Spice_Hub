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
                if (response.data && response.data.token) {
                    Swal.fire('User logged in successfully');
                    window.localStorage.setItem('token', response.data.token);
                    console.log('User role:', response.data.role); // Log the user role
                    if (response.data.role === 'ADMIN') {
                        window.location.href = 'admin_index.html';
                    } else {
                        window.location.href = 'user_index.html';
                    }
                } else {
                    Swal.fire('Login failed: Invalid response from server.');
                }
            },
            error: function (error) {
                console.log('Error:', error);
                if (error.status === 401) {
                    Swal.fire('Invalid Credentials. Please try again.');
                } else {
                    Swal.fire('Something went wrong! Error: ' + error.responseText);
                }
            }
        });
    });
});