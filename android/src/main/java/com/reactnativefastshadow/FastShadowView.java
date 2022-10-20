package com.reactnativefastshadow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

public class FastShadowView extends View {
  private float radius = 0;
  private int color = 0x00000000;

  public FastShadowView(Context context) {
    super(context);
  }

  public void setRadius(float radius) {
    this.radius = radius;
    this.invalidate();
  }

  public void setColor(int color) {
    this.color = color;
    this.invalidate();
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    int inset = (int)Math.ceil(radius);
    int width = pxToDp(getWidth()) - 2 * inset;
    int height = pxToDp(getHeight()) - 2 * inset;
    Bitmap shadowBitmap = ShadowBitmap.createShadowBitmap(getContext(), width, height, radius);

    if (shadowBitmap != null) {
      Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
      paint.setColor(color);
      canvas.drawBitmap(shadowBitmap, null, new Rect(0, 0, getWidth(), getHeight()), paint);
    }
  }

  private int pxToDp(int px) {
    DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
    return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
  }
}
