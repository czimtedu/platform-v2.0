/*
 * Copyright &copy; <a href="https://www.zlgx.com">zlgx</a> All rights reserved.
 */

package com.platform.framework.test.domain;

/**
 * 描述：Java编码格式示例
 * 类名：CodeRule
 * 作者：lufengc
 * 日期：2015/12/24 15:02
 */
public class CodeRule {

    //声明变量，等号两边有空格。
    private static int i = 1;

    //方法声明，右括号和左大括号中间有空格。
    public static void main(String[] args) {
        //if语句，比较连接符(>)左右有空格，小括号和大括号中间有空格。
        //if 与 左括号中间有空格
        if (i > 0) {
            System.out.println(i);
        }
        //两个条件的连接(&&)，左右有空格。
        if (i > 0 && i < 2) {
            System.out.println(i);
        }

        //if..else 语句
        //参考JDK，else跟大括号，前后都有空格
        if (i > 0 && i < 2) {
            System.out.println(i);
        } else if (i > 2) {
            System.out.println(i + 1);
        } else {
            System.out.println(i);
        }

        //while语句，与if语句类型，while与括号中间有空格，括号内格式与if相同
        while (i > 0 && i < 2) {
            System.out.println(i);
            i++;
        }

        //for语句
        //分号后带空格，每个子语句中，连接符左右都带空格。
        //for与括号中间带空格，大小括号中间带空格。
        for (int j = 0; j < 10; j++) {
            System.out.println(i);
        }

        //+-*/，格式，四则运算符号前后有空格。
        //在JDK的有些代码里，在方法调用的参传递或在判断语句中存在的四则运算中，四则运算符号前后无空格。
        //为了不造成困扰和混淆，个人为均保留空格。
        int a = 1 + 2;
        int b = 1 - 2;
        int c = 2 * 2;
        int d = 1 / 2;

        //三元表达式格式，每个符号中间均有空格
        int j = i > 2 ? 1 : -1;

        //方法声明和调用，用逗号分隔的参数，逗号后有空格。
        sum(a, b);
        sum(c + d, j);
    }

    //方法声明，多个参数，逗号后有空格
    private static int sum(int i, int j) {
        return i + j;
    }

}
