package net.kalinec.dndencounters.dice;

public final class InitiativeRoller extends DiceRoller
{
    public InitiativeRoller(int modifier)
    {
        super(20, 1);
        this.modifier = modifier;
    }
}
