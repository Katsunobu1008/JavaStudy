// npm i mathjs
import { eigs, transpose } from 'mathjs';

/** 仮想中央銀行法：自己ループを0にして他者へ等分再配分 */
function toVCB(E) {
  const N = E.length;
  const M = E.map((row) => row.slice());
  for (let i = 0; i < N; i++) {
    const diag = M[i][i];
    M[i][i] = 0;
    const add = diag / (N - 1);
    for (let j = 0; j < N; j++) if (j !== i) M[i][j] += add;
  }
  return M;
}

/** 行列が行確率（行和=1, 非負）か軽く検査 */
function assertRowStochastic(M, eps = 1e-12) {
  for (let i = 0; i < M.length; i++) {
    const row = M[i];
    const sum = row.reduce((a, b) => a + b, 0);
    if (Math.abs(sum - 1) > 1e-9) throw new Error(`row ${i} sum ≠ 1: ${sum}`);
    if (row.some((x) => x < -eps))
      throw new Error(`row ${i} has negative entry`);
  }
}

/** c * M を計算（c は行ベクトル） */
function rowTimesMatrix(c, M) {
  const N = M.length;
  const out = Array(N).fill(0);
  for (let j = 0; j < N; j++) {
    let s = 0;
    for (let i = 0; i < N; i++) s += c[i] * M[i][j];
    out[j] = s;
  }
  return out;
}

/** L1 正規化で sum=target（デフォルトN） */
function normalizeL1(v, target = v.length) {
  let s = v.reduce((a, b) => a + b, 0);
  if (s === 0) throw new Error('zero-sum eigenvector');
  if (s < 0) {
    v = v.map((x) => -x);
    s = -s;
  } // 符号を正へ
  return v.map((x) => (target * x) / s);
}

/** eigs で固有値1に最も近い固有ベクトルを選ぶ（実部を使用） */
function leftStationaryByEigen(M) {
  const { eigenvectors } = eigs(transpose(M));
  // いちばん λ≈1 に近いものを選ぶ
  let best = eigenvectors[0],
    bestDist = Infinity;
  for (const ev of eigenvectors) {
    const lam = typeof ev.value === 'object' ? ev.value.re : ev.value;
    const dist = Math.abs(lam - 1);
    if (dist < bestDist) {
      bestDist = dist;
      best = ev;
    }
  }
  // 実数化（虚部は誤差レベルを想定）
  let v = best.vector.map((x) => (typeof x === 'object' ? x.re : x));
  // sum=3 に L1 正規化（PICSY）
  return normalizeL1(v, 3);
}

/** パワー法（左側：c_{k+1}=c_k M） */
function leftStationaryByPower(M, tol = 1e-12, maxIter = 10000) {
  const N = M.length;
  let c = Array(N).fill(1); // 初期値
  c = normalizeL1(c, 3); // sum=3
  for (let k = 0; k < maxIter; k++) {
    const next = rowTimesMatrix(c, M);
    const err = Math.max(...next.map((x, i) => Math.abs(x - c[i])));
    c = normalizeL1(next, 3);
    if (err < tol) return c;
  }
  return c;
}

/*** ---- 実行例 ---- ***/
const E = [
  [0.3, 0.5, 0.2],
  [0.4, 0.2, 0.4],
  [0.1, 0.6, 0.3],
];

const M = toVCB(E); // = [[0,0.65,0.35],[0.5,0,0.5],[0.25,0.75,0]]
assertRowStochastic(M);

const c_eig = leftStationaryByEigen(M);
const c_pow = leftStationaryByPower(M);

// 検算：c M ≈ c
const check = rowTimesMatrix(c_eig, M).map((x, i) => x - c_eig[i]);
const maxAbsErr = Math.max(...check.map(Math.abs));

console.log('M (VCB):', M);
console.log('c (eigs):', c_eig);
console.log('c (power):', c_pow);
console.log('max |cM - c|:', maxAbsErr);
