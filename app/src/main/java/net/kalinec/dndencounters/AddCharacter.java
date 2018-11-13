package net.kalinec.dndencounters;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import net.kalinec.dndencounters.characters.Character;
import net.kalinec.dndencounters.db.AppDatabase;
import net.kalinec.dndencounters.db.CharacterDao;
import net.kalinec.dndencounters.db.PlayerDao;
import net.kalinec.dndencounters.players.Player;

public class AddCharacter extends AppCompatActivity
{
	protected Player owner;
	private EditText characterNameTxt, characterRaceTxt, characterClassTxt,
					characterAcTxt, characterHpTxt, characterPpTxt,
					characterLvTxt, characterDcTxt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		Bundle bundle = getIntent().getExtras();
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
		CharacterDao characterDao = AppDatabase.getDatabase(getApplicationContext()).characterDao();
		Character newPc = new Character(owner);
		newPc.setName(characterNameTxt.getText().toString());
		newPc.setCharacterClass(characterClassTxt.getText().toString());
		newPc.setCharacterRace(characterRaceTxt.getText().toString());
		
		newPc.setAc(Integer.parseInt(characterAcTxt.getText().toString()));
		newPc.setHp(Integer.parseInt(characterHpTxt.getText().toString()));
		newPc.setPp(Integer.parseInt(characterPpTxt.getText().toString()));
		
		newPc.setLevel(Integer.parseInt(characterLvTxt.getText().toString()));
		newPc.setSpellDc(Integer.parseInt(characterDcTxt.getText().toString()));
		
		characterDao.insert(newPc);
		finish();
	}
}
