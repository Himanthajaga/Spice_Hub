$(document).ready(function() {
    $.ajax({
        url: 'http://localhost:8080/api/v1/user/profile',
        method: 'GET',
        success: function(response) {
            if (response.code === 200) {
                const user = response.data;
                $('#profilePicture').attr('src', 'data:image/png;base64,' + user.profilePicture);
                $('#name').val(user.name);
                $('#email').val(user.email);
                $('#phone').val(user.phone);
                $('#address').val(user.address);
                // Password fields are typically not pre-filled for security reasons
            } else {
                alert('Failed to load user details');
            }
        },
        error: function() {
            alert('Error fetching user details');
        }
    });
});