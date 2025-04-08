$(document).ready(function () {
    // Fetch spices and populate the container
    fetchSpices();

    // Attach event listeners for dynamic filtering
    attachFilterListeners();
});
// Function to fetch spices and populate the container
function fetchSpices() {
    $.ajax({
        url: 'http://localhost:8080/api/v1/spice/get',
        method: 'GET',
        success: function (response) {
            populateSpiceContainer(response.data);
        },
        error: function (error) {
            console.error('Error fetching spices:', error);
        }
    });
}

// Function to populate the spice container
function populateSpiceContainer(spices) {
    let spiceContainer = $('#spiceContainer');
    spiceContainer.empty();

    spices.forEach(spice => {
        const imageUrl = spice.imageURL ? `data:image/png;base64,${spice.imageURL}` : 'assets/Images/noImage.png';
        const listedTime = spice.listedTime ? new Date(spice.listedTime).toLocaleString() : 'N/A';

        let spiceCard = `
            <div class="col-md-4 spice-card" data-url="${imageUrl}">
                <div class="card mb-4 fs-5 border border-success rounded shadow-lg bg-light text-dark p-3 mb-5 border-2" style="width: 20rem">
                    <img src="${imageUrl}" class="card-img-top" alt="Spice Image">
                    <div class="card-body">
                        <h5 class="card-title">${spice.name}</h5>
                        <p class="card-text">${spice.description}</p>
                        <p class="card-text"><strong>Price: </strong> <span class="price">${spice.price}</span></p>
                        <p class="card-text"><strong>Quantity: </strong> <span class="quantity">${spice.quantity}</span></p>
                        <p class="card-text"><strong>Location: </strong> <span class="location">${spice.location}</span></p>
                       <p class="card-text"><strong>Listed Time: </strong> <span class="listed-time">${spice.listedTime ? new Date(spice.listedTime).toLocaleString() : 'N/A'}</span></p>
                        <button class="btn btn-outline-success bid-btn" data-id="${spice.id}">Bid Now</button>
                    </div>
                </div>
            </div>
        `;
        spiceContainer.append(spiceCard);
    });

    // Attach click event to bid buttons
    attachBidButtonListeners();
}

// Function to attach event listeners for bid buttons
function attachBidButtonListeners() {
    $('.bid-btn').click(function (event) {
        event.stopPropagation();
        let spiceId = $(this).data('id');
        let bidAmount = parseFloat(prompt("Enter your bid amount:"));

        if (isNaN(bidAmount) || bidAmount <= 0) {
            Swal.fire('Invalid bid amount');
            return;
        }

        placeBid(spiceId, bidAmount, $(this).closest('.spice-card').data('url'));
    });
}

// Function to place a bid
function placeBid(spiceId, bidAmount, imageUrl) {
    let token = localStorage.getItem('token');
    if (!token) {
        Swal.fire('Authentication token not found');
        return;
    }

    let bidData = {
        bidAmount: bidAmount,
        listingId: spiceId,
        status: 'ACTIVE',
        bidTime: new Date().toISOString()
    };

    let formData = new FormData();
    formData.append('bid', JSON.stringify(bidData));

    if (imageUrl) {
        fetch(imageUrl)
            .then(res => res.blob())
            .then(blob => {
                const reader = new FileReader();
                reader.onloadend = () => {
                    const base64data = reader.result.split(',')[1];
                    formData.append('imageURL', base64data);

                    sendBidRequest(formData, token);
                };
                reader.readAsDataURL(blob);
            });
    } else {
        console.error('Image URL is undefined');
    }
}

// Function to send the bid request
function sendBidRequest(formData, token) {
    $.ajax({
        url: 'http://localhost:8080/api/v1/bids/save',
        method: 'POST',
        headers: {
            'Authorization': 'Bearer ' + token
        },
        contentType: false,
        processData: false,
        data: formData,
        success: function () {
            Swal.fire('Bid placed successfully');
        },
        error: function (error) {
            let errorMessage = error.responseText ? error.responseText : 'An unknown error occurred';
            Swal.fire('Error placing bid: ' + errorMessage);
        }
    });
}

// Debounce function to limit the frequency of filter execution
function debounce(func, delay) {
    let timeout;
    return function (...args) {
        clearTimeout(timeout);
        timeout = setTimeout(() => func.apply(this, args), delay);
    };
}

// Optimized filter function
function filterSpices() {
    console.log("Filter function triggered");

    const nameFilter = document.getElementById('filterInput').value.toLowerCase();
    const locationFilter = document.getElementById('locationInput').value.toLowerCase();
    const minPrice = parseFloat(document.getElementById('minPriceInput').value);
    const maxPrice = parseFloat(document.getElementById('maxPriceInput').value);
    const sortByLatest = document.getElementById('latestItemsCheckbox').checked;

    console.log("Name Filter:", nameFilter);
    console.log("Location Filter:", locationFilter);
    console.log("Min Price:", minPrice);
    console.log("Max Price:", maxPrice);
    console.log("Sort By Latest:", sortByLatest);

    const cards = Array.from(document.getElementsByClassName('spice-card'));

    // If all filter fields are empty and "Sort by Latest" is unchecked, reload all cards
    if (!nameFilter && !locationFilter && isNaN(minPrice) && isNaN(maxPrice) && !sortByLatest) {
        console.log("No filters applied, reloading all cards");
        fetchSpices(); // Re-fetch and re-render all cards
        return;
    }

    const filteredCards = [];

    cards.forEach(card => {
        const name = card.querySelector('.card-title').innerText.toLowerCase();
        const location = card.querySelector('.location').innerText.toLowerCase();
        const price = parseFloat(card.querySelector('.price').innerText);
        const listedTime = new Date(card.querySelector('.listed-time').innerText).getTime();

        let isVisible = true;

        if (nameFilter && !name.includes(nameFilter)) isVisible = false;
        if (locationFilter && !location.includes(locationFilter)) isVisible = false;
        if (!isNaN(minPrice) && price < minPrice) isVisible = false;
        if (!isNaN(maxPrice) && price > maxPrice) isVisible = false;

        card.dataset.listedTime = listedTime; // Cache listed time for sorting
        card.style.display = isVisible ? '' : 'none';
        if (isVisible) filteredCards.push(card);
    });

    // Sort filtered cards if "Sort by Latest" is checked
    if (sortByLatest) {
        filteredCards.sort((a, b) => b.dataset.listedTime - a.dataset.listedTime);
        const container = document.getElementById('spiceContainer');
        filteredCards.forEach(card => container.appendChild(card));
    }
}

// Attach debounced filter function to input events
function attachFilterListeners() {
    const debouncedFilter = debounce(filterSpices, 300);
    document.getElementById('filterInput').addEventListener('input', debouncedFilter);
    document.getElementById('locationInput').addEventListener('input', debouncedFilter);
    document.getElementById('minPriceInput').addEventListener('input', debouncedFilter);
    document.getElementById('maxPriceInput').addEventListener('input', debouncedFilter);
    document.getElementById('latestItemsCheckbox').addEventListener('change', debouncedFilter);
}

// Function to confirm logout
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

// Function to inactivate a bid
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
        .then(() => {
            Swal.fire('Bid inactivated successfully');
        })
        .catch(error => {
            console.error('Error:', error.message);
        });
}