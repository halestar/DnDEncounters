package net.kalinec.dndencounters.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterActor;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterMonster;
import net.kalinec.dndencounters.monsters.MonsterAbilitiesListAdapter;

import java.util.Locale;

public class MonsterTarget extends Fragment implements View.OnClickListener {

    private static final String ACTIVE_MONSTER = "ACTIVE_MONSTER";

    private AdventureEncounterMonster activeMonster;
    private RecyclerView MonsterInfoSpecialAbilitiesRv, MonsterInfoActionsRv, LegendaryActionsRv;
	private Button ActionsHideBt;
	private Button SpecialAbilitiesHideBt, LegendaryActionHideBt;
    private ToggleButton HpModTypeTb;
    private boolean hidingActions, hidingAbilities, hidingLegendaryActions;
    private EditText MonsterCurrentHpEt;

    public MonsterTarget() {
        // Required empty public constructor
    }

    public static MonsterTarget newInstance(AdventureEncounterMonster activeMonster) {
        MonsterTarget fragment = new MonsterTarget();
        Bundle args = new Bundle();
        args.putSerializable(ACTIVE_MONSTER, activeMonster);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            activeMonster = (AdventureEncounterMonster)getArguments().getSerializable(ACTIVE_MONSTER);
        }
    }

    @Override
    public View onCreateView(
		    @NonNull LayoutInflater inflater, ViewGroup container,
		    Bundle savedInstanceState
                            )
    {

        View v = inflater.inflate(R.layout.fragment_monster_target, container, false);
        //monster portrait
	    ImageView monsterInfoPortraitIv = v.findViewById(R.id.MonsterInfoPortraitIv);
	    activeMonster.getToken().makePortrait(monsterInfoPortraitIv);

        //fill in monster info
	    TextView monsterInfoNameTv = v.findViewById(R.id.MonsterInfoNameTv);
	    monsterInfoNameTv.setText(activeMonster.getMonster().getName());
	    TextView monsterInfoSizeTv = v.findViewById(R.id.MonsterInfoSizeTv);
	    monsterInfoSizeTv.setText(activeMonster.getMonster().getMonsterSize());
	    TextView monsterInfoTypeTv = v.findViewById(R.id.MonsterInfoTypeTv);
	    monsterInfoTypeTv.setText(activeMonster.getMonster().getMonsterType());
	    TextView monsterInfoAcTv = v.findViewById(R.id.MonsterInfoAcTv);
	    monsterInfoAcTv.setText(String.format(Locale.getDefault(), "%d", activeMonster.getMonster()
			    .getAc()));
        //monster stats block
	    TextView monsterInfoStrTv = v.findViewById(R.id.MonsterInfoStrTv);
	    monsterInfoStrTv.setText(activeMonster.getMonster().getStrMod());
	    TextView monsterInfoDexTv = v.findViewById(R.id.MonsterInfoDexTv);
	    monsterInfoDexTv.setText(activeMonster.getMonster().getDexMod());
	    TextView monsterInfoConTv = v.findViewById(R.id.MonsterInfoConTv);
	    monsterInfoConTv.setText(activeMonster.getMonster().getConMod());
	    TextView monsterInfoIntTv = v.findViewById(R.id.MonsterInfoIntTv);
	    monsterInfoIntTv.setText(activeMonster.getMonster().getIntelMod());
	    TextView monsterInfoWisTv = v.findViewById(R.id.MonsterInfoWisTv);
	    monsterInfoWisTv.setText(activeMonster.getMonster().getWisMod());
	    TextView monsterInfoChaTv = v.findViewById(R.id.MonsterInfoChaTv);
	    monsterInfoChaTv.setText(activeMonster.getMonster().getChaMod());
        TextView MonsterInfoSpeed = v.findViewById(R.id.MonsterInfoSpeed);
	    MonsterInfoSpeed.setText(activeMonster.getMonster().getSpeed());
	    TextView AlignmentTxt = v.findViewById(R.id.AlignmentTxt);
	    AlignmentTxt.setText(activeMonster.getMonster().getAlignment());
	    TextView ResistancesTv = v.findViewById(R.id.ResistancesTv);
	    ResistancesTv.setText(activeMonster.getMonster().getResistances());
	    TextView ImmunitiesTv = v.findViewById(R.id.ImmunitiesTv);
	    ImmunitiesTv.setText(activeMonster.getMonster().getImmunities());
	    TextView VulnerabilitiesTxt = v.findViewById(R.id.VulnerabilitiesTxt);
	    VulnerabilitiesTxt.setText(activeMonster.getMonster().getVulnerabilities());
	    TextView LanguagesTv = v.findViewById(R.id.LanguagesTv);
	    LanguagesTv.setText(activeMonster.getMonster().getLanguages());
	    TextView SensesTv = v.findViewById(R.id.SensesTv);
	    SensesTv.setText(activeMonster.getMonster().getSenses());
        //special abilities
        MonsterInfoSpecialAbilitiesRv = v.findViewById(R.id.MonsterInfoSpecialAbilitiesRv);
	    MonsterAbilitiesListAdapter monsterAbilitiesListAdapter
			    = new MonsterAbilitiesListAdapter(getContext());
	    monsterAbilitiesListAdapter
			    .setMonsterAbilityList(activeMonster.getMonster().getSpecialAbilities());
        MonsterInfoSpecialAbilitiesRv.setAdapter(monsterAbilitiesListAdapter);
        MonsterInfoSpecialAbilitiesRv.setLayoutManager(new LinearLayoutManager(getContext()));
        //actions
        MonsterInfoActionsRv = v.findViewById(R.id.MonsterInfoActionsRv);
	    MonsterAbilitiesListAdapter monsterActionsListAdapter
			    = new MonsterAbilitiesListAdapter(getContext());
        monsterActionsListAdapter.setMonsterAbilityList(activeMonster.getMonster().getActions());
        MonsterInfoActionsRv.setAdapter(monsterActionsListAdapter);
        MonsterInfoActionsRv.setLayoutManager(new LinearLayoutManager(getContext()));
        //legendary Actions
	    LegendaryActionsRv = v.findViewById(R.id.LegendaryActionsRv);
	    MonsterAbilitiesListAdapter monsterLegendaryActionsListAdapter
			    = new MonsterAbilitiesListAdapter(getContext());
	    monsterLegendaryActionsListAdapter.setMonsterAbilityList(activeMonster.getMonster().getLegendaryAbilities());
	    LegendaryActionsRv.setAdapter(monsterLegendaryActionsListAdapter);
	    LegendaryActionsRv.setLayoutManager(new LinearLayoutManager(getContext()));
        //hide buttons
        hidingActions = hidingAbilities = hidingLegendaryActions = false;
        SpecialAbilitiesHideBt = v.findViewById(R.id.SpecialAbilitiesHideBt);
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
        ActionsHideBt = v.findViewById(R.id.ActionsHideBt);
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
	    LegendaryActionHideBt = v.findViewById(R.id.LegendaryActionHideBt);
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
        MonsterCurrentHpEt = v.findViewById(R.id.MonsterCurrentHpEt);
	    MonsterCurrentHpEt.setText(String.format(Locale.getDefault(), "%d", activeMonster.getHp()));
        //hp modifier widget
        HpModTypeTb = v.findViewById(R.id.HpModTypeTb);
	    Button hpmod1 = v.findViewById(R.id.hpmod1);
        hpmod1.setOnClickListener(this);
	    Button hpmod2 = v.findViewById(R.id.hpmod2);
        hpmod2.setOnClickListener(this);
	    Button hpmod3 = v.findViewById(R.id.hpmod3);
        hpmod3.setOnClickListener(this);
	    Button hpmod4 = v.findViewById(R.id.hpmod4);
        hpmod4.setOnClickListener(this);
	    Button hpmod5 = v.findViewById(R.id.hpmod5);
        hpmod5.setOnClickListener(this);
	    Button hpmod6 = v.findViewById(R.id.hpmod6);
        hpmod6.setOnClickListener(this);
	    Button hpmod7 = v.findViewById(R.id.hpmod7);
        hpmod7.setOnClickListener(this);
	    Button hpmod8 = v.findViewById(R.id.hpmod8);
        hpmod8.setOnClickListener(this);
	    Button hpmod9 = v.findViewById(R.id.hpmod9);
        hpmod9.setOnClickListener(this);
	    Button hpmod10 = v.findViewById(R.id.hpmod10);
        hpmod10.setOnClickListener(this);
        //finish monster buttons
	    Button finishMonsterBt = v.findViewById(R.id.FinishMonsterBt);
	    finishMonsterBt.setOnClickListener(new View.OnClickListener()
	    {
            @Override
            public void onClick(View v) {
                activeMonster.setHp(Integer.parseInt(MonsterCurrentHpEt.getText().toString()));
                mListener.onMonsterCompletedListener(activeMonster);
            }
        });
	    Button markMonsterDeadBt = v.findViewById(R.id.MarkMonsterDeadBt);
	    markMonsterDeadBt.setOnClickListener(new View.OnClickListener()
	    {
            @Override
            public void onClick(View v) {
                activeMonster.setHp(0);
                activeMonster.setStatus(AdventureEncounterActor.DEAD);
                mListener.onMonsterCompletedListener(activeMonster);
            }
        });
        return v;
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
            if(curHp > activeMonster.getMaxHp())
                curHp = activeMonster.getMaxHp();
        }
	    MonsterCurrentHpEt.setText(String.format(Locale.getDefault(), "%d", curHp));
    }

    public interface OnMonsterCompletedListener
    {
	    void onMonsterCompletedListener(AdventureEncounterMonster activeMonster);
    }

    private MonsterTarget.OnMonsterCompletedListener mListener;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (MonsterTarget.OnMonsterCompletedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnArticleSelectedListener");
        }
    }
}
