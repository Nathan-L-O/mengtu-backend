package com.mengtu.kaichi.pm.pojo;

/*职位*/

import com.baomidou.mybatisplus.annotation.*;
import com.mengtu.kaichi.label.pojo.BaseDo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("pm")
public class Pm extends BaseDo {


    private static final long serialVersionUID = 5442080117475246007L;

    /*项目id*/
    @TableId(value = "pm_id",type = IdType.ASSIGN_UUID)
    private String pmId;

    /*项目名称*/
    @TableField(value = "pm_name")
    private String projectName;

    /*项目链接*/
    @TableField(value = "pm_url")
    private String pmUrl;

    /*项目图片*/
    @TableField(value = "pm_pic")
    private String pmPic;

    /*用户id*/
    @TableField(value = "initial_id")
    private String initialId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}
