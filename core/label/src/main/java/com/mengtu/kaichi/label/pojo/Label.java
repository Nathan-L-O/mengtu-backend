package com.mengtu.kaichi.label.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("case_labels")
public class Label extends BaseDo{

    private static final long serialVersionUID = 4007072533100784562L;

    /*案例标签id*/
    @TableId(type = IdType.ASSIGN_UUID, value = "label_id")
    private String labelId;

    /*案例标签名称*/
    @TableField(value = "label_name")
    private String labelName;

    /*是否隐藏*/
    @TableField(value = "ishidden")
    private String ishidden;

}
