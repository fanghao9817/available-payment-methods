package edu.nyit.haoyu.entity.po;

import java.io.Serializable;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Jerry Fang
 * @Date: 2023/07/11/2:45
 * @Description:
 */
@Data
public class PaymentMethod implements Serializable {

    private String provider;
    private String code;
    private String type;
    private String name;
    private String logo;
    private boolean direct;
    private boolean redirect;
    private String account_provider;
    private Long account_accountId;
    private String account_jsUrl;
    private String account_apiKey;
    private String currencies;
    private String country;
}
