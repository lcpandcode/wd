package cn.cpliang.wenda.dao;

import cn.cpliang.wenda.model.User;
import org.apache.ibatis.annotations.*;

/**
 * Created by lcplcp on 2017/5/5.
 */

@Mapper
public interface UserDAO {

    String TABLE_NAME = " user ";
    String INSERT_FIELDS = " name, password, salt, head_url, is_active, user_email";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{name},#{password},#{salt},#{headUrl},#{isActive},#{userEmail})"})
    int addUser(User user);
    @Delete({"delete from",TABLE_NAME," where id=#{id}"})
    int delete(int id);
    @Select({"select",SELECT_FIELDS,"from user where id=#{id}"})
    User selectUserById(int id);
    @Select({"select",SELECT_FIELDS,"from user where name=#{name}"})
    User getUserByName(String name);
    @Update({"update ",TABLE_NAME," set is_active=1 where id=#{userId}"})
    boolean active(String userId);
}
