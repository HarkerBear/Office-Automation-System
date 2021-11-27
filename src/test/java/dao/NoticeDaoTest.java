package dao;

import entity.Notice;
import junit.framework.TestCase;
import org.junit.Test;
import utils.MybatisUtils;

import java.util.Date;

public class NoticeDaoTest extends TestCase {

    @Test
    public void testInsert() {
        MybatisUtils.executeUpdate(sqlSession -> {
            NoticeDao dao=sqlSession.getMapper(NoticeDao.class);
            Notice notice=new Notice();
            notice.setReceiverId(2l);
            notice.setContent("test notice");
            notice.setCreateTime(new Date());
            dao.insert(notice);
            return null;
        });
    }
}