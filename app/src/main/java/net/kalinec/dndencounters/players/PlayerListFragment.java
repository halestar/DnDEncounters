package net.kalinec.dndencounters.players;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.kalinec.dndencounters.R;

import java.util.List;

public class PlayerListFragment extends Fragment
{
	private PlayerListAdapter playerListAdapter;
	private PlayerViewModel playerViewModel;
	private Context context;
	
	public static  PlayerListFragment newInstance()
	{
		return new PlayerListFragment();
	}
	
	@Override
	public void onAttach(Context context)
	{
		super.onAttach(context);
		this.context = context;
		playerListAdapter = new PlayerListAdapter(context);
	}
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initData();
	}
	
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_players, container, false);
		RecyclerView recyclerView = view.findViewById(R.id.recyclerview_players);
		recyclerView.setAdapter(playerListAdapter);
		recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
		recyclerView.setLayoutManager(new LinearLayoutManager(context));
		return view;
	}
	
	private void initData()
	{
		playerViewModel = ViewModelProviders.of(this).get(PlayerViewModel.class);
		playerViewModel.getPlayerList().observe(this, new Observer<List<Player>>()
		{
			@Override
			public void onChanged(@Nullable List<Player> players) {
				playerListAdapter.setPlayerList(players);
			}
		});
	}
	
	public void removeData() {
		if (playerViewModel != null) {
			playerViewModel.deleteAll();
		}
	}
}
