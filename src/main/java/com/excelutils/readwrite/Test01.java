package com.excelutils.readwrite;

import java.security.MessageDigest;

public class Test01 extends AbstraceTest {

    @Override
    public String getStringAbs(String str) {
        return str+"从父类那里继承过来的";
    }


}
