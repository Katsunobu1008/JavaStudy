// ===== 1) 初期化（年号／テーマ） =====
const yearEl = document.getElementById('year');
if (yearEl) yearEl.textContent = new Date().getFullYear();

const THEME_KEY = 'study-theme';
const root = document.documentElement;

function applyTheme(theme) {
  root.setAttribute('data-theme', theme);
  const pressed = theme === 'dark';
  const btn = document.getElementById('toggleTheme');
  if (btn) btn.setAttribute('aria-pressed', String(pressed));
}

// ローカルに保存されたテーマを適用
applyTheme(localStorage.getItem(THEME_KEY) || 'light');

// テーマ切替（状態は data-theme、永続化は localStorage）
document.getElementById('toggleTheme')?.addEventListener('click', () => {
  const next = root.getAttribute('data-theme') === 'dark' ? 'light' : 'dark';
  applyTheme(next);
  localStorage.setItem(THEME_KEY, next);
});

// ===== 2) Get Started ボタン（最小のイベント） =====
document.getElementById('startButton')?.addEventListener('click', () => {
  alert("Let's start studying!");
});

// ===== 3) いいね（イベント委譲：親でまとめて処理） =====
document.addEventListener('click', (ev) => {
  const btn = ev.target.closest('.js-like');
  if (!btn) return;
  const pressed = btn.getAttribute('aria-pressed') === 'true';
  btn.setAttribute('aria-pressed', String(!pressed));
});

// ===== 4) スクロールスパイ（IntersectionObserver） =====
// 現在見えている section に対応する nav リンクへ aria-current="page" を付与
const sections = [...document.querySelectorAll('section[data-spy-section]')];
const navLinks = new Map(
  [...document.querySelectorAll('.primary-nav a')].map((a) => [
    a.getAttribute('href'),
    a,
  ])
);

const io = new IntersectionObserver(
  (entries) => {
    // 一番見えている（交差率の高い）エントリを現在地とする
    const visible = entries
      .filter((e) => e.isIntersecting)
      .sort((a, b) => b.intersectionRatio - a.intersectionRatio)[0];

    if (!visible) return;
    const id = `#${visible.target.id}`;

    // 既存の aria-current をクリアしてから付与
    for (const [, link] of navLinks) link.removeAttribute('aria-current');
    navLinks.get(id)?.setAttribute('aria-current', 'page');
  },
  { rootMargin: '-40% 0px -55% 0px', threshold: [0, 0.25, 0.5, 0.75, 1] }
);

// 監視を開始
sections.forEach((section) => io.observe(section));
