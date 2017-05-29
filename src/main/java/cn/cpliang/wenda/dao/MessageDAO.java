package cn.cpliang.wenda.dao;

import cn.cpliang.wenda.model.Message;
import cn.cpliang.wenda.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by lcplcp on 2017/5/5.
 */

@Mapper
public interface MessageDAO {

    String TABLE_NAME = " message ";
    String INSERT_FIELDS = " from_id, to_id, created_date, content, conversation_id, has_read ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;
    //插入消息
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{fromId},#{toId},#{createdDate},#{content},#{conversationId},#{hasRead})"})
    int addMessage(Message message);
    //根据id读取消息
    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME," where id=#{id}"})
    Message selectMessageById(int id);
    //读取和某个用户的回话消息列表
    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME," where conversation_id=#{conversationId}" +
            "order by created_date DESC limit #{offset},#{limit}"})
    List<Message> selectMessageByConversationId(@Param("conversationId") String conversationId,
                                                @Param("offset") int offset,
                                                @Param("limit") int limit);
    //根据用户id读取发送给该用户的未读消息
    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME," where to_id=#{userId} and has_read=0 order by created_date " +
            "DESC limit #{offset},#{limit}"})
    List<Message> selectUnreadMessageByToId(@Param("userId") int userId,
                                            @Param("offset") int offset,
                                            @Param("limit") int limit);
    //根据发送者以及接收者id统计接收者的未读消息数量
    @Select({"select count(id) from",TABLE_NAME," where to_id=#{userId} and conversation_id=#{conversationId} and has_read=0"})
   // select count(id) from message where from_id=2 and to_id=3 and has_read=0;
    int countUnreadMessage(@Param("userId") int userId,
                           @Param("conversationId") String conversationId);


    //将消息的状态设置为已读
    @Update({"update ",TABLE_NAME," set has_read=#{hasRead} where id=#{id} and has_read=0"})
    int updateMessageStatusById(@Param("id") int id, @Param("hasRead") int hasRead);

    //更新所有未读消息读取状态
    @Update({"update ",TABLE_NAME," set has_read=#{hasRead} where conversation_id=#{conversationId} and to_id=#{userId} and has_read=0"})
    int updateMessageStatusByUserId(@Param("conversationId") String conversationId,
                                    @Param("userId") int userId,
                                    @Param("hasRead") int hasRead);
    //select *,count(*) from (select * from message ORDER by created_date desc) tt group by conversation_id order by created_date desc limit 0,2
    @Select({"select ",INSERT_FIELDS,", count(*) as id from (select * from ",TABLE_NAME," ORDER by created_date desc) " +
            "tt where to_id=#{userId} or from_id=#{userId} group by conversation_id order by created_date desc limit #{offset},#{limit}"})
    List<Message> selectMessagesListByUserId(@Param("userId") int userId, @Param("offset") int offset,@Param("limit") int limit);

}
