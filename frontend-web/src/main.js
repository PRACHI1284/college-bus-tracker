import './style.css'

// Configuration
const API_BASE_URL = 'http://localhost:3000/api/v1';

// DOM Elements
const loginView = document.getElementById('login-view');
const mainLayout = document.getElementById('main-layout');
const loginForm = document.getElementById('login-form');
const otpForm = document.getElementById('otp-form');
const busListContainer = document.getElementById('bus-list');
const canvas = document.getElementById('live-map-canvas');
const ctx = canvas.getContext('2d');

// Navigation
const navItems = document.querySelectorAll('.nav-item');
const contentSections = document.querySelectorAll('.content-section');
const logoutBtn = document.getElementById('logout-btn');

// State
let routes = [];
let buses = [];
let fetchInterval = null;

// --- Authentication Flow --- //

loginForm.addEventListener('submit', (e) => {
  e.preventDefault();
  // Simulate checking credentials, then show OTP form
  loginForm.classList.add('hidden');
  otpForm.classList.remove('hidden');
  
  // Auto-focus first OTP input
  document.querySelector('.otp-input').focus();
});

// Auto-advance OTP inputs
document.querySelectorAll('.otp-input').forEach((input, index, inputs) => {
  input.addEventListener('input', () => {
    if (input.value.length === 1 && index < inputs.length - 1) {
      inputs[index + 1].focus();
    }
  });
});

otpForm.addEventListener('submit', (e) => {
  e.preventDefault();
  // Simulate successful 2FA
  loginView.classList.remove('active');
  
  setTimeout(() => {
    mainLayout.classList.add('active');
    initDashboard();
  }, 400); // Wait for fade out
});

// --- Navigation Logic --- //

navItems.forEach(item => {
  item.addEventListener('click', () => {
    // Remove active class from all nav items
    navItems.forEach(nav => nav.classList.remove('active'));
    // Add active class to clicked item
    item.classList.add('active');
    
    // Hide all content sections
    contentSections.forEach(section => {
      section.classList.remove('active');
      section.classList.add('hidden');
    });
    
    // Show target section
    const targetId = item.getAttribute('data-target');
    const targetSection = document.getElementById(targetId);
    targetSection.classList.remove('hidden');
    targetSection.classList.add('active');
    
    // If returning to dashboard, redraw map
    if (targetId === 'dashboard-view' && routes.length > 0) {
      setTimeout(resizeCanvas, 50);
    }
  });
});

logoutBtn.addEventListener('click', () => {
  mainLayout.classList.remove('active');
  setTimeout(() => {
    loginView.classList.add('active');
    loginForm.classList.remove('hidden');
    otpForm.classList.add('hidden');
    // Reset OTP inputs
    document.querySelectorAll('.otp-input').forEach(i => i.value = '');
  }, 400);
});

// --- Dashboard Logic --- //

async function initDashboard() {
  // Resize canvas for sharp rendering
  resizeCanvas();
  window.addEventListener('resize', resizeCanvas);

  await fetchRoutes();
  await fetchBuses();
  
  renderBusList();
  drawMap();

  // Poll for updates every 5 seconds
  if (!fetchInterval) {
    fetchInterval = setInterval(async () => {
      await fetchBuses();
      renderBusList();
      drawMap();
    }, 5000);
  }
}

async function fetchRoutes() {
  try {
    const res = await fetch(`${API_BASE_URL}/routes`);
    routes = await res.json();
  } catch (error) {
    console.error('Failed to fetch routes:', error);
  }
}

async function fetchBuses() {
  try {
    const res = await fetch(`${API_BASE_URL}/buses/live`);
    buses = await res.json();
  } catch (error) {
    console.error('Failed to fetch live buses:', error);
  }
}

// --- UI Rendering --- //

function renderBusList() {
  if (buses.length === 0) {
    busListContainer.innerHTML = '<div class="loading">No active buses right now.</div>';
    return;
  }

  busListContainer.innerHTML = buses.map(bus => {
    const isDelayed = bus.delayMinutes > 0;
    const badgeClass = isDelayed ? 'warning' : 'success';
    const statusText = isDelayed ? `Delayed ${bus.delayMinutes}m` : 'On Time';
    
    return `
      <div class="glass-card bus-card">
        <div class="bus-card-header">
          <span class="bus-id">${bus.id}</span>
          <span class="badge ${badgeClass}">${statusText}</span>
        </div>
        <div class="bus-details">
          <p>Route: <strong>${bus.routeName}</strong></p>
          <p>Next Stop: <strong>${bus.nextStop}</strong></p>
          <p>ETA: <strong>${bus.etaMinutes} mins</strong></p>
          <p>Passengers: <strong>${bus.currentPassengers}/${bus.capacity}</strong></p>
        </div>
      </div>
    `;
  }).join('');
}

// --- Canvas Map Rendering --- //

function resizeCanvas() {
  const container = canvas.parentElement;
  // Make it crisp on high DPI screens
  const dpr = window.devicePixelRatio || 1;
  const rect = container.getBoundingClientRect();
  
  // Set actual size in memory (scaled to account for extra pixel density)
  canvas.width = rect.width * dpr;
  canvas.height = rect.height * dpr;
  
  // Normalize coordinate system to use css pixels
  ctx.scale(dpr, dpr);
  
  // Set visual size
  canvas.style.width = `${rect.width}px`;
  canvas.style.height = `${rect.height}px`;
  
  // Re-draw after resize
  if (routes.length > 0) drawMap();
}

function drawMap() {
  // Clear canvas
  const rect = canvas.parentElement.getBoundingClientRect();
  const width = rect.width;
  const height = rect.height;
  
  ctx.clearRect(0, 0, width, height);

  // 1. Draw Routes
  routes.forEach((route, index) => {
    ctx.beginPath();
    ctx.strokeStyle = index === 0 ? 'rgba(59, 130, 246, 0.4)' : 'rgba(16, 185, 129, 0.4)';
    ctx.lineWidth = 6;
    ctx.lineCap = 'round';
    ctx.lineJoin = 'round';

    route.pathPoints.forEach((point, i) => {
      const x = point.first * width;
      const y = point.second * height;
      if (i === 0) ctx.moveTo(x, y);
      else ctx.lineTo(x, y);
    });
    ctx.stroke();

    // Draw route stops
    route.pathPoints.forEach((point, i) => {
      const x = point.first * width;
      const y = point.second * height;
      
      ctx.beginPath();
      ctx.arc(x, y, 6, 0, Math.PI * 2);
      ctx.fillStyle = '#1e293b';
      ctx.fill();
      ctx.lineWidth = 2;
      ctx.strokeStyle = index === 0 ? '#3b82f6' : '#10b981';
      ctx.stroke();
      
      // Draw stop name
      if (route.stops[i]) {
        ctx.fillStyle = '#94a3b8';
        ctx.font = '12px Outfit';
        ctx.fillText(route.stops[i], x + 12, y + 4);
      }
    });
  });

  // 2. Draw Live Buses
  buses.forEach(bus => {
    const x = bus.latitudePercent * width;
    const y = bus.longitudePercent * height;
    
    // Draw bus pulse shadow
    const isDelayed = bus.delayMinutes > 0;
    ctx.beginPath();
    ctx.arc(x, y, 16, 0, Math.PI * 2);
    ctx.fillStyle = isDelayed ? 'rgba(245, 158, 11, 0.2)' : 'rgba(16, 185, 129, 0.2)';
    ctx.fill();

    // Draw bus icon (circle)
    ctx.beginPath();
    ctx.arc(x, y, 8, 0, Math.PI * 2);
    ctx.fillStyle = isDelayed ? '#f59e0b' : '#10b981';
    ctx.fill();
    ctx.lineWidth = 2;
    ctx.strokeStyle = '#ffffff';
    ctx.stroke();

    // Draw Bus ID text
    ctx.fillStyle = '#ffffff';
    ctx.font = 'bold 12px Outfit';
    ctx.fillText(bus.id, x - 20, y - 20);
  });
}
