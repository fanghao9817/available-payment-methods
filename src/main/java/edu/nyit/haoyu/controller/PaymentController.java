package edu.nyit.haoyu.controller;

import edu.nyit.haoyu.entity.dto.PaymentMethodDTO;
import edu.nyit.haoyu.entity.dto.PaymentMethodReqDTO;
import edu.nyit.haoyu.entity.vo.ApiResponse;
import edu.nyit.haoyu.entity.vo.ResponseUtils;
import edu.nyit.haoyu.service.PaymentService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Bowen huang
 * @date: 2021/06/09
 */
@Slf4j
@RestController
@RequestMapping("/api/bill/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping(value = "/methods/v2")
    public ApiResponse<List<PaymentMethodDTO>> paymentMethodV2(HttpServletRequest request,
            @RequestBody PaymentMethodReqDTO req) {
        List<PaymentMethodDTO> paymentMethod = paymentService.getUserPaymentMethodListV2(req.getUid(), req.getGoodsId(),
                req.getCurrency(), req.getCountry());

        return ResponseUtils.ok(paymentMethod);
    }
}
