package com.mengtu.util.common;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;


/**
 * ToString
 *
 * @author 过昊天 @ 2022/4/21 14:16
 * @version 1.0
 */
public class ToString implements Serializable {

    private static final long serialVersionUID = 3827293267122851492L;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
