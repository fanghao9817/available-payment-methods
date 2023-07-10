package edu.nyit.haoyu.service;

import com.opencsv.bean.CsvToBeanBuilder;
import edu.nyit.haoyu.entity.dto.MethodDTO;
import edu.nyit.haoyu.entity.po.PaymentMethod;
import edu.nyit.haoyu.entity.vo.PaymentMethodRespV2;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;


/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Jerry Fang
 * @Date: 2023/07/11/2:37
 * @Description:
 */

@Service
public class MockService {

    @Autowired
    private ResourceLoader resourceLoader;

    private List<PaymentMethod> paymentMethods;

    @PostConstruct
    public void init() throws Exception {
        Resource resource = resourceLoader.getResource("classpath:csv/paymentMethods.csv");
        try (Reader reader = new InputStreamReader(resource.getInputStream())) {
            paymentMethods = new CsvToBeanBuilder<PaymentMethod>(reader)
                    .withType(PaymentMethod.class)
                    .build()
                    .parse();
        }
    }

    public PaymentMethodRespV2 getPaymentGatewayAccountsAndMethodDTOs(String country, String currency) {
        List<MethodDTO> methodDTOS = new ArrayList<>();
        List<PaymentMethodRespV2.Account> providerAccounts = new ArrayList<>();
        Set<String> distinctProviders = new HashSet<>();
        Set<Long> distinctAccountIds = new HashSet<>();

        for (PaymentMethod method : paymentMethods) {
            if (method.getCountry().equals(country) && method.getCurrencies().contains(currency)) {
                if (!distinctProviders.contains(method.getProvider())) {
                    methodDTOS.add(
                            new MethodDTO(method.getProvider(), method.getCode(), method.getType(), method.getName(),
                                    method.getLogo(), method.isDirect(), method.isRedirect()));
                    distinctProviders.add(method.getProvider());
                }
                if (!distinctAccountIds.contains(method.getAccount_accountId())) {
                    providerAccounts.add(
                            new PaymentMethodRespV2.Account(method.getAccount_provider(), method.getAccount_accountId(),
                                    method.getAccount_jsUrl(), method.getAccount_apiKey()));
                    distinctAccountIds.add(method.getAccount_accountId());
                }
            }
        }

        return new PaymentMethodRespV2(methodDTOS, providerAccounts);
    }
}


