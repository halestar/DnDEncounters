package net.kalinec.dndencounters.dice;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.Serializable;
import java.util.Random;

public class DiceRoller implements Serializable
{
	protected int modifier;
	private int numSides, numDice;
	private char rollSign;
    protected static final Random random = new Random();
	
	DiceRoller(int numSides, int numDice)
	{
        this.numSides = numSides;
        this.numDice = numDice;
    }

    public int getModifier() {
        return modifier;
    }

    public void setModifier(int modifier) {
        this.modifier = modifier;
    }
	
	void setRollSign(char rollSign)
	{
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
	
	@NonNull
    @Override
    public String toString() {
        return (rollSign == '-'? "-": "") + Integer.toString(numDice) + "d" + numSides + (modifier != 0? (modifier < 0? "-" + Integer.toString(modifier): "+" + modifier): "");
    }
}
