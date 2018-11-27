package net.kalinec.dndencounters.dice;

public class DiceParserException extends Exception
{
    private final String token;

    public DiceParserException(String token) {
        super();
        this.token = token;
    }

    @Override
    public String getMessage() {
        return "Choked at token: " + token + ".";
    }
}
