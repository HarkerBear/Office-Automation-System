package dao;

import entity.ProcessFlow;
import junit.framework.TestCase;
import org.junit.Test;
import utils.MybatisUtils;

import java.util.Date;

public class ProcessFlowDaoTest extends TestCase {

    @Test
    public void testInsert() {
        MybatisUtils.executeUpdate(sqlSession -> {
            ProcessFlowDao dao=sqlSession.getMapper(ProcessFlowDao.class);
            ProcessFlow processFlow=new ProcessFlow();
            processFlow.setFormId(31);
            processFlow.setOperatorId(2l);
            processFlow.setAction("audit");
            processFlow.setReason("Understand");
            processFlow.setCreateTime(new Date());
            processFlow.setAuditTime(new Date());
            processFlow.setOrderNo(1);
            processFlow.setStatus("ready");
            processFlow.setIsLast(1);
            dao.insert(processFlow);
            return null;
        });
    }
}