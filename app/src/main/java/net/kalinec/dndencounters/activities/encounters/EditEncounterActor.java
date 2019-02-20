package net.kalinec.dndencounters.activities.encounters;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import net.kalinec.dndencounters.DnDEncountersActivity;
import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounter;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterActor;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterMonster;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterPlayer;
import net.kalinec.dndencounters.characters.Character;
import net.kalinec.dndencounters.encounters.Encounter;
import net.kalinec.dndencounters.monster_tokens.MonsterToken;
import net.kalinec.dndencounters.monster_tokens.MonsterTokenOnlySpinnerAdapter;
import net.kalinec.dndencounters.monster_tokens.MonsterTokenSpinnerAdapter;
import net.kalinec.dndencounters.monster_tokens.MonsterTokens;
import net.kalinec.dndencounters.monsters.Monster;
import net.kalinec.dndencounters.monsters.MonsterAbilitiesListAdapter;
import net.kalinec.dndencounters.players.Player;
import net.kalinec.dndencounters.players.Players;

import java.util.List;
import java.util.Locale;

public class EditEncounterActor extends DnDEncountersActivity implements View.OnClickListener
{

	public static final int REQUEST_UPDATED_ACTOR = 780;
	private AdventureEncounter adventureEncounter;
	private AdventureEncounterActor actor;
	private boolean hidingActions, hidingAbilities, hidingLegendaryActions;
	private RecyclerView MonsterInfoSpecialAbilitiesRv, MonsterInfoActionsRv, LegendaryActionsRv;
	private Button ActionsHideBt;
	private Button SpecialAbilitiesHideBt, LegendaryActionHideBt;
	private ToggleButton HpModTypeTb;
	private EditText MonsterCurrentHpEt;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);Bundle bundle = getIntent().getExtras();
		assert bundle != null;
		adventureEncounter = (AdventureEncounter)bundle.getSerializable(AdventureEncounter.PASSED_ADVENTURE_ENCOUNTER);
		actor = (AdventureEncounterActor)bundle.getSerializable(AdventureEncounterActor.PASSED_ACTOR);
		if(actor.getActorType() == AdventureEncounterActor.MONSTER_ACTOR)
		{
			setContentView(R.layout.activity_edit_encounter_actor_monster);
			fillMonsterDetails();
		}
		else
		{
			setContentView(R.layout.activity_edit_encounter_actor_pc);
			fillPcDetails();
		}
	}

	private void fillMonsterDetails()
	{
		AdventureEncounterMonster activeMonster = ((AdventureEncounterMonster)actor);
		//fill in monster info
		TextView monsterInfoNameTv = findViewById(R.id.MonsterInfoNameTv);
		monsterInfoNameTv.setText(activeMonster.getMonster().getName());
		TextView monsterInfoSizeTv = findViewById(R.id.MonsterInfoSizeTv);
		monsterInfoSizeTv.setText(activeMonster.getMonster().getMonsterSize());
		TextView monsterInfoTypeTv = findViewById(R.id.MonsterInfoTypeTv);
		monsterInfoTypeTv.setText(activeMonster.getMonster().getMonsterType());
		TextView monsterInfoAcTv = findViewById(R.id.MonsterInfoAcTv);
		monsterInfoAcTv.setText(String.format(Locale.getDefault(), "%d", activeMonster.getMonster()
		                                                                              .getAc()));
		//monster stats block
		TextView monsterInfoStrTv = findViewById(R.id.MonsterInfoStrTv);
		monsterInfoStrTv.setText(activeMonster.getMonster().getStrMod());
		TextView monsterInfoDexTv = findViewById(R.id.MonsterInfoDexTv);
		monsterInfoDexTv.setText(activeMonster.getMonster().getDexMod());
		TextView monsterInfoConTv = findViewById(R.id.MonsterInfoConTv);
		monsterInfoConTv.setText(activeMonster.getMonster().getConMod());
		TextView monsterInfoIntTv = findViewById(R.id.MonsterInfoIntTv);
		monsterInfoIntTv.setText(activeMonster.getMonster().getIntelMod());
		TextView monsterInfoWisTv = findViewById(R.id.MonsterInfoWisTv);
		monsterInfoWisTv.setText(activeMonster.getMonster().getWisMod());
		TextView monsterInfoChaTv = findViewById(R.id.MonsterInfoChaTv);
		monsterInfoChaTv.setText(activeMonster.getMonster().getChaMod());
		TextView MonsterInfoSpeed = findViewById(R.id.MonsterInfoSpeed);
		MonsterInfoSpeed.setText(activeMonster.getMonster().getSpeed());
		TextView AlignmentTxt = findViewById(R.id.AlignmentTxt);
		AlignmentTxt.setText(activeMonster.getMonster().getAlignment());
		TextView ResistancesTv = findViewById(R.id.ResistancesTv);
		ResistancesTv.setText(activeMonster.getMonster().getResistances());
		TextView ImmunitiesTv = findViewById(R.id.ImmunitiesTv);
		ImmunitiesTv.setText(activeMonster.getMonster().getImmunities());
		TextView VulnerabilitiesTxt = findViewById(R.id.VulnerabilitiesTxt);
		VulnerabilitiesTxt.setText(activeMonster.getMonster().getVulnerabilities());
		TextView LanguagesTv = findViewById(R.id.LanguagesTv);
		LanguagesTv.setText(activeMonster.getMonster().getLanguages());
		TextView SensesTv = findViewById(R.id.SensesTv);
		SensesTv.setText(activeMonster.getMonster().getSenses());
		//initiative
		EditText InitiativeEt = findViewById(R.id.InitiativeEt);
		InitiativeEt.setText(Integer.toString(activeMonster.getInitiative()));
		EditText InitiativePositionEt = findViewById(R.id.InitiativePositionEt);
		InitiativePositionEt.setText(Integer.toString(activeMonster.getInitiativePosition()));
		//monster spinner
		List<MonsterToken> monsterTokens = MonsterTokens.getAllMonsterTokens(getApplicationContext());
		Spinner MonsterInfoTokenSp = findViewById(R.id.MonsterInfoTokenSp);
		MonsterInfoTokenSp.setAdapter(new MonsterTokenSpinnerAdapter(getApplicationContext()));
		MonsterInfoTokenSp.setSelection(monsterTokens.indexOf(activeMonster.getToken()));

		//special abilities
		MonsterInfoSpecialAbilitiesRv = findViewById(R.id.MonsterInfoSpecialAbilitiesRv);
		MonsterAbilitiesListAdapter monsterAbilitiesListAdapter
				= new MonsterAbilitiesListAdapter(getApplicationContext());
		monsterAbilitiesListAdapter
				.setMonsterAbilityList(activeMonster.getMonster().getSpecialAbilities());
		MonsterInfoSpecialAbilitiesRv.setAdapter(monsterAbilitiesListAdapter);
		MonsterInfoSpecialAbilitiesRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
		//actions
		MonsterInfoActionsRv = findViewById(R.id.MonsterInfoActionsRv);
		MonsterAbilitiesListAdapter monsterActionsListAdapter
				= new MonsterAbilitiesListAdapter(getApplicationContext());
		monsterActionsListAdapter.setMonsterAbilityList(activeMonster.getMonster().getActions());
		MonsterInfoActionsRv.setAdapter(monsterActionsListAdapter);
		MonsterInfoActionsRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
		//legendary Actions
		LegendaryActionsRv = findViewById(R.id.LegendaryActionsRv);
		MonsterAbilitiesListAdapter monsterLegendaryActionsListAdapter
				= new MonsterAbilitiesListAdapter(getApplicationContext());
		monsterLegendaryActionsListAdapter.setMonsterAbilityList(activeMonster.getMonster().getLegendaryAbilities());
		LegendaryActionsRv.setAdapter(monsterLegendaryActionsListAdapter);
		LegendaryActionsRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
		//hide buttons
		hidingActions = hidingAbilities = hidingLegendaryActions = false;
		SpecialAbilitiesHideBt = findViewById(R.id.SpecialAbilitiesHideBt);
		SpecialAbilitiesHideBt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(hidingAbilities)
				{
					hidingAbilities = false;
					MonsterInfoSpecialAbilitiesRv.setVisibility(View.VISIBLE);
					SpecialAbilitiesHideBt.setText(R.string.fa_arrow_circle_down);
				}
				else
				{
					hidingAbilities = true;
					MonsterInfoSpecialAbilitiesRv.setVisibility(View.GONE);
					SpecialAbilitiesHideBt.setText(R.string.fa_arrow_circle_right);
				}
			}
		});
		ActionsHideBt = findViewById(R.id.ActionsHideBt);
		ActionsHideBt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(hidingActions)
				{
					hidingActions = false;
					MonsterInfoActionsRv.setVisibility(View.VISIBLE);
					ActionsHideBt.setText(R.string.fa_arrow_circle_down);
				}
				else
				{
					hidingActions = true;
					MonsterInfoActionsRv.setVisibility(View.GONE);
					ActionsHideBt.setText(R.string.fa_arrow_circle_right);
				}
			}
		});
		LegendaryActionHideBt = findViewById(R.id.LegendaryActionHideBt);
		LegendaryActionHideBt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(hidingLegendaryActions)
				{
					hidingLegendaryActions = false;
					LegendaryActionsRv.setVisibility(View.VISIBLE);
					LegendaryActionHideBt.setText(R.string.fa_arrow_circle_down);
				}
				else
				{
					hidingLegendaryActions = true;
					LegendaryActionsRv.setVisibility(View.GONE);
					LegendaryActionHideBt.setText(R.string.fa_arrow_circle_right);
				}
			}
		});
		//current HP
		MonsterCurrentHpEt = findViewById(R.id.MonsterCurrentHpEt);
		MonsterCurrentHpEt.setText(String.format(Locale.getDefault(), "%d", activeMonster.getHp()));
		//hp modifier widget
		HpModTypeTb = findViewById(R.id.HpModTypeTb);
		Button hpmod1 = findViewById(R.id.hpmod1);
		hpmod1.setOnClickListener(this);
		Button hpmod2 = findViewById(R.id.hpmod2);
		hpmod2.setOnClickListener(this);
		Button hpmod3 = findViewById(R.id.hpmod3);
		hpmod3.setOnClickListener(this);
		Button hpmod4 = findViewById(R.id.hpmod4);
		hpmod4.setOnClickListener(this);
		Button hpmod5 = findViewById(R.id.hpmod5);
		hpmod5.setOnClickListener(this);
		Button hpmod6 = findViewById(R.id.hpmod6);
		hpmod6.setOnClickListener(this);
		Button hpmod7 = findViewById(R.id.hpmod7);
		hpmod7.setOnClickListener(this);
		Button hpmod8 = findViewById(R.id.hpmod8);
		hpmod8.setOnClickListener(this);
		Button hpmod9 = findViewById(R.id.hpmod9);
		hpmod9.setOnClickListener(this);
		Button hpmod10 = findViewById(R.id.hpmod10);
		hpmod10.setOnClickListener(this);

		Button MarkMonsterDeadBt = findViewById(R.id.MarkMonsterDeadBt);
		if(actor.getStatus() == AdventureEncounterActor.DEAD)
		{
			MarkMonsterDeadBt.setBackgroundResource(R.color.alertBorderColor);
			MarkMonsterDeadBt.setText(R.string.MakeAliveMonsterTxt);
		}
	}

	private void fillPcDetails()
	{
		AdventureEncounterPlayer activePc = ((AdventureEncounterPlayer)actor);
		TextView PcNameTv = findViewById(R.id.PcNameTv);
		PcNameTv.setText(activePc.getPc().getName());

		EditText InitiativeEt = findViewById(R.id.InitiativeEt);
		InitiativeEt.setText(Integer.toString(activePc.getInitiative()));
		EditText InitiativePositionEt = findViewById(R.id.InitiativePositionEt);
		InitiativePositionEt.setText(Integer.toString(activePc.getInitiativePosition()));
		EditText AcEt = findViewById(R.id.AcEt);
		AcEt.setText(Integer.toString(activePc.getPc().getAc()));
		EditText HpEt = findViewById(R.id.HpEt);
		HpEt.setText(Integer.toString(activePc.getPc().getHp()));
		EditText PpEt = findViewById(R.id.PpEt);
		PpEt.setText(Integer.toString(activePc.getPc().getPp()));
		EditText SpellDcTv = findViewById(R.id.SpellDcTv);
		SpellDcTv.setText(Integer.toString(activePc.getPc().getSpellDc()));
	}

	@Override
	public void onClick(View v) {
		//HP button press.
		Button presseed = (Button)v;
		int hpMod = Integer.parseInt(presseed.getText().toString());
		int curHp = Integer.parseInt(MonsterCurrentHpEt.getText().toString());
		if(HpModTypeTb.isChecked())
		{
			//damage
			curHp -= hpMod;
			if(curHp < 0)
				curHp = 0;
		}
		else
		{
			//healing
			curHp += hpMod;
			if(curHp > ((AdventureEncounterMonster)actor).getMaxHp())
				curHp = ((AdventureEncounterMonster)actor).getMaxHp();
		}
		MonsterCurrentHpEt.setText(String.format(Locale.getDefault(), "%d", curHp));
	}

	private void updateMonsterActor()
	{
		List<MonsterToken> monsterTokens = MonsterTokens.getAllMonsterTokens(getApplicationContext());
		Spinner MonsterInfoTokenSp = findViewById(R.id.MonsterInfoTokenSp);
		AdventureEncounterMonster monsterActor = ((AdventureEncounterMonster)actor);
		monsterActor.setToken(monsterTokens.get(MonsterInfoTokenSp.getSelectedItemPosition()));

		EditText InitiativeEt = findViewById(R.id.InitiativeEt);
		monsterActor.setInitiative(Integer.parseInt(InitiativeEt.getText().toString()));
		EditText InitiativePositionEt = findViewById(R.id.InitiativePositionEt);
		monsterActor.setInitiativePosition(Integer.parseInt(InitiativePositionEt.getText().toString()));

		MonsterCurrentHpEt = findViewById(R.id.MonsterCurrentHpEt);
		monsterActor.setHp(Integer.parseInt(MonsterCurrentHpEt.getText().toString()));
	}

	private void updatePcActor()
	{
		AdventureEncounterPlayer playerActor = ((AdventureEncounterPlayer)actor);

		EditText InitiativeEt = findViewById(R.id.InitiativeEt);
		playerActor.setInitiative(Integer.parseInt(InitiativeEt.getText().toString()));
		EditText InitiativePositionEt = findViewById(R.id.InitiativePositionEt);
		playerActor.setInitiativePosition(Integer.parseInt(InitiativePositionEt.getText().toString()));

		Character pc = playerActor.getPc();
		if(pc != null)
		{
			EditText AcEt = findViewById(R.id.AcEt);
			EditText HpEt = findViewById(R.id.HpEt);
			EditText PpEt = findViewById(R.id.PpEt);
			EditText SpellDcTv = findViewById(R.id.SpellDcTv);
			pc.setAc(Integer.parseInt(AcEt.getText().toString()));
			pc.setHp(Integer.parseInt(HpEt.getText().toString()));
			pc.setPp(Integer.parseInt(PpEt.getText().toString()));
			pc.setSpellDc(Integer.parseInt(SpellDcTv.getText().toString()));
			Players.updatePc(getApplicationContext(), pc);
		}
	}

	private boolean isSameActor(AdventureEncounterActor a, AdventureEncounterActor b)
	{
		if(a.getActorType() != b.getActorType())
			return false;
		if(a.getActorType() == AdventureEncounterActor.MONSTER_ACTOR)
			return ((AdventureEncounterMonster)a).getMonster().equals(((AdventureEncounterMonster)b).getMonster());
		return ((AdventureEncounterPlayer)a).getPc().equals(((AdventureEncounterPlayer)b).getPc());
	}

	private void updateActor()
	{
		if(actor.getActorType() == AdventureEncounterActor.MONSTER_ACTOR)
			updateMonsterActor();
		else
			updatePcActor();
		//find the actor.
		int idx;
		for(idx = 0; idx < adventureEncounter.getActors().size(); idx++)
		{
			if(isSameActor(actor, adventureEncounter.getActors().get(idx)))
				break;
		}
		adventureEncounter.getActors().set(idx, actor);
		Intent data = new Intent();
		data.putExtra(AdventureEncounter.PASSED_ADVENTURE_ENCOUNTER, adventureEncounter);
		setResult(RESULT_OK, data);
		finish();
	}

	public void updateActorBt(View v)
	{
		updateActor();
	}

	public void markMonsterDead(View v)
	{
		if(actor.getStatus() == AdventureEncounterActor.DEAD)
			actor.setStatus(AdventureEncounterActor.ALIVE);
		else
			actor.setStatus(AdventureEncounterActor.DEAD);
		updateActor();
	}
}
