package net.kalinec.dndencounters.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.adventure_encounters.AdventureEncounterMonster;
import net.kalinec.dndencounters.monsters.MonsterAbilitiesListAdapter;

public class MonsterTarget extends Fragment {

    private static final String ACTIVE_MONSTER = "ACTIVE_MONSTER";

    private AdventureEncounterMonster activeMonster;
    private TextView MonsterInfoNameTv, MonsterInfoSizeTv, MonsterInfoTypeTv, MonsterInfoAcTv, MonsterInfoStrTv, MonsterInfoDexTv,
            MonsterInfoConTv, MonsterInfoIntTv, MonsterInfoWisTv, MonsterInfoChaTv;
    private ImageView MonsterInfoPortraitIv;
    private RecyclerView MonsterInfoSpecialAbilitiesRv, MonsterInfoActionsRv;
    private MonsterAbilitiesListAdapter monsterAbilitiesListAdapter, monsterActionsListAdapter;
    private Button ActionsHideBt, SpecialAbilitiesHideBt;
    private boolean hidingActions, hidingAbilities;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_monster_target, container, false);
        //monster portrait
        MonsterInfoPortraitIv = v.findViewById(R.id.MonsterInfoPortraitIv);
        activeMonster.getToken().makePortrait(MonsterInfoPortraitIv);

        //fill in monster info
        MonsterInfoNameTv = v.findViewById(R.id.MonsterInfoNameTv);
        MonsterInfoNameTv.setText(activeMonster.getMonster().getName());
        MonsterInfoSizeTv = v.findViewById(R.id.MonsterInfoSizeTv);
        MonsterInfoSizeTv.setText(activeMonster.getMonster().getMonsterSize());
        MonsterInfoTypeTv = v.findViewById(R.id.MonsterInfoTypeTv);
        MonsterInfoTypeTv.setText(activeMonster.getMonster().getMonsterType());
        MonsterInfoAcTv = v.findViewById(R.id.MonsterInfoAcTv);
        MonsterInfoAcTv.setText(Integer.toString(activeMonster.getMonster().getAc()));
        //monster stats block
        MonsterInfoStrTv = v.findViewById(R.id.MonsterInfoStrTv);
        MonsterInfoStrTv.setText(activeMonster.getMonster().getStrMod());
        MonsterInfoDexTv = v.findViewById(R.id.MonsterInfoDexTv);
        MonsterInfoDexTv.setText(activeMonster.getMonster().getDexMod());
        MonsterInfoConTv = v.findViewById(R.id.MonsterInfoConTv);
        MonsterInfoConTv.setText(activeMonster.getMonster().getDexMod());
        MonsterInfoIntTv = v.findViewById(R.id.MonsterInfoIntTv);
        MonsterInfoIntTv.setText(activeMonster.getMonster().getIntelMod());
        MonsterInfoWisTv = v.findViewById(R.id.MonsterInfoWisTv);
        MonsterInfoWisTv.setText(activeMonster.getMonster().getWisMod());
        MonsterInfoChaTv = v.findViewById(R.id.MonsterInfoChaTv);
        MonsterInfoChaTv.setText(activeMonster.getMonster().getChaMod());
        //special abilities
        MonsterInfoSpecialAbilitiesRv = v.findViewById(R.id.MonsterInfoSpecialAbilitiesRv);
        monsterAbilitiesListAdapter = new MonsterAbilitiesListAdapter(getContext());
        monsterAbilitiesListAdapter.setMonsterAbilityList(activeMonster.getMonster().getSpecialAbilities());
        MonsterInfoSpecialAbilitiesRv.setAdapter(monsterAbilitiesListAdapter);
        MonsterInfoSpecialAbilitiesRv.setLayoutManager(new LinearLayoutManager(getContext()));
        //actions
        MonsterInfoActionsRv = v.findViewById(R.id.MonsterInfoActionsRv);
        monsterActionsListAdapter = new MonsterAbilitiesListAdapter(getContext());
        monsterActionsListAdapter.setMonsterAbilityList(activeMonster.getMonster().getActions());
        MonsterInfoActionsRv.setAdapter(monsterActionsListAdapter);
        MonsterInfoActionsRv.setLayoutManager(new LinearLayoutManager(getContext()));
        //hide buttons
        hidingActions = hidingAbilities = false;
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
        return v;
    }
}
