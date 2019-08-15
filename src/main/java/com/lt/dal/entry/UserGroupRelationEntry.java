package com.lt.dal.entry;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author sj
 * @date 2019/8/15 10:47
 */
@Entity
@Table(name = "user_group_relation")
public class UserGroupRelationEntry implements Serializable {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "group_id")
    private Long groupId;
    @Column(name = "use_id")
    private Long userId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
