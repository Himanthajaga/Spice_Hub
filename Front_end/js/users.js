$(document).ready(function() {
    fetchUsers();

    function fetchUsers() {
        $.ajax({
            url: 'http://localhost:8080/api/v1/user/getAll',
            method: 'GET',
            success: function(users) {
                renderUsers(users);
            },
            error: function(error) {
                console.error('Error fetching users:', error);
            }
        });
    }

    function renderUsers(users) {
        const userContainer = $('#userContainer');
        userContainer.empty();
        users.forEach(user => {
            const profilePictureSrc = user.profilePicture ? `data:image/png;base64,${user.profilePicture}` : 'default-image.png';
            const userRow = `
                <tr>
                    <td><img src="${profilePictureSrc}" alt="Profile Image" class="img-thumbnail" style="width: 50px; height: 50px;"></td>
                    <td>${user.name}</td>
                    <td>${user.email}</td>
                    <td>
                        <button id="status-btn-${user.uid}" class="btn ${user.active ? 'btn-success' : 'btn-danger'}" onclick="toggleUserStatus('${user.uid}', ${user.active})">
                            ${user.active ? 'Active' : 'Inactive'}
                        </button>
                    </td>
                </tr>
            `;
            userContainer.append(userRow);
        });
    }

    window.toggleUserStatus = function(userId, currentStatus) {
        $.ajax({
            url: `http://localhost:8080/api/v1/user/${userId}/toggleStatus`,
            method: 'POST',
            success: function() {
                const button = $(`#status-btn-${userId}`);
                const newStatus = !currentStatus;
                button.removeClass(newStatus ? 'btn-danger' : 'btn-success');
                button.addClass(newStatus ? 'btn-success' : 'btn-danger');
                button.text(newStatus ? 'Active' : 'Inactive');
                button.attr('onclick', `toggleUserStatus('${userId}', ${newStatus})`);
            },
            error: function(error) {
                console.error('Error toggling user status:', error);
            }
        });
    }
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