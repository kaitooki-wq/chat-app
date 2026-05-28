package in.tech_camp.chat_app.repository;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.chat_app.entity.RoomEntity;
@Mapper
public interface  RoomRepository {
  @Insert("INSERT INTO rooms(name) VALUES(#{name})")
  // // roomsテーブルのnameカラムに、引数で受け取ったroomEntityのnameの値を挿入するSQL
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insert(RoomEntity roomEntity); 
  // チャットルームが入力され、DBに保存されたときに、自動的に生成されたキー（ID）が、
  // その場で手元にあるエンティティ（RoomEntity）のidフィールドに自動的に格納されるようになる設定」
  // DBにroomがインサートされる→DBがその部屋のキーを生成してidを決める→それをjava(mybatis)に返して、今挿入された部屋のidは○〇であるとJavaが認識できるようにする　
  
  @Select("SELECT * FROM rooms WHERE id = #{id}")
  RoomEntity findById(Integer id);

  @Delete("DELETE FROM rooms WHERE id = #{id}" )
  void deleteById(Integer id);
}
