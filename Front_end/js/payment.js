$(document).ready(function() {
    const bidId = new URLSearchParams(window.location.search).get('bidId');
    if (bidId) {
        // Fetch bid details from the server
        $.ajax({
            type: 'GET',
            url: `http://localhost:8080/api/v1/bids/${bidId}`,
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('token') // Assuming you store the token in localStorage
            },
            success: function(response) {
                const bid = response.data; // Ensure you access the data property
                $('#bidImage').attr('src', bid.imageURL ? `data:image/png;base64,${bid.imageURL}` : 'assets/Images/noImage.png');
                $('#amount').val(bid.bidAmount);
                $('#listingName').val(bid.listingName);
                $('#listingDescription').val(bid.listingDescription);
                $('#status').val(bid.status);
                $('#bidTime').val(new Date(bid.bidTime).toLocaleString());
            },
            error: function(error) {
                Swal.fire('Failed to load bid details: ' + error.responseJSON.message);
            }
        });
    }
});

const stripe = Stripe('pk_test_51R6ZgFKiBxldEfFS2fX0YC3riyZE1M5C8oFqG239MAcBiLl6TqyoKtzPsqiiXEV5ilYkqRYHvn8hnvqY5EdNfR8L00weOUntYV');
const elements = stripe.elements();
const cardElement = elements.create('card');
cardElement.mount('#card-element');

$('#paymentForm').on('submit', function(event) {
    event.preventDefault();
    stripe.createPaymentMethod({
        type: 'card',
        card: cardElement,
        billing_details: {
            name: $('#name').val(),
            email: $('#email').val(),
            phone: $('#phone').val(),
            address: {
                line1: $('#address').val(),
                city: $('#city').val(),
                state: $('#state').val(),
                postal_code: $('#zip').val(),
                country: $('#country').val()
            }
        },
    }).then(function(result) {
        if (result.error) {
            Swal.fire('Payment failed: ' + result.error.message);
        } else {
            const paymentMethodId = result.paymentMethod.id;
            const bidId = new URLSearchParams(window.location.search).get('bidId');
            $.ajax({
                type: 'POST',
                url: 'http://localhost:8080/api/v1/payment/confirm',
                data: JSON.stringify({ paymentMethodId, bidId, amount: $('#amount').val() }),
                contentType: 'application/json',
                success: function(response) {
                    const clientSecret = response.data;
                    stripe.confirmCardPayment(clientSecret).then(function(result) {
                        if (result.error) {
                            Swal.fire('Payment failed: ' + result.error.message);
                        } else {
                            Swal.fire('Payment successful!');
                            window.location.href = 'my_bids.html';
                            updateSpiceTable(bidId);
                        }
                    });
                },
                error: function(error) {
                    Swal.fire('Payment failed: ' + error.responseJSON.message);
                }
            });
        }
    });
});
function updateSpiceTable(spiceId) {
    $.ajax({
        type: 'PUT',
        url: `http://localhost:8080/api/v1/spices/${spiceId}`,
        data: JSON.stringify({ status: 'Sold' }),
        contentType: 'application/json',
        success: function(response) {
            window.location.href = 'my_bids.html';
        },
        error: function(error) {
            Swal.fire('Failed to update spice table: ' + error.responseJSON.message);
        }
    });
}