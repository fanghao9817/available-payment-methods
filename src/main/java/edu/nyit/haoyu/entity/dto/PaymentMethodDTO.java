package edu.nyit.haoyu.entity.dto;

import java.util.LinkedList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Bowen
 * @date 2021/06/09
 */
@Getter
@Setter
@NoArgsConstructor
public class PaymentMethodDTO {

    /**
     * Payment channel
     */
    private String providerId;

    /**
     * Payment channel name
     */
    private String providerName;

    /**
     * js security component address
     */
    private String jsUrl;

    /**
     * Call the public key of the smartfield component
     */
    private String apiKey;

    /**
     * payment method. For example: CARD OR TICKET
     */
    private String paymentMethod;

    /**
     * specific payment channel under the current payment method
     */
    private List<MethodItemDTO> paymentMethodItems = new LinkedList<>();

    private String paymentMethodText;

    private Long id;

    private transient int level = 0;

    private transient Integer seq;


    @Getter
    @Setter
    @NoArgsConstructor
    public static class MethodItemDTO {

        /**
         *
         */
        private String platform;
        private String code;
        private String name;
        private String logo;
        private Boolean direct;
        private Boolean redirect;

        public MethodItemDTO(MethodDTO methodDTO) {
            this.platform = methodDTO.getProvider();
            this.code = methodDTO.getCode();
            this.direct = methodDTO.isDirect();
            this.redirect = methodDTO.isRedirect();
            this.name = methodDTO.getName();
            this.logo = methodDTO.getLogo();
        }
    }
}
