package com.excelutils.duck;

import lombok.Data;

/**
 * 策略模式(策略模式定义了算法族,分别封装起来,让他们之间可以互相替换,此模式让算法的变化独立于使用算法的客户)
 * 对象的行为不是继承来的而是组合得来的
 * 可以让代码比继承具有更高的弹性,可以在动态改变类的行为
 */
@Data
public abstract class Duck {
    FlyBehavior flyBehavior;
    QuackBehavior quackBehavior;

    public abstract void swin();
    public abstract void display();

    /**
     * 飞行
     */
    public  void perfromFly(){
        flyBehavior.fly();
    }

    /**
     * 叫
     */
    public  void perfromQuack(){
        quackBehavior.quack();
    }

}
