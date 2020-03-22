package com.example.kioskmainpage.Utilities.multistatetogglebutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.kioskmainpage.R;

import java.util.ArrayList;
import java.util.List;

public class MultiStateToggleButton extends ToggleButton {

    private static final String TAG = MultiStateToggleButton.class.getSimpleName();

    private static final String KEY_BUTTON_STATES = "button_states";
    private static final String KEY_INSTANCE_STATE = "instance_state";

    /**
     * A list of rendered buttons. Used to get state, among others
     */
    List<Button> buttons;

    /**
     * The specified texts
     */
    CharSequence[] texts;

    /**
     * If true, multiple buttons can be pressed at the same time
     */
    boolean mMultipleChoice = false;

    /**
     * The layout containing all buttons
     */
    private LinearLayout mainLayout;


    public MultiStateToggleButton(Context context) {
        super(context, null);
    }

    public MultiStateToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MultiStateToggleButton, 0, 0);
        try {
            CharSequence[] texts = a.getTextArray(R.styleable.MultiStateToggleButton_values);
            colorPressed = a.getColor(R.styleable.MultiStateToggleButton_mstbPrimaryColor, 0);
            colorNotPressed = a.getColor(R.styleable.MultiStateToggleButton_mstbSecondaryColor, 0);
            colorPressedText = a.getColor(R.styleable.MultiStateToggleButton_mstbColorPressedText, 0);
            colorPressedBackground = a.getColor(R.styleable.MultiStateToggleButton_mstbColorPressedBackground, 0);
            pressedBackgroundResource = a.getResourceId(R.styleable.MultiStateToggleButton_mstbColorPressedBackgroundResource, 0);
            colorNotPressedText = a.getColor(R.styleable.MultiStateToggleButton_mstbColorNotPressedText, 0);
            colorNotPressedBackground = a.getColor(R.styleable.MultiStateToggleButton_mstbColorNotPressedBackground, 0);
            notPressedBackgroundResource = a.getResourceId(R.styleable.MultiStateToggleButton_mstbColorNotPressedBackgroundResource, 0);

            int length = 0;
            if (texts != null) {
                length = texts.length;
            }
            setElements(texts, null, new boolean[length]);
        } finally {
            a.recycle();
        }
    }

    /**
     * If multiple choice is enabled, the user can select multiple
     * values simultaneously.
     *
     * @param enable
     */
    public void enableMultipleChoice(boolean enable) {
        this.mMultipleChoice = enable;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putBooleanArray(KEY_BUTTON_STATES, getStates());
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            setStates(bundle.getBooleanArray(KEY_BUTTON_STATES));
            state = bundle.getParcelable(KEY_INSTANCE_STATE);
        }
        super.onRestoreInstanceState(state);
    }

    /**
     * Set the enabled state of this MultiStateToggleButton, including all of its child buttons.
     *
     * @param enabled True if this view is enabled, false otherwise.
     */
    @Override
    public void setEnabled(boolean enabled) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.setEnabled(enabled);
        }
    }

    /**
     * Set multiple buttons with the specified texts and default
     * initial values. Initial states are allowed, but both
     * arrays must be of the same size.
     *
     * @param texts            An array of CharSequences for the buttons
     * @param imageResourceIds an optional icon to show, either text, icon or both needs to be set.
     * @param selected         The default value for the buttons
     */
    public void setElements(@Nullable CharSequence[] texts, int[] imageResourceIds, boolean[] selected) {
        this.texts = texts;
        final int OPTION_MAX_IN_ONE_LINE = 4;//수정으로 추가됨
        final int textCount = texts != null ? texts.length : 0;
        final int iconCount = imageResourceIds != null ? imageResourceIds.length : 0;
        final int elementCount = Math.max(textCount, iconCount);
        if (elementCount == 0) {
            return;
        }

        boolean enableDefaultSelection = true;
        if (selected == null || elementCount != selected.length) {
            enableDefaultSelection = false;
        }

        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER_VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        if (mainLayout == null) {
//            mainLayout = (LinearLayout) inflater.inflate(R.layout.view_multi_state_toggle_button, this, true);
//        }
        if (mainLayout == null) {
            mainLayout = (LinearLayout) inflater.inflate(R.layout.view_multi_state_toggle_button_container, this, true);
        }

        mainLayout.removeAllViews();

        this.buttons = new ArrayList<>(elementCount);
        LinearLayout subLayout = (LinearLayout) inflater.inflate(R.layout.view_multi_state_toggle_button, this, false);
        for (int i = 0; i < elementCount; i++) {
            Button b;

            //요청에 따라 버튼 모양 수정됨 123456->123
            //                                     456 3자리씩 끊어짐
//            if (i == 0) {//기존 버튼-> 123456
            if (i % OPTION_MAX_IN_ONE_LINE == 0) {
                // Add a special view when there's only one element
                subLayout = (LinearLayout) inflater.inflate(R.layout.view_multi_state_toggle_button, this, false);
                if (elementCount == 1) {
                    b = (Button) inflater.inflate(R.layout.view_single_toggle_button, subLayout, false);
                } else {
                    b = (Button) inflater.inflate(R.layout.view_left_toggle_button, subLayout, false);
                }
//            } else if (i == elementCount - 1) {//기존 버튼-> 123456
            } else if ((i == elementCount - 1) || i % OPTION_MAX_IN_ONE_LINE == 2) {
                b = (Button) inflater.inflate(R.layout.view_right_toggle_button, subLayout, false);
            } else {
                b = (Button) inflater.inflate(R.layout.view_center_toggle_button, subLayout, false);
            }
            b.setText(texts != null ? texts[i] : "");
            if (imageResourceIds != null && imageResourceIds[i] != 0) {
                b.setCompoundDrawablesWithIntrinsicBounds(imageResourceIds[i], 0, 0, 0);
            }
            final int position = i;
            b.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    setValue(position);
                }

            });

//            mainLayout.addView(b);
            subLayout.addView(b);//추가부분 200129
            if ((i == elementCount - 1) || i % OPTION_MAX_IN_ONE_LINE == 2) {//추가부분 200129
                if (elementCount > OPTION_MAX_IN_ONE_LINE && (i != elementCount - 1)) {//한줄에 표시되는것들은 padding하지 않음, 마지막은 padding 하지않음
                    LinearLayout.LayoutParams sub_params = (LinearLayout.LayoutParams) subLayout.getLayoutParams();
                    sub_params.bottomMargin = Math.round(getResources().getDimension(R.dimen.button_bottom_margin));
                    subLayout.setLayoutParams(sub_params);
                }
                mainLayout.addView(subLayout);//추가부분 200129
            }

            if (enableDefaultSelection) {
                setButtonState(b, selected[i]);
            }

            int index = 0;
            String temp = b.getText().toString();
            String sub1 = temp, sub2 = "";
            index = temp.length();
            if (temp.contains("\n")) {
                index = temp.indexOf("\n");
                sub1 = temp.substring(0, index);
                sub2 = temp.substring(index);
            }
            if (sub1.getBytes().length > 21) {
                int charlength = 0;
                int pos;
                for (pos = 0; charlength < 21 && pos < sub1.length(); pos++)
                    charlength += sub1.codePointAt(pos) > 0x00ff ? 3 : 1;
                String sub1_temp = sub1;
                sub1 = sub1.substring(0, pos);

                if (!sub1_temp.equals(sub1))
                    sub1 += "...";
                index = sub1.length();
            }

            Float textsize1 = 1.0f;
            if (sub1.getBytes().length > 17 && sub1.getBytes().length < 21) { //9 10
                textsize1 = 0.9f;
            } else if (sub1.getBytes().length >= 21 && sub1.getBytes().length < 24) { //11 12
                textsize1 = 0.8f;
            } else if (sub1.getBytes().length >= 24) { //13 14
                textsize1 = 0.7f;
            }
            temp = sub1 + sub2;
            b.setText(temp);
            Spannable ssb = (Spannable) b.getText();
            ssb.setSpan(new RelativeSizeSpan(textsize1), 0, index, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//크기조절
            ssb.setSpan(new StyleSpan(Typeface.BOLD), 0, index, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //bold처리
            this.buttons.add(b);
        }
        mainLayout.setBackgroundResource(R.drawable.button_section_shape);
    }

    /**
     * @return An array of the buttons' text
     */
    public CharSequence[] getTexts() {
        return this.texts;
    }

    /**
     * Set multiple buttons with the specified texts and default
     * initial values. Initial states are allowed, but both
     * arrays must be of the same size.
     *
     * @param buttons  the array of button views to use
     * @param selected The default value for the buttons
     */
    public void setButtons(Button[] buttons, boolean[] selected) {
        final int elementCount = buttons.length;
        if (elementCount == 0) {
            return;
        }

        boolean enableDefaultSelection = true;
        if (selected == null || elementCount != selected.length) {
            Log.d(TAG, "Invalid selection array");
            enableDefaultSelection = false;
        }

        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (mainLayout == null) {
            mainLayout = (LinearLayout) inflater.inflate(R.layout.view_multi_state_toggle_button, this, true);
        }
        mainLayout.removeAllViews();

        this.buttons = new ArrayList<>();
        for (int i = 0; i < elementCount; i++) {
            Button b = buttons[i];
            final int position = i;
            b.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    setValue(position);
                }

            });
            mainLayout.addView(b);
            if (enableDefaultSelection) {
                setButtonState(b, selected[i]);
            }
            this.buttons.add(b);
        }
        mainLayout.setBackgroundResource(R.drawable.button_section_shape);
    }

    public void setElements(CharSequence[] elements) {
        int size = elements == null ? 0 : elements.length;
        setElements(elements, null, new boolean[size]);
    }

    public void setElements(List<?> elements) {
        int size = elements == null ? 0 : elements.size();
        setElements(elements, new boolean[size]);
    }

    public void setElements(List<?> elements, Object selected) {
        int size = 0;
        int index = -1;
        if (elements != null) {
            size = elements.size();
            index = elements.indexOf(selected);
        }
        boolean[] selectedArray = new boolean[size];
        if (index != -1 && index < size) {
            selectedArray[index] = true;
        }
        setElements(elements, selectedArray);
    }

    public void setElements(List<?> texts, boolean[] selected) {
        if (texts == null) {
            texts = new ArrayList<>(0);
        }
        int size = texts.size();
        setElements(texts.toArray(new String[size]), null, selected);
    }

    public void setElements(int arrayResourceId, int selectedPosition) {
        // Get resources
        String[] elements = this.getResources().getStringArray(arrayResourceId);

        // Set selected boolean array
        int size = elements == null ? 0 : elements.length;
        boolean[] selected = new boolean[size];
        if (selectedPosition >= 0 && selectedPosition < size) {
            selected[selectedPosition] = true;
        }

        // Super
        setElements(elements, null, selected);
    }

    public void setElements(int arrayResourceId, boolean[] selected) {
        setElements(this.getResources().getStringArray(arrayResourceId), null, selected);
    }

    public void setButtonState(View button, boolean selected) {
        if (button == null) {
            return;
        }
        button.setSelected(selected);
        button.setBackgroundResource(selected ? R.drawable.button_pressed : R.drawable.button_not_pressed);
        if (colorPressed != 0 || colorNotPressed != 0) {
            button.setBackgroundColor(selected ? colorPressed : colorNotPressed);
        } else if (colorPressedBackground != 0 || colorNotPressedBackground != 0) {
            button.setBackgroundColor(selected ? colorPressedBackground : colorNotPressedBackground);
        }
        if (button instanceof Button) {
            int style = selected ? R.style.WhiteBoldText : R.style.PrimaryNormalText;
            ((AppCompatButton) button).setTextAppearance(this.getContext(), style);
            if (colorPressed != 0 || colorNotPressed != 0) {
                ((AppCompatButton) button).setTextColor(!selected ? colorPressed : colorNotPressed);
            }
            if (colorPressedText != 0 || colorNotPressedText != 0) {
                ((AppCompatButton) button).setTextColor(selected ? colorPressedText : colorNotPressedText);
            }
            if (pressedBackgroundResource != 0 || notPressedBackgroundResource != 0) {
                button.setBackgroundResource(selected ? pressedBackgroundResource : notPressedBackgroundResource);
            }
        }
    }

    public int getValue() {
        for (int i = 0; i < this.buttons.size(); i++) {
            if (buttons.get(i).isSelected()) {
                return i;
            }
        }
        return -1;
    }

    public void setValue(int position) {
        for (int i = 0; i < this.buttons.size(); i++) {
            if (mMultipleChoice) {
                if (i == position) {
                    View b = buttons.get(i);
                    if (b != null) {
                        setButtonState(b, !b.isSelected());
                    }
                }
            } else {
                if (i == position) {
                    setButtonState(buttons.get(i), true);
                } else if (!mMultipleChoice) {
                    setButtonState(buttons.get(i), false);
                }
            }
        }
        super.setValue(position);
    }

    public boolean[] getStates() {
        int size = this.buttons == null ? 0 : this.buttons.size();
        boolean[] result = new boolean[size];
        for (int i = 0; i < size; i++) {
            result[i] = this.buttons.get(i).isSelected();
        }
        return result;
    }

    public void setStates(boolean[] selected) {
        if (this.buttons == null || selected == null ||
                this.buttons.size() != selected.length) {
            return;
        }
        int count = 0;
        for (View b : this.buttons) {
            setButtonState(b, selected[count]);
            count++;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setColors(int colorPressed, int colorNotPressed) {
        super.setColors(colorPressed, colorNotPressed);
        refresh();
    }

    private void refresh() {
        boolean[] states = getStates();
        for (int i = 0; i < states.length; i++) {
            setButtonState(buttons.get(i), states[i]);
        }
    }

    public List<Button> getButtons() {
        return buttons;
    }

    public void setButtons(List<Button> buttons) {
        this.buttons = buttons;
    }
}
