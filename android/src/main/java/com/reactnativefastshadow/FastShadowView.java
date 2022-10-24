package com.reactnativefastshadow;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;


import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.views.view.ReactViewGroup;

public class FastShadowView extends ReactViewGroup {
  private static ShadowCache shadowCache = new ShadowCache();

  private Shadow shadow;
  private int color = 0xff000000;
  private float opacity = 0;
  private float radius = 0;
  private float offsetX = 0;
  private float offsetY = -3;
  private float[] cornerRadii = {0, 0, 0, 0}; // in clockwise order: tl, tr, br, bl

  public FastShadowView(Context context) {
    super(context);
  }

  public void setColor(int color) {
    this.color = color;
    this.invalidate();
  }

  public void setOpacity(float opacity) {
    this.opacity = opacity;

    // We need to call this, as draw() is not called by default on ViewGroup
    this.setWillNotDraw(opacity <= 0);
    this.invalidate();
  }

  public void setRadius(float radius) {
    this.radius = radius;
    this.invalidate();
  }

  public void setOffset(float offsetX, float offsetY) {
    this.offsetX = offsetX;
    this.offsetY = offsetY;
    this.invalidate();
  }

  public void resetOffset() {
    setOffset(0, -3);
  }

  public void setCornerRadii(float[] cornerRadii) {
    this.cornerRadii = cornerRadii;
    this.invalidate();
  }

  public void releaseShadow() {
    shadowCache.releaseShadow(shadow);
    shadow = null;
  }

  @Override
  protected void onDraw(Canvas canvas) {
    int width = Math.round(PixelUtil.toDIPFromPixel(getWidth()));
    super.onDraw(canvas);
  }

  @Override
  public void draw(Canvas canvas) {
    // We override draw() and not onDraw() as we want to draw
    // our shadow behind the background drawable of the View.

    if (opacity > 0) {
      int width = dp(getWidth());
      int height = dp(getHeight());

      Shadow prevShadow = shadow;
      shadow = shadowCache.getOrCreateShadow(getContext(), width, height, cornerRadii, radius);
      shadowCache.releaseShadow(prevShadow);

      if (shadow != null) {
        Drawable drawable = shadow.getDrawable();
        drawable.setBounds(new Rect(
          px(-radius + offsetX),
          px(-radius + offsetY),
          getWidth() + px(radius + offsetX),
          getHeight() + px(radius + offsetY)
        ));
        drawable.setAlpha(Math.round(opacity * 255));
        drawable.setTint(color);
        drawable.draw(canvas);
      }
    }

    super.draw(canvas);
  }

  static int dp(float px) {
    return Math.round(PixelUtil.toDIPFromPixel(px));
  }

  static int px(float dp) {
    return Math.round(PixelUtil.toPixelFromDIP(dp));
  }
}
