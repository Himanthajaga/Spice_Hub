$(document).ready(function() {
    $.ajax({
        url: 'http://localhost:8080/api/v1/user/profile',
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + window.localStorage.getItem('token')
        },
        success: function(response) {
            if (response.code === 202) {
                const user = response.data;
                if (user.profilePicture) {
                    $('#profilePicture').attr('src', 'data:image/png;base64,'+user.profilePicture);
                }
                $('#uid').val(user.uid); // Ensure the hidden field is populated with a valid UUID
                $('#name').val(user.name);
                $('#email').val(user.email);
                $('#password').val(user.password);
                $('#confirmPassword').val(user.password);
                $('#phone').val(user.phone);
                $('#address').val(user.address);
            } else {
                Swal.fire('Failed to load user details');
            }
        },
        error: function() {
            Swal.fire('Error fetching user details');
        }
    });

    $('#profileForm').submit(function(event) {
        event.preventDefault();
        const formData = new FormData();
        formData.append('file', $('#profilePictureFile')[0].files[0]);
        const user = {
            uid: $('#uid').val(), // Ensure this is a valid UUID
            name: $('#name').val(),
            email: $('#email').val(),
            address: $('#address').val(),
            password: $('#password').val(),
            phone: $('#phone').val(),
        };
        formData.append('user', JSON.stringify(user));

        $.ajax({
            url: 'http://localhost:8080/api/v1/user/update',
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + window.localStorage.getItem('token')
            },
            data: formData,
            processData: false,
            contentType: false,
            success: function(response) {
                Swal.fire('Profile updated successfully');
                // Redirect to the Home page
                window.location.href = 'admin.html';
            },
            error: function() {
                Swal.fire('Error updating profile');
            }
        });
    });
});
function confirmLogout() {
    Swal.fire({
        title: 'Are you sure?',
        text: "You won't be able to revert this!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#0080ff',
        cancelButtonColor: '#ff0000',
        confirmButtonText: 'Yes, logout!'
    }).then((result) => {
        if (result.isConfirmed) {
            window.location.href = "login.html";
        }
    });
}