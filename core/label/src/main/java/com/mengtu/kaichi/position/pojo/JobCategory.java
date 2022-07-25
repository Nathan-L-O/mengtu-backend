package com.mengtu.kaichi.position.pojo;

/*职位类型*/


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
@TableName("job_category")
public class JobCategory extends BaseDo {

    private static final long serialVersionUID = -5579949957933336879L;

    /*工作类型id*/
    @TableId(value = "Job_category_id",type = IdType.ASSIGN_UUID)
    private String jobCategoryId;

    /*工作类型名称*/
    @TableField(value = "Job_category_name")
    private String jobCategoryName;
}
