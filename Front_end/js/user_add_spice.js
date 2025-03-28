function isValidUUID(uuid) {
    const regex = /^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$/i;
    return regex.test(uuid);
}

document.addEventListener('DOMContentLoaded', function() {
    fetchCategories();
});

function fetchCategories() {
    fetch('http://localhost:8080/api/v1/category/get', {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('token'),
            'Accept': 'application/json'
        }
    })
        .then(response => response.json())
        .then(data => {
            if (data.code === 200) {
                const categories = data.data;
                const categorySelect = document.getElementById('spiceCategory');
                categories.forEach(category => {
                    const option = document.createElement('option');
                    option.value = category.id;
                    option.text = category.name;
                    categorySelect.appendChild(option);
                });
            } else {
                console.error('Failed to load categories');
            }
        })
        .catch(error => {
            console.error('Error fetching categories:', error);
        });
}

document.getElementById('addSpiceForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const spice = {
        name: document.getElementById('spiceName').value.trim(),
        description: document.getElementById('spiceDescription').value.trim(),
        quantity: parseInt(document.getElementById('spiceStock').value),
        price: parseFloat(document.getElementById('spicePrice').value),
        category: document.getElementById('spiceCategory').selectedOptions[0].text.trim() // Get the category name
    };

    const formData = new FormData();
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
        body: formData,
        headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('token'),
            'Accept': 'application/json'
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
            Swal.fire('Spice saved successfully');
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