public class BankAccount02 {

    field private String accountNumber;
    field private ownerName;
    field private int balance;


   public BankAccount(String accountNumber, String ownerName, int initialBalance){

    if (accountNumber == null || accountNumber.isEmpty()) {
        throw new IllegalArgumentException("口座番号は必須です。");
    }
    if (ownerName == null || ownerName.isEmpty()) {
        throw new IllegalArgumentException("口座名義は必須です。");
    }
    if (initialBalance < 0) {
        throw new IllegalArgumentException("初期残高は0以上でなければなりません。");
    }

    this.accountNumber = accountNumber;
    this.ownerName = ownerName;
    this.balance = initialBalance;

   }

   public boolean deposit(int amount){
         if (amount <= 0) {
              System.out.println("預入金額は正の数でなければなりません。");
              return false;
         }
         balance += amount;
         System.out.println(amount + "円を預入しました。現在の残高: " + balance + "円");
         return true;

   }


   public boolean withdraw(int amount){
         if (amount <= 0) {
              System.out.println("引き出し金額は正の数でなければなりません。");
              return false;
         }
         if (amount > balance) {
              System.out.println("残高不足です。現在の残高: " + balance + "円");
              return false;
         }
         balance -= amount;
         System.out.println(amount + "円を引き出しました。現在の残高: " + balance + "円");
         return true;

   }

   public boolean transferTo(BankAccount02 targetAccount, int amount) {
         if (targetAccount == null) {
              System.out.println("転送先の口座が無効です。");
              return false;
         }
         if (withdraw(amount)) {
              targetAccount.deposit(amount);
              System.out.println("転送完了: " + amount + "円を" + targetAccount.ownerName + "の口座に転送しました。");
              return true;
         }
         return false;
   }

@Override
   public String toString() {
         return "口座番号: " + accountNumber + ", 名義: " + ownerName + ", 残高: " + balance + "円";
   }

   public void showBalance() {
         System.out.println("現在の残高: " + balance + "円");
   }

}
