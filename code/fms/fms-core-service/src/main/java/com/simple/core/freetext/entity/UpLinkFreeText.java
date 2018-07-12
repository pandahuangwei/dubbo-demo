package com.simple.core.freetext.entity;

import java.io.Serializable;

/**********************************************************************************
 * Copyright(c)2017 Dcits-air.com All rights reserved.
 * @Title: UpLinkFreeText.java.
 * @Package com.simple.core.freetext.entity.
 * @Description: UpLinkFreeText.
 *
 * @author Administrator.
 * @version 1.0.
 * @created 2017/4/10 10:14.
 **********************************************************************************/
public class UpLinkFreeText implements Serializable {
    private static final long serialVersionUID = 154511184L;

    private String text; //自由报内容

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
