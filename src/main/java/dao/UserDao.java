package dao;

import entity.User;
import utils.MybatisUtils;

/**
 * user Dao
 */
public class UserDao {
    /**
     * select by userName
     * @param userName
     * @return all user info, if not exists, return null
     */
    public User selectByUserName(String userName){
        return (User) MybatisUtils.executeQuery(sqlSession -> sqlSession.selectOne("usermapper.selectByUserName",userName));
    }

}
