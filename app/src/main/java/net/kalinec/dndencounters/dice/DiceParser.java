package net.kalinec.dndencounters.dice;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiceParser implements Serializable
{
    private String diceStr;
    private ArrayList<DiceRoller> dice;

    private void parseDice() throws DiceParserException {
        StringTokenizer diceTokenizer = new StringTokenizer(diceStr, "+-", true);
        DiceRoller currentDice = null;
        char currentSign = '+';
	    int currentMod;
        while(diceTokenizer.hasMoreTokens())
        {
            String token = diceTokenizer.nextToken();
            //3 choices: #?d#, #, +|-
	        if (token.matches("[+\\-]"))
            {
                //set the correct sign
                currentSign = token.charAt(0);
            }
            else if(token.matches("\\d+"))
            {
                currentMod = Integer.parseInt(token);
                if(currentSign == '-')
                    currentMod *= -1;
                if(currentDice != null)
                {
                    currentDice.setModifier(currentMod);
                    dice.add(currentDice);
                    currentDice = null;
                }
                currentSign = '+';
            }
	        else if (token.matches("\\d*([dD])\\d+"))
            {
                StringTokenizer diceExpr = new StringTokenizer(token, "dD");
                if(diceExpr.countTokens() == 2)
                {
                    if(currentDice != null)
                    {
                        dice.add(currentDice);
                    }
                    int numDice = Integer.parseInt(diceExpr.nextToken());
                    int numFaces = Integer.parseInt(diceExpr.nextToken());
                    currentDice = new DiceRoller(numFaces, numDice);
                    currentDice.setRollSign(currentSign);
                }
                else
                    throw new DiceParserException(token);
            }
        }
        if(currentDice != null)
            dice.add(currentDice);
    }

    public DiceParser(String roll)
    {
        diceStr = roll;
        dice = new ArrayList<>();
        try
        {
            parseDice();
        }
        catch (DiceParserException e)
        {
            Log.d("DiceParser", e.getMessage());
        }
    }

    public int result()
    {
        int result = 0;
        Log.d("DiceParser", "Rolling " + dice);
        for(DiceRoller d: dice)
            result += d.roll();
        return result;
    }

    public String getDiceStr() {
        return diceStr;
    }
    
    public static List<DiceParser> parseString(String str)
    {
        ArrayList<DiceParser> tokens = new ArrayList<>();
        Pattern diceRegEx = Pattern.compile("\\b(\\d*[dD](4|6|8|10|12|20)(\\s*[+|-]\\s*\\d)?)\\b");
        Matcher diceMatches = diceRegEx.matcher(str);
        while(diceMatches.find())
        {
            String diceStr = diceMatches.group(1).replace(" ", "");
            Log.d("DiceParser.List","found " + diceStr);
            tokens.add(new DiceParser(diceStr));
        }
        return tokens;
    }
}
