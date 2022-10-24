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
  private int color = 0x00000000;
  private float opacity = 0;
  private float radius = 0;
  private float offsetX = 0;
  private float offsetY = -3;
  private float[] borderRadii = {0, 0, 0, 0}; // in clockwise order: tl, tr, br, bl

  public FastShadowView(Context context) {
    super(context);
  }

  public void setColor(int color) {
    this.color = color;
    this.invalidate();
  }

  public void setOpacity(float opacity) {
    this.opacity = opacity;
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

  public void setBorderRadii(float[] borderRadii) {
    this.borderRadii = borderRadii;
    this.invalidate();
  }

  public void releaseShadow() {
    shadowCache.releaseShadow(shadow);
    shadow = null;
  }

  @Override
  public void draw(Canvas canvas) {
    int width = Math.round(PixelUtil.toDIPFromPixel(getWidth()));
    int height = Math.round(PixelUtil.toDIPFromPixel(getHeight()));

    Shadow prevShadow = shadow;
    shadow = shadowCache.getOrCreateShadow(getContext(), width, height, borderRadii, radius);
    shadowCache.releaseShadow(prevShadow);

    if (shadow != null) {
      Drawable drawable = shadow.getDrawable();
      drawable.setBounds(new Rect(
        Math.round(PixelUtil.toPixelFromDIP(-radius + offsetX)),
        Math.round(PixelUtil.toPixelFromDIP(-radius + offsetY)),
        getWidth() + Math.round(PixelUtil.toPixelFromDIP(radius + offsetX)),
        getHeight() + Math.round(PixelUtil.toPixelFromDIP(radius + offsetY))
      ));
      drawable.setAlpha(Math.round(opacity * 255));
      drawable.setTint(color);
      drawable.draw(canvas);
    }

    super.draw(canvas);
  }
}
