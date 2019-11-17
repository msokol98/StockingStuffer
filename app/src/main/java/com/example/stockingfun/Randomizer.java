package com.example.stockingfun;

import java.util.Random;
import java.util.List;

public class Randomizer {

    public int getRandomElement(List<Integer> list)
    {
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }

}
