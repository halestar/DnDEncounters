package net.kalinec.dndencounters.activities.monster_tokens;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.constraint.Group;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import net.kalinec.dndencounters.DnDEncountersActivity;
import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.monster_tokens.MonsterToken;
import net.kalinec.dndencounters.monster_tokens.MonsterTokens;

import yuku.ambilwarna.AmbilWarnaDialog;

public class AddMonsterToken extends DnDEncountersActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_NEW_MONSTER_TOKEN = 50;
    private Group SingleNumberGroup, SingleColorGroup, ColorAndNumberGroup, MiniGroup;
    private Bitmap miniPortrait;
    private ImageView MiniIv;
    private EditText SingleNumberEt, ColoredNumberEt, addTokenMonsterTokenNameEt;
    private Button SingleColorBt, ColoredNumberBt;
    private int singleColor, coloredNumberColor;
    private int tokenType;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_monster_token);

        //groups
        SingleNumberGroup = findViewById(R.id.SingleNumberGroup);
        SingleColorGroup = findViewById(R.id.SingleColorGroup);
        ColorAndNumberGroup = findViewById(R.id.ColorAndNumberGroup);
        MiniGroup = findViewById(R.id.MiniGroup);

        //radio buttons
	    RadioGroup tokenTypeRg = findViewById(R.id.TokenTypeRg);
	    RadioButton singleNumberRb = findViewById(R.id.singleNumberRb);
        singleNumberRb.setChecked(true);
	    RadioButton singleColorRb = findViewById(R.id.SingleColorRb);
	    singleColorRb.setChecked(false);
	    RadioButton coloredNumberRb = findViewById(R.id.ColoredNumberRb);
	    coloredNumberRb.setChecked(false);
	    RadioButton miniRb = findViewById(R.id.MiniRb);
	    miniRb.setChecked(false);

        setSingleNumberToken();
	
	    tokenTypeRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
	    {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId)
                {
                    case R.id.singleNumberRb: setSingleNumberToken();
                        tokenType = MonsterToken.TOKEN_TYPE_NUMBER;
                        break;
                    case R.id.SingleColorRb: setSingleColorToken();
                        tokenType = MonsterToken.TOKEN_TYPE_COLOR;
                        break;
                    case R.id.ColoredNumberRb: setColoredNumberToken();
                        tokenType = MonsterToken.TOKEN_TYPE_COLORED_NUMBER;
                        break;
                    case R.id.MiniRb: setMiniToken();
                        tokenType = MonsterToken.TOKEN_TYPE_MINI;
                        break;
                }
            }
        });

        //elkements
        MiniIv = findViewById(R.id.MiniIv);
        SingleNumberEt = findViewById(R.id.SingleNumberEt);
        ColoredNumberEt = findViewById(R.id.ColoredNumberEt);
        SingleColorBt = findViewById(R.id.SingleColorBt);
        ColoredNumberBt = findViewById(R.id.ColoredNumberBt);
        addTokenMonsterTokenNameEt = findViewById(R.id.addTokenMonsterTokenNameEt);
        singleColor = coloredNumberColor = Color.WHITE;
        tokenType = MonsterToken.TOKEN_TYPE_NUMBER;
    }

    public void setSingleNumberToken()
    {
        SingleNumberGroup.setVisibility(View.VISIBLE);
        SingleColorGroup.setVisibility(View.GONE);
        ColorAndNumberGroup.setVisibility(View.GONE);
        MiniGroup.setVisibility(View.GONE);
    }

    public void setSingleColorToken()
    {
        SingleNumberGroup.setVisibility(View.GONE);
        SingleColorGroup.setVisibility(View.VISIBLE);
        ColorAndNumberGroup.setVisibility(View.GONE);
        MiniGroup.setVisibility(View.GONE);
    }

    public void setColoredNumberToken()
    {
        SingleNumberGroup.setVisibility(View.GONE);
        SingleColorGroup.setVisibility(View.GONE);
        ColorAndNumberGroup.setVisibility(View.VISIBLE);
        MiniGroup.setVisibility(View.GONE);
    }

    public void setMiniToken()
    {
        SingleNumberGroup.setVisibility(View.GONE);
        SingleColorGroup.setVisibility(View.GONE);
        ColorAndNumberGroup.setVisibility(View.GONE);
        MiniGroup.setVisibility(View.VISIBLE);
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
                singleColor = Color.WHITE;
                SingleColorBt.setBackgroundColor(Color.WHITE);
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
                coloredNumberColor = Color.WHITE;
                ColoredNumberBt.setBackgroundColor(Color.WHITE);
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                coloredNumberColor = color;
                ColoredNumberBt.setBackgroundColor(color);
            }
        });
        dialog.show();
    }

    public void addToken(View v)
    {
        if(addTokenMonsterTokenNameEt.getText().toString() == "")
        {
            Toast errorToast = Toast.makeText(getApplicationContext(), "You must enter a monster token name", Toast.LENGTH_LONG);
            errorToast.show();
            return;
        }
        MonsterToken newToken = new MonsterToken(tokenType);
        switch(tokenType)
        {
            case MonsterToken.TOKEN_TYPE_NUMBER:
                newToken.setTokenNumber(Integer.parseInt(SingleNumberEt.getText().toString()));
                break;
            case MonsterToken.TOKEN_TYPE_COLOR:
                newToken.setTokenColor(singleColor);
                break;
            case MonsterToken.TOKEN_TYPE_COLORED_NUMBER:
                newToken.setTokenNumber(Integer.parseInt(ColoredNumberEt.getText().toString()));
                newToken.setTokenColor(coloredNumberColor);
                break;
            case MonsterToken.TOKEN_TYPE_MINI:
                newToken.setMiniPortrait(miniPortrait);
                break;
        }
        newToken.setTokenName(addTokenMonsterTokenNameEt.getText().toString());
        MonsterTokens.addMonsterToken(getApplicationContext(), newToken);
        Intent data = new Intent();
        data.putExtra(MonsterToken.PASSED_MONSTER_TOKEN, newToken);
        setResult(RESULT_OK, data);
        finish();
    }
}
