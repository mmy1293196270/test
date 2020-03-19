package com.excelutils.readwrite;

public abstract class AbstraceTest {

    public String  getString(String str){
        return str +"这个是默认实现";
    }

    public abstract String getStringAbs(String str);
}
