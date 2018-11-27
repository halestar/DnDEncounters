package net.kalinec.dndencounters.dice;

import android.util.Log;

import java.io.Serializable;
import java.util.Random;

public class DiceRoller implements Serializable
{
    protected int numSides, modifier, numDice;
    protected char rollSign;
    protected static final Random random = new Random();

    public DiceRoller(int numSides, int modifier, int numDice, char rollSign) {
        this.numSides = numSides;
        this.modifier = modifier;
        this.numDice = numDice;
        this.rollSign = (rollSign == '-')? '-': '+';
    }

    public DiceRoller(int numSides, int modifier, int numDice) {
        this.numSides = numSides;
        this.modifier = modifier;
        this.numDice = numDice;
        this.rollSign = '+';
    }

    public DiceRoller(int numSides, int numDice) {
        this.numSides = numSides;
        this.numDice = numDice;
    }

    public int getModifier() {
        return modifier;
    }

    public void setModifier(int modifier) {
        this.modifier = modifier;
    }

    public char getRollSign() {
        return rollSign;
    }

    public void setRollSign(char rollSign) {
        this.rollSign = rollSign;
    }

    public int roll()
    {
        Log.d("DiceRoller", "Rolling " + this.toString());
        int roll = 0;
        for(int i = 0; i < numDice; i++)
        {
            int rand = (1 + random.nextInt(numSides));
            Log.d("DiceRoller", "rolled " + Integer.toString(rand));
            roll += rand;
        }
        roll += modifier;
        if(rollSign == '-')
            roll = roll * -1;
        Log.d("DiceRoller", "total roll " + Integer.toString(roll));
        return roll;
    }

    @Override
    public String toString() {
        return (rollSign == '-'? "-": "") + Integer.toString(numDice) + "d" + numSides + (modifier != 0? (modifier < 0? "-" + Integer.toString(modifier): "+" + modifier): "");
    }
}
