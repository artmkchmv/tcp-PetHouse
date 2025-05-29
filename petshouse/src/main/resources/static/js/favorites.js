document.addEventListener('DOMContentLoaded', () => {
  const userId = document.body.getAttribute('data-owner-id');
  const buttons = document.querySelectorAll('.remove-fav-btn');
  const isUserLoggedIn = document.body.getAttribute('data-logged-in');

    // AUTH UI
  document.querySelectorAll('.auth-buttons a').forEach(link => {
    link.style.display = isUserLoggedIn ? 'none' : 'inline-block';
  });
  const logoutBtn = document.getElementById('logoutButton');
  if (logoutBtn) logoutBtn.style.display = isUserLoggedIn ? 'inline-block' : 'none';

  logoutBtn?.addEventListener('click', () => {
    fetch('/api/auth/logout', { method: 'POST', credentials: 'include' })
      .then(res => {
        if (res.ok) window.location.href = '/main';
        else showErrorMessage('Ошибка при выходе из системы');
      })
      .catch(() => showErrorMessage('Ошибка при выходе из системы'));
  }); 

  buttons.forEach(button => {
    button.addEventListener('click', () => {
      const petId = button.getAttribute('data-id');

      fetch('/api/favorite/deleted', {
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ userId: userId, petId: Number(petId) })
      })
      .then(res => {
        if (res.ok) {
          const petCard = button.closest('.pet-card');
          if (petCard) petCard.remove();
        } else {
          console.error('Ошибка удаления из избранного. Статус:', res.status);
        }
      })
      .catch(e => console.error('Ошибка сети:', e));
    });
  });
});
