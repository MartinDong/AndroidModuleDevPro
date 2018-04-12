package com.dong.module.knowledge.arithmetic;

/**
 * ================================================
 * 从键盘输入一个任意整数,判断当前的数字能否被3整除；
 * 已知条件：一串数字各位数字之和如果可以被3整除那么这串数字一定能被3整除；
 * Created by KotlinD on 2018/4/12.
 * <p>
 * ================================================
 */
public class ExactDivision3 {

    private static boolean calculate(String numberStr) {
        //将输入的字符串数据拆分
        char[] numbers = numberStr.toCharArray();
        //定义求和接收变量
        int sum = 0;
        //循环每一位数据
        for (char number : numbers) {
            //将数据转成int进行求和
            sum += Integer.parseInt(number + "");
        }

        //判断如果当前的数据是否是两位数
        if (sum > 10) {
            //如果是两位数那么就进行递归处理
            return calculate(String.valueOf(sum));
        } else {
            //否则将当前的数字对3求模运算，是否有余数，如果没有表示可以被3整除
            return sum % 3 == 0;
        }
    }


    public static void main(String[] args) {
        String numberStr = "124";

        if (calculate(numberStr)) {
            System.out.print(numberStr + " 可以被3整除");
        } else {
            System.out.print(numberStr + " 不可以被3整除");
        }
    }
}
