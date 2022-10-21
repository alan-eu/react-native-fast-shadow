package com.reactnativefastshadow;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class Shadow {
  private ShadowSpecs specs;
  private Drawable drawable;
  private Bitmap bitmap;
  private int refCount = 0;

  public Shadow(ShadowSpecs specs, Drawable drawable, Bitmap bitmap) {
    this.specs = specs;
    this.drawable = drawable;
    this.bitmap = bitmap;
  }

  public ShadowSpecs getSpecs() {
    return specs;
  }

  public Drawable getDrawable() {
    return drawable;
  }

  public synchronized void retain() {
    refCount++;
  }

  public synchronized boolean release() {
    refCount--;
    if (refCount == 0) {
      bitmap.recycle();
      return true;
    }
    return false;
  }
}
