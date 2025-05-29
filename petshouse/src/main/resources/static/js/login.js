document.addEventListener('DOMContentLoaded', function () {
  const loginForm = document.getElementById('loginForm');
  if (!loginForm) return;

  loginForm.addEventListener('submit', async function(event) {
    event.preventDefault();

    const login = document.getElementById('login').value;
    const password = document.getElementById('password').value;

    const authData = { login, password };

    try {
      const response = await fetch('/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(authData),
        credentials: 'include'
      });

      if (response.ok) {
        showSuccessMessage('Login successful! Redirecting to main page...');
        setTimeout(() => window.location.href = '/main', 2000);
      } else {
        const errorJson = await response.json();
        showErrorMessage(errorJson.message || 'Login failed');
      }
    } catch (error) {
      showErrorMessage('Network Error! Try again later.');
      console.error(error);
    }
  });
});

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
