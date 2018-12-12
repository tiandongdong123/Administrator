package com.wf.bean;

import java.io.Serializable;

public class InformationLabelResponse implements Serializable {
    /**
     * 资讯标签信息
     */
    private InformationLabel informationLabel;
    /**
     * 判定批次是否插入成功
     */
    private boolean success = false;

    public InformationLabel getInformationLabel() {
        return informationLabel;
    }

    public void setInformationLabel(InformationLabel informationLabel) {
        this.informationLabel = informationLabel;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "InformationLabelResponse{" +
                "informationLabel=" + informationLabel +
                ", success=" + success +
                '}';
    }
}
