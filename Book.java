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
    System.out.println("【タイトル】"+title+
                      "【著者】"+author+
                      "【ページ数】"+pageCount+
                      "【現在のページ】"+currentPage);

}


}
