package edu.nyit.haoyu.entity.vo;

import edu.nyit.haoyu.entity.dto.MethodDTO;
import java.io.Serializable;
import java.util.List;
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
public class PaymentMethodRespV2 implements Serializable {

    private List<MethodDTO> methods;
    private List<Account> providerAccounts;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Account {

        private String provider;
        private Long accountId;
        private String jsUrl;
        private String apiKey;
    }
}
