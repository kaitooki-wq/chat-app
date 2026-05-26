package in.tech_camp.chat_app.form;

import org.hibernate.validator.constraints.Length;

import in.tech_camp.chat_app.validation.ValidationPriority1;
import in.tech_camp.chat_app.validation.ValidationPriority2;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class UserEditForm {
  private Integer id;
  
  @NotBlank(message = "NickName can't be blank", groups = ValidationPriority1.class)
  @Length(max = 6, message= "NickName is toooo long damn it!(maximum is 6 characters)",groups = ValidationPriority2.class)
  private String name;

  @NotBlank(message = "Email can't be blank", groups = ValidationPriority1.class)
  @Email(message = "Email should be valid", groups = ValidationPriority2.class)
  private String email;

}
