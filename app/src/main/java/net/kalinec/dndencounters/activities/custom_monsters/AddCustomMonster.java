package net.kalinec.dndencounters.activities.custom_monsters;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import net.kalinec.dndencounters.monsters.Monster;
import net.kalinec.dndencounters.monsters.MonsterAbility;

import java.util.ArrayList;
import java.util.List;

public class AddCustomMonster extends AppCompatActivity
{
    public static final int REQUEST_NEW_MONSTER = 101;
    private TextView HitDiceDisplayTv;
    private EditText CustomMonsterNameEt, CustomMonsterTypeEt, CustomMonsterSizeEt, CustomMonsterStrEt, CustomMonsterDexEt, CustomMonsterConEt,
            CustomMonsterWisEt, CustomMonsterIntEt, CustomMonsterChaEt, CustomMonsterHpEt, CustomMonsterAcEt, CustomMonsterCr, CustomMonsterSpeed,
            AlignmentTv, ResistancesTv, ImmunitiesTv, VulnerabilitiesTv, LanguagesTv, SensesTv;
    private RecyclerView CustomMonsterSpecialAbilitiesRv, CustomMonsterActionsRv, CustomMonsterLegendaryActionsRv;
    private ArrayList<MonsterAbility> actions, specialAbilities, legendaryAbilities;
    private CustomMonsterAbilitiesListAdapter actionLA, specialAbilitiesLA, legendaryAbilitiesLA;
    private int editPosition = -1;
    private Monster template = null;

    private void fillTemplate()
    {
        if(template != null)
        {
            CustomMonsterNameEt.setText(template.getName());
            CustomMonsterTypeEt.setText(template.getMonsterType());
            CustomMonsterSizeEt.setText(template.getMonsterSize());
            CustomMonsterStrEt.setText(Integer.toString(template.getStr()));
            CustomMonsterDexEt.setText(Integer.toString(template.getDex()));
            CustomMonsterConEt.setText(Integer.toString(template.getCon()));
            CustomMonsterWisEt.setText(Integer.toString(template.getWis()));
            CustomMonsterIntEt.setText(Integer.toString(template.getIntel()));
            CustomMonsterChaEt.setText(Integer.toString(template.getCha()));
            CustomMonsterHpEt.setText(Integer.toString(template.getHp()));
            CustomMonsterAcEt.setText(Integer.toString(template.getAc()));
            CustomMonsterCr.setText(template.getCr());
            CustomMonsterSpeed.setText(template.getSpeed());
            AlignmentTv.setText(template.getAlignment());
            ResistancesTv.setText(template.getResistances());
            ImmunitiesTv.setText(template.getImmunities());
            VulnerabilitiesTv.setText(template.getVulnerabilities());
            LanguagesTv.setText(template.getLanguages());
            SensesTv.setText(template.getSenses());
            actions = template.getActions();
            actionLA.setMonsterAbilityList(actions);
            specialAbilities = template.getSpecialAbilities();
            specialAbilitiesLA.setMonsterAbilityList(specialAbilities);
            legendaryAbilities = template.getLegendaryAbilities();
	        legendaryAbilitiesLA.setMonsterAbilityList(legendaryAbilities);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
            template = (Monster)bundle.getSerializable(CustomMonster.PASSED_MONSTER);
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
        CustomMonsterSpeed = findViewById(R.id.CustomMonsterSpeed);
        AlignmentTv = findViewById(R.id.AlignmentTv);
        ResistancesTv = findViewById(R.id.ResistancesTv);
        ImmunitiesTv = findViewById(R.id.ImmunitiesTv);
        VulnerabilitiesTv = findViewById(R.id.VulnerabilitiesTv);
        LanguagesTv = findViewById(R.id.LanguagesTv);
        SensesTv = findViewById(R.id.SensesTv);
        //special abilities
        CustomMonsterSpecialAbilitiesRv = findViewById(R.id.CustomMonsterSpecialAbilitiesRv);
        specialAbilities = new ArrayList<>();
        specialAbilitiesLA = new CustomMonsterAbilitiesListAdapter(getApplicationContext(), new RvClickListener() {
            @Override
            public void onClick(View view, int position) {
                //edit special ability
                MonsterAbility monsterAbility = specialAbilities.get(position);
                editPosition = position;
                Intent myIntent = new Intent(AddCustomMonster.this, ManageCustomMonsterAbility.class);
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
        actions = new ArrayList<>();
        actionLA = new CustomMonsterAbilitiesListAdapter(getApplicationContext(), new RvClickListener() {
            @Override
            public void onClick(View view, int position) {
                //edit action
                MonsterAbility monsterAbility = actions.get(position);
                editPosition = position;
                Intent myIntent = new Intent(AddCustomMonster.this, ManageCustomMonsterAbility.class);
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
        legendaryAbilities = new ArrayList<>();
        legendaryAbilitiesLA = new CustomMonsterAbilitiesListAdapter(getApplicationContext(), new RvClickListener() {
            @Override
            public void onClick(View view, int position) {
                //edit special ability
                MonsterAbility monsterAbility = legendaryAbilities.get(position);
                editPosition = position;
                Intent myIntent = new Intent(AddCustomMonster.this, ManageCustomMonsterAbility.class);
                Bundle bundle = new Bundle();
                bundle.putInt(ManageCustomMonsterAbility.PASSED_ACTION, ManageCustomMonsterAbility.REQUEST_UPDATE_LEGENDARY_ABILITY);
                bundle.putSerializable(ManageCustomMonsterAbility.PASSED_ABILITY, monsterAbility);
                myIntent.putExtras(bundle);
                startActivityForResult(myIntent, ManageCustomMonsterAbility.REQUEST_UPDATE_LEGENDARY_ABILITY);
            }
        }, new RvClickListener() {
            @Override
            public void onClick(View view, int position) {
                //delete legendasry ability
                legendaryAbilities.remove(position);
                legendaryAbilitiesLA.setMonsterAbilityList(legendaryAbilities);
            }
        });
        legendaryAbilitiesLA.setMonsterAbilityList(legendaryAbilities);
        CustomMonsterLegendaryActionsRv.setAdapter(legendaryAbilitiesLA);
        CustomMonsterLegendaryActionsRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
	    fillTemplate();
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
        if(CustomMonsterSpeed.getText().toString() == "")
        {
            Toast errorToast = Toast.makeText(getApplicationContext(), "You must enter a monster speed", Toast.LENGTH_LONG);
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
        newMonster.setSpeed(CustomMonsterSpeed.getText().toString());
        newMonster.setAlignment(AlignmentTv.getText().toString());
        newMonster.setResistances(ResistancesTv.getText().toString());
        newMonster.setImmunities(ImmunitiesTv.getText().toString());
        newMonster.setVulnerabilities(VulnerabilitiesTv.getText().toString());
        newMonster.setLanguages(LanguagesTv.getText().toString());
        newMonster.setSenses(SensesTv.getText().toString());
        newMonster.setSpecialAbilities(specialAbilities);
        newMonster.setActions(actions);
        newMonster.setLegendaryAbilities(legendaryAbilities);
        CustomMonsters.addCustomMonster(getApplicationContext(), newMonster);
        setResult(RESULT_OK);
        finish();
    }

    public void addSpecialAbility(View v)
    {
        Intent myIntent = new Intent(AddCustomMonster.this, ManageCustomMonsterAbility.class);
        Bundle bundle = new Bundle();
        bundle.putInt(ManageCustomMonsterAbility.PASSED_ACTION, ManageCustomMonsterAbility.REQUEST_NEW_SPECIAL_ABILITY);
        myIntent.putExtras(bundle);
        startActivityForResult(myIntent, ManageCustomMonsterAbility.REQUEST_NEW_SPECIAL_ABILITY);
    }

    public void addAction(View v)
    {
        Intent myIntent = new Intent(AddCustomMonster.this, ManageCustomMonsterAbility.class);
        Bundle bundle = new Bundle();
        bundle.putInt(ManageCustomMonsterAbility.PASSED_ACTION, ManageCustomMonsterAbility.REQUEST_NEW_ACTION);
        myIntent.putExtras(bundle);
        startActivityForResult(myIntent, ManageCustomMonsterAbility.REQUEST_NEW_ACTION);
    }

    public void addLegendaryAbility(View v)
    {
        Intent myIntent = new Intent(AddCustomMonster.this, ManageCustomMonsterAbility.class);
        Bundle bundle = new Bundle();
        bundle.putInt(ManageCustomMonsterAbility.PASSED_ACTION, ManageCustomMonsterAbility.REQUEST_NEW_LEGENDARY_ABILITY);
        myIntent.putExtras(bundle);
        startActivityForResult(myIntent, ManageCustomMonsterAbility.REQUEST_NEW_LEGENDARY_ABILITY);
    }


}
