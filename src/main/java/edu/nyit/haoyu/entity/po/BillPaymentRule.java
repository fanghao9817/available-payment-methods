package edu.nyit.haoyu.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


/**
 * (BillPaymentRule) entity
 *
 * @author pojer
 * @date 2021-08-25 13:54:00
 */
@Data
@TableName("bill_payment_rule")
public class BillPaymentRule {

    private Long id;

    private String currency;

    private String paymentMethod;

    private String rule;

    private Boolean enable;


}

