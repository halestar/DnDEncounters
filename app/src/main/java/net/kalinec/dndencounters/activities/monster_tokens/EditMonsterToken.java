package net.kalinec.dndencounters.activities.monster_tokens;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.constraint.Group;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import net.kalinec.dndencounters.DnDEncountersActivity;
import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.monster_tokens.MonsterToken;
import net.kalinec.dndencounters.monster_tokens.MonsterTokens;
import net.kalinec.dndencounters.monsters.Monster;

import java.util.Locale;

import yuku.ambilwarna.AmbilWarnaDialog;

public class EditMonsterToken extends DnDEncountersActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_NEW_MONSTER_TOKEN = 50;
    private Group SingleNumberGroup, SingleColorGroup, ColorAndNumberGroup, MiniGroup;
    private Bitmap miniPortrait;
    private ImageView MiniIv;
    private EditText SingleNumberEt, ColoredNumberEt, addTokenMonsterTokenNameEt;
    private Button SingleColorBt, ColoredNumberBt;
    private int singleColor, coloredNumberColor;
    private MonsterToken selectedToken;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
	    assert bundle != null;
        selectedToken = (MonsterToken) bundle.getSerializable(MonsterToken.PASSED_MONSTER_TOKEN);
        if(selectedToken.getTokenType() == MonsterToken.TOKEN_TYPE_MINI)
            miniPortrait = selectedToken.getMiniPortrait();

        Log.d("ViewMonsterTokens", "received: " + selectedToken);
        setContentView(R.layout.activity_edit_monster_token);
        //groups
        SingleNumberGroup = findViewById(R.id.SingleNumberGroup);
        SingleColorGroup = findViewById(R.id.SingleColorGroup);
        ColorAndNumberGroup = findViewById(R.id.ColorAndNumberGroup);
        MiniGroup = findViewById(R.id.MiniGroup);


        //elkements
        MiniIv = findViewById(R.id.MiniIv);
        SingleNumberEt = findViewById(R.id.SingleNumberEt);
        ColoredNumberEt = findViewById(R.id.ColoredNumberEt);
        SingleColorBt = findViewById(R.id.SingleColorBt);
        ColoredNumberBt = findViewById(R.id.ColoredNumberBt);
        addTokenMonsterTokenNameEt = findViewById(R.id.addTokenMonsterTokenNameEt);
        singleColor = coloredNumberColor = Color.WHITE;

        addTokenMonsterTokenNameEt.setText(selectedToken.getTokenName());

        if(selectedToken.getTokenType() == MonsterToken.TOKEN_TYPE_NUMBER)
            setSingleNumberToken();
        else if(selectedToken.getTokenType() == MonsterToken.TOKEN_TYPE_COLOR)
            setSingleColorToken();
        else if(selectedToken.getTokenType() == MonsterToken.TOKEN_TYPE_COLORED_NUMBER)
            setColoredNumberToken();
        else if(selectedToken.getTokenType() == MonsterToken.TOKEN_TYPE_MINI)
            setMiniToken();

    }

    public void setSingleNumberToken()
    {
        SingleNumberGroup.setVisibility(View.VISIBLE);
	    SingleNumberEt.setText(String.format(Locale.getDefault(), "%d", selectedToken
			    .getTokenNumber()));
        SingleColorGroup.setVisibility(View.GONE);
        ColorAndNumberGroup.setVisibility(View.GONE);
        MiniGroup.setVisibility(View.GONE);
    }

    public void setSingleColorToken()
    {
        SingleNumberGroup.setVisibility(View.GONE);
        SingleColorGroup.setVisibility(View.VISIBLE);
        SingleColorBt.setBackgroundColor(selectedToken.getTokenColor());
        singleColor = selectedToken.getTokenColor();
        ColorAndNumberGroup.setVisibility(View.GONE);
        MiniGroup.setVisibility(View.GONE);
    }

    public void setColoredNumberToken()
    {
        SingleNumberGroup.setVisibility(View.GONE);
        SingleColorGroup.setVisibility(View.GONE);
        ColorAndNumberGroup.setVisibility(View.VISIBLE);
	    ColoredNumberEt.setText(String.format(Locale.getDefault(), "%d", selectedToken
			    .getTokenNumber()));
        ColoredNumberBt.setBackgroundColor(selectedToken.getTokenColor());
        coloredNumberColor = selectedToken.getTokenColor();
        MiniGroup.setVisibility(View.GONE);
    }

    public void setMiniToken()
    {
        SingleNumberGroup.setVisibility(View.GONE);
        SingleColorGroup.setVisibility(View.GONE);
        ColorAndNumberGroup.setVisibility(View.GONE);
        MiniGroup.setVisibility(View.VISIBLE);
        MiniIv.setImageBitmap(selectedToken.getMiniPortrait());
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
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
	        assert extras != null;
            miniPortrait = (Bitmap) extras.get("data");
            MiniIv.setImageBitmap(miniPortrait);
        }
    }

    public void pickColorTokenColor(View v)
    {
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, Color.RED, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                singleColor = selectedToken.getTokenColor();
                SingleColorBt.setBackgroundColor(singleColor);
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                singleColor = color;
                SingleColorBt.setBackgroundColor(color);
            }
        });
        dialog.show();
    }

    public void pickColoredNumberTokenColor(View v)
    {
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, Color.RED, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                coloredNumberColor = selectedToken.getTokenColor();
                ColoredNumberBt.setBackgroundColor(coloredNumberColor);
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                coloredNumberColor = color;
                ColoredNumberBt.setBackgroundColor(color);
            }
        });
        dialog.show();
    }

    public void updateMonsterToken(View v)
    {
        if(addTokenMonsterTokenNameEt.getText().toString() == "")
        {
            Toast errorToast = Toast.makeText(getApplicationContext(), "You must enter a monster token name", Toast.LENGTH_LONG);
            errorToast.show();
            return;
        }
        selectedToken.setTokenType(selectedToken.getTokenType());
        selectedToken.setTokenName(addTokenMonsterTokenNameEt.getText().toString());
        switch(selectedToken.getTokenType())
        {
            case MonsterToken.TOKEN_TYPE_NUMBER:
                selectedToken.setTokenNumber(Integer.parseInt(SingleNumberEt.getText().toString()));
                break;
            case MonsterToken.TOKEN_TYPE_COLOR:
                selectedToken.setTokenColor(singleColor);
                break;
            case MonsterToken.TOKEN_TYPE_COLORED_NUMBER:
                selectedToken.setTokenNumber(Integer.parseInt(ColoredNumberEt.getText().toString()));
                selectedToken.setTokenColor(coloredNumberColor);
                break;
            case MonsterToken.TOKEN_TYPE_MINI:
                selectedToken.setMiniPortrait(miniPortrait);
                break;
        }
        MonsterTokens.updateMonsterToken(getApplicationContext(), selectedToken);
        Intent data = new Intent();
        data.putExtra(MonsterToken.PASSED_MONSTER_TOKEN, selectedToken);
        setResult(RESULT_OK, data);
        finish();
    }

    public void deleteMonsterToken(View v)
    {
        MonsterTokens.removeMonsterToken(getApplicationContext(), selectedToken);
        Intent data = new Intent();
        data.putExtra(MonsterToken.PASSED_MONSTER_TOKEN, selectedToken);
        setResult(RESULT_OK, data);
        finish();
    }
}
