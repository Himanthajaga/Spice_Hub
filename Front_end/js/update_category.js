$(document).ready(function() {
    const urlParams = new URLSearchParams(window.location.search);
    const categoryId = urlParams.get('id');
    const token = window.localStorage.getItem('token');

    if (categoryId && token) {
        $.ajax({
            url: `http://localhost:8080/api/v1/category/getById?id=${categoryId}`,
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token
            },
            success: function(response) {
                if (response.code === 200) {
                    const category = response.data;
                    if (category.imageURL) {
                        $('#profilePicture').attr('src', 'data:image/png;base64,' + category.imageURL);
                    }
                    $('#uid').val(category.id);
                    $('#categoryName').val(category.name);
                    $('#categoryDescription').val(category.description);
                } else {
                    Swal.fire('Failed to load category details');
                }
            },
            error: function() {
                Swal.fire('Error fetching category details');
            }
        });
    } else {
        Swal.fire('Invalid category ID or user not logged in');
    }

    $('#updateCategoryForm').submit(function(event) {
        event.preventDefault();
        const formData = new FormData();
        formData.append('file', $('#profilePictureFile')[0].files[0]);
        const category = {
            id: $('#uid').val(),
            name: $('#categoryName').val(),
            description: $('#categoryDescription').val()
        };
        formData.append('category', JSON.stringify(category));

        $.ajax({
            url: 'http://localhost:8080/api/v1/category/update',
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + token
            },
            data: formData,
            processData: false,
            contentType: false,
            success: function(response) {
                Swal.fire('Category updated successfully');
                window.location.href = 'manage_category.html';
            },
            error: function() {
                Swal.fire('Error updating category');
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