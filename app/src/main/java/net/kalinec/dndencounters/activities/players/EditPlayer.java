package net.kalinec.dndencounters.activities.players;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.db.AppDatabase;
import net.kalinec.dndencounters.db.PlayerDao;
import net.kalinec.dndencounters.players.Player;

import java.io.ByteArrayOutputStream;

public class EditPlayer extends AppCompatActivity
{
	static final int REQUEST_IMAGE_CAPTURE = 1;
	private EditText nameTxt, dciTxt;
	private Bitmap portrait;
	private ImageView portraitContainer;
	protected Player selectedPlayer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		Bundle bundle = getIntent().getExtras();
		selectedPlayer = (Player)bundle.getSerializable(Player.PASSED_PLAYER);
		
		setContentView(R.layout.activity_edit_player);
		
		nameTxt = findViewById(R.id.editHumanNameTextInput);
		nameTxt.setText(selectedPlayer.getName());
		dciTxt = findViewById(R.id.editHumanDciTextInput);
		dciTxt.setText(selectedPlayer.getDci());
		portraitContainer = findViewById(R.id.editHumanPortraitIv);
		if(selectedPlayer.getPortrait() != null)
			portrait = BitmapFactory.decodeByteArray(selectedPlayer.getPortrait(), 0, selectedPlayer.getPortrait().length);
		portraitContainer.setImageBitmap(portrait);
	}
	
	public void updatePlayer(View target)
	{
		PlayerDao playerDao = AppDatabase.getDatabase(getApplicationContext()).playerDao();
		selectedPlayer.setName(nameTxt.getText().toString());
		selectedPlayer.setDci(dciTxt.getText().toString());
		if(portrait != null)
		{
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			portrait.compress(Bitmap.CompressFormat.PNG, 100, stream);
			selectedPlayer.setPortrait(stream.toByteArray());
			portrait.recycle();
		}
		playerDao.update(selectedPlayer);
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
			portrait = (Bitmap) extras.get("data");
			portraitContainer.setImageBitmap(portrait);
		}
	}
}
