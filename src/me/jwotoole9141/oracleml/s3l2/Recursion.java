/*
 * AUTH: Jared O'Toole
 * DATE: Tue, Oct 15th, 2019
 * PROJ: OracleMachineLearning
 * FILE: Recursion.java
 *
 * Recursion task for Section 3 Lesson 2 of the
 * AI with ML in Java Oracle iLearning Course.
 *
 * Create recursive methods to calculate the following:
 * - Factorial: a!
 * - Fibonacci Numbers: F(n) = F(n-1) + F(n-2) with seed values F(0)=0 and F(1)=1
 *
 */

package me.jwotoole9141.oracleml.s3l2;

public class Recursion {

    /**
     * Test the factorial and fibonacci functions.
     */
    public static void main(String[] args) {
        System.out.printf("4! = %s%n", factorial(4));  // 24
        System.out.printf("12! = %s%n", factorial(12));  // 479001600
        System.out.printf("0! = %s%n", factorial(0));  // 1
        System.out.printf("F(2) = %s%n", fibonacci(2));  // 3
        System.out.printf("F(8) = %s%n", fibonacci(8));  // 133
        System.out.printf("F(35) = %s%n", fibonacci(35));  // 63245948
        System.out.printf("F(1) = %s%n", fibonacci(1));  // 1
        System.out.printf("F(0) = %s%n", fibonacci(0));  // 0
    }

    /**
     * Compute the factorial of a, recursively.
     *
     * @param a a non-negative integer
     * @return the result of a!
     */
    public static int factorial(int a) {
        // EXCEPTION: a is less than zero
        // BASE CASE: a is 0 or 1
        // RECURSION: a is greater than 1
        if (a < 0) { throw new IllegalArgumentException("invalid domain"); }
        return ((a > 1) ? (a * factorial(a - 1)) : 1);
    }

    /**
     * Compute the nth fibonacci number, recursively.
     *
     * @param n a non-negative integer
     * @return the result of F(n)
     */
    public static int fibonacci(int n) {
        // EXCEPTION: n is less than zero
        // BASE CASE: n is 0 or 1
        // RECURSION: n is greater than 1
        if (n < 0) { throw new IllegalArgumentException("invalid domain"); }
        return ((n > 1) ? (n + fibonacci(n - 1) + fibonacci(n - 2)) : n);
        /*
        // switch statements are intuitive for several base cases...
        switch (n) {
            case 0: return 0;
            case 1: return 1;
            default: return n + fibonacci(n - 1) + fibonacci(n - 2);
        }
        */
    }
}
