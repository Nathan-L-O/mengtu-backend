package com.mengtu.kaichi.label.pojo;


/*
* 案例类
* */

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mengtu.util.common.ToString;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("case1")
public class Case extends BaseDo {

    private static final long serialVersionUID = -499253336720993988L;

    /*案例id*/
    @TableId(value = "case_id",type = IdType.ASSIGN_UUID)
    private String caseId;

    /*案例标签id*/
    @TableField(value = "label_id")
    private String labelId;

    /*案例名称*/
    @TableField(value = "case_name")
    private String caseName;

    /*案例路径*/
    @TableField(value = "case_url")
    private String caseUrl;

    /*案例url*/
    @TableField(exist = false)
    private String caseMp4;

    /*案例封面*/
    @TableField(exist = false)
    private String casePic;

    @TableField(exist = false)
    private MultipartFile mp4;

    @TableField(exist = false)
    private MultipartFile pic;
}
