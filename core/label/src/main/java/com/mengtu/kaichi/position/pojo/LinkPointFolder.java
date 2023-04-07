package com.mengtu.kaichi.position.pojo;

/*职位*/

import com.baomidou.mybatisplus.annotation.*;
import com.mengtu.kaichi.label.pojo.BaseDo;
import com.mengtu.util.common.ToString;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("linkpoint_folder")
public class LinkPointFolder extends ToString {


    private static final long serialVersionUID = 5442080117475246007L;

    /*文件夹id*/
    @TableId(value = "id",type = IdType.ASSIGN_UUID)
    private String id;

    /*文件夹名称*/
    @TableField(value = "folder_name")
    private String folderName;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(value = "delete_flag",fill = FieldFill.INSERT)
    @TableLogic(value = "false", delval = "true")
    private Boolean deleteFlag;

}
