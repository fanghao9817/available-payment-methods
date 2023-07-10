package edu.nyit.haoyu.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.nyit.haoyu.entity.po.BillPaymentRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * (BillPaymentRule) data access layer
 *
 * @author pojer
 * @date 2021-08-25 13:54:02
 */
@Mapper
public interface BillPaymentRuleDao extends BaseMapper<BillPaymentRule> {


    int deleteByCurrency(@Param("currency") String currency);
}

