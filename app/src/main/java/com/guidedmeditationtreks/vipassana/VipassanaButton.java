package com.guidedmeditationtreks.vipassana;

import android.app.ActionBar;
import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by czar on 3/2/18.
 */

public class VipassanaButton extends AppCompatButton {

    public VipassanaButton(Context context, AttributeSet attrs, String text, int tag) {
        super(context, attrs);
        this.setText(text);
        this.setTag(tag);
    }

}
