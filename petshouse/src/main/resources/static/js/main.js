// --- Message Handling ---
function createMessageBox(message, type = 'success') {
  const box = document.createElement('div');
  box.className = `message-box ${type}`;
  box.textContent = message;
  document.body.appendChild(box);
  setTimeout(() => {
    box.style.opacity = '0';
    setTimeout(() => box.remove(), 300);
  }, type === 'error' ? 6000 : 2000);
}

function showSuccessMessage(msg) {
  createMessageBox(msg, 'success');
}

function showErrorMessage(msg) {
  createMessageBox(msg, 'error');
}

// --- Modal Helpers ---
function openModal(modal) {
  modal.style.display = 'flex';
  document.body.style.overflow = 'hidden';
}

function closeModal(modal) {
  modal.style.display = 'none';
  document.body.style.overflow = '';
}

// --- Favorite Manager ---
const FavoriteManager = (() => {
  const petIdToFavMap = new Map();

  function updateIcon(btn, isActive) {
    const icon = btn.querySelector('i');
    if (!icon) return;
    if (isActive) {
      icon.classList.remove("fa-regular", "text-gray-300", "hover:text-red-500");
      icon.classList.add("fa-solid", "text-red-500", "hover:text-gray-300");
    } else {
      icon.classList.remove("fa-solid", "text-red-500", "hover:text-gray-300");
      icon.classList.add("fa-regular", "text-gray-300", "hover:text-red-500");
    }
  }

  function syncFavoriteButtons(petId, isActive, favId) {
    const buttons = document.querySelectorAll(`.favorite-btn[data-pet-id="${petId}"]`);
    buttons.forEach(btn => {
      if (isActive) {
        btn.classList.add("active");
        updateIcon(btn, true);
        if (favId) btn.dataset.favId = favId;
      } else {
        btn.classList.remove("active");
        updateIcon(btn, false);
        delete btn.dataset.favId;
      }
    });
  }

  async function toggleFavorite(userId, petId, btn) {
    const isFav = btn.classList.contains("active");
    try {
      if (isFav) {
        const fav = petIdToFavMap.get(String(petId));
        if (!fav || !fav.favId && !btn.dataset.favId) throw new Error('Favorite ID not found');
        const favId = fav?.favId || btn.dataset.favId;
        const response = await fetch('/api/favorite/delete', {
          method: 'DELETE',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ favId })
        });
        if (!response.ok) throw new Error('Failed to delete favorite');
        
        petIdToFavMap.delete(String(petId));
        syncFavoriteButtons(petId, false);
      } else {
        const response = await fetch('/api/favorite/add', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ userId, petId: Number(petId) })
        });
        if (!response.ok) throw new Error('Failed to add favorite');
        const data = await response.json();
        petIdToFavMap.set(String(petId), data);
        syncFavoriteButtons(petId, true, data.id || data.favId);
      }
    } catch (error) {
      showErrorMessage(error.message || "Ошибка при изменении избранного");
    }
  }

  function initialize(userId) {
    fetch('/api/favorite/user')
      .then(res => {
        if (!res.ok) throw new Error(`Status: ${res.status}`);
        return res.json();
      })
      .then(favorites => {
        favorites.forEach(fav => {
          petIdToFavMap.set(String(fav.petId), fav);
        });
        document.querySelectorAll('.favorite-btn').forEach(btn => {
          const petIdStr = btn.dataset.petId;
          if (petIdToFavMap.has(petIdStr)) {
            btn.classList.add('active');
            updateIcon(btn, true);
            btn.dataset.favId = petIdToFavMap.get(petIdStr).favId || petIdToFavMap.get(petIdStr).id || '';
          }
          btn.addEventListener('click', e => {
            e.stopPropagation();
            toggleFavorite(userId, btn.dataset.petId, btn);
          });
        });
      })
      .catch(err => {
        console.debug('Failed to load favorites:', err);
        showErrorMessage('Ошибка при загрузке избранного');
      });
  }

  return { initialize, toggleFavorite, updateIcon, petIdToFavMap };
})();

// --- Main ---
document.addEventListener('DOMContentLoaded', () => {
  const ownerId = document.body.dataset.ownerId;
  const isUserLoggedIn = document.body.dataset.loggedIn === "true";

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

  // SEARCH
  const searchBtn = document.querySelector('.search-button');
  searchBtn?.addEventListener('click', () => {
    const query = document.querySelector('.search-input').value.trim().toLowerCase();
    const cards = document.querySelectorAll('.pet-card');
    cards.forEach(card => {
      const name = card.querySelector('.pet-name')?.textContent.toLowerCase();
      card.style.display = (!query || (name && name.includes(query))) ? 'block' : 'none';
    });
  });

  // FILTER
  const filterButtons = document.querySelectorAll('.filter-buttons button:not(.add-pet-btn)');
  const petCards = document.querySelectorAll('.pet-card');

  filterButtons.forEach(button => {
    button.addEventListener('click', () => {
      filterButtons.forEach(btn => btn.classList.remove('selected'));
      button.classList.add('selected');
      const type = button.dataset.type;

      petCards.forEach(card => {
        card.style.display = (type === 'ALL' || card.dataset.petType === type) ? '' : 'none';
      });
    });
  });

  // ADD PET MODAL
  const addPetBtn = document.querySelector('.add-pet-btn');
  const addPetModal = document.getElementById('addPetModal');
  const cancelModalBtn = document.getElementById('cancelModalBtn');
  const addPetForm = document.getElementById('addPetForm');

  if (!isUserLoggedIn && addPetBtn) addPetBtn.style.display = 'none';

  addPetBtn?.addEventListener('click', e => {
    e.preventDefault();
    openModal(addPetModal);
  });

  cancelModalBtn?.addEventListener('click', () => closeModal(addPetModal));

  addPetForm?.addEventListener('submit', async e => {
    e.preventDefault();
    const formData = new FormData(addPetForm);
    const data = Object.fromEntries(formData.entries());
    const payload = {
      petName: data.petName,
      petAge: data.petAge ? parseInt(data.petAge) : null,
      petType: data.petType,
      petDescription: data.petDescription,
      petStatus: "AVAILABLE",
      petOwnerId: ownerId,
      ...(data.petPhotoURL?.trim() && { petPhotoURL: data.petPhotoURL.trim() })
    };

    try {
      const res = await fetch('/api/pets/create', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });
      if (!res.ok) {
        const errText = await res.text();
        throw new Error(errText);
      }
      closeModal(addPetModal);
      showSuccessMessage('Питомец успешно добавлен!');
      addPetForm.reset();
      window.location.reload();
    } catch (err) {
      showErrorMessage('Ошибка: ' + err.message);
    }
  });

  // DETAIL MODAL SETUP
  const detailModal = document.getElementById("petDetailModal");
  const closeDetailBtn = document.getElementById("closePetDetailModal");

  const messageOwnerBtn = detailModal.querySelector("#messageOwnerBtn");
  const messageForm = detailModal.querySelector("#messageForm");
  const messageText = detailModal.querySelector("#messageText");
  const sendMessageBtn = detailModal.querySelector("#sendMessageBtn");
  const cancelMessageBtn = detailModal.querySelector("#cancelMessageBtn");

  function resetMessageForm() {
    messageForm.style.display = "none";
    messageOwnerBtn.style.display = "inline-block";
    messageText.value = "";
  }

  closeDetailBtn.addEventListener("click", () => closeModal(detailModal));
  detailModal.addEventListener("click", e => {
    if (e.target === detailModal) closeModal(detailModal);
  });

  // Обработчики добавляем один раз
  messageOwnerBtn.addEventListener("click", () => {
    messageOwnerBtn.style.display = "none";
    messageForm.style.display = "block";
    messageText.focus();
  });

  cancelMessageBtn.addEventListener("click", () => {
    resetMessageForm();
  });

  sendMessageBtn.addEventListener("click", async () => {
    const text = messageText.value.trim();
    if (text.length < 1) {
      showErrorMessage("Сообщение должно содержать минимум 1 символ");
      return;
    }

    // Здесь ownerId и petData должны быть доступны в нужном контексте
    // Предполагаю, что можно хранить petData глобально или в замыкании (см. ниже)

    try {
      const payload = {
        senderId: Number(ownerId),
        receiverId: Number(currentPetData.ownerId),
        petId: Number(currentPetData.id),
        messageText: text
      };

      const response = await fetch("/api/messages", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || "Ошибка при отправке сообщения");
      }

      showSuccessMessage("Сообщение отправлено успешно");
      resetMessageForm();
    } catch (err) {
      showErrorMessage(err.message || "Ошибка при отправке сообщения");
    }
  });

  // Чтобы sendMessageBtn знал про текущего питомца, сохраним данные глобально
  let currentPetData = null;

  // DETAIL MODAL OPEN
  document.querySelectorAll(".pet-card").forEach(petCard => {
    petCard.addEventListener("click", (event) => {

      if (event.target.closest('.favorite-btn')) {
        return;
      }

      if (!isUserLoggedIn) {
        return;
      }

      // Собираем данные питомца
      const petData = {
        id: petCard.dataset.petId,
        name: petCard.querySelector(".pet-name")?.textContent || '',
        age: petCard.querySelector(".pet-age")?.textContent || '',
        type: petCard.dataset.petType || '',
        description: petCard.querySelector(".pet-description")?.textContent || '',
        photoURL: petCard.querySelector("img")?.src || '',
        ownerName: petCard.querySelector(".owner-name")?.textContent || '',
        ownerId: petCard.dataset.ownerId || '',
      };

      currentPetData = petData; // Сохраняем для отправки сообщения

      detailModal.querySelector("#detailName").textContent = petData.name;
      detailModal.querySelector("#detailAge").textContent = `Возраст: ${petData.age}`;
      detailModal.querySelector("#detailType").textContent = `Тип: ${petData.type}`;
      detailModal.querySelector("#detailDescription").textContent = petData.description;
      detailModal.querySelector("#detailPhoto").src = petData.photoURL;

      const favBtn = detailModal.querySelector(".favorite-btn");
      favBtn.dataset.petId = petData.id;
      favBtn.dataset.ownerId = petData.ownerId;

      if (FavoriteManager.petIdToFavMap.has(petData.id)) {
        favBtn.classList.add("active");
        FavoriteManager.updateIcon(favBtn, true);
        favBtn.dataset.favId = FavoriteManager.petIdToFavMap.get(petData.id).favId || FavoriteManager.petIdToFavMap.get(petData.id).id || '';
      } else {
        favBtn.classList.remove("active");
        FavoriteManager.updateIcon(favBtn, false);
        delete favBtn.dataset.favId;
      }

      favBtn.replaceWith(favBtn.cloneNode(true));
      const newFavBtn = detailModal.querySelector(".favorite-btn");
      newFavBtn.addEventListener('click', () => {
        if (!isUserLoggedIn) {
          showErrorMessage("Пожалуйста, войдите в систему, чтобы добавить в избранное.");
          return;
        }
        FavoriteManager.toggleFavorite(ownerId, petData.id, newFavBtn);
      });

      // Сброс формы сообщений при открытии
      resetMessageForm();

      // Если владелец — не показывать кнопку отправки сообщения
      if (petData.ownerId && ownerId && petData.ownerId === ownerId) {
        messageOwnerBtn.style.display = "none";
      } else {
        messageOwnerBtn.style.display = "inline-block";
      }

      openModal(detailModal);
    });
  });

  if (isUserLoggedIn) FavoriteManager.initialize(ownerId);
}); 
