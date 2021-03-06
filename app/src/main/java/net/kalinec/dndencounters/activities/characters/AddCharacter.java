package net.kalinec.dndencounters.activities.characters;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import net.kalinec.dndencounters.DnDEncountersActivity;
import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.characters.Character;
import net.kalinec.dndencounters.players.Player;
import net.kalinec.dndencounters.players.Players;

public class AddCharacter extends DnDEncountersActivity
{

	public static final int REQUEST_NEW_CHARACTER = 43;
	protected Player owner;
	private EditText characterNameTxt, characterRaceTxt, characterClassTxt,
					characterAcTxt, characterHpTxt, characterPpTxt,
					characterLvTxt, characterDcTxt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		Bundle bundle = getIntent().getExtras();
		assert bundle != null;
		owner = (Player)bundle.getSerializable(Player.PASSED_PLAYER);
		
		setContentView(R.layout.activity_add_character);
		characterNameTxt = findViewById(R.id.characterNameTxt);
		characterClassTxt = findViewById(R.id.characterClassTxt);
		characterRaceTxt = findViewById(R.id.characterRaceTxt);
		
		
		characterAcTxt = findViewById(R.id.characterAcTxt);
		characterHpTxt = findViewById(R.id.characterHpTxt);
		characterPpTxt = findViewById(R.id.characterPpTxt);
		
		
		characterLvTxt = findViewById(R.id.characterLvTxt);
		characterDcTxt = findViewById(R.id.characterDcTxt);
	}
	
	public void addPC(View target)
	{
		Character newPc = new Character();
		newPc.setName(characterNameTxt.getText().toString());
		newPc.setCharacterClass(characterClassTxt.getText().toString());
		newPc.setCharacterRace(characterRaceTxt.getText().toString());
		
		newPc.setAc(Integer.parseInt(characterAcTxt.getText().toString()));
		newPc.setHp(Integer.parseInt(characterHpTxt.getText().toString()));
		newPc.setPp(Integer.parseInt(characterPpTxt.getText().toString()));
		
		newPc.setLevel(Integer.parseInt(characterLvTxt.getText().toString()));
		try {
			newPc.setSpellDc(Integer.parseInt(characterDcTxt.getText().toString()));
		}
		catch(NumberFormatException e)
		{
			newPc.setSpellDc(0);
		}
		
		owner.getPcs().add(newPc);
		Players.updatePlayer(getApplicationContext(), owner);
		Intent data = new Intent();
		data.putExtra(Character.PASSED_CHARACTER, newPc);
		setResult(RESULT_OK, data);
		finish();
	}
}
