import java.util.ArrayList;
import java.util.List;

public class JavaStudy {
    public static void main(String[] args) {
        // --- 基本的な変数の宣言と表示 ---
        System.out.println("Welcome to Java Programming!");

        char ch = 'a';
        System.out.println(ch);

        String str = "こんにちは";
        System.out.println(str);

        int number = 20;
        System.out.println(number);

        // --- 簡単な計算 ---
        int times = 100 + number;
        System.out.println(times * number);

        int a = 4;
        int b = 8;
        int numberOnePlayer = a * b;
        System.out.println(numberOnePlayer);

        double pi = 3.14;
        System.out.println(pi * a * a);

        // --- エスケープシーケンス ---
        System.out.println("こんにちは\nJavaの世界へようこそ！");

        // --- インクリメントとデクリメント ---
        int n = 10;
        n++;
        System.out.println("インクリメント後のn: " + n);
        n--;
        System.out.println("デクリメント後のn: " + n);

        // --- Mathクラスの利用 ---
        System.out.println("円周率の平方根: " + Math.sqrt(pi));

        double value1 = Math.random();
        System.out.println("0.0以上1.0未満の乱数: " + value1);

        double value2 = Math.pow(2.5, 3);
        System.out.println("2.5の3乗は: " + value2);

        // --- 書式文字列 ---
        String name = "山田";
        int age = 30;
        double height = 170.5;
        System.out.printf("名前:%s, 年齢:%d, 身長:%.2fcm%n", name, age, height);

        // --- 配列と拡張for文 ---
        int[] numbers = {1, 2, 3, 4, 5};
        int total = 0;
        for (int m : numbers) {
            total += m; // 配列の各要素mを合計に加える
            System.out.println("配列の要素: " + m);
        }
        System.out.println("合計: " + total);

        // --- Listと拡張for文 ---
        var fruits = List.of("バナナ", "リンゴ", "オレンジ");
        for (String fruit : fruits) {
            System.out.println("フルーツ: " + fruit);
        }

        List<Integer> numberList = List.of(1, 2, 3, 4, 5);
        for (int t : numberList) {
            System.out.println("リストの要素: " + t);
        }

        // --- ArrayList ---
        var numbers2 = new ArrayList<Integer>();
        numbers2.add(10);
        numbers2.add(20);
        numbers2.add(30);
        numbers2.remove(1); // インデックス1の要素(20)を削除
        for (int n1 : numbers2) {
            System.out.println("ArrayListの要素: " + n1);
        }

        // --- レコードの利用 ---
        var data1 = new Measurement("田中", 70.5, 175.0);
        var data2 = new Measurement("佐藤", 60.0, 160.0);
        System.out.println(data1);
        System.out.println(data2);
    }
}
