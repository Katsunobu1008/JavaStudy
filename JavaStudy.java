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

        double value1 = Math.random();
        System.out.println("0.0以上1.0未満の乱数: " + value1);

        double value2 = Math.pow(2.5, 3);
        System.out.println("2.5の3乗は: " + value2);

        double value3 = Math.min(value1,value2);
        System.out.println("最大値は: " + value3);

        double r = Math.toRadians(30.5);
        System.out.println("30.5度をラジアンに変換: " + r);

        String name = "山田";
        int age = 30;
        double height = 170.5;
        System.out.printf("名前:%s,年齢:%d,身長:%.2fcm%n", name, age, height);

    }
}
