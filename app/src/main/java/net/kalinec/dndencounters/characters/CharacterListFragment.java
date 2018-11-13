package net.kalinec.dndencounters.characters;

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
import net.kalinec.dndencounters.players.Player;

import java.util.List;

public class CharacterListFragment extends Fragment
{
	private CharacterListAdapter characterListAdaper;
	private CharacterViewModel characterViewModel;
	private Context context;
	
	public static CharacterListFragment newInstance()
	{
		return new CharacterListFragment();
	}
	
	@Override
	public void onAttach(Context context)
	{
		super.onAttach(context);
		this.context = context;
		characterListAdaper = new CharacterListAdapter(context);
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
		View view = inflater.inflate(R.layout.content_view_players_characters, container, false);
		RecyclerView recyclerView = view.findViewById(R.id.recyclerview_characters);
		recyclerView.setAdapter(characterListAdaper);
		recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
		recyclerView.setLayoutManager(new LinearLayoutManager(context));
		return view;
	}
	
	private void initData()
	{
		characterViewModel = ViewModelProviders.of(this).get(CharacterViewModel.class);
		characterViewModel.getCharacterList().observe(this, new Observer<List<Character>>()
		{
			@Override
			public void onChanged(@Nullable List<Character> characters) {
				characterListAdaper.setCharacterList(characters);
			}
		});
	}
	
	public void removeData() {
		if (characterViewModel != null) {
			characterViewModel.deleteAll();
		}
	}
}
