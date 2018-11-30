package net.kalinec.dndencounters.activities.spells;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import net.kalinec.dndencounters.R;
import net.kalinec.dndencounters.spells.Spell;

public class ViewSpell extends AppCompatActivity {
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
		assert bundle != null;
		Spell selectedSpell = (Spell) bundle.getSerializable(Spell.PASSED_SPELL);
        setContentView(R.layout.activity_view_spell);
		
		TextView pageTv = findViewById(R.id.PageTv);
		assert selectedSpell != null;
		pageTv.setText(selectedSpell.getPage());
		TextView spellNameTv = findViewById(R.id.SpellNameTv);
		spellNameTv.setText(selectedSpell.getName());
		TextView spellDescriptionTv = findViewById(R.id.SpellDescriptionTv);
		spellDescriptionTv.setText(Html.fromHtml(selectedSpell
				                                         .getDescription(), Html.FROM_HTML_MODE_COMPACT));
		TextView spellComponentsTv = findViewById(R.id.SpellComponentsTv);
		spellComponentsTv.setText(selectedSpell.getComponents());
		TextView spellConcentrationTv = findViewById(R.id.SpellConcentrationTv);
		spellConcentrationTv.setVisibility(selectedSpell
				                                   .isConcentration() ? View.VISIBLE : View.GONE);
		TextView spellRitualTv = findViewById(R.id.SpellRitualTv);
		spellRitualTv.setVisibility(selectedSpell.isRitual() ? View.VISIBLE : View.GONE);
		TextView higherLevelTv = findViewById(R.id.HigherLevelTv);
		if (selectedSpell.getHigher_level() == null)
			higherLevelTv.setVisibility(View.GONE);
		else
		{
			higherLevelTv.setVisibility(View.VISIBLE);
			higherLevelTv.setText(Html.fromHtml(selectedSpell
					                                    .getHigher_level(), Html.FROM_HTML_MODE_COMPACT));
		}
		TextView spellDurationTv = findViewById(R.id.SpellDurationTv);
		spellDurationTv.setText(selectedSpell.getDuration());
		TextView castingTimeTv = findViewById(R.id.CastingTimeTv);
		castingTimeTv.setText(selectedSpell.getCasting_time());
		TextView spellLevelTv = findViewById(R.id.SpellLevelTv);
		spellLevelTv.setText(selectedSpell.getLevel());
		TextView SPellClassesTv = findViewById(R.id.SPellClassesTv);
		SPellClassesTv.setText(selectedSpell.getSpellClass());
		TextView spellSchoolTv = findViewById(R.id.SpellSchoolTv);
		spellSchoolTv.setText(selectedSpell.getSchool());

    }
}
