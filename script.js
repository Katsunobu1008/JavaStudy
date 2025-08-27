document.addEventListener('DOMContentLoaded', () => {
  const startButton = document.getElementById('startButton');
  if (startButton) {
    startButton.addEventListener('click', () => {
      alert("Let's start studying!");
    });
  }
});
