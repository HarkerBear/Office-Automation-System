package service;

import dao.EmployeeDao;
import dao.LeaveFormDao;
import dao.ProcessFlowDao;
import entity.Employee;
import entity.LeaveForm;
import entity.ProcessFlow;
import utils.MybatisUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class LeaveFormService {

    public LeaveForm createLeaveForm(LeaveForm form){
        LeaveForm savedForm=(LeaveForm) MybatisUtils.executeUpdate(sqlSession -> {
            EmployeeDao employeeDao=sqlSession.getMapper(EmployeeDao.class);
            Employee employee=employeeDao.selectById(form.getEmployeeId());
            if(employee.getLevel()==8){
                form.setStatus("Approved");
            }else{
                form.setStatus("Processing");
            }
            LeaveFormDao leaveFormDao=sqlSession.getMapper(LeaveFormDao.class);
            leaveFormDao.insert(form);
            ProcessFlowDao processFlowDao=sqlSession.getMapper(ProcessFlowDao.class);
            ProcessFlow flow1=new ProcessFlow();
            flow1.setFormId(form.getFormId());
            flow1.setOperatorId(employee.getEmployeeId());
            flow1.setAction("Applied");
            flow1.setCreateTime(new Date());
            flow1.setOrderNo(1);
            flow1.setStatus("Completed");
            flow1.setIsLast(0);
            processFlowDao.insert(flow1);

            if(employee.getLevel()<7){
                Employee dmanager=employeeDao.selectLeader(employee);
                ProcessFlow flow2=new ProcessFlow();
                flow2.setFormId(form.getFormId());
                flow2.setOperatorId(dmanager.getEmployeeId());
                flow2.setAction("Audit");
                flow2.setCreateTime(new Date());
                flow2.setOrderNo(2);
                flow2.setStatus("Processing");
                long diff=form.getEndTime().getTime()-form.getStartTime().getTime();
                float hours= diff/(1000*60*60)*1f;
                if(hours>=BusinessConstants.AUDIT_THRESHOLD){
                    flow2.setIsLast(0);
                    processFlowDao.insert(flow2);
                    Employee manager=employeeDao.selectLeader(dmanager);
                    ProcessFlow flow3=new ProcessFlow();
                    flow3.setFormId(form.getFormId());
                    flow3.setOperatorId(manager.getEmployeeId());
                    flow3.setAction("Audit");
                    flow3.setCreateTime(new Date());
                    flow3.setStatus("Ready");
                    flow3.setOrderNo(3);
                    flow3.setIsLast(1);
                    processFlowDao.insert(flow3);
                }else{
                    flow2.setIsLast(1);
                    processFlowDao.insert(flow2);
                }
            }else if(employee.getLevel()==7){
                Employee manager=employeeDao.selectLeader(employee);
                ProcessFlow flow=new ProcessFlow();
                flow.setFormId(form.getFormId());
                flow.setOperatorId(manager.getEmployeeId());
                flow.setAction("Audit");
                flow.setCreateTime(new Date());
                flow.setStatus("Processing");
                flow.setOrderNo(1);
                flow.setIsLast(1);
                processFlowDao.insert(flow);
            }else if(employee.getLevel()==8){
                Employee manager=employeeDao.selectLeader(employee);
                ProcessFlow flow=new ProcessFlow();
                flow.setFormId(form.getFormId());
                flow.setOperatorId(employee.getEmployeeId());
                flow.setAction("Audit");
                flow.setResult("Approved");
                flow.setReason("Automatic Approval");
                flow.setCreateTime(new Date());
                flow.setAuditTime(new Date());
                flow.setStatus("Completed");
                flow.setOrderNo(2);
                flow.setIsLast(1);
                processFlowDao.insert(flow);
            }
            return form;
        });
        return savedForm;
    }

    public List<Map> getLeaveFormList(String pfStatus, Long operatorId){
        return (List<Map>) MybatisUtils.executeQuery(sqlSession -> {
            LeaveFormDao dao=sqlSession.getMapper(LeaveFormDao.class);
            List<Map> list=dao.selectByParams(pfStatus,operatorId);
            return list;
        });
    }
}
