package cn.cpliang.wenda.dao;

import cn.cpliang.wenda.model.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * Created by lcplcp on 2017/5/7.
 */
@Mapper
public interface TicketDAO {
    String TABLE_NAME = " login_ticket ";
    String INSERT_FIELDS = " user_id, expired, status, ticket ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{userId},#{expired},#{status},#{ticket})"})
    int addTicket(LoginTicket ticket);
    @Delete({"delete from",TABLE_NAME," where ticket=#{ticket}"})
    int delete(String ticket);
    @Update({"update ",TABLE_NAME," set status=#{status} where ticket=#{ticket}"})
    int update(@Param("status") int status,@Param("ticket") String ticket);
    @Select({" select ",SELECT_FIELDS," from login_ticket where ticket=#{ticket}"})
    LoginTicket selectLoginTicket(String ticket);
}
