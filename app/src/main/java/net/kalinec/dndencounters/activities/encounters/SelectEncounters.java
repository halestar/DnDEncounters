package net.kalinec.dndencounters.activities.encounters;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.encounters.Encounter;
import net.kalinec.dndencounters.encounters.Encounters;
import net.kalinec.dndencounters.lib.RvSelectListener;
import net.kalinec.dndencounters.lib.SelectableAdapter;
import net.kalinec.dndencounters.lib.SelectableItem;
import net.kalinec.dndencounters.monsters.Monster;

import java.util.ArrayList;
import java.util.List;

public class SelectEncounters extends AppCompatActivity {

    public static final int REQUEST_ENCOUNTER_LIST = 44;
    private RecyclerView encounterListRv;
    private SelectableAdapter adapter;
    private List<SelectableItem> encounters, selectedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_encounters);
        encounterListRv = findViewById(R.id.encounterListRv);
        encounterListRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        encounters = Encounters.getAllAsSelectibles(getApplicationContext());
        Log.d("SelecEncounters", "encounter as selectible passes: " + encounters);
        adapter = new SelectableAdapter(encounters, true, new RvSelectListener() {
            @Override
            public void onItemSelected(SelectableItem item) {
                selectedItems = adapter.getSelectedItems();
            }
        });
        encounterListRv.setAdapter(adapter);
    }

    public void returnSelectedEncounter(View w)
    {
        ArrayList<Encounter> selectedEncounters = new ArrayList<>();
        for(SelectableItem s: selectedItems)
            selectedEncounters.add((Encounter)s);
        Intent data = new Intent();
        data.putExtra(Encounter.PASSED_ENCOUNTERS, selectedEncounters);
        setResult(RESULT_OK, data);
        finish();
    }
}
