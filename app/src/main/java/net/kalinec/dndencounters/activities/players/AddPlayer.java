package net.kalinec.dndencounters.activities.players;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import net.kalinec.dndencounters.DnDEncountersActivity;
import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.db.AppDatabase;
import net.kalinec.dndencounters.db.PlayerDao;
import net.kalinec.dndencounters.players.Player;

import java.io.ByteArrayOutputStream;

public class AddPlayer extends DnDEncountersActivity
{
	static final int REQUEST_IMAGE_CAPTURE = 1;
	private EditText nameTxt, dciTxt;
	private Bitmap portrait;
	private ImageView portraitContainer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_player);
		nameTxt = findViewById(R.id.addHumanNameTextInput);
		dciTxt = findViewById(R.id.addHumanDciTextInput);
		portraitContainer = findViewById(R.id.addHumanPortraitIv);
		
	}
	
	public void addPlayer(View target)
	{
		String playerName = nameTxt.getText().toString();
		PlayerDao playerDao = AppDatabase.getDatabase(getApplicationContext()).playerDao();
		Player newPlayer = new Player(playerName);
		newPlayer.setDci(dciTxt.getText().toString());
		if(portrait != null)
		{
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			portrait.compress(Bitmap.CompressFormat.PNG, 100, stream);
			newPlayer.setPortrait(stream.toByteArray());
			portrait.recycle();
		}
		playerDao.insert(newPlayer);
		finish();
	}
	
	public void dispatchTakePictureIntent(View target)
	{
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent.resolveActivity(getPackageManager()) != null)
		{
			startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
		{
			Bundle extras = data.getExtras();
			assert extras != null;
			portrait = (Bitmap) extras.get("data");
			portraitContainer.setImageBitmap(portrait);
		}
	}
}
