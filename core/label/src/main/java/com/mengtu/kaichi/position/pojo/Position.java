package com.mengtu.kaichi.position.pojo;

/*职位*/

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mengtu.kaichi.label.pojo.BaseDo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("position")
public class Position extends BaseDo {


    private static final long serialVersionUID = 5442080117475246007L;

    /*工作id*/
    @TableId(value = "position_id",type = IdType.ASSIGN_UUID)
    private String positionId;

    /*工作名称*/
    @TableField(value = "position_name")
    private String positionName;

    /*工作类型id*/
    @TableField(value = "job_category_id")
    private String jobCategoryId;

    /*工作地点*/
    @TableField(value = "workplace")
    private String workplace;

    /*工作类型,0全职 1兼职*/
    @TableField(value = "position_type")
    private Integer positionType;

    /*工作职责*/
    @TableField(value = "position_responsibility")
    private String positionResponsibility;

    /*工作要求*/
    @TableField(value = "position_request")
    private String positionRequest;

}
