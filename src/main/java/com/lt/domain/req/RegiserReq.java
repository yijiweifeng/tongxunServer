package com.lt.domain.req;

import java.io.Serializable;

/**
 * @author sj
 * @date 2019/8/14 16:55
 */
public class RegiserReq implements Serializable {

    private String name;
    private String password;
    private String tel;

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
