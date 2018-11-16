package net.kalinec.dndencounters.monsters;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.widget.ImageView;

import net.kalinec.dndencounters.lib.SelectableItem;

import java.io.Serializable;
import java.util.Objects;

public class MonsterToken implements Serializable, SelectableItem
{
    public static final int TOKEN_TYPE_COLORED_NUMBER = 1;
    public static final int TOKEN_TYPE_NUMBER = 2;
    public static final int TOKEN_TYPE_MINI = 3;
    public static final int TOKEN_TYPE_COLOR = 4;
    private Bitmap miniPortrait;
    private String tokenName;
    private int tokenType;
    private Color tokenColor;
    private int tokenNumber;
    private boolean isSelected = false;

    public MonsterToken(int tokenType)
    {
        this.tokenType = tokenType;
    }

    public Bitmap getMiniPortrait() {
        return miniPortrait;
    }

    public void setMiniPortrait(Bitmap miniPortrait) {
        this.miniPortrait = miniPortrait;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public Color getTokenColor() {
        return tokenColor;
    }

    public void setTokenColor(Color tokenColor) {
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
                Objects.equals(miniPortrait, that.miniPortrait) &&
                Objects.equals(tokenName, that.tokenName) &&
                Objects.equals(tokenColor, that.tokenColor);
    }

    @Override
    public int hashCode() {

        return Objects.hash(miniPortrait, tokenName, tokenType, tokenColor, tokenNumber);
    }

    @Override
    public String toString() {
        return "MonsterToken{" +
                "miniPortrait=" + miniPortrait +
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

    public Bitmap drawText(String text, int textWidth, int textColor, int backgroudColor)
    {

        // Get text dimensions
        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(textColor);
        textPaint.setTextSize(30);

        StaticLayout mTextLayout = new StaticLayout(text, textPaint, textWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);

        // Create bitmap and canvas to draw to
        Bitmap b = Bitmap.createBitmap(textWidth, mTextLayout.getHeight(), Bitmap.Config.ARGB_4444);
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
            holder.setImageBitmap(miniPortrait);
        }
        else if(tokenType == MonsterToken.TOKEN_TYPE_NUMBER)
        {
            holder.setImageBitmap(drawText(Integer.toString(tokenNumber), 20, Color.BLACK, Color.WHITE));
        }
        else if(tokenType == MonsterToken.TOKEN_TYPE_COLORED_NUMBER)
        {
            holder.setImageBitmap(drawText(Integer.toString(tokenNumber), 20, Color.BLACK, tokenColor.toArgb()));
        }
        else if(tokenType == MonsterToken.TOKEN_TYPE_COLOR)
        {
            holder.setBackgroundColor(tokenColor.toArgb());
        }
    }
}