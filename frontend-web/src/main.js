import './style.css'

document.addEventListener('DOMContentLoaded', () => {
  
  // --- DOM Elements ---
  const loginForm = document.getElementById('login-form')
  const loginView = document.getElementById('login-view')
  const mainLayout = document.getElementById('main-layout')
  const logoutBtn = document.getElementById('logout-btn')
  
  const navItems = document.querySelectorAll('.nav-item')
  const tabContents = document.querySelectorAll('.tab-content')

  // --- Login Logic ---
  loginForm.addEventListener('submit', (e) => {
    e.preventDefault()
    // Simulate login
    loginView.classList.add('hidden')
    loginView.classList.remove('active')
    
    setTimeout(() => {
      mainLayout.classList.remove('hidden')
      mainLayout.classList.add('active')
      initMap() // Initialize map after container is visible
    }, 300)
  })

  // --- Logout Logic ---
  if(logoutBtn) {
    logoutBtn.addEventListener('click', () => {
      mainLayout.classList.remove('active')
      mainLayout.classList.add('hidden')
      
      setTimeout(() => {
        loginView.classList.remove('hidden')
        loginView.classList.add('active')
      }, 300)
    })
  }

  // --- Bottom Navigation Logic ---
  navItems.forEach(item => {
    item.addEventListener('click', () => {
      // Remove active class from all nav items
      navItems.forEach(nav => nav.classList.remove('active'))
      // Add active class to clicked item
      item.classList.add('active')

      // Get target view id
      const targetId = item.getAttribute('data-target')

      // Hide all tab contents
      tabContents.forEach(content => {
        content.classList.remove('active')
        content.classList.add('hidden')
      })

      // Show target tab content
      const targetContent = document.getElementById(targetId)
      if (targetContent) {
        targetContent.classList.remove('hidden')
        // Small delay for CSS transition
        setTimeout(() => {
          targetContent.classList.add('active')
        }, 50)
      }
    })
  })

  // --- Mock Canvas Map ---
  function initMap() {
    const canvas = document.getElementById('live-map-canvas')
    if (!canvas) return
    const ctx = canvas.getContext('2d')
    
    // Resize canvas to match wrapper
    const wrapper = canvas.parentElement
    canvas.width = wrapper.clientWidth
    canvas.height = wrapper.clientHeight

    // Draw some mock paths matching the screenshot (North Campus Loop, South Express)
    ctx.clearRect(0, 0, canvas.width, canvas.height)
    
    // Set styles for routes
    ctx.lineWidth = 4
    ctx.lineCap = 'round'
    ctx.lineJoin = 'round'

    // South Express (Green)
    ctx.beginPath()
    ctx.strokeStyle = '#34d399'
    ctx.moveTo(canvas.width * 0.4, canvas.height * 0.4)
    ctx.lineTo(canvas.width * 0.6, canvas.height * 0.6)
    ctx.lineTo(canvas.width * 0.3, canvas.height * 0.8)
    ctx.stroke()

    // North Campus Loop (Blue)
    ctx.beginPath()
    ctx.strokeStyle = '#3b82f6'
    ctx.moveTo(canvas.width * 0.4, canvas.height * 0.4)
    ctx.lineTo(canvas.width * 0.2, canvas.height * 0.5)
    ctx.lineTo(canvas.width * 0.3, canvas.height * 0.2)
    ctx.lineTo(canvas.width * 0.6, canvas.height * 0.3)
    ctx.stroke()
    
    // Draw bus markers
    drawBusMarker(ctx, canvas.width * 0.6, canvas.height * 0.6, '#34d399') // Green bus
    drawBusMarker(ctx, canvas.width * 0.2, canvas.height * 0.5, '#3b82f6') // Blue bus
  }

  function drawBusMarker(ctx, x, y, color) {
    ctx.beginPath()
    ctx.fillStyle = color
    ctx.arc(x, y, 12, 0, Math.PI * 2)
    ctx.fill()
    ctx.fillStyle = '#151a23'
    ctx.textAlign = 'center'
    ctx.textBaseline = 'middle'
    ctx.font = '10px sans-serif'
    ctx.fillText('B', x, y + 1)
  }

  // Handle window resize for canvas
  window.addEventListener('resize', () => {
    if (mainLayout.classList.contains('active')) {
      initMap()
    }
  })
})
