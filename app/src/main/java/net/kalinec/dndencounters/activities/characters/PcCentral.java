package net.kalinec.dndencounters.activities.characters;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import net.kalinec.dndencounters.DnDEncountersActivity;
import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.characters.PcDisplayListAdapter;
import net.kalinec.dndencounters.playsessions.PlaySession;

public class PcCentral extends DnDEncountersActivity
{
	private PlaySession activeSession;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		assert bundle != null;
		activeSession = (PlaySession)bundle.getSerializable(PlaySession.PASSED_SESSION);
		setContentView(R.layout.activity_pc_central);

		RecyclerView PartyRv = findViewById(R.id.PartyRv);
		PcDisplayListAdapter pcDisplayListAdapter = new PcDisplayListAdapter(getApplicationContext());
		pcDisplayListAdapter.setCharacterList(activeSession.getPlayers().getMembers());
		PartyRv.setAdapter(pcDisplayListAdapter);
		PartyRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
	}
}
