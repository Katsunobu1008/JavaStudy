public class Book{

// フィールドを定義
  private String title;
  private String author;
  private int pageCount;
  private int currentPage;

  private static int bookCount = 0;

  // コンストラクタ
  public Book(String title,String author,int pageCount){
    this.title = title;
    this.author = author;
    this.pageCount = 0;
    this.currentPage = 1;
    bookCount++;
  }

  // コンストラクタオーバーロード
  public Book(String title, String author) {
    this(title, author, 0); // デフォルトのページ数を0に設定
    System.out.println("タイトル: " + title + ", 著者: " + author + " の本が作成されました。");
  }


  //メソッドを定義
  public void readPage(){
    if(currentPage < pageCount){
      currentPage++;
    } else {
      System.out.println("これ以上読むことはできません。");
    }
  }

  public void showStatus(){
    String status = "'%s' / %s - %d/%dページ".formatted(this.title, this.author, this.currentPage, this.pageCount);
    System.out.println(status);
    }
  public static int getBookCount(){
    return bookCount;
  }


}
