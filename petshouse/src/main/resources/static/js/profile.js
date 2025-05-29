document.addEventListener('DOMContentLoaded', () => {
  function showSuccessMessage(message) {
    const messageBox = document.createElement('div');
    messageBox.textContent = message;
    messageBox.style.position = 'fixed';
    messageBox.style.top = '20px';
    messageBox.style.left = '50%';
    messageBox.style.transform = 'translateX(-50%)';
    messageBox.style.padding = '10px 20px';
    messageBox.style.backgroundColor = 'green';
    messageBox.style.color = 'white';
    messageBox.style.borderRadius = '5px';
    messageBox.style.fontSize = '16px';
    messageBox.style.zIndex = '1000';
    document.body.appendChild(messageBox);
    setTimeout(() => messageBox.remove(), 4000);
  }

  function showErrorMessage(message) {
    const messageBox = document.createElement('div');
    messageBox.textContent = message;
    messageBox.style.position = 'fixed';
    messageBox.style.top = '20px';
    messageBox.style.left = '50%';
    messageBox.style.transform = 'translateX(-50%)';
    messageBox.style.padding = '10px 20px';
    messageBox.style.backgroundColor = 'crimson';
    messageBox.style.color = 'white';
    messageBox.style.borderRadius = '5px';
    messageBox.style.fontSize = '16px';
    messageBox.style.zIndex = '1000';
    document.body.appendChild(messageBox);
    setTimeout(() => messageBox.remove(), 6000);
  }

  async function apiRequest(url, options = {}) {
    try {
      const res = await fetch(url, { credentials: 'include', ...options });
      if (res.status === 204) {
        return null;
      }
      const data = await res.json();
      if (!res.ok) {
        throw new Error(data.message || 'Error');
      }
      return data;
    } catch (err) {
      showErrorMessage(err.message || 'Network error! Try again later!');
      throw err;
    }
  }

  const userNameElem = document.getElementById('sidebarLogin');
  const isUserLoggedIn = userNameElem && userNameElem.textContent.trim() !== 'Guest';

  const logoutBtn = document.getElementById('logoutButton');
  const authButtons = document.querySelectorAll('.auth-buttons a');

  if (isUserLoggedIn) {
    authButtons.forEach(link => (link.style.display = 'none'));
    if (logoutBtn) logoutBtn.style.display = 'inline-block';
  } else {
    authButtons.forEach(link => (link.style.display = 'inline-block'));
    if (logoutBtn) logoutBtn.style.display = 'none';
  }

  logoutBtn?.addEventListener('click', async () => {
    try {
      await apiRequest('/api/auth/logout', { method: 'POST' });
      location.href = '/main';
    } catch {}
  });

  let currentUserId = null;

  apiRequest('/api/users/me')
    .then(user => {
      currentUserId = user.id;
      document.getElementById('currentEmail').textContent = user.email || '';
      document.getElementById('currentLocation').textContent = user.location || '';

      const emailForm = document.getElementById('emailChangeForm');
      document.getElementById('changeEmailBtn')?.addEventListener('click', () => {
        emailForm.style.display = 'block';
        document.getElementById('changeEmailBtn').style.display = 'none';
      });

      document.getElementById('confirmEmailBtn')?.addEventListener('click', async () => {
        const newEmail = document.getElementById('newEmail').value;
        if (!newEmail) return alert('Enter new Email');

        try {
          await apiRequest('/api/users/update/email', {
            method: 'PATCH',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ id: currentUserId, newEmail })
          });
          showSuccessMessage('Email updated succesfully!');
          document.getElementById('currentEmail').textContent = newEmail;
          document.getElementById('userEmail').textContent = newEmail;
          emailForm.style.display = 'none';
          document.getElementById('changeEmailBtn').style.display = 'inline-block';
          
        } catch {}
      });

      document.getElementById('cancelEmailBtn')?.addEventListener('click', () => {
        emailForm.style.display = 'none';
        document.getElementById('changeEmailBtn').style.display = 'inline-block';
      });

      const locationForm = document.getElementById('locationChangeForm');
      document.getElementById('changeLocationBtn')?.addEventListener('click', () => {
        locationForm.style.display = 'block';
        document.getElementById('changeLocationBtn').style.display = 'none';
      });

      document.getElementById('confirmLocationBtn')?.addEventListener('click', async () => {
        const newLocation = document.getElementById('newLocation').value;
        try {
          await apiRequest('/api/users/update/location', {
            method: 'PATCH',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ id: currentUserId, newLocation })
          });
          showSuccessMessage('Location updated successfully!');
          document.getElementById('currentLocation').textContent = newLocation;
          locationForm.style.display = 'none';
          document.getElementById('changeLocationBtn').style.display = 'inline-block';
        } catch {}
      });

      document.getElementById('cancelLocationBtn')?.addEventListener('click', () => {
        locationForm.style.display = 'none';
        document.getElementById('changeLocationBtn').style.display = 'inline-block';
      });

      const passwordForm = document.getElementById('passwordChangeForm');
      document.getElementById('changepasswordBtn')?.addEventListener('click', () => {
        passwordForm.style.display = 'block';
        document.getElementById('changepasswordBtn').style.display = 'none';
      });

      document.getElementById('confirmpasswordBtn')?.addEventListener('click', async () => {
        const newPassword = document.getElementById('newpassword').value;
        if (!newPassword) return alert('Enter new password');

        try {
          await apiRequest('/api/users/update/password', {
            method: 'PATCH',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ id: currentUserId, newPassword })
          });
          showSuccessMessage('Password updated successfully');
          passwordForm.style.display = 'none';
          document.getElementById('changepasswordBtn').style.display = 'inline-block';
        } catch {}
      });

      document.getElementById('cancelpasswordBtn')?.addEventListener('click', () => {
        passwordForm.style.display = 'none';
        document.getElementById('changepasswordBtn').style.display = 'inline-block';
      });

      const deleteBtn = document.getElementById('deleteAccountBtn');
      const confirmDeleteBtn = document.getElementById('confirmDeleteBtn');

      deleteBtn?.addEventListener('click', () => {
        deleteBtn.style.display = 'none';
        confirmDeleteBtn.style.display = 'inline-block';
        setTimeout(() => {
          confirmDeleteBtn.style.display = 'none';
          deleteBtn.style.display = 'inline-block';
        }, 10000);
      });

      confirmDeleteBtn?.addEventListener('click', async () => {
        try {
          await apiRequest('/api/users/delete', {
            method: 'DELETE',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ id: currentUserId })
          });
          showSuccessMessage('Account deleted successfully!');
          await fetch('/api/auth/logout', { method: 'POST', credentials: 'include' });
          location.href = '/main';
        } catch {}
      });
    })
    .catch(() => showErrorMessage('Ошибка при получении данных пользователя'));
});
