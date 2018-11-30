package net.kalinec.dndencounters.monster_tokens;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.kalinec.dndencounters.R;

import java.util.List;

public class MonsterTokenSpinnerAdapter extends BaseAdapter
{
    private List<MonsterToken> monsterTokens;
    private Context context;
    
    MonsterTokenSpinnerAdapter(Context context)
    {
        this.context = context;
        monsterTokens = MonsterTokens.getAllMonsterTokens(context);
    }
    
    @Override
    public int getCount() {
        return monsterTokens.size();
    }

    @Override
    public Object getItem(int position) {
        return monsterTokens.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.getItem(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_list_monster_tokens, parent, false);
        MonsterToken monsterToken = (MonsterToken)getItem(position);
        TextView MonsterTokenName = convertView.findViewById(R.id.MonsterTokenName);
        ImageView MonsterTokenPortrait = convertView.findViewById(R.id.MonsterTokenPortrait);
        MonsterTokenName.setText(monsterToken.getTokenName());
        monsterToken.makePortrait(MonsterTokenPortrait);
        return convertView;
    }
}
