package dao;

import entity.LeaveForm;
import junit.framework.TestCase;
import org.junit.Test;
import utils.MybatisUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LeaveFormDaoTest extends TestCase {

    @Test
    public void testInsert() {
        MybatisUtils.executeUpdate(sqlSession -> {
            LeaveFormDao leaveFormDao=sqlSession.getMapper(LeaveFormDao.class);
            LeaveForm leaveForm=new LeaveForm();
            leaveForm.setEmployeeId(4l);
            leaveForm.setFormType(1);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startTime=null;
            Date endTime=null;
            try {
                startTime=sdf.parse("2020-03-22 09:20:00");
                endTime=sdf.parse("2020-04-01 09:20:00");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            leaveForm.setStartTime(startTime);
            leaveForm.setEndTime(endTime);
            leaveForm.setCreateTime(new Date());
            leaveForm.setReason("Trip");
            leaveForm.setStatus("Processing");
            leaveFormDao.insert(leaveForm);
            return null;
        });

    }
}