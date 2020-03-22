package com.excelutils;

import com.excelutils.duck.Duck;
import com.excelutils.duck.ModelDuck;
import com.excelutils.duck.impl.FlyNoWay;

public class TestController {

    public static void main(String[] args) {
        Duck duck = new ModelDuck();
        duck.setFlyBehavior(new FlyNoWay());
        duck.perfromFly();
        duck.perfromFly();
    }
}
