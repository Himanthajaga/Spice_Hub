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
                                                            <p class="card-text"><strong>Price: </strong> $${spice.price}</p>
                                                            <p class="card-text"><strong>Items in Stock: </strong> ${spice.quantity}</p>
                                                            <a href="#" class="btn btn-primary">View</a>
                                                            <a href="#" class="btn btn-danger delete-btn" data-id="${spice.id}">Delete</a>
                                                            <a href="update_spice.html?id=${spice.id}" class="btn btn-warning update-btn" data-id="${spice.id}">Update</a>
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
                                                            alert('Spice deleted successfully');
                                                            location.reload(); // Reload the page to update the spice list
                                                        } else {
                                                            alert('Failed to delete spice');
                                                        }
                                                    },
                                                    error: function() {
                                                        alert('Error deleting spice');
                                                    }
                                                });
                                            }
                                        });

                                        spiceContainer.on('click', '.update-btn', function() {
                                            const spiceId = $(this).data('id');
                                            window.location.href = `update_spice.html?id=${spiceId}`;
                                        });
                                    } else {
                                        alert('Failed to load spices');
                                    }
                                },
                                error: function() {
                                    alert('Error fetching spices');
                                }
                            });
                        } else {
                            alert('Failed to fetch user details');
                        }
                    },
                    error: function() {
                        alert('Error fetching user details');
                    }
                });
            } else {
                console.log('User email not found in token'); // Log the case when user email is not found in token
                alert('User email not found in token');
            }
        } catch (e) {
            console.log('Error parsing token payload', e); // Log parsing errors
            alert('Invalid token');
        }
    } else {
        console.log('User not logged in'); // Log the case when user is not logged in
        alert('User not logged in');
    }
});