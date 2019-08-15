package com.lt.dal.entry;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author sj
 * @date 2019/8/15 10:41
 */
@Entity
@Table(name = "user_group")
public class GroupEntry implements Serializable {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "group_name")
    private String groupName;
    @Column(name = "group_type")
    private Integer groupType;
    @Column(name = "create_user_id")
    private Long createUserId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getGroupType() {
        return groupType;
    }

    public void setGroupType(Integer groupType) {
        this.groupType = groupType;
    }
}
