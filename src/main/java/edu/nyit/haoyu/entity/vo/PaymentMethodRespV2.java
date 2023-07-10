package edu.nyit.haoyu.entity.vo;

import edu.nyit.haoyu.entity.dto.MethodDTO;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author: Bowen huang
 * @date: 2021/06/02
 */
@Data
@AllArgsConstructor
public class PaymentMethodRespV2 implements Serializable {

    private List<MethodDTO> methods;
    private List<Account> providerAccounts;

    @Data
    @AllArgsConstructor
    public static class Account {

        private String provider;
        private Long accountId;
        private String jsUrl;
        private String apiKey;
    }
}
