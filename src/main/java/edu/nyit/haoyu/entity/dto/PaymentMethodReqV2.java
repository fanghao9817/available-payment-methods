package edu.nyit.haoyu.entity.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author: Bowen huang
 * @date: 2021/06/02
 */
@Getter
@Setter
@NoArgsConstructor
public class PaymentMethodReqV2 {

    private String country;

    private String currency;


    public PaymentMethodReqV2(String country, String currency) {
        this.country = country;
        this.currency = currency;
    }
}
