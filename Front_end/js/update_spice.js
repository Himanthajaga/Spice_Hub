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
                    $('#uid').val(spice.id); // Ensure the hidden field is populated with a valid UUID
                    $('#spiceName').val(spice.name);
                    $('#spiceDescription').val(spice.description);
                    $('#spicePrice').val(spice.price);
                    $('#spiceStock').val(spice.quantity);
                    $('#spiceCategory').val(spice.category);
                } else {
                    alert('Failed to load spice details');
                }
            },
            error: function() {
                alert('Error fetching spice details');
            }
        });
    } else {
        alert('Invalid spice ID or user not logged in');
    }

    $('#updateSpiceForm').submit(function(event) {
        event.preventDefault();
        const formData = new FormData();
        formData.append('file', $('#profilePictureFile')[0].files[0]);
        const spice = {
            id: $('#uid').val(), // Ensure this is a valid UUID
            name: $('#spiceName').val(),
            description: $('#spiceDescription').val(),
            price: $('#spicePrice').val(),
            quantity: $('#spiceStock').val(),
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
                alert('Spice updated successfully');
                // Redirect to the manage spices page
                window.location.href = 'manage_spice.html';
            },
            error: function() {
                alert('Error updating spice');
            }
        });
    });
});