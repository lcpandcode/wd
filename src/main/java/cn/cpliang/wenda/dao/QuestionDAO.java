package cn.cpliang.wenda.dao;

import cn.cpliang.wenda.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by lcplcp on 2017/5/5.
 */

@Mapper
public interface QuestionDAO {

    String TABLE_NAME = " question ";
    String INSERT_FIELDS = " title, content, user_id, created_date, comment_count ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{title},#{content},#{userId},#{createdDate},#{commentCount})"})
    int addQuestion(Question question);
    @Delete({"delete from",TABLE_NAME," where id=#{id}"})
    int delete(int id);
    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME," where id=#{id}"})
    Question selectQuestionById(int id);

    List<Question> selectLatestQuestions(@Param("userId") int userId,
                                         @Param("offset") int offset,
                                         @Param("limit") int limit);
    @Update({"update ",TABLE_NAME," set comment_count=#{commentCount} where id=#{id}"})
    int updateCommentCountById(@Param("commentCount") int commentCount,@Param("id") int id);
}
