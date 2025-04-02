$(document).ready(function() {
    const urlParams = new URLSearchParams(window.location.search);
    const spiceId = urlParams.get('id');
    const token = window.localStorage.getItem('token');

    if (spiceId && token) {
        $.ajax({
            url: `http://localhost:8080/api/v1/spice/getById?id=${spiceId}`,
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token
            },
            success: function(response) {
                if (response.code === 200) {
                    const spice = response.data;
                    if (spice.imageURL) {
                        $('#profilePicture').attr('src', 'data:image/png;base64,' + spice.imageURL);
                    }
                    $('#uid').val(spice.id);
                    $('#spiceName').val(spice.name);
                    $('#spiceDescription').val(spice.description);
                    $('#spicePrice').val(spice.price);
                    $('#spiceStock').val(spice.quantity);
                    $('#spiceLocation').val(spice.location);
                    $('#spiceCategory').val(spice.category);
                } else {
                    Swal.fire('Failed to load spice details');
                }
            },
            error: function() {
                Swal.fire('Error fetching spice details');
            }
        });
    } else {
        Swal.fire('Invalid spice ID or user not logged in');
    }

    $('#updateSpiceForm').submit(function(event) {
        event.preventDefault();
        const formData = new FormData();
        const fileInput = $('#profilePictureFile')[0].files[0];
        if (fileInput) {
            formData.append('file', fileInput);
        } else {
            const existingImage = $('#profilePicture').attr('src').split(',')[1];
            formData.append('existingImage', existingImage);
        }
        const spice = {
            id: $('#uid').val(),
            name: $('#spiceName').val(),
            description: $('#spiceDescription').val(),
            price: $('#spicePrice').val(),
            quantity: $('#spiceStock').val(),
            location: $('#spiceLocation').val(),
            category: $('#spiceCategory').val()
        };
        formData.append('spice', JSON.stringify(spice));

        $.ajax({
            url: 'http://localhost:8080/api/v1/spice/update',
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + token
            },
            data: formData,
            processData: false,
            contentType: false,
            success: function(response) {
                Swal.fire('Spice updated successfully');
                window.location.href = 'manage_spice.html';
            },
            error: function() {
                Swal.fire('Error updating spice');
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
        confirmButtonColor: '#0084ff',
        cancelButtonColor: '#ff0000',
        confirmButtonText: 'Yes, logout!'
    }).then((result) => {
        if (result.isConfirmed) {
            window.location.href = "login.html";
        }
    });
}