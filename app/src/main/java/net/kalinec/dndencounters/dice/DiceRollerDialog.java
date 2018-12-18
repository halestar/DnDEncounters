package net.kalinec.dndencounters.dice;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import net.kalinec.dndencounters.R;

public class DiceRollerDialog extends AppCompatActivity implements View.OnClickListener {

    public static final String PASSED_ROLL = "PASSED_ROLL";
    public static final int REQUEST_DICE_ROLL = 100;
    private ToggleButton DiceNum1, DiceNum2, DiceNum3, DiceNum4, DiceNum5;
    private ToggleButton DiceNum6, DiceNum7, DiceNum8, DiceNum9, DiceNum10;
    private ToggleButton DiceD4, DiceD6, DiceD8, DiceD10, DiceD12, DiceD20;
    private EditText RollModifierEt;
    private String diceNum, diceType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_dice_roller);
        DiceNum1 = findViewById(R.id.DiceNum1);
        DiceNum2 = findViewById(R.id.DiceNum2);
        DiceNum3 = findViewById(R.id.DiceNum3);
        DiceNum4 = findViewById(R.id.DiceNum4);
        DiceNum5 = findViewById(R.id.DiceNum5);
        DiceNum6 = findViewById(R.id.DiceNum6);
        DiceNum7 = findViewById(R.id.DiceNum7);
        DiceNum8 = findViewById(R.id.DiceNum8);
        DiceNum9 = findViewById(R.id.DiceNum9);
        DiceNum10 = findViewById(R.id.DiceNum10);
        DiceNum1.setOnClickListener(this);
        DiceNum2.setOnClickListener(this);
        DiceNum3.setOnClickListener(this);
        DiceNum4.setOnClickListener(this);
        DiceNum5.setOnClickListener(this);
        DiceNum6.setOnClickListener(this);
        DiceNum7.setOnClickListener(this);
        DiceNum8.setOnClickListener(this);
        DiceNum9.setOnClickListener(this);
        DiceNum10.setOnClickListener(this);

        DiceD4 = findViewById(R.id.DiceD4);
        DiceD6 = findViewById(R.id.DiceD6);
        DiceD8 = findViewById(R.id.DiceD8);
        DiceD10 = findViewById(R.id.DiceD10);
        DiceD12 = findViewById(R.id.DiceD12);
        DiceD20 = findViewById(R.id.DiceD20);
        DiceD4.setOnClickListener(this);
        DiceD6.setOnClickListener(this);
        DiceD8.setOnClickListener(this);
        DiceD10.setOnClickListener(this);
        DiceD12.setOnClickListener(this);
        DiceD20.setOnClickListener(this);

        RollModifierEt = findViewById(R.id.RollModifierEt);
        diceNum = "1";
        Button rollBt = findViewById(R.id.RollBt);
        rollBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeDice();
            }
        });
        Button cancelBtn = findViewById(R.id.CancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelRoll();
            }
        });
    }


    @Override
    public void onClick(View v) {
        if(v == DiceNum1)
        {
            DiceNum2.setChecked(false);
            DiceNum3.setChecked(false);
            DiceNum4.setChecked(false);
            DiceNum5.setChecked(false);
            DiceNum6.setChecked(false);
            DiceNum7.setChecked(false);
            DiceNum8.setChecked(false);
            DiceNum9.setChecked(false);
            DiceNum10.setChecked(false);
            diceNum = "1";
        }
        else if(v == DiceNum2)
        {
            DiceNum1.setChecked(false);
            DiceNum3.setChecked(false);
            DiceNum4.setChecked(false);
            DiceNum5.setChecked(false);
            DiceNum6.setChecked(false);
            DiceNum7.setChecked(false);
            DiceNum8.setChecked(false);
            DiceNum9.setChecked(false);
            DiceNum10.setChecked(false);
            diceNum = "2";
        }
        else if(v == DiceNum3)
        {
            DiceNum1.setChecked(false);
            DiceNum2.setChecked(false);
            DiceNum4.setChecked(false);
            DiceNum5.setChecked(false);
            DiceNum6.setChecked(false);
            DiceNum7.setChecked(false);
            DiceNum8.setChecked(false);
            DiceNum9.setChecked(false);
            DiceNum10.setChecked(false);
            diceNum = "3";
        }
        else if(v == DiceNum4)
        {
            DiceNum1.setChecked(false);
            DiceNum3.setChecked(false);
            DiceNum2.setChecked(false);
            DiceNum5.setChecked(false);
            DiceNum6.setChecked(false);
            DiceNum7.setChecked(false);
            DiceNum8.setChecked(false);
            DiceNum9.setChecked(false);
            DiceNum10.setChecked(false);
            diceNum = "4";
        }
        else if(v == DiceNum5)
        {
            DiceNum1.setChecked(false);
            DiceNum3.setChecked(false);
            DiceNum4.setChecked(false);
            DiceNum2.setChecked(false);
            DiceNum6.setChecked(false);
            DiceNum7.setChecked(false);
            DiceNum8.setChecked(false);
            DiceNum9.setChecked(false);
            DiceNum10.setChecked(false);
            diceNum = "5";
        }
        else if(v == DiceNum6)
        {
            DiceNum1.setChecked(false);
            DiceNum3.setChecked(false);
            DiceNum4.setChecked(false);
            DiceNum2.setChecked(false);
            DiceNum5.setChecked(false);
            DiceNum7.setChecked(false);
            DiceNum8.setChecked(false);
            DiceNum9.setChecked(false);
            DiceNum10.setChecked(false);
            diceNum = "6";
        }
        else if(v == DiceNum7)
        {
            DiceNum1.setChecked(false);
            DiceNum3.setChecked(false);
            DiceNum4.setChecked(false);
            DiceNum2.setChecked(false);
            DiceNum5.setChecked(false);
            DiceNum6.setChecked(false);
            DiceNum8.setChecked(false);
            DiceNum9.setChecked(false);
            DiceNum10.setChecked(false);
            diceNum = "7";
        }
        else if(v == DiceNum8)
        {
            DiceNum1.setChecked(false);
            DiceNum3.setChecked(false);
            DiceNum4.setChecked(false);
            DiceNum2.setChecked(false);
            DiceNum5.setChecked(false);
            DiceNum7.setChecked(false);
            DiceNum6.setChecked(false);
            DiceNum9.setChecked(false);
            DiceNum10.setChecked(false);
            diceNum = "8";
        }
        else if(v == DiceNum9)
        {
            DiceNum1.setChecked(false);
            DiceNum3.setChecked(false);
            DiceNum4.setChecked(false);
            DiceNum2.setChecked(false);
            DiceNum5.setChecked(false);
            DiceNum7.setChecked(false);
            DiceNum8.setChecked(false);
            DiceNum6.setChecked(false);
            DiceNum10.setChecked(false);
            diceNum = "9";
        }
        else if(v == DiceNum10)
        {
            DiceNum1.setChecked(false);
            DiceNum3.setChecked(false);
            DiceNum4.setChecked(false);
            DiceNum2.setChecked(false);
            DiceNum5.setChecked(false);
            DiceNum7.setChecked(false);
            DiceNum8.setChecked(false);
            DiceNum9.setChecked(false);
            DiceNum6.setChecked(false);
            diceNum = "10";
        }
        else if(v == DiceD4)
        {
            DiceD6.setChecked(false);
            DiceD8.setChecked(false);
            DiceD10.setChecked(false);
            DiceD12.setChecked(false);
            DiceD20.setChecked(false);
            diceType = "d4";
        }
        else if(v == DiceD6)
        {
            DiceD4.setChecked(false);
            DiceD8.setChecked(false);
            DiceD10.setChecked(false);
            DiceD12.setChecked(false);
            DiceD20.setChecked(false);
            diceType = "d6";
        }
        else if(v == DiceD8)
        {
            DiceD6.setChecked(false);
            DiceD4.setChecked(false);
            DiceD10.setChecked(false);
            DiceD12.setChecked(false);
            DiceD20.setChecked(false);
            diceType = "d8";
        }
        else if(v == DiceD10)
        {
            DiceD6.setChecked(false);
            DiceD8.setChecked(false);
            DiceD4.setChecked(false);
            DiceD12.setChecked(false);
            DiceD20.setChecked(false);
            diceType = "d10";
        }
        else if(v == DiceD12)
        {
            DiceD6.setChecked(false);
            DiceD8.setChecked(false);
            DiceD10.setChecked(false);
            DiceD4.setChecked(false);
            DiceD20.setChecked(false);
            diceType = "d12";
        }
        else if(v == DiceD20)
        {
            DiceD6.setChecked(false);
            DiceD8.setChecked(false);
            DiceD10.setChecked(false);
            DiceD12.setChecked(false);
            DiceD4.setChecked(false);
            diceType = "d20";
        }
    }

    public void makeDice()
    {
        if(DiceNum1.isChecked() || DiceNum2.isChecked() || DiceNum3.isChecked() || DiceNum4.isChecked() || DiceNum5.isChecked() ||
                DiceNum6.isChecked() || DiceNum7.isChecked() || DiceNum8.isChecked() || DiceNum9.isChecked() || DiceNum10.isChecked())
        {
            String result = diceNum;
            if(DiceD4.isChecked() || DiceD6.isChecked() || DiceD8.isChecked() || DiceD10.isChecked() || DiceD12.isChecked() || DiceD20.isChecked())
            {
                result += diceType;
                try
                {
                    int mod = Integer.parseInt(RollModifierEt.getText().toString());
                    if(mod > 0)
                        result += "+" + Integer.toString(mod);
                    else if(mod < 0)
                        result += "-" + Integer.toString(mod);
                    Intent data = new Intent();
                    data.putExtra(DiceRollerDialog.PASSED_ROLL, result);
                    setResult(RESULT_OK, data);
                    finish();
                }
                catch(NumberFormatException e)
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "You enter a vaild number for the modifier", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
            else
            {
                Toast toast = Toast.makeText(getApplicationContext(), "You must select the type of dice!", Toast.LENGTH_LONG);
                toast.show();
            }
        }
        else
        {
            Toast toast = Toast.makeText(getApplicationContext(), "You must select the number of dice!", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void cancelRoll()
    {
        Intent data = new Intent();
        setResult(RESULT_CANCELED);
        finish();
    }
}
