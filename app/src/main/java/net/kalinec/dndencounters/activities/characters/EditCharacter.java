package net.kalinec.dndencounters.activities.characters;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import net.kalinec.dndencounters.DnDEncountersActivity;
import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.characters.Character;
import net.kalinec.dndencounters.db.AppDatabase;
import net.kalinec.dndencounters.db.CharacterDao;

import java.util.Locale;

public class EditCharacter extends DnDEncountersActivity
{
	private Character selectedCharacter;
	private EditText characterNameTxt, characterRaceTxt, characterClassTxt,
			characterAcTxt, characterHpTxt, characterPpTxt,
			characterLvTxt, characterDcTxt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		Bundle bundle = getIntent().getExtras();
		assert bundle != null;
		selectedCharacter = (Character)bundle.getSerializable(Character.PASSED_CHARACTER);
		
		setContentView(R.layout.activity_edit_character);
		
		characterNameTxt = findViewById(R.id.characterNameTxt);
		characterNameTxt.setText(selectedCharacter.getName());
		characterClassTxt = findViewById(R.id.characterClassTxt);
		characterClassTxt.setText(selectedCharacter.getCharacterClass());
		characterRaceTxt = findViewById(R.id.characterRaceTxt);
		characterRaceTxt.setText(selectedCharacter.getCharacterRace());
		
		
		characterAcTxt = findViewById(R.id.characterAcTxt);
		characterAcTxt.setText(String.format(Locale.getDefault(), "%d", selectedCharacter.getAc()));
		characterHpTxt = findViewById(R.id.characterHpTxt);
		characterHpTxt.setText(String.format(Locale.getDefault(), "%d", selectedCharacter.getHp()));
		characterPpTxt = findViewById(R.id.characterPpTxt);
		characterPpTxt.setText(String.format(Locale.getDefault(), "%d", selectedCharacter.getPp()));
		
		
		characterLvTxt = findViewById(R.id.characterLvTxt);
		characterLvTxt.setText(String.format(Locale.getDefault(), "%d", selectedCharacter
				.getLevel()));
		characterDcTxt = findViewById(R.id.characterDcTxt);
		characterDcTxt.setText(String.format(Locale.getDefault(), "%d", selectedCharacter
				.getSpellDc()));
	}
	
	public void updatePC(View target)
	{
		CharacterDao characterDao = AppDatabase.getDatabase(getApplicationContext()).characterDao();
		selectedCharacter.setName(characterNameTxt.getText().toString());
		selectedCharacter.setCharacterClass(characterClassTxt.getText().toString());
		selectedCharacter.setCharacterRace(characterRaceTxt.getText().toString());
		
		selectedCharacter.setAc(Integer.parseInt(characterAcTxt.getText().toString()));
		selectedCharacter.setHp(Integer.parseInt(characterHpTxt.getText().toString()));
		selectedCharacter.setPp(Integer.parseInt(characterPpTxt.getText().toString()));
		
		selectedCharacter.setLevel(Integer.parseInt(characterLvTxt.getText().toString()));
		selectedCharacter.setSpellDc(Integer.parseInt(characterDcTxt.getText().toString()));
		
		characterDao.update(selectedCharacter);
		finish();
	}
	
	
	
	public void confirmDeletePc(View target)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(EditCharacter.this);
		builder.setMessage(R.string.ConfirmPcDelete)
				.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						deletePc();
					}
				})
				.setNegativeButton(R.string.No, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.cancel();
					}
				})
				.show();
	}
	
	public void deletePc()
	{
		CharacterDao characterDao = AppDatabase.getDatabase(getApplicationContext()).characterDao();
		characterDao.delete(selectedCharacter);
		finish();
	}
}
