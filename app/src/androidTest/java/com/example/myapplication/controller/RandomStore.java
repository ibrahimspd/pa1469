package com.example.myapplication.controller;

import java.util.Random;

/***************Animator duration scale -> off***************/

/*class for setting an int to value 0 up to a million, used in testing to generate unique strings*/
public final class RandomStore {
    static int randomNumber;
    public static int getNumber()
    {
        return randomNumber;
    }
    public static void setNumber()
    {
        int min = 0;
        int max = 1000000;
        Random random = new Random();
        randomNumber = random.nextInt(max - min + 1) + min;
    }
}
