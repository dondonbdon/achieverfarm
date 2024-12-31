package dev.bti.achieverfarm.util;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class SwipeDismissLayout extends FrameLayout implements View.OnTouchListener {

    private float initialY;

    public SwipeDismissLayout(Context context) {
        super(context);
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initialY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float currentY = event.getRawY();
                float deltaY = currentY - initialY;
                if (deltaY > 0) {
                    setTranslationY(deltaY);
                }
                break;
            case MotionEvent.ACTION_UP:
                float finalY = event.getRawY();
                if (finalY - initialY > 200) {
                } else {
                    setTranslationY(0);
                }
                break;
        }
        return true;
    }
}