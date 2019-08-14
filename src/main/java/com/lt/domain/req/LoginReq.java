package com.lt.domain.req;

import java.io.Serializable;

/**
 * @author sj
 * @date 2019/8/14 16:53
 */
public class LoginReq implements Serializable {
    private String password;
    private String tel;

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
