
public class JavaStudy {
    public static void main(String[] args) {

        // System.out.println("--- 基本的な変数と表示 ---");
        // System.out.println("Welcome to Java Programming!");

        // char ch = 'a';
        // System.out.println(ch);

        // String str = "こんにちは";
        // System.out.println(str);

        // int number = 20;
        // System.out.println(number);

        // System.out.println("\n--- 簡単な計算 ---");
        // int times = 100 + number;
        // System.out.println("120 * 20 = " + (times * number));

        // int a = 4;
        // int b = 8;
        // int numberOnePlayer = a * b;
        // System.out.println("4 * 8 = " + numberOnePlayer);

        // double pi = 3.14;
        // System.out.println("半径4の円の面積(概算): " + (pi * a * a));

        // System.out.println("\n--- エスケープシーケンス ---");
        // System.out.println("こんにちは\nJavaの世界へようこそ！");

        // System.out.println("\n--- インクリメントとデクリメント ---");
        // int n = 10;
        // n++;
        // System.out.println("インクリメント後のn: " + n);
        // n--;
        // System.out.println("デクリメント後のn: " + n);

        // System.out.println("\n--- Mathクラスの利用 ---");
        // System.out.println("円周率の平方根: " + Math.sqrt(pi));

        // double value1 = Math.random();
        // System.out.println("0.0以上1.0未満の乱数: " + value1);

        // double value2 = Math.pow(2.5, 3);
        // System.out.println("2.5の3乗は: " + value2);

        // System.out.println("\n--- 書式文字列 ---");
        // String name = "山田";
        // int age = 30;
        // double height = 170.5;
        // System.out.printf("名前:%s, 年齢:%d, 身長:%.2fcm%n", name, age, height);

        // System.out.println("\n--- 配列とfor文 ---");
        // int[] numbers = {1, 2, 3, 4, 5};
        // int total = 0;
        // for (int m : numbers) {
        //     total += m;
        // }
        // System.out.println("配列の合計: " + total);

        // System.out.println("\n--- Listと拡張for文 ---");
        // var fruits = List.of("バナナ", "リンゴ", "オレンジ");
        // for (String fruit : fruits) {
        //     System.out.println("フルーツ: " + fruit);
        // }

        // List<Integer> numberList = List.of(1, 2, 3, 4, 5);
        // for (int t : numberList) {
        //     System.out.println("リストの要素: " + t);
        // }

        // System.out.println("\n--- ArrayList ---");
        // var numbers2 = new ArrayList<Integer>();
        // numbers2.add(10);
        // numbers2.add(20);
        // numbers2.add(30);
        // numbers2.remove(1); // インデックス1の要素(20)を削除
        // for (int n1 : numbers2) {
        //     System.out.println("ArrayListの要素: " + n1);
        // }

        // System.out.println("\n--- if文 ---");
        // int score = 85;
        // System.out.println("現在の点数: " + score);
        // if (score >= 90) {
        //     System.out.println("素晴らしい");
        // } else if (score >= 70) {
        //     System.out.println("良い");
        // } else {
        //     System.out.println("もう少し");
        // }

        // --- レコードの利用（Measurement.javaが同じフォルダにあればコメントを外して使えます） ---
        // System.out.println("\n--- レコード ---");
        // var data1 = new Measurement("田中", 70.5, 175.0);
        // System.out.println(data1);


    // int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

    // System.out.println("\n--- 偶数だけを表示する ---");
    // for(int number : numbers){
    //     if(number %2 == 0){
    //         System.out.println("偶数: " + number);
    //     }
    // }
    // System.out.println();
    // System.out.println("\n ---奇数だけを表示する----");
    // for(int number : numbers ){
    //     if(number %2 ==1){
    //         System.out.println("奇数: " + number);
    //     }
    // }
    // System.out.println();
    // System.out.println("\n--- 偶数と奇数のカウント ---");
    // int evenCount = 0;
    // int oddCount = 0;
    // for(int number : numbers){
    //     if(number % 2 == 0){
    //         evenCount ++;
    //     }else{
    //         oddCount ++;
    //     }
    // }
    // System.out.println(evenCount + "個の偶数と" + oddCount + "個の奇数があります。");

    // // この配列を使ってください
    // int[] scores = {65, 92, 78, 100, 58, 85, 71};

    // int maxScore = scores[0];
    // int minScore = scores[0];

    // for(int score : scores){
    //     if(score > maxScore){
    //         maxScore = score;
    //     }
    // }
    // System.out.println("最高点: " + maxScore);
    // for(int score : scores){
    //     if(score < minScore){
    //         minScore = score;
    //     }
    // }
    // System.out.println("最低点: " + minScore);


    // // この配列を使ってください
    // int[] scores = {88, 76, -1, 95, 43, 100, 76};
    // int total = 0;
    // int perfectScore = 100;
    // for(int score : scores){
    //     if(score >= 0){
    //         total += score;
    //     }
    // }
    // System.out.println("有効な点数の合計: " + total);
    // for(int score : scores){
    //     if(score == 100 ){
    //         System.out.prrintln("満点がいたぞお");
    //     }
    // }

    // この配列を使ってください


    //   int[] numbers = {3, 5, 2, 8, 5, 4, 2};
    //     int firstDuplicate = -1; // 重複が見つからなかった場合の初期値

    //     // 外側のループ (基準となる数値を選ぶ)
    //     outerLoop: // 外側のループに名前を付ける
    //     for (int i = 0; i < numbers.length; i++) {

    //         // 内側のループ (比較対象の数値を選ぶ)
    //         for (int j = i + 1; j < numbers.length; j++) {

    //             // 基準の数値と比較対象の数値が同じかチェック
    //             if (numbers[i] == numbers[j]) {
    //                 firstDuplicate = numbers[i]; // 重複した数値を記録
    //                 break outerLoop; // 名前を付けた外側のループを抜ける
    //             }
    //         }
    //     }

    //     // 最終的な結果を表示
    //     if (firstDuplicate != -1) {
    //         System.out.println("最初の重複する数値は " + firstDuplicate + " です。");
    //     } else {
    //         System.out.println("重複する数値はありません。");
    //     }
    // }
    Dog pochi = new Dog("ポチ",3);

    pochi.bark();
      // 1. 鈴木さんの口座インスタンスを作成
        BankAccount suzukiAccount = new BankAccount("鈴木", 10000);

        System.out.println("--- 取引開始 ---");
        // 2. 3000円を預入
        suzukiAccount.deposit(3000);

        // 3. 1500円を引き出し
        suzukiAccount.withdraw(1500);
        System.out.println("--- 取引終了 ---");

        // 4. 最終残高を表示
        suzukiAccount.showBalance()


    }
} // mainメソッドの終わり

 // JavaStudyクラスの終わり
