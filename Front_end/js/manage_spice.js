
    document.addEventListener('DOMContentLoaded', function() {
    fetchSpices();
});

    function fetchSpices() {
    fetch('/api/v1/spice/get')
        .then(response => response.json())
        .then(data => {
            const spiceContainer = document.getElementById('spiceContainer');
            spiceContainer.innerHTML = '';
            data.data.forEach(spice => {
                const card = document.createElement('div');
                card.className = 'col-md-4 spice-card';
                card.innerHTML = `
                            <div class="card mb-4">
                                <img src="${spice.imageURL}" class="card-img-top" alt="Spice Image">
                                <div class="card-body">
                                    <h5 class="card-title">${spice.name}</h5>
                                    <p class="card-text">${spice.description}</p>
                                    <p class="card-text"><strong>Price: </strong> $${spice.price}</p>
                                    <p class="card-text"><strong>Items in Stock: </strong> ${spice.stock}</p>
                                    <a href="#" class="btn btn-primary">View</a>
                                    <a href="#" class="btn btn-danger disabled">Delete</a>
                                </div>
                            </div>
                        `;
                spiceContainer.appendChild(card);
            });
        })
        .catch(error => console.error('Error fetching spices:', error));
}

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
