document.getElementById('addSpiceForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const spiceData = {
        name: document.getElementById('spiceName').value,
        description: document.getElementById('spiceDescription').value,
        price: parseFloat(document.getElementById('spicePrice').value),
        quantity: parseInt(document.getElementById('spiceStock').value),
        imageURL: document.getElementById('spiceImageURL').value,
        category: document.getElementById('spiceCategory').value
    };

    fetch('http://localhost:8080/api/v1/spice/save', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(spiceData)
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert('Spice added successfully!');
            } else {
                alert('Failed to add spice.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('An error occurred while adding the spice.');
        });
});