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
                    <div class="col-md-4 spice-card">
                        <div class="card mb-4">
                            <img src="${imageUrl}" class="card-img-top" alt="Spice Image">
                            <div class="card-body">
                                <h5 class="card-title">${spice.name}</h5>
                                <p class="card-text">${spice.description}</p>
                                <p class="card-text"><strong>Price: </strong> ${spice.price}</p>
                                <button class="btn btn-primary bid-btn" data-id="${spice.id}">Bid Now</button>
                            </div>
                        </div>
                    </div>
                `;
                spiceContainer.append(spiceCard);
            });

            $('.bid-btn').click(function() {
                let spiceId = $(this).data('id');
                $.ajax({
                    url: 'http://localhost:8080/api/v1/bids',
                    method: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify({ spiceId: spiceId }),
                    success: function(response) {
                        alert('Bid placed successfully');
                    },
                    error: function(error) {
                        alert('Error placing bid: ' + error.responseText);
                    }
                });
            });
        },
        error: function(error) {
            console.log('Error:', error);
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