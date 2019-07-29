package org.poem.nt;

import lombok.Data;
import org.poem.nt.field.NtField;

/**
 * @author poem
 */
@Data
public class NtDataElementVO {

    @NtField(name = "内部标识符")
    private String internaldentifier;
    @NtField(name = "中文名称")
    private String chineseName;
    @NtField(name = "中文全拼")
    private String pinyin;
    @NtField(name = "标识符")
    private String flag;
    @NtField(name = "版本")
    private String version;
    @NtField(name = "同义名称")
    private String synonym;
    @NtField(name = "说明")
    private String explain;
    @NtField(name = "对象词")
    private String className;
    @NtField(name = "特性词")
    private String characteristic;
    @NtField(name = "表示词")
    private String preposition;
    @NtField(name = "数据类型")
    private String dataType;
    @NtField(name = "表示格式")
    private String format;
    @NtField(name = "值域")
    private String range;
    @NtField(name = "关系")
    private String relationship;
    @NtField(name = "计量单位")
    private String unit;
    @NtField(name = "状态")
    private String status;
    @NtField(name = "提交机构")
    private String submittedOrg;
    @NtField(name = "主要起草人")
    private String startUser;
    @NtField(name = "批准日期")
    private String approvedDate;
    @NtField(name = "备注")
    private String remarks;
}
