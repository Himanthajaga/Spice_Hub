$(document).ready(function () {
    fetchSpices();
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
            <div class="col-md-4 spice-card" data-id="${spice.id}" data-url="${imageUrl}">
                <div class="card mb-4 border border-success rounded shadow-lg bg-light text-dark p-3">
                    <div class="card-header d-flex justify-content-end">
                        <button class="btn btn-danger btn-sm remove-btn">&times;</button>
                    </div>
                    <img src="${imageUrl}" class="card-img-top" alt="Spice Image">
                    <div class="card-body">
                        <h5 class="card-title">${spice.name}</h5>
                        <p class="card-text">${spice.description}</p>
                        <p class="card-text"><strong>Price: </strong> <span class="price">${spice.price}</span></p>
                        <p class="card-text"><strong>Quantity: </strong> <span class="quantity">${spice.quantity}</span></p>
                        <p class="card-text"><strong>Location: </strong> <span class="location">${spice.location}</span></p>
                          <p class="card-text"><strong>Listed Time: </strong> <span class="listed-time">${spice.listedTime ? new Date(spice.listedTime).toLocaleString() : 'N/A'}</span></p>
                    </div>
                </div>
            </div>
        `;
        spiceContainer.append(spiceCard);
    });

    attachRemoveButtonListeners();
}

// Function to attach event listeners for remove buttons
function attachRemoveButtonListeners() {
    $('.remove-btn').click(function () {
        let spiceId = $(this).closest('.spice-card').data('id');
        if (!spiceId) {
            console.error('Spice ID is undefined');
            return;
        }
        Swal.fire({
            title: 'Are you sure?',
            text: "You won't be able to revert this!",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, remove it!'
        }).then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    url: `http://localhost:8080/api/v1/spice/delete?id=${spiceId}`,
                    method: 'DELETE',
                    success: function () {
                        Swal.fire('Deleted!', 'Your spice has been removed.', 'success').then(() => {
                            fetchSpices();
                        });
                    },
                    error: function (error) {
                        Swal.fire('Error!', 'There was an error removing the spice.', 'error');
                    }
                });
            }
        });
    });
}

// Function to filter spices dynamically
function filterSpices() {
    const nameFilter = $('#filterInput').val().toLowerCase();
    const locationFilter = $('#locationInput').val().toLowerCase();
    const minPrice = parseFloat($('#minPriceInput').val());
    const maxPrice = parseFloat($('#maxPriceInput').val());
    const sortByLatest = $('#latestItemsCheckbox').is(':checked');

    const cards = $('.spice-card');

    if (!nameFilter && !locationFilter && isNaN(minPrice) && isNaN(maxPrice) && !sortByLatest) {
        fetchSpices();
        return;
    }

    cards.each(function () {
        const card = $(this);
        const name = card.find('.card-title').text().toLowerCase();
        const location = card.find('.location').text().toLowerCase();
        const price = parseFloat(card.find('.price').text());
        const listedTime = new Date(card.find('.listed-time').text()).getTime();

        let isVisible = true;

        if (nameFilter && !name.includes(nameFilter)) isVisible = false;
        if (locationFilter && !location.includes(locationFilter)) isVisible = false;
        if (!isNaN(minPrice) && price < minPrice) isVisible = false;
        if (!isNaN(maxPrice) && price > maxPrice) isVisible = false;

        card.data('listedTime', listedTime);
        card.toggle(isVisible);
    });

    if (sortByLatest) {
        const sortedCards = cards.sort((a, b) => $(b).data('listedTime') - $(a).data('listedTime'));
        $('#spiceContainer').append(sortedCards);
    }
}

// Function to attach filter listeners
function attachFilterListeners() {
    $('#filterInput, #locationInput, #minPriceInput, #maxPriceInput').on('input', debounce(filterSpices, 300));
    $('#latestItemsCheckbox').on('change', debounce(filterSpices, 300));
}

// Debounce function to limit the frequency of filter execution
function debounce(func, delay) {
    let timeout;
    return function (...args) {
        clearTimeout(timeout);
        timeout = setTimeout(() => func.apply(this, args), delay);
    };
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