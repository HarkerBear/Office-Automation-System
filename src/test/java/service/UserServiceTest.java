package service;

import entity.Node;
import entity.User;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.List;

public class UserServiceTest extends TestCase {
    private UserService userService=new UserService();

    @Test
    //no such user
    public void testCheckLogin1() {
        userService.checkLogin("uu","kdsjn");
    }

    @Test
    //wrong password
    public void testCheckLogin2() {
        userService.checkLogin("R7","dasfgfa");
    }

    @Test
    //correct
    public void testCheckLogin3() {
        User user=userService.checkLogin("R7","R7");
        System.out.println(user);
    }

    @Test
    public void testSelectNodeByUserId() {
        List<Node> list=userService.selectNodeByUserId(3l);
        System.out.println(list);
    }
}