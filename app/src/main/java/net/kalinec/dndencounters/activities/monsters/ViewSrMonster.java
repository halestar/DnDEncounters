package net.kalinec.dndencounters.activities.monsters;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.kalinec.dndencounters.DnDEncountersActivity;
import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.activities.custom_monsters.AddCustomMonster;
import net.kalinec.dndencounters.activities.custom_monsters.ViewMonsters;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounter;
import net.kalinec.dndencounters.custom_monsters.CustomMonster;
import net.kalinec.dndencounters.monsters.Monster;
import net.kalinec.dndencounters.monsters.MonsterAbilitiesListAdapter;

import java.util.Locale;

public class ViewSrMonster extends DnDEncountersActivity
{
	public static final int VIEW_SR_MONSTER = 780;
	private Monster activeMonster;
	private boolean hidingActions, hidingAbilities, hidingLegendaryActions;
	private RecyclerView MonsterInfoSpecialAbilitiesRv, MonsterInfoActionsRv, LegendaryActionsRv;
	private Button SpecialAbilitiesHideBt, ActionsHideBt, LegendaryActionHideBt;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		assert bundle != null;
		activeMonster = (Monster)bundle.getSerializable(Monster.PASSED_MONSTER);
		setContentView(R.layout.activity_view_sr_monster);

		//fill in monster info
		TextView monsterInfoNameTv = findViewById(R.id.MonsterInfoNameTv);
		monsterInfoNameTv.setText(activeMonster.getName());
		TextView monsterInfoSizeTv = findViewById(R.id.MonsterInfoSizeTv);
		monsterInfoSizeTv.setText(activeMonster.getMonsterSize());
		TextView monsterInfoTypeTv = findViewById(R.id.MonsterInfoTypeTv);
		monsterInfoTypeTv.setText(activeMonster.getMonsterType());
		TextView monsterInfoAcTv = findViewById(R.id.MonsterInfoAcTv);
		monsterInfoAcTv.setText(String.format(Locale.getDefault(), "%d", activeMonster.getAc()));
		//monster stats block
		TextView monsterInfoStrTv = findViewById(R.id.MonsterInfoStrTv);
		monsterInfoStrTv.setText(activeMonster.getStrMod());
		TextView monsterInfoDexTv = findViewById(R.id.MonsterInfoDexTv);
		monsterInfoDexTv.setText(activeMonster.getDexMod());
		TextView monsterInfoConTv = findViewById(R.id.MonsterInfoConTv);
		monsterInfoConTv.setText(activeMonster.getConMod());
		TextView monsterInfoIntTv = findViewById(R.id.MonsterInfoIntTv);
		monsterInfoIntTv.setText(activeMonster.getIntelMod());
		TextView monsterInfoWisTv = findViewById(R.id.MonsterInfoWisTv);
		monsterInfoWisTv.setText(activeMonster.getWisMod());
		TextView monsterInfoChaTv = findViewById(R.id.MonsterInfoChaTv);
		monsterInfoChaTv.setText(activeMonster.getChaMod());
		TextView MonsterInfoSpeed = findViewById(R.id.MonsterInfoSpeed);
		MonsterInfoSpeed.setText(activeMonster.getSpeed());
		TextView AlignmentTxt = findViewById(R.id.AlignmentTxt);
		AlignmentTxt.setText(activeMonster.getAlignment());
		TextView ResistancesTv = findViewById(R.id.ResistancesTv);
		ResistancesTv.setText(activeMonster.getResistances());
		TextView ImmunitiesTv = findViewById(R.id.ImmunitiesTv);
		ImmunitiesTv.setText(activeMonster.getImmunities());
		TextView VulnerabilitiesTxt = findViewById(R.id.VulnerabilitiesTxt);
		VulnerabilitiesTxt.setText(activeMonster.getVulnerabilities());
		TextView LanguagesTv = findViewById(R.id.LanguagesTv);
		LanguagesTv.setText(activeMonster.getLanguages());
		TextView SensesTv = findViewById(R.id.SensesTv);
		SensesTv.setText(activeMonster.getSenses());
		//hp
		TextView MonsterInfoHp = findViewById(R.id.MonsterInfoHp);
		MonsterInfoHp.setText(Integer.toString(activeMonster.getHp()));
		//hd
		TextView MonsterInfoHd = findViewById(R.id.MonsterInfoHd);
		MonsterInfoHd.setText(activeMonster.getHitDice().getDiceStr());
		//special abilities
		MonsterInfoSpecialAbilitiesRv = findViewById(R.id.MonsterInfoSpecialAbilitiesRv);
		MonsterAbilitiesListAdapter monsterAbilitiesListAdapter
				= new MonsterAbilitiesListAdapter(getApplicationContext());
		monsterAbilitiesListAdapter
				.setMonsterAbilityList(activeMonster.getSpecialAbilities());
		MonsterInfoSpecialAbilitiesRv.setAdapter(monsterAbilitiesListAdapter);
		MonsterInfoSpecialAbilitiesRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
		//actions
		MonsterInfoActionsRv = findViewById(R.id.MonsterInfoActionsRv);
		MonsterAbilitiesListAdapter monsterActionsListAdapter
				= new MonsterAbilitiesListAdapter(getApplicationContext());
		monsterActionsListAdapter.setMonsterAbilityList(activeMonster.getActions());
		MonsterInfoActionsRv.setAdapter(monsterActionsListAdapter);
		MonsterInfoActionsRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
		//legendary Actions
		LegendaryActionsRv = findViewById(R.id.LegendaryActionsRv);
		MonsterAbilitiesListAdapter monsterLegendaryActionsListAdapter
				= new MonsterAbilitiesListAdapter(getApplicationContext());
		monsterLegendaryActionsListAdapter.setMonsterAbilityList(activeMonster.getLegendaryAbilities());
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
	}

	public void addTemplateMonster(View v)
	{
		Intent myIntent = new Intent(ViewSrMonster.this, AddCustomMonster.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable(Monster.PASSED_MONSTER, activeMonster);
		myIntent.putExtras(bundle);
		startActivityForResult(myIntent, AddCustomMonster.REQUEST_NEW_MONSTER);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
	{
		finish();
	}
}
