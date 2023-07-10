package edu.nyit.haoyu.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.nyit.haoyu.api.PaymentApiService;
import edu.nyit.haoyu.dao.BillPaymentRuleDao;
import edu.nyit.haoyu.dao.BillUserAccountBindDao;
import edu.nyit.haoyu.entity.dto.MethodDTO;
import edu.nyit.haoyu.entity.dto.PaymentMethodDTO;
import edu.nyit.haoyu.entity.dto.PaymentMethodReqV2;
import edu.nyit.haoyu.entity.dto.WeightV2;
import edu.nyit.haoyu.entity.po.BillPaymentRule;
import edu.nyit.haoyu.entity.po.BillUserAccountBind;
import edu.nyit.haoyu.entity.vo.ApiResponse;
import edu.nyit.haoyu.entity.vo.PaymentMethodRespV2;
import edu.nyit.haoyu.service.MockService;
import edu.nyit.haoyu.service.PaymentService;
import edu.nyit.haoyu.utils.Validator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @description:
 * @author: verity zhan
 * @time: 2021/5/27 11:32
 */
@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentApiService paymentApiService;

    @Resource
    private BillUserAccountBindDao userAccountBindDao;

    @Resource
    private BillPaymentRuleDao billPaymentRuleDao;

    @Autowired
    MockService mockService;

    /**
     * This method is the entry point for retrieving user-specific payment methods. It queries the payment gateway for
     * available accounts and payment methods, checks the user's existing binding status, performs cleaning, and
     * re-binds based on the weight rule. It then assembles the resulting bindings into the format expected by the front
     * end.
     *
     * @param userId   The ID of the user for whom to retrieve payment methods
     * @param currency The currency associated with the user's payment method
     * @param country  The country associated with the user's payment method
     * @return A list of payment method DTOs
     */
    @Override
    public List<PaymentMethodDTO> getUserPaymentMethodListV2(Long userId, String currency,
            String country) {
        // Log the input parameters
        log.info(
                "[PaymentServiceImpl/getUserPaymentMethodList]find payment methods V2 req = userId={}, planId={}, currency={}, country={}",
                userId, currency, country);

        // Retrieve all supported third-party payment channel merchant accounts (PaymentMethodRespV2.accounts)
        // and most granular payment methods (PaymentMethodRespV2.methodDTOs) from the payment gateway based on the input currency and country.
        //GetPaymentGatewayAccountsAndMethodDTOs method originally interface call here, here we can mock a set of data
//        PaymentMethodRespV2 paymentMethodRespV2 = getPaymentGatewayAccountsAndMethodDTOs(country, currency);
        PaymentMethodRespV2 paymentMethodRespV2 = mockService.getPaymentGatewayAccountsAndMethodDTOs(country, currency);
        log.info("[PaymentServiceImpl/getUserPaymentMethodList]PAYMENT GATEWAY payment methods V2 resp={}",
                paymentMethodRespV2);

        // If either the third-party payment channel merchant accounts or the most granular payment methods are empty, return an empty list.
        if (Validator.isNullOrEmpty(paymentMethodRespV2.getMethods()) || Validator.isNullOrEmpty(
                paymentMethodRespV2.getProviderAccounts())) {
            log.info(
                    "[PaymentServiceImpl/getUserPaymentMethodList]payment method returned by the PAYMENT GATEWAY is empty, req: country={}, currency={}",
                    country, currency);
            return Collections.emptyList();
        }

        List<MethodDTO> methods = paymentMethodRespV2.getMethods();// Retrieve a list of payment methods from the response
        List<PaymentMethodRespV2.Account> providerAccounts = paymentMethodRespV2.getProviderAccounts();// Retrieve a list of provider accounts from the response

        //The key method is executed here
        List<PaymentMethodDTO> paymentMethodDTOs = methodsTurnToPaymentMethodDTOs(methods, providerAccounts, userId,
                currency);

        return paymentMethodDTOs;

    }

    /*
        This method is to update and maintain user payment binding information based on the available
        payment methods and collection account information returned by the payment gateway, in
        combination with the existing user-bound payment methods and collection account data. This
        includes the following steps:

        1.Check the payment methods and collection account information returned by the payment gateway.
        If the bound payment methods or collection accounts are no longer available in the data returned
        by the payment gateway, then clean up these invalid binding information.

        2.According to the set weight rules, rebind the new available payment methods and collection
        accounts for the cleaned or to-be-bound user payment methods.

        3.The processed binding information may include three situations: the original valid binding;
        the binding that has been cleaned or reset because the payment gateway does not return the
        corresponding payment method or collection account; the new binding because the payment gateway
        returns new payment methods and collection accounts.

        4.Update all the above valid binding information in the database and return it after assembling
        in the format required by the front end.

         * @param methods The list of methods to convert
         * @param providerAccounts The list of provider accounts to use for binding
         * @param userId The user ID for the bindings
         * @param currency The currency for the bindings
         * @return A list of PaymentMethodDTOs
     */
    @Override
    public List<PaymentMethodDTO> methodsTurnToPaymentMethodDTOs(List<MethodDTO> methods,
            List<PaymentMethodRespV2.Account> providerAccounts, Long userId, String currency) {

        // Aggregate PaymentMethodRespV2.methodDTOs by method_type. The key is the payment channel method_type, and the value is methodDTOs.
        Map<String, List<MethodDTO>> type2methodDTOs =
                methods.stream().collect(Collectors.groupingBy(MethodDTO::getType));

        // Define the final return object paymentMethodDTOs. Each loop iteration fills at most one PaymentMethodDTO.
        List<PaymentMethodDTO> paymentMethodDTOs = new LinkedList<>();

        // Iterate over type2methodDTOs by method_type.
        for (Map.Entry<String, List<MethodDTO>> entry : type2methodDTOs.entrySet()) {
            // For each individual method_type, use the input userId, input currency, and method_type from the loop to query if there is a binding.
            BillUserAccountBind billUserAccountBind = userAccountBindDao.selectOne(
                    new LambdaQueryWrapper<BillUserAccountBind>()
                            .eq(BillUserAccountBind::getUserId, userId)
                            .eq(BillUserAccountBind::getCurrency, currency)
                            .eq(BillUserAccountBind::getMethodType, entry.getKey()));
            // Define an empty account for later use.
            PaymentMethodRespV2.Account accountForPaymentMethodDTO = null;
            // Special block for handling the case where billUserAccountBind is null.
            //=================================== Start =========================================
            if (Validator.isNullOrEmpty(billUserAccountBind)) {
                try {
                    // Before binding, you need to fetch the binding weight rules.
                    BillPaymentRule rule = billPaymentRuleDao.selectOne(new LambdaQueryWrapper<BillPaymentRule>()
                            .eq(BillPaymentRule::getCurrency, currency)
                            .eq(BillPaymentRule::getPaymentMethod, entry.getKey()));
                    //// If the fetched binding rule is not empty, try to bind. If the rules are all empty, skip directly.
                    if (!Validator.isNullOrEmpty(rule)) {
                        // Get the weight rules for this payment method and currency.
                        List<WeightV2> weightV2s = JSONArray.parseArray(rule.getRule(), WeightV2.class);
                        for (WeightV2 weightV2 : weightV2s) {
                            log.error(
                                    "weightV2.getWeight()={},weightV2.getCode()={},weightV2.getProviderAccountId()={},rule={}",
                                    weightV2.getWeight(), weightV2.getCode(), weightV2.getProviderAccountId(), rule);
                        }

                        // Get the accountIds returned by the payment gateway.
                        List<Long> accountIds = providerAccounts.stream().map(PaymentMethodRespV2.Account::getAccountId)
                                .collect(Collectors.toList());
                        // Use the accounts returned by the payment gateway to filter weightV2s.
                        weightV2s = weightV2s.stream().filter(w -> accountIds.contains(w.getProviderAccountId()))
                                .collect(Collectors.toList());
                        // Get a WeightV2.accountId by weight randomly.
                        int sum = weightV2s.stream().mapToInt(WeightV2::getWeight).sum();
                        // If the sum of all weights in this rule is zero, skip directly.
                        if (sum == 0) {
                            continue;
                        }
                        WeightV2 weightV2 = chooseAccountWithWeight(weightV2s);
                        List<PaymentMethodRespV2.Account> accounts = providerAccounts.stream()
                                .filter(account -> weightV2.getProviderAccountId().equals(account.getAccountId()))
                                .collect(Collectors.toList());
                        // If none of the available accounts returned by the payment gateway match this rule, skip directly.
                        if (Validator.isNullOrEmpty(accounts)) {
                            continue;
                        }
                        accountForPaymentMethodDTO = accounts.get(0);

                        // Get the selected account weightV2.providerAccountId and bind it.
                        billUserAccountBind = new BillUserAccountBind();
                        billUserAccountBind.setUserId(userId.toString());
                        billUserAccountBind.setCurrency(currency);
                        billUserAccountBind.setMethodType(entry.getKey());
                        billUserAccountBind.setProvider(weightV2.getCode());
                        billUserAccountBind.setProviderAccountId(weightV2.getProviderAccountId());
                        billUserAccountBind.setBindTime(System.currentTimeMillis());
                        userAccountBindDao.insert(billUserAccountBind);
                    } else {
                        continue;
                    }
                } catch (Exception ex) {
                    log.error(
                            "An exception occurred while handling the situation where billUserAccountBind is null (i.e., when the entire currency, userId, and method_type are not bound to third-party channel accounts). The process does not interrupt",
                            ex);
                }
            }
            //=================================== End =========================================

            // If there are no binding rules for this currency and method_type, and the attempt to bind also failed due to the rules, if there is no billUserAccountBind in the end, skip this iteration
            if (Validator.isNullOrEmpty(billUserAccountBind)) {
                continue;
            }
            // In any case, once a binding relationship is obtained, take the single entry.getValue() of the same method_type in MethodDTOs, and filter only the MethodDTOs of the provider to which billUserAccountBind belongs
            String bindProvider = billUserAccountBind.getProvider();
            long bindProviderAccountId = billUserAccountBind.getProviderAccountId();
            List<MethodDTO> entryFilteredMethodDTOs = entry.getValue().stream()
                    .filter(e -> e.getProvider().equals(bindProvider)).collect(Collectors.toList());
            if (Validator.isNullOrEmpty(accountForPaymentMethodDTO)) {
                //Note that if the packaging type Long==Long is compared, it is comparable only if it is less than 127
                List<PaymentMethodRespV2.Account> bindProviderAccounts = providerAccounts.stream()
                        .filter(a -> a.getAccountId().longValue() == bindProviderAccountId)
                        .collect(Collectors.toList());
                if (!Validator.isNullOrEmpty(bindProviderAccounts)) {
                    accountForPaymentMethodDTO = bindProviderAccounts.get(0);
                } else {
                    // If there are no matching available accounts in this case, it means that the account has been disabled or deleted, and all binding relationships under this account should be deleted
                    userAccountBindDao.delete(new LambdaQueryWrapper<BillUserAccountBind>()
                            .eq(BillUserAccountBind::getProviderAccountId, billUserAccountBind.getProviderAccountId()));
                }
            }
            // If there are no matching accounts in the final gateway return, skip this iteration
            if (Validator.isNullOrEmpty(accountForPaymentMethodDTO)) {
                continue;
            }
            PaymentMethodDTO singlePaymentMethodDTO = new PaymentMethodDTO();
            singlePaymentMethodDTO.setProviderId(billUserAccountBind.getProvider());
            singlePaymentMethodDTO.setProviderName(billUserAccountBind.getProvider().toLowerCase());
            singlePaymentMethodDTO.setJsUrl(accountForPaymentMethodDTO.getJsUrl());
            singlePaymentMethodDTO.setApiKey(accountForPaymentMethodDTO.getApiKey());
            singlePaymentMethodDTO.setPaymentMethod(billUserAccountBind.getMethodType());
            List<PaymentMethodDTO.MethodItemDTO> paymentMethodItems = new ArrayList<>(entryFilteredMethodDTOs.size());
            // Convert and populate MethodItemDTOs from MethodDTOs
            for (MethodDTO entryFilteredMethodDTO : entryFilteredMethodDTOs) {
                paymentMethodItems.add(new PaymentMethodDTO.MethodItemDTO(entryFilteredMethodDTO));
            }
            // Populate all filtered paymentMethodItems for a single method_type
            singlePaymentMethodDTO.setPaymentMethodItems(paymentMethodItems);

            paymentMethodDTOs.add(singlePaymentMethodDTO);
        }
        return paymentMethodDTOs;
    }

    /**
     * This method makes a request to the payment gateway to get available accounts and payment methods for a given
     * country and currency.
     *
     * @param country  The country for the request
     * @param currency The currency for the request
     * @return A PaymentMethodRespV2 object containing the response data
     */
    @Override
    public PaymentMethodRespV2 getPaymentGatewayAccountsAndMethodDTOs(String country, String currency) {

        PaymentMethodReqV2 paymentMethodReqV2 = new PaymentMethodReqV2();
        paymentMethodReqV2.setCountry(country);
        paymentMethodReqV2.setCurrency(currency);
        // Send a request to the payment gateway
        ApiResponse<PaymentMethodRespV2> response = new ApiResponse<>();
        try {
            log.info(
                    "[PaymentServiceImpl/getPaymentGatewayAccountsAndMethodDTOs]call PAYMENT GATEWAY payment methods V2 req:currency={}, country={}",
                    currency, country);
            response = paymentApiService.getPaymentMethodV2(paymentMethodReqV2);
        } catch (Exception e) {
            log.error(
                    "[PaymentServiceImpl/getPaymentGatewayAccountsAndMethodDTOs]call PAYMENT GATEWAY payment methods V2 failure");
        }

        // Return the data from the response, which contains the available accounts and payment methods
        return response.getData();
    }

    /**
     * This method selects an account randomly based on the provided weights.
     *
     * @param weightV2s The list of weights to use for selection
     * @return A WeightV2 object representing the selected account
     */
    private WeightV2 chooseAccountWithWeight(List<WeightV2> weightV2s) {
        int sum = weightV2s.stream().mapToInt(WeightV2::getWeight).sum();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int num = random.nextInt(sum);
        WeightV2 result = null;
        int left = 0;
        for (WeightV2 ta : weightV2s) {
            if (num < left + ta.getWeight()) {
                result = ta;
                log.info(
                        "[PaymentServiceImpl/chooseAccountWithWeight]Chosen account: provider={}, accountId={}, num={}",
                        ta.getCode(), ta.getProviderAccountId(), num);
                break;
            } else {
                left += ta.getWeight();
            }
        }
        return result;
    }
}

