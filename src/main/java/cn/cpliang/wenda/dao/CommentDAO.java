package cn.cpliang.wenda.dao;

import cn.cpliang.wenda.model.Comment;
import cn.cpliang.wenda.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by lcplcp on 2017/5/5.
 */

@Mapper
public interface CommentDAO {

    String TABLE_NAME = " comment ";
    String INSERT_FIELDS = " user_id, entity_id, entity_type, content, status, create_date ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{userId},#{entityId},#{entityType},#{content},#{status},#{createDate}) "})
    int addComment(Comment comment);
    @Select({"select",SELECT_FIELDS,"from ",TABLE_NAME," where id=#{id}"})
    Comment selectCommentById(int id);
    @Select({"select",SELECT_FIELDS,"from ",TABLE_NAME," where entity_id=#{entityId} and " +
            "entity_type=#{entityType} order by create_date DESC limit #{offset},#{limit}"})
    List<Comment> selectCommentByEntity( @Param("entityType") int entityType,@Param("entityId") int entityId,
                                     @Param("offset")int offset, @Param("limit") int limit);
    @Update({"update ",TABLE_NAME," set status=#{status} where id=#{id}"})
    int updateStatus(@Param("status") int status,@Param("id") int id);
    @Select({"select count(id) from ",TABLE_NAME," where entity_id=#{entityId} and entity_type=#{entityType}"})
    int countComment(@Param("entityId") int entityType , @Param("entityType") int entityId);

}
