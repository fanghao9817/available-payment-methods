package edu.nyit.haoyu.entity.dto;

import java.io.Serializable;
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
public class MethodDTO implements Serializable {

    private String provider;

    private String code;

    private String type;

    private String name;

    private String logo;

    private boolean direct;

    private boolean redirect;


    public MethodDTO(String provider, String code, String type, String name, String logo, boolean direct,
            boolean redirect) {
        this.provider = provider;
        this.code = code;
        this.type = type;
        this.name = name;
        this.logo = logo;
        this.direct = direct;
        this.redirect = redirect;
    }
}
