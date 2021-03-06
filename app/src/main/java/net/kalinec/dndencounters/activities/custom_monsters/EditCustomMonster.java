package net.kalinec.dndencounters.activities.custom_monsters;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.kalinec.dndencounters.DnDEncountersActivity;
import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.custom_monsters.CustomMonster;
import net.kalinec.dndencounters.custom_monsters.CustomMonsterAbilitiesListAdapter;
import net.kalinec.dndencounters.custom_monsters.CustomMonsters;
import net.kalinec.dndencounters.dice.DiceParser;
import net.kalinec.dndencounters.dice.DiceRollerDialog;
import net.kalinec.dndencounters.lib.RvClickListener;
import net.kalinec.dndencounters.monsters.MonsterAbility;

import java.util.ArrayList;

public class EditCustomMonster extends DnDEncountersActivity
{
    public static final int REQUEST_UDPATED_MONSTER = 102;
    private TextView HitDiceDisplayTv;
    private EditText CustomMonsterNameEt, CustomMonsterTypeEt, CustomMonsterSizeEt, CustomMonsterStrEt, CustomMonsterDexEt, CustomMonsterConEt,
            CustomMonsterWisEt, CustomMonsterIntEt, CustomMonsterChaEt, CustomMonsterHpEt, CustomMonsterAcEt, CustomMonsterCr, CustomMonsterSpeed,
            AlignmentTv, ResistancesTv, ImmunitiesTv, VulnerabilitiesTv, LanguagesTv, SensesTv;
    private CustomMonster monster;
    private RecyclerView CustomMonsterSpecialAbilitiesRv, CustomMonsterActionsRv, CustomMonsterLegendaryActionsRv;
    private ArrayList<MonsterAbility> actions, specialAbilities, legendaryAbilities;
    private CustomMonsterAbilitiesListAdapter actionLA, specialAbilitiesLA, legendaryAbilitiesLA;
    private int editPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        monster = (CustomMonster)bundle.getSerializable(CustomMonster.PASSED_MONSTER);
        setContentView(R.layout.activity_edit_custom_monster);
        HitDiceDisplayTv = findViewById(R.id.HitDiceDisplayTv);
        HitDiceDisplayTv.setText(monster.getHitDice().getDiceStr());
        CustomMonsterNameEt = findViewById(R.id.CustomMonsterNameEt);
        CustomMonsterNameEt.setText(monster.getName());
        CustomMonsterTypeEt = findViewById(R.id.CustomMonsterTypeEt);
        CustomMonsterTypeEt.setText(monster.getMonsterType());
        CustomMonsterSizeEt = findViewById(R.id.CustomMonsterSizeEt);
        CustomMonsterSizeEt.setText(monster.getMonsterSize());
        CustomMonsterStrEt = findViewById(R.id.CustomMonsterStrEt);
        CustomMonsterStrEt.setText(Integer.toString(monster.getStr()));
        CustomMonsterDexEt = findViewById(R.id.CustomMonsterDexEt);
        CustomMonsterDexEt.setText(Integer.toString(monster.getDex()));
        CustomMonsterConEt = findViewById(R.id.CustomMonsterConEt);
        CustomMonsterConEt.setText(Integer.toString(monster.getCon()));
        CustomMonsterWisEt = findViewById(R.id.CustomMonsterWisEt);
        CustomMonsterWisEt.setText(Integer.toString(monster.getWis()));
        CustomMonsterIntEt = findViewById(R.id.CustomMonsterIntEt);
        CustomMonsterIntEt.setText(Integer.toString(monster.getIntel()));
        CustomMonsterChaEt = findViewById(R.id.CustomMonsterChaEt);
        CustomMonsterChaEt.setText(Integer.toString(monster.getCha()));
        CustomMonsterHpEt = findViewById(R.id.CustomMonsterHpEt);
        CustomMonsterHpEt.setText(Integer.toString(monster.getHp()));
        CustomMonsterAcEt = findViewById(R.id.CustomMonsterAcEt);
        CustomMonsterAcEt.setText(Integer.toString(monster.getAc()));
        CustomMonsterCr = findViewById(R.id.CustomMonsterCr);
        CustomMonsterCr.setText(monster.getCr());
        CustomMonsterSpeed = findViewById(R.id.CustomMonsterSpeed);
        CustomMonsterSpeed.setText(monster.getSpeed());
        AlignmentTv = findViewById(R.id.AlignmentTv);
        AlignmentTv.setText(monster.getAlignment());
        ResistancesTv = findViewById(R.id.ResistancesTv);
        ResistancesTv.setText(monster.getResistances());
        ImmunitiesTv = findViewById(R.id.ImmunitiesTv);
        ImmunitiesTv.setText(monster.getImmunities());
        VulnerabilitiesTv = findViewById(R.id.VulnerabilitiesTv);
        VulnerabilitiesTv.setText(monster.getVulnerabilities());
        LanguagesTv = findViewById(R.id.LanguagesTv);
        LanguagesTv.setText(monster.getLanguages());
        SensesTv = findViewById(R.id.SensesTv);
        SensesTv.setText(monster.getSenses());

        //special abilities
        CustomMonsterSpecialAbilitiesRv = findViewById(R.id.CustomMonsterSpecialAbilitiesRv);
        specialAbilities = monster.getSpecialAbilities();
        specialAbilitiesLA = new CustomMonsterAbilitiesListAdapter(getApplicationContext(), new RvClickListener() {
            @Override
            public void onClick(View view, int position) {
                //edit special ability
                MonsterAbility monsterAbility = specialAbilities.get(position);
                editPosition = position;
                Intent myIntent = new Intent(EditCustomMonster.this, ManageCustomMonsterAbility.class);
                Bundle bundle = new Bundle();
                bundle.putInt(ManageCustomMonsterAbility.PASSED_ACTION, ManageCustomMonsterAbility.REQUEST_UPDATE_SPECIAL_ABILITY);
                bundle.putSerializable(ManageCustomMonsterAbility.PASSED_ABILITY, monsterAbility);
                myIntent.putExtras(bundle);
                startActivityForResult(myIntent, ManageCustomMonsterAbility.REQUEST_UPDATE_SPECIAL_ABILITY);
            }
        }, new RvClickListener() {
            @Override
            public void onClick(View view, int position) {
                //delete special ability
                specialAbilities.remove(position);
                specialAbilitiesLA.setMonsterAbilityList(specialAbilities);
            }
        });
        specialAbilitiesLA.setMonsterAbilityList(specialAbilities);
        CustomMonsterSpecialAbilitiesRv.setAdapter(specialAbilitiesLA);
        CustomMonsterSpecialAbilitiesRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        //actions
        CustomMonsterActionsRv = findViewById(R.id.CustomMonsterActionsRv);
        actions = monster.getActions();
        actionLA = new CustomMonsterAbilitiesListAdapter(getApplicationContext(), new RvClickListener() {
            @Override
            public void onClick(View view, int position) {
                //edit action
                MonsterAbility monsterAbility = actions.get(position);
                editPosition = position;
                Intent myIntent = new Intent(EditCustomMonster.this, ManageCustomMonsterAbility.class);
                Bundle bundle = new Bundle();
                bundle.putInt(ManageCustomMonsterAbility.PASSED_ACTION, ManageCustomMonsterAbility.REQUEST_UPDATE_ACTION);
                bundle.putSerializable(ManageCustomMonsterAbility.PASSED_ABILITY, monsterAbility);
                myIntent.putExtras(bundle);
                startActivityForResult(myIntent, ManageCustomMonsterAbility.REQUEST_UPDATE_ACTION);
            }
        }, new RvClickListener() {
            @Override
            public void onClick(View view, int position) {
                //delete action
                actions.remove(position);
                actionLA.setMonsterAbilityList(actions);
            }
        });
        actionLA.setMonsterAbilityList(actions);
        CustomMonsterActionsRv.setAdapter(actionLA);
        CustomMonsterActionsRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        //legendary abilities
	    CustomMonsterLegendaryActionsRv = findViewById(R.id.CustomMonsterLegendaryActionsRv);
	    legendaryAbilities = monster.getActions();
	    legendaryAbilitiesLA = new CustomMonsterAbilitiesListAdapter(getApplicationContext(), new RvClickListener() {
            @Override
            public void onClick(View view, int position) {
                //edit action
                MonsterAbility monsterAbility = legendaryAbilities.get(position);
                editPosition = position;
                Intent myIntent = new Intent(EditCustomMonster.this, ManageCustomMonsterAbility.class);
                Bundle bundle = new Bundle();
                bundle.putInt(ManageCustomMonsterAbility.PASSED_ACTION, ManageCustomMonsterAbility.REQUEST_UPDATE_LEGENDARY_ABILITY);
                bundle.putSerializable(ManageCustomMonsterAbility.PASSED_ABILITY, monsterAbility);
                myIntent.putExtras(bundle);
                startActivityForResult(myIntent, ManageCustomMonsterAbility.REQUEST_UPDATE_LEGENDARY_ABILITY);
            }
        }, new RvClickListener() {
            @Override
            public void onClick(View view, int position) {
                //delete action
	            legendaryAbilities.remove(position);
	            legendaryAbilitiesLA.setMonsterAbilityList(legendaryAbilities);
            }
        });
	    legendaryAbilitiesLA.setMonsterAbilityList(legendaryAbilities);
	    CustomMonsterLegendaryActionsRv.setAdapter(legendaryAbilitiesLA);
	    CustomMonsterLegendaryActionsRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    public void enterHitDice(View v)
    {
        Intent myIntent = new Intent(EditCustomMonster.this, DiceRollerDialog.class);
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
        else if(requestCode == ManageCustomMonsterAbility.REQUEST_NEW_ACTION && resultCode == RESULT_OK)
        {
            MonsterAbility newAction = (MonsterAbility)data.getSerializableExtra(ManageCustomMonsterAbility.PASSED_ABILITY);
            actions.add(newAction);
            actionLA.setMonsterAbilityList(actions);
        }
        else if(requestCode == ManageCustomMonsterAbility.REQUEST_NEW_SPECIAL_ABILITY && resultCode == RESULT_OK)
        {
            MonsterAbility newAbility = (MonsterAbility)data.getSerializableExtra(ManageCustomMonsterAbility.PASSED_ABILITY);
            specialAbilities.add(newAbility);
            specialAbilitiesLA.setMonsterAbilityList(specialAbilities);
        }
        else if(requestCode == ManageCustomMonsterAbility.REQUEST_NEW_LEGENDARY_ABILITY && resultCode == RESULT_OK)
        {
	        MonsterAbility newAbility = (MonsterAbility)data.getSerializableExtra(ManageCustomMonsterAbility.PASSED_ABILITY);
	        legendaryAbilities.add(newAbility);
	        legendaryAbilitiesLA.setMonsterAbilityList(legendaryAbilities);
        }
        else if(requestCode == ManageCustomMonsterAbility.REQUEST_UPDATE_SPECIAL_ABILITY && resultCode == RESULT_OK)
        {
            if(editPosition >= 0)
            {
                MonsterAbility newAbility = (MonsterAbility)data.getSerializableExtra(ManageCustomMonsterAbility.PASSED_ABILITY);
                specialAbilities.set(editPosition, newAbility);
                specialAbilitiesLA.setMonsterAbilityList(specialAbilities);
                editPosition = -1;
            }
        }
        else if(requestCode == ManageCustomMonsterAbility.REQUEST_UPDATE_ACTION && resultCode == RESULT_OK)
        {
            if(editPosition >= 0)
            {
                MonsterAbility newAbility = (MonsterAbility)data.getSerializableExtra(ManageCustomMonsterAbility.PASSED_ABILITY);
                actions.set(editPosition, newAbility);
                actionLA.setMonsterAbilityList(actions);
                editPosition = -1;
            }
        }
        else if(requestCode == ManageCustomMonsterAbility.REQUEST_UPDATE_LEGENDARY_ABILITY && resultCode == RESULT_OK)
        {
	        if(editPosition >= 0)
	        {
		        MonsterAbility newAbility = (MonsterAbility)data.getSerializableExtra(ManageCustomMonsterAbility.PASSED_ABILITY);
		        legendaryAbilities.set(editPosition, newAbility);
		        legendaryAbilitiesLA.setMonsterAbilityList(legendaryAbilities);
		        editPosition = -1;
	        }
        }
    }

    public void UpdateMonster(View v)
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
        if(CustomMonsterSpeed.getText().toString() == "")
        {
            Toast errorToast = Toast.makeText(getApplicationContext(), "You must enter a monster speed", Toast.LENGTH_LONG);
            errorToast.show();
            return;
        }
        monster.setMonsterName(CustomMonsterNameEt.getText().toString());
        monster.setMonsterType(CustomMonsterTypeEt.getText().toString());
        monster.setMonsterSize(CustomMonsterSizeEt.getText().toString());
        monster.setStr(Integer.parseInt(CustomMonsterStrEt.getText().toString()));
        monster.setDex(Integer.parseInt(CustomMonsterDexEt.getText().toString()));
        monster.setCon(Integer.parseInt(CustomMonsterConEt.getText().toString()));
        monster.setWis(Integer.parseInt(CustomMonsterWisEt.getText().toString()));
        monster.setIntel(Integer.parseInt(CustomMonsterIntEt.getText().toString()));
        monster.setCha(Integer.parseInt(CustomMonsterChaEt.getText().toString()));
        monster.setHp(Integer.parseInt(CustomMonsterHpEt.getText().toString()));
        monster.setAc(Integer.parseInt(CustomMonsterAcEt.getText().toString()));
        monster.setHitDice(new DiceParser(HitDiceDisplayTv.getText().toString()));
        monster.setCr(CustomMonsterCr.getText().toString());
        monster.setSpeed(CustomMonsterSpeed.getText().toString());
	    monster.setAlignment(AlignmentTv.getText().toString());
	    monster.setResistances(ResistancesTv.getText().toString());
	    monster.setImmunities(ImmunitiesTv.getText().toString());
	    monster.setVulnerabilities(VulnerabilitiesTv.getText().toString());
	    monster.setLanguages(LanguagesTv.getText().toString());
	    monster.setSenses(SensesTv.getText().toString());
	    monster.setSpecialAbilities(specialAbilities);
	    monster.setActions(actions);
	    monster.setLegendaryAbilities(legendaryAbilities);
        CustomMonsters.updateCustomMonster(getApplicationContext(), monster);
        setResult(RESULT_OK);
        finish();
    }

    public void confirmDeleteMonster(View target)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditCustomMonster.this);
        builder.setMessage(R.string.ConfirmMonsterDelete)
                .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        deleteMonster();
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

    public void deleteMonster()
    {
        CustomMonsters.removeCustomMonster(getApplicationContext(), monster);
        setResult(RESULT_OK);
        finish();
    }

    public void addSpecialAbility(View v)
    {
        Intent myIntent = new Intent(EditCustomMonster.this, ManageCustomMonsterAbility.class);
        Bundle bundle = new Bundle();
        bundle.putInt(ManageCustomMonsterAbility.PASSED_ACTION, ManageCustomMonsterAbility.REQUEST_NEW_SPECIAL_ABILITY);
        myIntent.putExtras(bundle);
        startActivityForResult(myIntent, ManageCustomMonsterAbility.REQUEST_NEW_SPECIAL_ABILITY);
    }

    public void addAction(View v)
    {
        Intent myIntent = new Intent(EditCustomMonster.this, ManageCustomMonsterAbility.class);
        Bundle bundle = new Bundle();
        bundle.putInt(ManageCustomMonsterAbility.PASSED_ACTION, ManageCustomMonsterAbility.REQUEST_NEW_ACTION);
        myIntent.putExtras(bundle);
        startActivityForResult(myIntent, ManageCustomMonsterAbility.REQUEST_NEW_ACTION);
    }

    public void addLegendaryAbility(View v)
	{
		Intent myIntent = new Intent(EditCustomMonster.this, ManageCustomMonsterAbility.class);
		Bundle bundle = new Bundle();
		bundle.putInt(ManageCustomMonsterAbility.PASSED_ACTION, ManageCustomMonsterAbility.REQUEST_NEW_LEGENDARY_ABILITY);
		myIntent.putExtras(bundle);
		startActivityForResult(myIntent, ManageCustomMonsterAbility.REQUEST_NEW_LEGENDARY_ABILITY);
	}
}
