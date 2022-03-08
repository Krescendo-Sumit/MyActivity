package myactvity.mahyco.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Akash Namdev on 2019-08-13.
 */

@SuppressLint("AppCompatCustomView")
public class UnderLineTextView extends TextView {
    private boolean m_modifyingText = false;

    public UnderLineTextView(Context context) {
        super(context);
        init();
    }

    public UnderLineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UnderLineTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Do nothing here... we don't care
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Do nothing here... we don't care
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (m_modifyingText)
                    return;

                underlineText();
            }
        });

        underlineText();
    }

    private void underlineText() {
        if (m_modifyingText)
            return;

        m_modifyingText = true;

        SpannableString content = new SpannableString(getText());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        setText(content);

        m_modifyingText = false;
    }
}

