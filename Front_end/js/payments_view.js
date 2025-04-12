$(document).ready(function () {
    fetchPayments();

    function fetchPayments() {
        $.ajax({
            url: 'http://localhost:8080/api/v1/payment/get',
            method: 'GET',
            success: function (response) {
                renderPayments(response.data);
            },
            error: function (error) {
                console.error('Error fetching payments:', error);
                Swal.fire('Error fetching payments: ' + (error.responseJSON?.message || 'Unknown error'));
            }
        });
    }

    function renderPayments(payments) {
        const paymentContainer = $('#paymentContainer');
        paymentContainer.empty();
        payments.forEach(payment => {
            const paymentRow = `
                <tr>
                   <td>${payment.friendlyId}</td>
                    <td>${payment.amount.toFixed(2)}</td>
                    <td>${new Date(payment.paymentDate).toLocaleDateString()}</td>
                    <td>${payment.paymentMethodId}</td>
                    <td>${payment.buyerEmail}</td>
                    <td>${payment.spiceOwnerEmail}</td>
                </tr>
            `;
            paymentContainer.append(paymentRow);
        });
    }
});