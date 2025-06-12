const API_BASE = "https://stockmarketapi-qr65.onrender.com";

// Enhanced chart configuration with better styling
const chartDefaults = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: { 
      display: false 
    },
    tooltip: {
      backgroundColor: 'rgba(0, 0, 0, 0.8)',
      titleColor: '#fff',
      bodyColor: '#fff',
      borderColor: 'rgba(255, 255, 255, 0.1)',
      borderWidth: 1,
      cornerRadius: 8,
      displayColors: false,
      callbacks: {
        label: function (context) {
          const label = context.dataset.label;
          const value = context.parsed.y;
          return `${label}: ${typeof value === 'number' ? value.toLocaleString() : value}`;
        }
      }
    }
  },
  scales: {
    y: {
      beginAtZero: true,
      ticks: {
        precision: 0,
        color: '#666',
        font: {
          size: 12
        }
      },
      grid: {
        color: 'rgba(0, 0, 0, 0.05)',
        drawBorder: false
      }
    },
    x: {
      ticks: {
        autoSkip: false,
        maxRotation: 45,
        minRotation: 0,
        color: '#666',
        font: {
          size: 12
        }
      },
      grid: {
        display: false
      }
    }
  },
  elements: {
    bar: {
      borderRadius: 6,
      borderSkipped: false,
    }
  },
  animation: {
    duration: 1000,
    easing: 'easeInOutQuart'
  }
};

async function loadChart(endpoint, canvasId, label, valueKey, colors) {
  const loadingId = canvasId.replace('Chart', 'Loading');
  const errorId = canvasId.replace('Chart', 'Error');
  
  try {
    // Show loading state
    document.getElementById(loadingId).style.display = 'flex';
    document.getElementById(canvasId).style.display = 'none';
    document.getElementById(errorId).style.display = 'none';

    const res = await fetch(`${API_BASE}${endpoint}`, {
      method: 'GET',
      headers: {
        'Accept': 'application/json',
      }
    });

    if (!res.ok) {
      throw new Error(`HTTP error! status: ${res.status}`);
    }

    const data = await res.json();

    if (!Array.isArray(data) || data.length === 0) {
      throw new Error('No data available');
    }

    const labels = data.map(item => item._id || 'Unknown');
    const values = data.map(item => item[valueKey] || 0);

    // Hide loading, show chart
    document.getElementById(loadingId).style.display = 'none';
    document.getElementById(canvasId).style.display = 'block';

    // Create gradient for bars
    const canvas = document.getElementById(canvasId);
    const ctx = canvas.getContext('2d');
    const gradient = ctx.createLinearGradient(0, 0, 0, 300);
    gradient.addColorStop(0, colors.start);
    gradient.addColorStop(1, colors.end);

    new Chart(ctx, {
      type: 'bar',
      data: {
        labels: labels,
        datasets: [{
          label: label,
          data: values,
          backgroundColor: gradient,
          borderColor: colors.border,
          borderWidth: 1,
          hoverBackgroundColor: colors.hover,
          hoverBorderColor: colors.border,
          hoverBorderWidth: 2
        }]
      },
      options: chartDefaults
    });

  } catch (err) {
    console.error(`Error loading ${endpoint}:`, err);
    
    // Show error state
    document.getElementById(loadingId).style.display = 'none';
    document.getElementById(canvasId).style.display = 'none';
    document.getElementById(errorId).style.display = 'flex';
  }
}

// Color schemes for different charts
const colorSchemes = {
  stock: {
    start: '#007bff',
    end: '#0056b3',
    border: '#004085',
    hover: '#0056b3'
  },
  screen: {
    start: '#28a745',
    end: '#1e7e34',
    border: '#155724',
    hover: '#1e7e34'
  },
  duration: {
    start: '#ffc107',
    end: '#e0a800',
    border: '#d39e00',
    hover: '#e0a800'
  }
};

// Load all charts with improved error handling and styling
Promise.all([
  loadChart("/analytics/stock-clicks", "stockClicksChart", "Clicks", "clicks", colorSchemes.stock),
  loadChart("/analytics/stock-comparisons", "comparisonChart", "Comparisons", "comparisons", {
    start: '#6f42c1',
    end: '#5a32a3',
    border: '#4b2788',
    hover: '#5a32a3'
  }),
  loadChart("/analytics/top-duration", "durationChart", "Avg Duration (s)", "avg_time", colorSchemes.duration)
])

// Add keyboard navigation for accessibility
document.addEventListener('keydown', function(e) {
  if (e.key === 'Tab') {
    const cards = document.querySelectorAll('.chart-card');
    cards.forEach(card => {
      card.setAttribute('tabindex', '0');
    });
  }
});

// Add resize handler for better responsiveness
let resizeTimeout;
window.addEventListener('resize', function() {
  clearTimeout(resizeTimeout);
  resizeTimeout = setTimeout(function() {
    Chart.helpers.each(Chart.instances, function(instance) {
      instance.resize();
    });
  }, 100);
});