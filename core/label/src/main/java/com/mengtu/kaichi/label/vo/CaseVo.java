package com.mengtu.kaichi.label.vo;

import com.mengtu.kaichi.label.pojo.Case;
import com.mengtu.kaichi.label.pojo.Label;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaseVo {
    private Case aCase;
    private Label label;

}
