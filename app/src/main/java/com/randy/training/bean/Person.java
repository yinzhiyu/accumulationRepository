package com.randy.training.bean;

/**
 * author : yinzhiyu
 * e-mail : yinzhiyu@zongheng.com
 * date   : 2021/8/2617:41
 * desc   :
 */
public class Person {

    public static void main(String[] args) {
        Person person = new Person();
        person.work();
        person.hashCode();

        String s = new String("");
    }

    private int work() {
        int x = 3;
        int y = 5;
        int z = (x + y) * 10 - 1;
        return z;

    }
}
