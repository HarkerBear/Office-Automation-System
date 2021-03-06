package service;

import dao.EmployeeDao;
import dao.LeaveFormDao;
import dao.NoticeDao;
import dao.ProcessFlowDao;
import entity.Employee;
import entity.LeaveForm;
import entity.Notice;
import entity.ProcessFlow;
import service.exception.BusinessException;
import utils.MybatisUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd-HH H");
            NoticeDao noticeDao=sqlSession.getMapper(NoticeDao.class);
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
                String noticeContent=String.format("You have applied leave[%s-%s] and please wait for the audit process.",sdf.format(form.getStartTime()),sdf.format(form.getEndTime()));
                noticeDao.insert(new Notice(employee.getEmployeeId(),noticeContent));
                noticeContent=String.format("%s-%s has applied leave[%s-%s], please audit soon."
                ,employee.getTitle(),employee.getName(),sdf.format(form.getStartTime()),sdf.format(form.getEndTime()));
                noticeDao.insert(new Notice(dmanager.getEmployeeId(),noticeContent));
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
                String noticeContent=String.format("You have applied leave[%s-%s] and please wait for the audit process."
                        ,sdf.format(form.getStartTime()),sdf.format(form.getEndTime()));
                noticeDao.insert(new Notice(employee.getEmployeeId(),noticeContent));
                noticeContent=String.format("%s-%s has applied leave[%s-%s], please audit soon."
                        ,employee.getTitle(),employee.getName(),sdf.format(form.getStartTime()),sdf.format(form.getEndTime()));
                noticeDao.insert(new Notice(manager.getEmployeeId(),noticeContent));
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
                String noticeContent=String.format("You have applied leave[%s-%s]. It has been approved automatically."
                        ,sdf.format(form.getStartTime()),sdf.format(form.getEndTime()));
                noticeDao.insert(new Notice(employee.getEmployeeId(),noticeContent));
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

    public void audit(Long formId,Long operatorId,String result,String reason){
        MybatisUtils.executeUpdate(sqlSession -> {
            //convert status to 'completed'
            ProcessFlowDao processFlowDao=sqlSession.getMapper(ProcessFlowDao.class);
            List<ProcessFlow> flowList=processFlowDao.selectByFormId(formId);
            if(flowList.size()==0){
                throw new BusinessException("PF001","Valid");
            }
            List<ProcessFlow> processList=flowList.stream().filter(p->p.getOperatorId()==operatorId && p.getStatus().equals("Processing")).collect(Collectors.toList());
            ProcessFlow process=null;
            if(processList.size()==0){
                throw new BusinessException("PF002","No task");
            }else{
                process=processList.get(0);
                process.setStatus("Completed");
                process.setResult(result);
                process.setReason(reason);
                process.setAuditTime(new Date());
                processFlowDao.update(process);
            }
            //if isLast==1, end the process
            LeaveFormDao leaveFormDao=sqlSession.getMapper(LeaveFormDao.class);
            LeaveForm form=leaveFormDao.selectById(formId);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd-HH H");
            EmployeeDao employeeDao=sqlSession.getMapper(EmployeeDao.class);
            Employee employee=employeeDao.selectById(form.getEmployeeId());
            Employee operator=employeeDao.selectById(operatorId);
            NoticeDao noticeDao=sqlSession.getMapper(NoticeDao.class);
            if(process.getIsLast()==1){
                form.setStatus(result);
                leaveFormDao.update(form);
                //if isLast==0 and get approved. The next node's status should be ready
                //other wise, the next node's status should be declined
                String noticeContent=String.format("You leave application[%s-%s] has ended by %s-%s. Result:%s Reason:%s"
                ,sdf.format(form.getStartTime()),sdf.format(form.getEndTime())
                ,operator.getTitle(),operator.getName(),result,reason);
                noticeDao.insert(new Notice(form.getEmployeeId(),noticeContent));
                noticeContent=String.format("You have resolved [%s-%s]'s leave application[%s-%s]. Result:%s Reason:%s"
                        ,employee.getTitle(),employee.getName(),sdf.format(form.getStartTime()),sdf.format(form.getEndTime())
                        ,result,reason);
                noticeDao.insert(new Notice(operator.getEmployeeId(),noticeContent));
            }else{
                List<ProcessFlow> readyList=flowList.stream().filter(p->p.getStatus().equals("Ready")).collect(Collectors.toList());
                if(result.equals("Approved")){
                    ProcessFlow readyProcess=readyList.get(0);
                    readyProcess.setStatus("Processing");
                    processFlowDao.update(readyProcess);
                    String noticeContent=String.format("You leave application[%s-%s] has been approved by %s-%s. Reason:%s"
                            ,sdf.format(form.getStartTime()),sdf.format(form.getEndTime())
                            ,operator.getTitle(),operator.getName(),reason);
                    noticeDao.insert(new Notice(form.getEmployeeId(),noticeContent));

                    noticeContent=String.format("You have resolved [%s-%s]'s leave application[%s-%s]. Result:%s Reason:%s"
                            ,employee.getTitle(),employee.getName(),sdf.format(form.getStartTime()),sdf.format(form.getEndTime())
                            ,result,reason);
                    noticeDao.insert(new Notice(operator.getEmployeeId(),noticeContent));

                    noticeContent=String.format("%s-%s has applied leave[%s-%s], please audit soon."
                            ,employee.getTitle(),employee.getName(),sdf.format(form.getStartTime()),sdf.format(form.getEndTime()));
                    noticeDao.insert(new Notice(readyProcess.getOperatorId(),noticeContent));

                }else if(result.equals("Declined")) {
                    for (ProcessFlow p : readyList) {
                        p.setStatus("Cancelled");
                        processFlowDao.update(p);
                    }
                    form.setStatus("Declined");
                    leaveFormDao.update(form);
                }
                String noticeContent=String.format("You leave application[%s-%s] has been declined by %s-%s. Reason:%s"
                        ,sdf.format(form.getStartTime()),sdf.format(form.getEndTime())
                        ,operator.getTitle(),operator.getName(),reason);
                noticeDao.insert(new Notice(form.getEmployeeId(),noticeContent));

                noticeContent=String.format("You have resolved [%s-%s]'s leave application[%s-%s]. Result:%s Reason:%s"
                        ,employee.getTitle(),employee.getName(),sdf.format(form.getStartTime()),sdf.format(form.getEndTime())
                        ,result,reason);
                noticeDao.insert(new Notice(operator.getEmployeeId(),noticeContent));
            }
            return null;
        });
    }
}
