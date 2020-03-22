package com.excelutils.duck.impl;

import com.excelutils.duck.QuackBehavior;

public class Quack implements QuackBehavior {
    @Override
    public void quack() {
        System.out.println("com.excelutils.duck.impl.Quack");
    }
}
