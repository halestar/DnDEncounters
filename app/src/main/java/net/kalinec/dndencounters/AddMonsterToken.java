package net.kalinec.dndencounters;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.Group;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class AddMonsterToken extends AppCompatActivity {

    private ConstraintLayout SingleNumberLy, SingleColorLy, ColorAndNumberLy, MiniLy;
    private RadioGroup TokenTypeRg;
    private RadioButton singleNumberRb, SingleColorRb, ColoredNumberRb, MiniRb;
    private Group SingleNumberGroup, SingleColorGroup, ColorAndNumberGroup, MiniGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_monster_token);

        //constraint layouts
        SingleNumberLy = findViewById(R.id.SingleNumberLy);
        SingleColorLy = findViewById(R.id.SingleNumberLy);
        ColorAndNumberLy = findViewById(R.id.SingleNumberLy);
        MiniLy = findViewById(R.id.SingleNumberLy);

        //groups
        SingleNumberGroup = findViewById(R.id.SingleNumberGroup);
        SingleColorGroup = findViewById(R.id.SingleColorGroup);
        ColorAndNumberGroup = findViewById(R.id.ColorAndNumberGroup);
        MiniGroup = findViewById(R.id.MiniGroup);

        //radio buttons
        TokenTypeRg = findViewById(R.id.TokenTypeRg);
        singleNumberRb = findViewById(R.id.singleNumberRb);
        singleNumberRb.setChecked(true);
        SingleColorRb = findViewById(R.id.SingleColorRb);
        SingleColorRb.setChecked(false);
        ColoredNumberRb = findViewById(R.id.ColoredNumberRb);
        ColoredNumberRb.setChecked(false);
        MiniRb = findViewById(R.id.MiniRb);
        MiniRb.setChecked(false);

        setSingleNumberToken();

        TokenTypeRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId)
                {
                    case R.id.singleNumberRb: setSingleNumberToken(); break;
                    case R.id.SingleColorRb: setSingleColorToken(); break;
                    case R.id.ColoredNumberRb: setColoredNumberToken(); break;
                    case R.id.MiniRb: setMiniToken(); break;
                }
            }
        });
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
}
