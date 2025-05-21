const API_BASE = "https://stockmarketapi-qr65.onrender.com";

async function loadChart(endpoint, canvasId, label, valueKey, color) {
  try {
    const res = await fetch(`${API_BASE}${endpoint}`);
    const data = await res.json();

    const labels = data.map(item => item._id);
    const values = data.map(item => item[valueKey]);

    new Chart(document.getElementById(canvasId), {
      type: 'bar',
      data: {
        labels: labels,
        datasets: [{
          label: label,
          data: values,
          backgroundColor: color,
          borderRadius: 8
        }]
      },
      options: {
        responsive: true,
        plugins: {
          legend: { display: false },
          tooltip: {
            callbacks: {
              label: function (context) {
                return `${label}: ${context.parsed.y}`;
              }
            }
          }
        },
        scales: {
          y: {
            beginAtZero: true,
            ticks: {
              precision: 0
            }
          },
          x: {
            ticks: {
              autoSkip: false,
              maxRotation: 45,
              minRotation: 0
            }
          }
        }
      }
    });
  } catch (err) {
    console.error(`Error loading ${endpoint}:`, err);
  }
}

loadChart("/analytics/stock-clicks", "stockClicksChart", "Clicks", "clicks", "#0d6efd");
loadChart("/analytics/screen-views", "screenViewsChart", "Views", "views", "#28a745");
loadChart("/analytics/top-duration", "durationChart", "Avg Duration (s)", "avg_time", "#ffc107");
