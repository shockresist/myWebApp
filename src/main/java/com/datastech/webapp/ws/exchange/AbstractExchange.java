/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datastech.webapp.ws.exchange;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author dbabich
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
        "head",
        "body"
})
@JsonPropertyOrder({
        "head",
        "body"
})
public abstract class AbstractExchange<T> implements Serializable {
    @JsonProperty(value = "body", required = true)
    @NotNull
    @Valid
    private T body;

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
    
    
}
