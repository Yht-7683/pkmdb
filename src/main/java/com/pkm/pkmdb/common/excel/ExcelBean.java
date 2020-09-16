package com.pkm.pkmdb.common.excel;

import java.io.Serializable;

/**
 * @Title:
 * @Package:com.zongs.commons.web.utils.excel
 * @ClassName:ExcelBean
 * @Description:
 * @Author:zhixiang.yang
 * @CreateDate:2019/3/720:01
 * @UpdateUser:zhixiang.yang
 * @UpdateDate:2019/3/720:01
 * @UpdateRemark:
 * @Version:1.0
 */
public class ExcelBean implements Serializable {
    /**
     * 列头（标题）名
     */
    private String headTextName;
    /**
     * 对应属性名
     */
    private String propertyName;

    public ExcelBean() {

    }

    public ExcelBean(String headTextName, String propertyName) {
        this.headTextName = headTextName;
        this.propertyName = propertyName;
    }

    public String getHeadTextName() {
        return headTextName;
    }

    public void setHeadTextName(String headTextName) {
        this.headTextName = headTextName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }
}
