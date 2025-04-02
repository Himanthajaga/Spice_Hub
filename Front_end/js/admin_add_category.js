document.getElementById('addCategoryForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const category = {
        name: document.getElementById('categoryName').value.trim(),
        description: document.getElementById('categoryDescription').value.trim()
    };

    const formData = new FormData();
    formData.append('category', JSON.stringify(category));

    const categoryImageFile = document.getElementById('categoryImageURL');
    if (categoryImageFile && categoryImageFile.files.length > 0) {
        formData.append('file', categoryImageFile.files[0]);
    } else {
        console.error('No image file selected');
        return;
    }

    fetch('http://localhost:8080/api/v1/category/save', {
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
            console.log('Category saved successfully:', data);
            Swal.fire('Category saved successfully');
            window.location.href = 'admin.html';
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
document.getElementById('cancelButton').addEventListener('click', function() {
    window.location.href = 'admin.html';
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