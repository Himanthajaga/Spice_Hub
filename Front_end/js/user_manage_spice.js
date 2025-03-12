document.getElementById('addSpiceForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const formData = new FormData();
    const spice = {
        name: document.getElementById('spiceName').value,
        description: document.getElementById('spiceDescription').value,
        price: parseFloat(document.getElementById('spicePrice').value),
        quantity: parseInt(document.getElementById('spiceStock').value),
        category: document.getElementById('spiceCategory').value,
        imageURL: document.getElementById('spiceImageURL').value
    };
    formData.append('spice', JSON.stringify(spice));

    const spiceImageFile = document.getElementById('spiceImageURL');
    if (spiceImageFile && spiceImageFile.files.length > 0) {
        formData.append('file', spiceImageFile.files[0]);
    } else {
        console.error('No image file selected');
        return;
    }

    fetch('http://localhost:8080/api/v1/spice/save', {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(errorData => {
                    throw new Error(JSON.stringify(errorData));
                });
            }
            return response.json();
        })
        .then(data => {
            console.log('Spice saved successfully:', data);
        })
        .catch(error => {
            console.error('Error:', error);
            try {
                const errorData = JSON.parse(error.message);
                console.log('Response data:', errorData);
                if (errorData.data) {
                    for (const [field, message] of Object.entries(errorData.data)) {
                    }
                }
            } catch (e) {
                console.error('Failed to parse error message:', e);
            }
        });
});