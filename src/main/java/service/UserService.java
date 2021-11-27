package service;

import dao.RbacDao;
import dao.UserDao;
import entity.Node;
import entity.User;
import service.exception.BusinessException;
import utils.MD5Utils;

import java.util.List;

public class UserService {
    private UserDao userDao=new UserDao();
    private RbacDao rbacDao=new RbacDao();

    /**
     * authorize by userName and password
     * @param userName
     * @param password
     * @return if authorized, return an User
     * @throws BusinessException L001-no such user, L002-wrong password
     */
    public User checkLogin(String userName,String password){
        User user=userDao.selectByUserName(userName);
        if(user==null){
            throw new BusinessException("L001","No Such User");
        }
        String md5=MD5Utils.md5Digest(password,user.getSalt());
        if(!md5.equals(user.getPassword())){
            throw new BusinessException("L002","Wrong Password");
        }
        return user;
    }

    public List<Node> selectNodeByUserId(Long userId){
        List<Node> nodeList=rbacDao.selectNodebyUserId(userId);
        return nodeList;
    }
}
