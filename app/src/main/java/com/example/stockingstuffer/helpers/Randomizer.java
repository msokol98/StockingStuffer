package com.example.stockingstuffer.helpers;

import java.util.List;
import java.util.Random;

public class Randomizer {

    public int getRandomElement(List<Integer> list)
    {
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }

}
