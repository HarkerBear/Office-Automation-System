package utils;

import org.junit.Test;

public class MybatisUtilsTest {

    @Test
    public void testcase1(){
        String result=(String)MybatisUtils.executeQuery(sqlSession -> {
            String out=(String) sqlSession.selectOne("test.sample");
            return out;
        });
        System.out.println(result);
    }
}
