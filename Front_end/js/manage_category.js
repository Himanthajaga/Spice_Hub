$(document).ready(function() {
    const token = window.localStorage.getItem('token');

    if (token) {
        $.ajax({
            url: 'http://localhost:8080/api/v1/category/get',
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token
            },
            success: function(response) {
                if (response.code === 200) {
                    const categories = response.data;
                    const categoryContainer = $('#categoryContainer');
                    categoryContainer.empty();
                    categories.forEach(category => {
                        const card = `
                            <div class="col-md-4 category-card">
                                <div class="card mb-4">
                                    <img src="data:image/png;base64,${category.imageURL}" class="card-img-top" alt="Category Image">
                                    <div class="card-body">
                                        <h5 class="card-title">${category.name}</h5>
                                        <p class="card-text">${category.description}</p>
                                        <a href="#" class="btn btn-primary">View</a>
                                        <a href="#" class="btn btn-danger delete-btn" data-id="${category.id}">Delete</a>
                                        <a href="update_category.html?id=${category.id}" class="btn btn-warning update-btn" data-id="${category.id}">Update</a>
                                    </div>
                                </div>
                            </div>
                        `;
                        categoryContainer.append(card);
                    });

                    categoryContainer.on('click', '.delete-btn', function() {
                        const categoryId = $(this).data('id');
                        if (confirm('Are you sure you want to delete this category?')) {
                            $.ajax({
                                url: `http://localhost:8080/api/v1/category/delete/${categoryId}`,
                                method: 'DELETE',
                                headers: {
                                    'Authorization': 'Bearer ' + token
                                },
                                success: function(response) {
                                    if (response.code === 201) {
                                        Swal.fire('Category deleted successfully');
                                        location.reload();
                                    } else {
                                        Swal.fire('Failed to delete category');
                                    }
                                },
                                error: function() {
                                    Swal.fire('Error deleting category');
                                }
                            });
                        }
                    });

                    categoryContainer.on('click', '.update-btn', function() {
                        const categoryId = $(this).data('id');
                        window.location.href = `update_category.html?id=${categoryId}`;
                    });
                } else {
                    Swal.fire('Failed to load categories');
                }
            },
            error: function() {
                Swal.fire('Error fetching categories');
            }
        });
    } else {
        Swal.fire('User not logged in');
    }
});
function filterCategories() {
    const filterValue = document.getElementById('filterInput').value.toLowerCase();
    const categoryCards = document.querySelectorAll('.category-card');

    categoryCards.forEach(card => {
        const categoryName = card.querySelector('.card-title').textContent.toLowerCase();
        if (categoryName.includes(filterValue)) {
            card.style.display = ''; // Show the card
        } else {
            card.style.display = 'none'; // Hide the card
        }
    });
}
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