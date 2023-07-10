package edu.nyit.haoyu.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


/**
 * (BillUserAccountBind) entity
 *
 * @author bowen
 * @date 2021-08-25 16:25:21
 */
@Data
@TableName("bill_user_account_bind")
public class BillUserAccountBind {

    private static final long serialVersionUID = -75766938343285757L;


    private String userId;

    private String currency;

    private String methodType;

    private String provider;

    private Long providerAccountId;

    private Long bindTime;


}

