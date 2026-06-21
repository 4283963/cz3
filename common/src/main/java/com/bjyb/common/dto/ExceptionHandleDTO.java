package com.bjyb.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDate;

public class ExceptionHandleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "异常编号不能为空")
    private String exceptionNo;

    @NotBlank(message = "处理人不能为空")
    private String handler;

    @NotBlank(message = "处理方式不能为空")
    private String handleMethod;

    private String handleRemark;

    private String followUpPerson;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate nextFollowUpDate;

    public String getExceptionNo() {
        return exceptionNo;
    }

    public void setExceptionNo(String exceptionNo) {
        this.exceptionNo = exceptionNo;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public String getHandleMethod() {
        return handleMethod;
    }

    public void setHandleMethod(String handleMethod) {
        this.handleMethod = handleMethod;
    }

    public String getHandleRemark() {
        return handleRemark;
    }

    public void setHandleRemark(String handleRemark) {
        this.handleRemark = handleRemark;
    }

    public String getFollowUpPerson() {
        return followUpPerson;
    }

    public void setFollowUpPerson(String followUpPerson) {
        this.followUpPerson = followUpPerson;
    }

    public LocalDate getNextFollowUpDate() {
        return nextFollowUpDate;
    }

    public void setNextFollowUpDate(LocalDate nextFollowUpDate) {
        this.nextFollowUpDate = nextFollowUpDate;
    }
}
