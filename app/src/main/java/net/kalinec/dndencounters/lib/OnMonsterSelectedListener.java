package net.kalinec.dndencounters.lib;

import net.kalinec.dndencounters.monsters.Monster;

import java.io.Serializable;

public interface OnMonsterSelectedListener extends Serializable
{
    void onMonsterSelected(Monster monster);
}
