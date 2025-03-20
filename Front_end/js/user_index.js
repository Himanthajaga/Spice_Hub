$(document).ready(function() {
    $.ajax({
        url: 'http://localhost:8080/api/v1/spice/get',
        method: 'GET',
        success: function(response) {
            let spiceContainer = $('#spiceContainer');
            spiceContainer.empty();
            response.data.forEach(spice => {
                const imageUrl = spice.imageURL ? `data:image/png;base64,${spice.imageURL}` : 'assests/Images/noImage.png';
                let spiceCard = `
                    <div class="col-md-4">
                        <div class="card mb-4 fs-5 border border-success rounded shadow-lg spice-card bg-light text-dark p-3 mb-5 border-2" style="width: 20rem">
                            <img src="${imageUrl}" class="card-img-top" alt="Spice Image">
                            <div class="card-body">
                                <h5 class="card-title">${spice.name}</h5>
                                <p class="card-text">${spice.description}</p>
                                <p class="card-text"><strong>Price: </strong> ${spice.price}</p>
                                <button class="btn btn-outline-success bid-btn" data-id="${spice.id}">Bid Now</button>
                            </div>
                        </div>
                    </div>
                `;
                spiceContainer.append(spiceCard);
            });
            $('.bid-btn').click(function() {
                let spiceId = $(this).data('id');
                let bidAmount = parseFloat(prompt("Enter your bid amount:"));
                let userId = localStorage.getItem('userId'); // Assuming userId is stored in localStorage

                if (isNaN(bidAmount) || bidAmount <= 0) {
                    Swal.fire('Invalid bid amount');
                    return;
                }

                let bidData = {
                    bidAmount: bidAmount,
                    listingId: spiceId,
                    userId: userId,
                    status: 'ACTIVE',
                    bidTime: new Date().toISOString()
                };

                $.ajax({
                    url: 'http://localhost:8080/api/v1/bids/save',
                    method: 'POST',
                    headers: {
                        'Authorization': 'Bearer ' + localStorage.getItem('token'), // Include authentication token if required
                        'Accept': 'application/json' // Ensure the server expects JSON response
                    },
                    contentType: 'application/json',
                    data: JSON.stringify(bidData),
                    success: function(response) {
                        Swal.fire('Bid placed successfully');
                    },
                    error: function(error) {
                        let errorMessage = error.responseText ? error.responseText : 'An unknown error occurred';
                        Swal.fire('Error placing bid: ' + errorMessage);
                    }
                });
            });
        },
        error: function(error) {
            console.log(error);
        }
    });
});
function filterSpices() {
    const filter = document.getElementById('filterInput').value.toLowerCase();
    const cards = document.getElementsByClassName('spice-card');
    for (let i = 0; i < cards.length; i++) {
        const card = cards[i];
        const name = card.getElementsByClassName('card-title')[0].innerText.toLowerCase();
        if (name.includes(filter)) {
            card.style.display = '';
        } else {
            card.style.display = 'none';
        }
    }
}
function confirmLogout() {
    Swal.fire({
        title: 'Are you sure?',
        text: "You won't be able to revert this!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#0082ff',
        cancelButtonColor: '#ff0000',
        confirmButtonText: 'Yes, logout!'
    }).then((result) => {
        if (result.isConfirmed) {
            window.location.href = "login.html";
        }
    });
}
function inactivateBid(bidId) {
    fetch(`http://localhost:8080/api/v1/bids/inactivate/${bidId}`, {
        method: 'PUT',
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('token')
        }
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(errorText => {
                    console.error('Server response:', errorText);
                    throw new Error(errorText);
                });
            }
            return response.json();
        })
        .then(data => {
            console.log('Bid inactivated successfully:', data);
            Swal.fire('Bid inactivated successfully');
        })
        .catch(error => {
            console.error('Error:', error.message);
        });
}