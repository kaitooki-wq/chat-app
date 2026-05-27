package in.tech_camp.chat_app.controller;

import java.util.List;
import java.util.stream.Collectors; // 追加

import org.springframework.context.support.DefaultMessageSourceResolvable; // 追加
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult; // 追加
import org.springframework.validation.annotation.Validated; // 追加
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import in.tech_camp.chat_app.custom_user.CustomUserDetail;
import in.tech_camp.chat_app.entity.RoomEntity;
import in.tech_camp.chat_app.entity.RoomUserEntity;
import in.tech_camp.chat_app.entity.UserEntity;
import in.tech_camp.chat_app.form.RoomForm;
import in.tech_camp.chat_app.repository.RoomRepository;
import in.tech_camp.chat_app.repository.RoomUserRepository;
import in.tech_camp.chat_app.repository.UserRepository;
import in.tech_camp.chat_app.validation.ValidationOrder; // 追加（必要に応じてパッケージを調整してください）
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class RoomController {
  
  private final UserRepository userRepository;
  
  private final RoomRepository roomRepository;

  private final RoomUserRepository roomUserRepository;

  @GetMapping("/rooms/new")
  public String showRoomNew(@AuthenticationPrincipal CustomUserDetail currentUser, Model model){
    List<UserEntity> users = userRepository.findAllExcept(currentUser.getId());
    model.addAttribute("users", users);
    model.addAttribute("roomForm", new RoomForm());
    return "rooms/new";
  }
  
  @PostMapping("/rooms")
  public String createRoom(
      @ModelAttribute("RoomForm") @Validated(ValidationOrder.class) RoomForm roomForm, // @Validatedを追加
      BindingResult bindingResult, // BindingResultを追加（必ず対象フォームの直後に置く）
      @AuthenticationPrincipal CustomUserDetail currentUser, 
      Model model
  ){
    // --- バリデーションエラーのチェック処理を追加 ---
    if (bindingResult.hasErrors()) {
      // エラーメッセージのリストを取得
      List<String> errorMessages = bindingResult.getAllErrors().stream()
          .map(DefaultMessageSourceResolvable::getDefaultMessage)
          .collect(Collectors.toList());
      
      // 画面の再表示に必要なデータをModelに詰める
      List<UserEntity> users = userRepository.findAllExcept(currentUser.getId());
      model.addAttribute("users", users);
      model.addAttribute("roomForm", roomForm); // 入力内容を保持させる
      model.addAttribute("errorMessages", errorMessages);
      
      return "rooms/new"; // 新規作成画面に戻す
    }
    // ---------------------------------------------

    RoomEntity roomEntity = new RoomEntity();
    roomEntity.setName(roomForm.getName());
    try {
      roomRepository.insert(roomEntity);
    } catch (Exception e) {
      System.out.println("エラー：" + e);
      List<UserEntity> users = userRepository.findAllExcept(currentUser.getId());
      model.addAttribute("users", users);
      model.addAttribute("roomForm", new RoomForm());
      return "rooms/new";
    }

    List<Integer> memberIds = roomForm.getMemberIds();
    for (Integer userId : memberIds) {
      UserEntity userEntity = userRepository.findById(userId);
      RoomUserEntity roomUserEntity = new RoomUserEntity();
      roomUserEntity.setRoom(roomEntity);
      roomUserEntity.setUser(userEntity);
      try {
        roomUserRepository.insert(roomUserEntity);
      } catch (Exception e) {
        System.out.println("エラー：" + e);
        List<UserEntity> users = userRepository.findAllExcept(currentUser.getId());
        model.addAttribute("users", users);
        model.addAttribute("roomForm", new RoomForm());
        return "rooms/new";
      }
    }
    return "redirect:/";
  }
}