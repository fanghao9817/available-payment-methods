package edu.nyit.haoyu.entity.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Bowen
 * @date 2021/06/09
 */
@Getter
@Setter
public class PaymentMethodReqDTO {

    /**
     * Amount, used to determine the amount limit of the current tenant's account
     */
    private BigDecimal amount;

    /**
     * The currency of the current payment. eg. BRL
     */
    private String currency;

    /**
     * Country code of the current payment. eg. BR
     */
    private String country;

    /**
     * ID of the logged in user
     */
    private Long uid;


}
