package net.kalinec.dndencounters.activities.custom_monsters;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.kalinec.dndencounters.DnDEncountersActivity;
import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.custom_monsters.CustomMonster;
import net.kalinec.dndencounters.custom_monsters.CustomMonsters;
import net.kalinec.dndencounters.dice.DiceParser;
import net.kalinec.dndencounters.dice.DiceRollerDialog;

public class AddCustomMonster extends AppCompatActivity
{
    public static final int REQUEST_NEW_MONSTER = 101;
    private TextView HitDiceDisplayTv;
    private EditText CustomMonsterNameEt, CustomMonsterTypeEt, CustomMonsterSizeEt, CustomMonsterStrEt, CustomMonsterDexEt, CustomMonsterConEt,
            CustomMonsterWisEt, CustomMonsterIntEt, CustomMonsterChaEt, CustomMonsterHpEt, CustomMonsterAcEt, CustomMonsterCr;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_custom_monster);
        HitDiceDisplayTv = findViewById(R.id.HitDiceDisplayTv);
        CustomMonsterNameEt = findViewById(R.id.CustomMonsterNameEt);
        CustomMonsterTypeEt = findViewById(R.id.CustomMonsterTypeEt);
        CustomMonsterSizeEt = findViewById(R.id.CustomMonsterSizeEt);
        CustomMonsterStrEt = findViewById(R.id.CustomMonsterStrEt);
        CustomMonsterDexEt = findViewById(R.id.CustomMonsterDexEt);
        CustomMonsterConEt = findViewById(R.id.CustomMonsterConEt);
        CustomMonsterWisEt = findViewById(R.id.CustomMonsterWisEt);
        CustomMonsterIntEt = findViewById(R.id.CustomMonsterIntEt);
        CustomMonsterChaEt = findViewById(R.id.CustomMonsterChaEt);
        CustomMonsterHpEt = findViewById(R.id.CustomMonsterHpEt);
        CustomMonsterAcEt = findViewById(R.id.CustomMonsterAcEt);
        CustomMonsterCr = findViewById(R.id.CustomMonsterCr);
    }

    public void enterHitDice(View v)
    {
        Intent myIntent = new Intent(AddCustomMonster.this, DiceRollerDialog.class);
        startActivityForResult(myIntent, DiceRollerDialog.REQUEST_DICE_ROLL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if(requestCode == DiceRollerDialog.REQUEST_DICE_ROLL)
        {
            if(resultCode == RESULT_OK)
            {
                String rollResult = data.getStringExtra(DiceRollerDialog.PASSED_ROLL);
                HitDiceDisplayTv.setText(rollResult);
            }
        }
    }

    public void AddMonster(View v)
    {
        if(CustomMonsterNameEt.getText().toString() == "")
        {
            Toast errorToast = Toast.makeText(getApplicationContext(), "You must enter a monster name", Toast.LENGTH_LONG);
            errorToast.show();
            return;
        }
        if(CustomMonsterTypeEt.getText().toString() == "")
        {
            Toast errorToast = Toast.makeText(getApplicationContext(), "You must enter a monster type", Toast.LENGTH_LONG);
            errorToast.show();
            return;
        }
        if(CustomMonsterSizeEt.getText().toString() == "")
        {
            Toast errorToast = Toast.makeText(getApplicationContext(), "You must enter a monster size", Toast.LENGTH_LONG);
            errorToast.show();
            return;
        }
        if(CustomMonsterStrEt.getText().toString() == "")
        {
            Toast errorToast = Toast.makeText(getApplicationContext(), "You must enter a monster strength", Toast.LENGTH_LONG);
            errorToast.show();
            return;
        }
        if(CustomMonsterDexEt.getText().toString() == "")
        {
            Toast errorToast = Toast.makeText(getApplicationContext(), "You must enter a monster dexterity", Toast.LENGTH_LONG);
            errorToast.show();
            return;
        }
        if(CustomMonsterConEt.getText().toString() == "")
        {
            Toast errorToast = Toast.makeText(getApplicationContext(), "You must enter a monster constitution", Toast.LENGTH_LONG);
            errorToast.show();
            return;
        }
        if(CustomMonsterWisEt.getText().toString() == "")
        {
            Toast errorToast = Toast.makeText(getApplicationContext(), "You must enter a monster wisdom", Toast.LENGTH_LONG);
            errorToast.show();
            return;
        }
        if(CustomMonsterIntEt.getText().toString() == "")
        {
            Toast errorToast = Toast.makeText(getApplicationContext(), "You must enter a monster intelligence", Toast.LENGTH_LONG);
            errorToast.show();
            return;
        }
        if(CustomMonsterChaEt.getText().toString() == "")
        {
            Toast errorToast = Toast.makeText(getApplicationContext(), "You must enter a monster charisma", Toast.LENGTH_LONG);
            errorToast.show();
            return;
        }
        if(CustomMonsterHpEt.getText().toString() == "")
        {
            Toast errorToast = Toast.makeText(getApplicationContext(), "You must enter a monster hit points", Toast.LENGTH_LONG);
            errorToast.show();
            return;
        }
        if(CustomMonsterAcEt.getText().toString() == "")
        {
            Toast errorToast = Toast.makeText(getApplicationContext(), "You must enter a monster armor class", Toast.LENGTH_LONG);
            errorToast.show();
            return;
        }
        if(CustomMonsterCr.getText().toString() == "")
        {
            Toast errorToast = Toast.makeText(getApplicationContext(), "You must enter a monster challenge rating", Toast.LENGTH_LONG);
            errorToast.show();
            return;
        }
        CustomMonster newMonster = new CustomMonster(CustomMonsterNameEt.getText().toString());
        newMonster.setMonsterType(CustomMonsterTypeEt.getText().toString());
        newMonster.setMonsterSize(CustomMonsterSizeEt.getText().toString());
        newMonster.setStr(Integer.parseInt(CustomMonsterStrEt.getText().toString()));
        newMonster.setDex(Integer.parseInt(CustomMonsterDexEt.getText().toString()));
        newMonster.setCon(Integer.parseInt(CustomMonsterConEt.getText().toString()));
        newMonster.setWis(Integer.parseInt(CustomMonsterWisEt.getText().toString()));
        newMonster.setIntel(Integer.parseInt(CustomMonsterIntEt.getText().toString()));
        newMonster.setCha(Integer.parseInt(CustomMonsterChaEt.getText().toString()));
        newMonster.setHp(Integer.parseInt(CustomMonsterHpEt.getText().toString()));
        newMonster.setAc(Integer.parseInt(CustomMonsterAcEt.getText().toString()));
        newMonster.setHitDice(new DiceParser(HitDiceDisplayTv.getText().toString()));
        newMonster.setCr(CustomMonsterCr.getText().toString());
        CustomMonsters.addCustomMonster(getApplicationContext(), newMonster);
        setResult(RESULT_OK);
        finish();
    }

}
