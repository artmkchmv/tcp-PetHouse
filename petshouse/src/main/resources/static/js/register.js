document.addEventListener('DOMContentLoaded', function() {

  document.getElementById('registerForm').addEventListener('submit', async function(event) {

    event.preventDefault();

    const login = document.getElementById('login').value;
    const email = document.getElementById('email').value;
    const location = document.getElementById('location').value;
    const password = document.getElementById('password').value;

    const userData = { login, email, location, password };

    try {
      const response = await fetch('/api/auth/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(userData)
      });

      if (response.ok) {
        showSuccessMessage('Registration successful! Redirecting to login...');
        setTimeout(() => window.location.href = '/login', 2000);
      } else {
        const errorJson = await response.json();
        showErrorMessage(errorJson.message || 'Registration error!');
      }
    } catch (error) {
      showErrorMessage('Network error or server unavailable');
      console.error('Error:', error);
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
  setTimeout(() => messageBox.remove(), 2000);
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
  setTimeout(() => messageBox.remove(), 4000);
}
