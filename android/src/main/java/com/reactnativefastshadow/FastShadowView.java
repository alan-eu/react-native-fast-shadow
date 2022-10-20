package com.reactnativefastshadow;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;

public class FastShadowView extends View {
  private int color = 0x00000000;
  private float radius = 0;
  private float[] borderRadii = { 0, 0, 0, 0 }; // in clockwise order: tl, tr, br, bl

  public FastShadowView(Context context) {
    super(context);
  }

  public void setColor(int color) {
    this.color = color;
    this.invalidate();
  }

  public void setRadius(float radius) {
    this.radius = radius;
    this.invalidate();
  }

  public void setBorderRadii(float[] borderRadii) {
    this.borderRadii = borderRadii;
    this.invalidate();
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    int inset = (int)Math.ceil(radius);
    int width = pxToDp(getWidth()) - 2 * inset;
    int height = pxToDp(getHeight()) - 2 * inset;
    Drawable drawable = ShadowBitmap.createShadowDrawable(getContext(), width, height, borderRadii, radius);

    if (drawable != null) {
      drawable.setBounds(new Rect(0, 0, getWidth(), getHeight()));
      drawable.setTint(color);
      drawable.draw(canvas);
    }
  }

  private int pxToDp(int px) {
    DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
    return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
  }
}
