package com.excelutils.duck.impl;

import com.excelutils.duck.QuackBehavior;
import org.springframework.stereotype.Service;

@Service
public class Squack implements QuackBehavior {
    @Override
    public void quack() {
        System.out.println("com.excelutils.duck.impl.Squack");
    }
}
