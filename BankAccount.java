public class BankAccount {
    // フィールド
    private String accountHolder;
    private long balance;

    // コンストラクタ
    public BankAccount(String holder, long initialDeposit) {
        this.accountHolder = holder;
        this.balance = initialDeposit;
        System.out.println(holder + "さんの口座を作成しました。初期残高: " + initialDeposit + "円");
    }

    // メソッド
    public void deposit(long amount) {
        this.balance += amount; // 残高に加算
        System.out.println(amount + "円を入金しました。");
    }

    public void withdraw(long amount) {
        this.balance -= amount; // 残高から減算
        System.out.println(amount + "円を引き出しました。");
    }

    public void showBalance() {
        System.out.printf("%sさんの残高: %,d円\n", this.accountHolder, this.balance);
    }
}
