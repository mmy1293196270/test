package com.excelutils.duck.impl;

import com.excelutils.duck.FlyBehavior;
import org.springframework.stereotype.Service;

@Service
public class FlyNoWay implements FlyBehavior {
    @Override
    public void fly() {
        System.out.println("com.excelutils.duck.impl.FlyNoWay");
    }
}
