package com.excelutils.readwrite;

import java.security.SecureRandom;
import java.util.IdentityHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

    public static void main(String[] args) {
//        Pattern subPattern = Pattern.compile("^[ \t]*official\\d{13}[ \t]*sub[1,2][ \t]*$");
//        Pattern pattern = Pattern.compile("official\\d{13}");
//        String str = "      official2002111000001    sub2    " +
//                "";
//        Matcher matcher = pattern.matcher(str);
//        boolean matches = matcher.matches();
//        boolean b = matcher.find();
//        String group = matcher.group();
//        System.out.println(b);

        Test01 test01 = new Test01();
        String aaa = test01.getString("我是你大爷");
        String bbb = test01.getStringAbs("我是你二大爷");
        byte[] bytes = new byte[128];
        SecureRandom secureRandom = new SecureRandom();
        IdentityHashMap
        secureRandom.nextBytes(bytes);
        String algorithm = secureRandom.getAlgorithm();
        System.out.println(algorithm);
        System.out.println(aaa+bbb);
    }
}
