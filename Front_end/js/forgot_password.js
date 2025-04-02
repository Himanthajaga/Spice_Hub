$('#forgotPasswordForm').on('submit', function(e) {
    e.preventDefault();
    const email = $('#email').val();
    $.post('http://localhost:8080/api/v1/auth/forgot-password', { email: email }, function(data) {
        alert('OTP sent to your email.');
        window.location.href = 'reset-password.html?email=' + encodeURIComponent(email);
    }).fail(function() {
        alert('Error sending OTP.');
    });
});