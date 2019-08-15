package com.lt.domain.resq;

import java.io.Serializable;

/**
 * @author sj
 * @date 2019/8/15 12:43
 */
public class UserResq implements Serializable {


        private Long id;
        private String name;
        private Long tel;
        private Boolean onLine ;

        public Boolean getOnLine() {
            return onLine;
        }

        public void setOnLine(Boolean onLine) {
            this.onLine = onLine;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getTel() {
            return tel;
        }

        public void setTel(Long tel) {
            this.tel = tel;
        }


}
