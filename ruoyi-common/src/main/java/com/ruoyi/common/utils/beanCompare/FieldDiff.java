package com.ruoyi.common.utils.beanCompare;

/**
 * @author yuanjie
 * @date 2022/10/11
 */
public class FieldDiff {
    /**
     * 字段英文名
     */
    private String fieldENName;

    /**
     * 字段中文名
     */
    private String fieldCNName;

    /**
     * 旧值
     */
    private Object oldValue;

    /**
     * 新值
     */
    private Object newValue;


    public FieldDiff(String fieldENName, String fieldCNName, Object oldValue, Object newValue) {
        this.fieldENName = fieldENName;
        this.fieldCNName = fieldCNName;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    // 这里省略get set 方法

    @Override
    public String toString() {
        String oldVal = this.oldValue == null ? "" : this.oldValue.toString();
        String newVal = this.newValue == null ? "" : this.newValue.toString();
        return "将 " + this.fieldCNName + " 从“" + oldVal + "” 修改为 “" + newVal + "”";
    }
}
