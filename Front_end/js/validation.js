// Utility function to validate all input fields in a form
function validateForm(formId) {
    const form = document.getElementById(formId);
    const inputs = form.querySelectorAll('input, textarea, select');
    let isValid = true;

    inputs.forEach(input => {
        if (!input.checkValidity()) {
            isValid = false;
            const errorMessage = input.validationMessage || 'Invalid input';
            Swal.fire({
                icon: 'error',
                title: 'Validation Error',
                text: `${input.name || 'Field'}: ${errorMessage}`,
            });
            input.focus();
            return false; // Stop further validation
        }
    });

    return isValid;
}

// Attach validation to all forms on the page
document.addEventListener('DOMContentLoaded', () => {
    const forms = document.querySelectorAll('form');
    forms.forEach(form => {
        form.addEventListener('submit', (event) => {
            if (!validateForm(form.id)) {
                event.preventDefault(); // Prevent form submission if validation fails
            }
        });
    });
});