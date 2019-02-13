package net.kalinec.dndencounters.activities.players;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import net.kalinec.dndencounters.DnDEncountersActivity;
import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.characters.Character;
import net.kalinec.dndencounters.players.Player;
import net.kalinec.dndencounters.players.Players;

public class CreatePlayerAndCharacter extends DnDEncountersActivity {

    public static final int REQUEST_NEW_CHARACTER = 41;
    private EditText addPPCNameEt, addPPCDciEt, addPPCPcNameTxt, addPPCPcClassTxt, addPPCPcRaceTxt, addPPCPAcTxt, addPPCPcHpTxt, addPPCPcPpTxt, addPPCPcLvTxt,addPPCPcDcTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_player_and_character);
        addPPCNameEt = findViewById(R.id.addPPCNameEt);
        addPPCDciEt = findViewById(R.id.addPPCDciEt);
        addPPCPcNameTxt = findViewById(R.id.addPPCPcNameTxt);
        addPPCPcClassTxt = findViewById(R.id.addPPCPcClassTxt);
        addPPCPcRaceTxt = findViewById(R.id.addPPCPcRaceTxt);
        addPPCPAcTxt = findViewById(R.id.addPPCPAcTxt);
        addPPCPcHpTxt = findViewById(R.id.addPPCPcHpTxt);
        addPPCPcPpTxt = findViewById(R.id.addPPCPcPpTxt);
        addPPCPcLvTxt = findViewById(R.id.addPPCPcLvTxt);
        addPPCPcDcTxt = findViewById(R.id.addPPCPcDcTxt);
    }

    public void createPPc(View v)
    {
        Player newPlayer = new Player(addPPCNameEt.getText().toString());
        newPlayer.setDci(addPPCDciEt.getText().toString());
        //make the character.
        String pcName = addPPCPcNameTxt.getText().toString();
        String pcClass = addPPCPcClassTxt.getText().toString();
        String pcRace = addPPCPcRaceTxt.getText().toString();
        int pcAc = Integer.parseInt(addPPCPAcTxt.getText().toString());
        int pcHp = Integer.parseInt(addPPCPcHpTxt.getText().toString());
        int pcPp = Integer.parseInt(addPPCPcPpTxt.getText().toString());
        int pcLv = Integer.parseInt(addPPCPcLvTxt.getText().toString());
        int pcDc;
        try {
            pcDc = Integer.parseInt(addPPCPcDcTxt.getText().toString());
        }
        catch(NumberFormatException e)
        {
            pcDc = 0;
        }
        Character newPc = new Character(pcName, pcAc, pcHp, pcPp, pcClass, pcLv);
        newPc.setCharacterRace(pcRace);
        newPc.setSpellDc(pcDc);
        newPlayer.getPcs().add(newPc);
	    Players.addPlayer(getApplicationContext(), newPlayer);

        Intent data = new Intent();
        data.putExtra(Player.PASSED_PLAYER, newPlayer);
        data.putExtra(Character.PASSED_CHARACTER, newPc);
        setResult(RESULT_OK, data);
        finish();
    }
}
