package com.simple.core.common.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 报文结果
 *
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.02.06 17:06.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Message<T extends MsgBody> implements Serializable {
    private static final long serialVersionUID = 1L;

}
