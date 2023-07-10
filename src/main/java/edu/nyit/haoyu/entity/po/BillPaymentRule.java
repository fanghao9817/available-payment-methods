package edu.nyit.haoyu.entity.po;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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

    private static final long serialVersionUID = 382607334360544339L;

    @TableId(
            value = "id",
            type = IdType.AUTO
    )
    private Long id;

    @TableField(
            updateStrategy = FieldStrategy.NEVER
    )
    private Long tenantId;

    private String currency;

    private String paymentMethod;

    private String rule;

    private Boolean enable;


}

