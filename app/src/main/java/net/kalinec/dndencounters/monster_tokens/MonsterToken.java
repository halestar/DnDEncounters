package net.kalinec.dndencounters.monster_tokens;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.widget.ImageView;

import net.kalinec.dndencounters.lib.SelectableItem;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class MonsterToken implements Serializable, SelectableItem
{
    public static final int TOKEN_TYPE_COLORED_NUMBER = 1;
    public static final int TOKEN_TYPE_NUMBER = 2;
    public static final int TOKEN_TYPE_MINI = 3;
    public static final int TOKEN_TYPE_COLOR = 4;
    public static final String PASSED_MONSTER_TOKEN = "PASSED_MONSTER_TOKEN";
    private byte[] miniPortrait;
    private String tokenName = "Random Token";
    private int tokenType;
    private int tokenColor = Color.WHITE;
    private int tokenNumber = 0;
    private boolean isSelected = false;

    public MonsterToken(int tokenType)
    {
        this.tokenType = tokenType;
    }

    public Bitmap getMiniPortrait() {
        return BitmapFactory.decodeByteArray(miniPortrait, 0, miniPortrait.length);
    }

    public void setMiniPortrait(Bitmap miniPortrait) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        miniPortrait.compress(Bitmap.CompressFormat.PNG, 100, stream);
        this.miniPortrait = stream.toByteArray();
    }

    public String getTokenName() {
        if(tokenName == null)
            return "";
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public int getTokenColor() {
        return tokenColor;
    }

    public void setTokenColor(int tokenColor) {
        this.tokenColor = tokenColor;
    }

    public int getTokenNumber() {
        return tokenNumber;
    }

    public void setTokenNumber(int tokenNumber) {
        this.tokenNumber = tokenNumber;
    }

    public int getTokenType() {
        return tokenType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonsterToken that = (MonsterToken) o;
        return tokenType == that.tokenType &&
                tokenNumber == that.tokenNumber &&
               Arrays.equals(miniPortrait, that.miniPortrait) &&
               Objects.equals(tokenName, that.tokenName) &&
               Objects.equals(tokenColor, that.tokenColor);
    }

    @Override
    public int hashCode() {

        return Objects.hash(miniPortrait, tokenName, tokenType, tokenColor, tokenNumber);
    }

    @NonNull
    @Override
    public String toString() {
        return "MonsterToken{" +
               "miniPortrait=" + Arrays.toString(miniPortrait) +
               ", tokenName='" + tokenName + '\'' +
               ", tokenType=" + tokenType +
               ", tokenColor=" + tokenColor +
               ", tokenNumber=" + tokenNumber +
               '}';
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public String getSelectableText() {
        return tokenName;
    }
	
	private Bitmap drawText(String text, int backgroudColor)
    {

        // Get text dimensions
        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        textPaint.setStyle(Paint.Style.FILL);
	    textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(30);
	
	    StaticLayout mTextLayout
			    = new StaticLayout(text, textPaint, 64, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);

        // Create bitmap and canvas to draw to
	    Bitmap b = Bitmap.createBitmap(64, mTextLayout.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas c = new Canvas(b);

        // Draw background
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(backgroudColor);
        c.drawPaint(paint);

        // Draw text
        c.save();
        c.translate(0, 0);
        mTextLayout.draw(c);
        c.restore();

        return b;
    }

    public void makePortrait(ImageView holder)
    {
        if(tokenType == MonsterToken.TOKEN_TYPE_MINI)
        {
            holder.setImageBitmap(getMiniPortrait());
        }
        else if(tokenType == MonsterToken.TOKEN_TYPE_NUMBER)
        {
	        holder.setImageBitmap(drawText(Integer.toString(tokenNumber), Color.WHITE));
        }
        else if(tokenType == MonsterToken.TOKEN_TYPE_COLORED_NUMBER)
        {
	        holder.setImageBitmap(drawText(Integer.toString(tokenNumber), tokenColor));
        }
        else if(tokenType == MonsterToken.TOKEN_TYPE_COLOR)
        {
            holder.setBackgroundColor(tokenColor);
        }
    }
}
