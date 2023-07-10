package edu.nyit.haoyu.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.nyit.haoyu.entity.po.BillPaymentRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * (BillPaymentRule) data access layer
 *
 * @author pojer
 * @date 2021-08-25 13:54:02
 */
@Mapper
public interface BillPaymentRuleDao extends BaseMapper<BillPaymentRule> {


    int deleteByCurrency(@Param("currency") String currency, @Param("tenantId") Integer tenantId);

    @Update("UPDATE bill_payment_rule SET del_flag = 1 WHERE  del_flag = 0 AND tenant_id = #{tenantId}")
    int deleteByTenantId(@Param("tenantId") Long tenantId);
}

