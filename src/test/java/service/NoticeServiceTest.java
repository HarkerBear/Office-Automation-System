package service;

import junit.framework.TestCase;
import org.junit.Test;

public class NoticeServiceTest extends TestCase {

    @Test
    public void testGetNoticeList() {
        NoticeService noticeService=new NoticeService();
        System.out.println(noticeService.getNoticeList(2l));
    }
}