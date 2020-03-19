package com.excelutils.readwrite;

import lombok.Data;

import java.util.List;
@Data
public class CalendarExportDTO {
    private String sheetName;
    private Class clazz;
    private List<? extends Object> datas;

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public List<? extends Object> getDatas() {
        return datas;
    }

    public void setDatas(List<? extends Object> datas) {
        this.datas = datas;
    }
}
