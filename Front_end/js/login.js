// Google Sign-In handler
function onSignIn(googleUser) {
    const profile = googleUser.getBasicProfile();
    const email = profile.getEmail();

    // Autofill the login form
    document.getElementById('email').value = email;

    // Save user details in the database
    saveUserDetails(email);
}

// Save user details to the database
function saveUserDetails(email) {
    $.ajax({
        url: 'http://localhost:8080/api/v1/user/register',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            email: email,
            password: 'defaultPassword' // Default or generated password
        }),
        success: function(response) {
            console.log('User details saved successfully:', response);
        },
        error: function(error) {
            console.error('Error saving user details:', error);
        }
    });
}

$(document).ready(function() {
    // Login button click handler
    $('#loginBtn').click(function() {
        const email = $('#email').val();
        const password = $('#password').val();

        $.ajax({
            url: 'http://localhost:8080/api/v1/auth/authenticate',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                email: email,
                password: password
            }),
            success: function(response) {
                console.log(response);
                if (response.data && response.data.token) {
                    Swal.fire('User logged in successfully');
                    window.localStorage.setItem('token', response.data.token);
                    console.log('User role:', response.data.role); // Log the user role
                    if (response.data.role === 'ADMIN') {
                        window.location.href = 'admin.html';
                    } else {
                        window.location.href = 'user_index.html';
                    }
                } else {
                    Swal.fire('Login failed: Invalid response from server.');
                }
            },
            error: function(error) {
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