$(document).ready(function() {
    $.ajax({
        url: 'http://localhost:8080/api/v1/bids/user',
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('token')
        },
        success: function(response) {
            let bidsContainer = $('#bidsContainer');
            bidsContainer.empty();
            response.data.forEach(bid => {
                const imageUrl = bid.imageURL ? `data:image/png;base64,${bid.imageURL}` : 'assets/Images/noImage.png';
                let bidCard = `
                    <div class="col-md-4 bid-card">
                        <div class="card mb-4">
                            <img src="${imageUrl}" class="card-img-top" alt="Bid Image">
                            <div class="card-body">
                                <h5 class="card-title">${bid.listingName}</h5>
                                <p class="card-text">${bid.listingDescription}</p>
                                <p class="card-text"><strong>Bid Amount: </strong> ${bid.bidAmount}</p>
                                <p class="card-text"><strong>Status: </strong> ${bid.status}</p>
                                <p class="card-text"><strong>Bid Time: </strong> ${new Date(bid.bidTime).toLocaleString()}</p>
                                <button class="btn btn-outline-dark pay-btn" data-bid-id="${bid.id}">Pay</button>
                               <button class="btn btn-outline-danger remove-btn no-style-change" data-bid-id="${bid.id}">Remove</button>
                            </div>
                        </div>
                    </div>
                `;
                bidsContainer.append(bidCard);
            });

            // Add click event listener for pay buttons
            $('.pay-btn').click(function() {
                const bidId = $(this).data('bid-id');
                window.location.href = `payment.html?bidId=${bidId}`;
            });

            // Add click event listener for remove buttons
            $('.remove-btn').click(function() {
                const bidId = $(this).data('bid-id');
                $.ajax({
                    url: `http://localhost:8080/api/v1/bids/delete/${bidId}`,
                    method: 'DELETE',
                    headers: {
                        'Authorization': 'Bearer ' + localStorage.getItem('token')
                    },
                    success: function() {
                        Swal.fire('Bid removed successfully').then(() => {
                            location.reload();
                        });
                    },
                    error: function() {
                        Swal.fire('Error removing bid');
                    }
                });
            });
        },
        error: function(error) {
            console.log('Error:', error);
        }
    });
});

function filterBids() {
    const filter = document.getElementById('filterInput').value.toLowerCase();
    const cards = document.getElementsByClassName('bid-card');
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
        confirmButtonColor: '#0084ff',
        cancelButtonColor: '#ff0000',
        confirmButtonText: 'Yes, logout!'
    }).then((result) => {
        if (result.isConfirmed) {
            window.location.href = "login.html";
        }
    });
}