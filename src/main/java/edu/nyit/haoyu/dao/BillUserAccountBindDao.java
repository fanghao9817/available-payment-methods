package edu.nyit.haoyu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.nyit.haoyu.entity.po.BillUserAccountBind;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * (BillUserAccountBind) data access layer
 *
 * @author bowen
 * @date 2021-08-25 16:25:22
 */
@Mapper
public interface BillUserAccountBindDao extends BaseMapper<BillUserAccountBind> {

    void deleteByAccountId(@Param("list") List<Long> list);

    void clearByAccountId(@Param("list") List<Long> list);
}

