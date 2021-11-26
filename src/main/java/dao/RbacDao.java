package dao;

import entity.Node;
import utils.MybatisUtils;

import java.util.List;

public class RbacDao {

    public List<Node> selectNodebyUserId(Long userId){
        return (List)MybatisUtils.executeQuery(sqlSession -> sqlSession.selectList("rbacmapper.selectNodeByUserId",userId));
    }
}
