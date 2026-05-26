package in.tech_camp.chat_app.form;


import lombok. Data;

@Data
public class LoginForm {
  // この LoginForm が何なのかというと、これは「DBのデータ」ではなく、「ユーザーが画面の入力欄（フォーム）に打ち込んだデータ」を一時的に受け取るための箱です。
  // DBのデータ宣言」ではなく、比較処理（getPassword() など）を可能にするために、ゲッターとセッターを自動作成している、という認識で大正解です
  private String email;
  private String password;
  
}
