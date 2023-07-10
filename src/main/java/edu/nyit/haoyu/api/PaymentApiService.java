package edu.nyit.haoyu.api;

import edu.nyit.haoyu.entity.dto.PaymentMethodReqV2;
import edu.nyit.haoyu.entity.vo.ApiResponse;
import edu.nyit.haoyu.entity.vo.PaymentMethodRespV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentApiService {

    @Autowired
    private PaymentApi paymentApi;

    /**
     * @param request
     * @description: 获取支付方式
     * @return: ApiResponse
     * @author: verity zhan
     * @time: 2021/6/8 17:51
     */
    public ApiResponse<PaymentMethodRespV2> getPaymentMethodV2(PaymentMethodReqV2 request) {
        ApiResponse<PaymentMethodRespV2> response = paymentApi.getPaymentMethodV2(request);
        return response;
    }

}
