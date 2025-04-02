$(document).ready(function() {
    const urlParams = new URLSearchParams(window.location.search);
    const email = urlParams.get('email');
    $('#email').val(email);
});

$('#resetPasswordForm').on('submit', function(e) {
    e.preventDefault();
    const email = $('#email').val();
    const otp = $('#otp').val();
    const password = $('#password').val();
    $.post('http://localhost:8080/api/v1/auth/reset-password', { email: email, otp: otp, newPassword: password }, function(data) {
        alert('Password reset successfully.');
        window.location.href = 'login.html';
    }).fail(function() {
        alert('Error resetting password.');
    });
});