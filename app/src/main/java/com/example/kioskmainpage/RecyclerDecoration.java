package com.example.kioskmainpage;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RecyclerDecoration extends RecyclerView.ItemDecoration {
    private final int divHeight;

    //PageFragment에서 사용 줄간격 조절 작성자 : 2019-1 종합설계프로젝트 팀 (팀장 박준현)

    public RecyclerDecoration(int divHeight) {
        this.divHeight = divHeight;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1)

            outRect.bottom = divHeight;

    }

}
