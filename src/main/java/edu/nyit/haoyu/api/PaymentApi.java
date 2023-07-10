package edu.nyit.haoyu.api;

import edu.nyit.haoyu.entity.dto.PaymentMethodReqV2;
import edu.nyit.haoyu.entity.vo.ApiResponse;
import edu.nyit.haoyu.entity.vo.PaymentMethodRespV2;
import org.springframework.stereotype.Service;

@Service
public interface PaymentApi {

    //@POST("/api/payment/methods/v2")
    ApiResponse<PaymentMethodRespV2> getPaymentMethodV2(PaymentMethodReqV2 request);
}