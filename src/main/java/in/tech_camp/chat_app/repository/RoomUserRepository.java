package in.tech_camp.chat_app.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.chat_app.entity.RoomUserEntity;

@Mapper
public interface RoomUserRepository {
  @Insert("INSERT INTO room_users(user_id, room_id) VALUES(#{user.id}, #{room.id})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insert(RoomUserEntity userRoomEntity);
  
  @Select("SELECT * FROM room_users WHERE user_id = #{userId}")
  @Result(property = "room", column = "room_id",
          one = @One(select = "in.tech_camp.chat_app.repository.RoomRepository.findById"))
  List<RoomUserEntity> findByUserId(Integer userId);
   
  /* * 👆【解説：このメソッド(@Select~List)がやっていることと役割】
   * * 1. 何のためにあるの？
   * トップページ（ルートパス "/"）を開いた際に、サイドバーへ「ログイン中のユーザーが
   * 参加しているチャットルームの一覧」を正しく出し分けるために存在します。
   * * 2. 「自分が作った部屋」と「参加している部屋」は両方出る？
   * 両方とも漏れなくすべて表示されます！
   * 部屋を作成した際、作成者（自分）のIDも招待されたメンバーのIDと一緒に 
   * `room_users` テーブル（中間テーブル）に保存される仕組みになっているため、
   * 自分が作ったか他人が作ったかに関係なく、自分が所属する部屋がすべてヒットします。
   * * 3. 処理の具体的な流れ（MyBatisの連携技）
   * ① @Select
   * 中間テーブル（room_users）から、現在ログインしているユーザーのID（#{userId}）が
   * 含まれるレコードをすべて特定して抜き出します。
   * ② @Result と @One
   * ①で抜き出したレコードの `room_id` を使って、自動的に `RoomRepository.findById` を
   * 裏で見にいき、「チャットルームの名前（name）」などの詳細情報をガッチャンコ（結合）します。
   * * 4. 注意点（画面遷移について）
   * このコード単体では「チャットルームを押したらその部屋に飛ぶ」という画面遷移の動きにはなりません。
   * これはあくまで「サイドバーに自分の部屋一覧を表示するためのデータをDBから持ってくる」処理です。
   * クリックして中に進む動きは、この後の章でHTMLにリンク（href）を設定し、コントローラーに
   * 部屋ごとのメッセージ画面を返す処理を追加することで完成します。
   */
}
