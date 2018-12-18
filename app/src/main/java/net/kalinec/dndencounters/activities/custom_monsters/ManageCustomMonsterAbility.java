package net.kalinec.dndencounters.activities.custom_monsters;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.kalinec.dndencounters.DnDEncountersActivity;
import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounter;
import net.kalinec.dndencounters.custom_monsters.CustomMonster;
import net.kalinec.dndencounters.monsters.Monster;
import net.kalinec.dndencounters.monsters.MonsterAbility;

public class ManageCustomMonsterAbility extends DnDEncountersActivity {

    public final static int REQUEST_NEW_SPECIAL_ABILITY = 200;
    public final static int REQUEST_UPDATE_SPECIAL_ABILITY = 201;
    public final static int REQUEST_NEW_ACTION = 202;
    public final static int REQUEST_UPDATE_ACTION= 203;

    public final static String PASSED_ACTION = "PASSED_ACTION";
    public final static String PASSED_ABILITY = "PASSED_ABILITY";
    private MonsterAbility ability = null;
    private int action;
    private TextView ActionHeaderTv;
    private EditText AbilityNameEt, AbilityDescriptionEt;
    private Button SubmitBt;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        action = bundle.getInt(PASSED_ACTION);
        if(action == REQUEST_UPDATE_ACTION || action == REQUEST_UPDATE_SPECIAL_ABILITY)
            ability = (MonsterAbility)bundle.getSerializable(PASSED_ABILITY);
        setContentView(R.layout.activity_manage_custom_monster_ability);

        ActionHeaderTv = findViewById(R.id.ActionHeaderTv);
        AbilityNameEt = findViewById(R.id.AbilityNameEt);
        AbilityDescriptionEt = findViewById(R.id.AbilityDescriptionEt);
        SubmitBt = findViewById(R.id.SubmitBt);

        if(action == REQUEST_NEW_SPECIAL_ABILITY)
        {
            ActionHeaderTv.setText(R.string.NewSpecialAbilityTxt);
            AbilityNameEt.setHint(R.string.SpecialAbilityName);
            AbilityDescriptionEt.setHint(R.string.SpecialAbilityDescription);
            SubmitBt.setText(R.string.NewSpecialAbilityTxt);
        }
        else if(action == REQUEST_UPDATE_SPECIAL_ABILITY)
        {
            ActionHeaderTv.setText(R.string.UpdateSpecialAbilityTxt);
            AbilityNameEt.setHint(R.string.SpecialAbilityName);
            AbilityDescriptionEt.setHint(R.string.SpecialAbilityDescription);
            SubmitBt.setText(R.string.UpdateSpecialAbilityTxt);
            AbilityNameEt.setText(ability.getName());
            AbilityDescriptionEt.setText(ability.getDescription());
        }
        else if(action == REQUEST_NEW_ACTION)
        {
            ActionHeaderTv.setText(R.string.NewActionTxt);
            AbilityNameEt.setHint(R.string.ActionName);
            AbilityDescriptionEt.setHint(R.string.ActionDescription);
            SubmitBt.setText(R.string.NewActionTxt);
        }
        else
        {
            ActionHeaderTv.setText(R.string.UpdateActionTxt);
            AbilityNameEt.setHint(R.string.ActionName);
            AbilityDescriptionEt.setHint(R.string.ActionDescription);
            SubmitBt.setText(R.string.UpdateActionTxt);
            AbilityNameEt.setText(ability.getName());
            AbilityDescriptionEt.setText(ability.getDescription());
        }
    }

    public void manageAbility(View v)
    {
        MonsterAbility monsterAbility = new MonsterAbility(AbilityNameEt.getText().toString(), AbilityDescriptionEt.getText().toString());
        Intent data = new Intent();
        data.putExtra(PASSED_ABILITY, monsterAbility);
        setResult(RESULT_OK, data);
        finish();
    }
}
