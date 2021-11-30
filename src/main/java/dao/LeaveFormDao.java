package dao;

import entity.LeaveForm;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface LeaveFormDao {
    public void insert(LeaveForm form);
    public List<Map> selectByParams(@Param("pf_status") String pfStatus, @Param("pf_operator_id") Long operatorId);
    public LeaveForm selectById(Long formId);
    public void update(LeaveForm form);
}
