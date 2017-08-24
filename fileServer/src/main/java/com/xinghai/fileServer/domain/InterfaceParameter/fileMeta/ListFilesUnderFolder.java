package com.xinghai.fileServer.domain.InterfaceParameter.fileMeta;

/**
 * Created by scream on 2017/8/3.
 */
public class ListFilesUnderFolder {
    private Integer parentId;
    private String type;
    private String orderField;
    private Integer orderSequence;

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrderField() {
        return orderField;
    }

    public void setOrderField(String orderField) {
        this.orderField = orderField;
    }

    public Integer getOrderSequence() {
        return orderSequence;
    }

    public void setOrderSequence(Integer orderSequence) {
        this.orderSequence = orderSequence;
    }
}
