package in.tech_camp.chat_app.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org. apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import in.tech_camp.chat_app.entity.UserEntity;


@Mapper
// このインターフェースがデータベース（MyBatis）と通信する仕組み（マッパー）であることを示します
public interface UserRepository {

  @Insert("INSERT INTO users (name, email, password) VALUES (#{name}, #{email}, #{password})")
  // 【新規登録】usersテーブルに名前、メアド、暗号化パスワードを新しく挿入するSQLです
  @Options(useGeneratedKeys = true, keyProperty = "id")
  // 【自動採番】DBで自動生成されたID（主キー）を、引数のUserEntityのidフィールドに自動でセットします
  void insert(UserEntity user);
  // 上記の挿入処理を実行するメソッドです。引数としてユーザーのエンティティを受け取ります
 
  @Select("SELECT EXISTS(SELECT 1 FROM users WHERE email = #{email})")
  // 【新規用の重複チェック】指定されたメールアドレスが、すでにusersテーブルに1件でも存在するかを判定します
  boolean existsByEmail(String email);
  // メアドを引数に受け取り、存在していれば true、存在していなければ false を返します

  @Select("SELECT * FROM users WHERE email = #{email}")
  // 【ログイン用】入力されたメールアドレスに一致するユーザーの全情報をusersテーブルから検索します
  UserEntity findByEmail(String email);
  // 一致するユーザーが見つかれば、そのデータをUserEntityオブジェクトに詰めて返します

  @Select("SELECT * FROM users WHERE id = #{id}")
  // 【編集画面表示用】指定されたID（主キー）に一致するユーザーの全情報をusersテーブルから検索します
  UserEntity findById(Integer id);
  // 指定されたIDのユーザーデータをUserEntityオブジェクトに詰めて返します

  @Update("UPDATE users SET name = #{name}, email = #{email} WHERE id = #{id}")
  // 【ユーザー情報更新】指定されたIDのユーザーの名前（name）とメールアドレス（email）を書き換えるSQLです
  void update(UserEntity user);
  // 新しい名前とメアドが入ったUserEntityを受け取り、DBのデータを更新します

  @Select("SELECT EXISTS(SELECT 1 FROM users WHERE email = #{email} AND id != #{id})")
  // 【編集用の重複チェック】「指定されたID（自分）以外」のユーザーの中で、同じメールアドレスが使われているかを判定します
  boolean existsByEmailAndIdNot(String email, Integer id);
  // メアドと自分のIDを受け取り、他人がそのメアドをすでに使っていれば true、誰も使っていなければ false を返します
  @Select("SELECT *  FROM users WHERE id <> #{excluded}")
  // <> は SQL で「〜ではない（等しくない）」を表す比較演算子です（!= と同じ意味です）。
  List<UserEntity> findAllExcept(Integer excluded);
}

