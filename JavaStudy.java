public class JavaStudy {
    public static void main(String[] args) {
        System.out.println("Welcome to Java Programming!");

        char ch;
        ch = 'a';

        System.out.println(ch);

        String str;
        // ↓↓↓ ここにセミコロンを追加 ↓↓↓
        str = "こんにちは";

        System.out.println(str);

        int number = 20;
        System.out.println(number);

        int times = 100+ number;
        System.out.println(times * number);

        int a = 4;
        int b = 8;
        int numberOnePlayer = a * b;

        System.out.println(numberOnePlayer);

        double pi = 3.14;
        // 自動型変換を確かめる
        System.out.println(pi * a*a);

        // エスケープシーケンスについて
        System.out.println("こんにちは\nJavaの世界へようこそ！");
        System.out.println("こんにちは\nPythonの世界へようこそ！");

        int n = 10;
        n++;
        System.out.println(n);
        n--;
        System.out.println(n);

        int ans, s = 10;
        ans = s++;

        String msg = "こんにちは";
        System.out.println("msgの値は" + msg + "です。");

        System.out.println(Math.sqrt(pi));

        double value = Math.random();
        System.out.println("0.0以上1.0未満の乱数: " + value);

        double value2 = Math.pow(2.5, 3);
        System.out.println("2.5の3乗は: " + value2);

    }
}
