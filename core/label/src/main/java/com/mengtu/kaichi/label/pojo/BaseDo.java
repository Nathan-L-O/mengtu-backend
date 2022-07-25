package com.mengtu.kaichi.label.pojo;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.mengtu.util.common.ToString;

import java.util.Date;

public class BaseDo extends ToString {


    private static final long serialVersionUID = -8210391678060954969L;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
