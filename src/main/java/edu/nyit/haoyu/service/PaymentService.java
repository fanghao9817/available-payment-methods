package edu.nyit.haoyu.service;

import edu.nyit.haoyu.entity.dto.MethodDTO;
import edu.nyit.haoyu.entity.dto.PaymentMethodDTO;
import edu.nyit.haoyu.entity.vo.PaymentMethodRespV2;
import java.util.List;

public interface PaymentService {

    /**
     * 结算页 支付方式列表[转换paymentMethod]
     *
     * @param userId
     * @param planId
     * @return
     */
    List<PaymentMethodDTO> getUserPaymentMethodListV2(Long userId, String currency, String country);

    PaymentMethodRespV2 getPaymentGatewayAccountsAndMethodDTOs(String country, String currency);

    List<PaymentMethodDTO> methodsTurnToPaymentMethodDTOs(List<MethodDTO> methods,
            List<PaymentMethodRespV2.Account> providerAccounts, Long userId, String currency);


}
