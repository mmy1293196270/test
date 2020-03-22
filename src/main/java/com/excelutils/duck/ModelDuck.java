package com.excelutils.duck;

import com.excelutils.duck.impl.FlyWithWings;
import com.excelutils.duck.impl.MuteQuack;

public class ModelDuck extends Duck {

    public ModelDuck() {
        flyBehavior = new FlyWithWings();
        quackBehavior = new MuteQuack();
    }

    @Override
    public void swin() {

    }

    @Override
    public void display() {

    }
}
