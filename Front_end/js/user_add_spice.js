function isValidUUID(uuid) {
    const regex = /^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$/i;
    return regex.test(uuid);
}

document.getElementById('addSpiceForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const spice = {
        name: document.getElementById('spiceName').value.trim(),
        description: document.getElementById('spiceDescription').value.trim(),
        quantity: parseInt(document.getElementById('spiceStock').value),
        price: parseFloat(document.getElementById('spicePrice').value),
        category: document.getElementById('spiceCategory').value.trim()
    };

    // Validate the name field
    if (!spice.name) {
        return;
    }

    const formData = new FormData();
    formData.append('spice', JSON.stringify(spice));

    const spiceImageFile = document.getElementById('spiceImageURL');
    if (spiceImageFile && spiceImageFile.files.length > 0) {
        formData.append('file', spiceImageFile.files[0]);
    } else {
        console.error('No image file selected');
        return;
    }

    // Log the FormData object
    for (let pair of formData.entries()) {
        console.log(pair[0] + ': ' + pair[1]);
    }

    fetch('http://localhost:8080/api/v1/spice/save', {
        method: 'POST',
        body: formData,
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('token'), // Include authentication token if required
            'Accept': 'application/json' // Ensure the server expects JSON response
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
            console.log('Spice saved successfully:', data);
            alert('Spice saved successfully');
            window.location.href = 'user_index.html';
        })
        .catch(error => {
            console.error('Error:', error.message);
            try {
                const errorData = JSON.parse(error.message);
                console.log('Response data:', errorData);
                if (errorData.data) {
                    for (const [field, message] of Object.entries(errorData.data)) {
                        // Handle field-specific errors
                    }
                }
            } catch (e) {
                console.error('Failed to parse error message:', e);
            }
        });
});