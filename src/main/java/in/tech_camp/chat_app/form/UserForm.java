package in.tech_camp.chat_app.form;

import org.hibernate.validator.constraints.Length;
import org.springframework.validation.BindingResult;

import in.tech_camp.chat_app.validation.ValidationPriority1;
import in.tech_camp.chat_app.validation.ValidationPriority2;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserForm {
  @NotBlank(message = "Name can't be blank", groups = ValidationPriority1.class)
  private String name;

@NotBlank(message = "Email can't be blank", groups = ValidationPriority1.class)
  // ① まずメールアドレスが空欄（未入力）じゃないかを確認する
  @Email(message = "Email should be valid", groups = ValidationPriority2.class)
  // ② ①をクリアしたメールアドレスが、今度は「@」が入っているかなど有効な形式かどうかを判定する
  private String email;

  @NotBlank(message = "Password can't be blank", groups = ValidationPriority1.class)
  // ① パスワードが空欄（未入力）じゃないか判定する
  @Length(min = 6, max = 128, message = "Password should be between 6 and 128 characters", groups = ValidationPriority2.class)
  // ② ①をクリアしたら、今度は文字数が6文字以上128文字以下かどうかを判定する
  private String password;
  
  // 画面の「パスワード（確認用）」に入力された値を受け取る変数
  private String passwordConfirmation;

  // 【修正部分】「パスワード」と「確認用パスワード」が一致しているか（タイポがないか）を判定する
  public void validatePasswordConfirmation(BindingResult result) {
    // もし「パスワード」と「確認用パスワード」が一致していなければ
    if (!password.equals(passwordConfirmation)) {
      // passwordConfirmation フィールドに対して、一致しない旨のエラーメッセージをセットする
      result.rejectValue("passwordConfirmation", "error.user", "Password confirmation doesn't match Password");
    } 
  }
}