package vttp2023.batch3.ssf.frontcontroller.model;

import java.io.Serializable;
import java.security.SecureRandom;

public class Captcha implements Serializable {
    private final int firstNumber;
    private final int secondNumber;
    private final char operator;

    public Captcha() {
        this.firstNumber = this.generateNumber(1, 50);
        this.secondNumber = this.generateNumber(1, 50);
        this.operator = this.generateOperator();

    }

    private int generateNumber(int origin, int bound) {
        SecureRandom rand = new SecureRandom();
        return rand.nextInt(origin, bound);
    }

    private char generateOperator() {
        char[] operators = new char[]{'+', '-', '*', '/'};
        SecureRandom rand = new SecureRandom();
        return operators[rand.nextInt(0, operators.length - 1)];
    }

    public static boolean validateCaptcha(String captchaAnswer, Captcha captcha) {
        if (captchaAnswer == null || captcha == null) {
            return false;
        }

        int answer = switch (captcha.getOperator()) {
            case '+' -> captcha.getFirstNumber() + captcha.getSecondNumber();
            case '-' -> captcha.getFirstNumber() - captcha.getSecondNumber();
            case '*' -> captcha.getFirstNumber() * captcha.getSecondNumber();
            case '/' -> captcha.getFirstNumber() / captcha.getSecondNumber();
            default -> -1;
        };

        return Integer.parseInt(captchaAnswer) == answer;
    }

    public int getFirstNumber() {
        return firstNumber;
    }

    public int getSecondNumber() {
        return secondNumber;
    }

    public char getOperator() {
        return operator;
    }

    @Override
    public String toString() {
        return "Captcha{" +
                "firstNumber=" + firstNumber +
                ", secondNumber=" + secondNumber +
                ", operator=" + operator +
                '}';
    }
}
