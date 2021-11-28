package service;

import entity.LeaveForm;
import junit.framework.TestCase;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LeaveFormServiceTest extends TestCase {
    LeaveFormService leaveFormService=new LeaveFormService();
    @Test
    public void testCreateLeaveForm1() throws ParseException {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd-HH");
        LeaveForm form=new LeaveForm();
        form.setEmployeeId(8l);
        form.setStartTime(sdf.parse("2020-03-04-08"));
        form.setEndTime(sdf.parse("2020-03-05-12"));
        form.setFormType(1);
        form.setReason("Trip");
        form.setCreateTime(new Date());
        LeaveForm savedForm=leaveFormService.createLeaveForm(form);
        System.out.println(savedForm.getFormId());
    }
    @Test
    public void testCreateLeaveForm2() throws ParseException {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd-HH");
        LeaveForm form=new LeaveForm();
        form.setEmployeeId(8l);
        form.setStartTime(sdf.parse("2020-03-04-08"));
        form.setEndTime(sdf.parse("2020-03-09-12"));
        form.setFormType(1);
        form.setReason("Trip");
        form.setCreateTime(new Date());
        LeaveForm savedForm=leaveFormService.createLeaveForm(form);
        System.out.println(savedForm.getFormId());
    }
    @Test
    public void testCreateLeaveForm3() throws ParseException {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd-HH");
        LeaveForm form=new LeaveForm();
        form.setEmployeeId(2l);
        form.setStartTime(sdf.parse("2020-03-04-08"));
        form.setEndTime(sdf.parse("2020-03-09-12"));
        form.setFormType(1);
        form.setReason("Trip");
        form.setCreateTime(new Date());
        LeaveForm savedForm=leaveFormService.createLeaveForm(form);
        System.out.println(savedForm.getFormId());
    }
    @Test
    public void testCreateLeaveForm4() throws ParseException {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd-HH");
        LeaveForm form=new LeaveForm();
        form.setEmployeeId(1l);
        form.setStartTime(sdf.parse("2020-03-04-08"));
        form.setEndTime(sdf.parse("2020-03-09-12"));
        form.setFormType(1);
        form.setReason("Trip");
        form.setCreateTime(new Date());
        LeaveForm savedForm=leaveFormService.createLeaveForm(form);
        System.out.println(savedForm.getFormId());
    }
}