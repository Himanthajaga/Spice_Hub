$(document).ready(function() {
    const token = window.localStorage.getItem('token');

    if (token) {
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            const userEmail = payload.sub; // Assuming the email is stored in the 'sub' field

            if (userEmail) {
                console.log('User Email:', userEmail); // Log the user email for debugging
                $.ajax({
                    url: `http://localhost:8080/api/v1/user/getByEmail?email=${userEmail}`,
                    method: 'GET',
                    headers: {
                        'Authorization': 'Bearer ' + token
                    },
                    success: function(response) {
                        console.log('User Response:', response); // Log the user response for debugging
                        if (response.code === 200) {
                            const userId = response.data.uid;
                            console.log('User ID:', userId); // Log the user ID for debugging
                            $.ajax({
                                url: `http://localhost:8080/api/v1/spice/getByUser?userId=${userId}`,
                                method: 'GET',
                                headers: {
                                    'Authorization': 'Bearer ' + token
                                },
                                success: function(response) {
                                    console.log('Spice Response:', response); // Log the spice response for debugging
                                    if (response.code === 201) {
                                        const spices = response.data;
                                        const spiceContainer = $('#spiceContainer');
                                        spiceContainer.empty();
                                        spices.forEach(spice => {
                                            const card = `
                                                <div class="col-md-4 spice-card">
                                                    <div class="card mb-4">
                                                        <img src="data:image/png;base64,${spice.imageURL}" class="card-img-top" alt="Spice Image">
                                                        <div class="card-body">
                                                            <h5 class="card-title">${spice.name}</h5>
                                                            <p class="card-text">${spice.description}</p>
                                                            <p class="card-text"><strong>Price: </strong> <span class="price">${spice.price}</span></p>
                                                            <p class="card-text"><strong>Location: </strong> <span class="location">${spice.location}</span></p>
                                                            <p class="card-text"><strong>Items in Stock: </strong> <span class = "qty">${spice.quantity}</span> </p>
                                                            <a href="#" class="btn btn-outline-info">View</a>
                                                            <a href="#" class="btn btn-outline-danger delete-btn" data-id="${spice.id}">Delete</a>
                                                            <a href="update_spice.html?id=${spice.id}" class="btn btn-outline-warning update-btn" data-id="${spice.id}">Update</a>
                                                        </div>
                                                    </div>
                                                </div>
                                            `;
                                            spiceContainer.append(card);
                                        });

                                        // Event delegation for dynamically added elements
                                        spiceContainer.on('click', '.delete-btn', function() {
                                            const spiceId = $(this).data('id');
                                            if (confirm('Are you sure you want to delete this spice?')) {
                                                $.ajax({
                                                    url: `http://localhost:8080/api/v1/spice/delete?id=${spiceId}`,
                                                    method: 'DELETE',
                                                    headers: {
                                                        'Authorization': 'Bearer ' + token
                                                    },
                                                    success: function(response) {
                                                        if (response.code === 200) {
                                                              Swal.fire('Spice deleted successfully');
                                                            location.reload(); // Reload the page to update the spice list
                                                        } else {
                                                              Swal.fire('Failed to delete spice');
                                                        }
                                                    },
                                                    error: function() {
                                                          Swal.fire('Error deleting spice');
                                                    }
                                                });
                                            }
                                        });

                                        spiceContainer.on('click', '.update-btn', function() {
                                            const spiceId = $(this).data('id');
                                            window.location.href = `update_spice.html?id=${spiceId}`;
                                        });
                                    } else {
                                          Swal.fire('Failed to load spices');
                                    }
                                },
                                error: function() {
                                      Swal.fire('Error fetching spices');
                                }
                            });
                        } else {
                              Swal.fire('Failed to fetch user details');
                        }
                    },
                    error: function() {
                          Swal.fire('Error fetching user details');
                    }
                });
            } else {
                console.log('User email not found in token'); // Log the case when user email is not found in token
                  Swal.fire('User email not found in token');
            }
        } catch (e) {
            console.log('Error parsing token payload', e); // Log parsing errors
              Swal.fire('Invalid token');
        }
    } else {
        console.log('User not logged in'); // Log the case when user is not logged in
          Swal.fire('User not logged in');
    }
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