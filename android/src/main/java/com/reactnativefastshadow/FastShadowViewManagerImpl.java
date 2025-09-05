package com.reactnativefastshadow;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.ThemedReactContext;

public class FastShadowViewManagerImpl {
  public static final String NAME = "FastShadowView";

  public FastShadowView createViewInstance(ThemedReactContext context) {
    return new FastShadowView(context);
  }

  public void setShadowColor(FastShadowView view, int color) {
    view.setColor(color);
  }

  public void setShadowOpacity(FastShadowView view, float opacity) {
    view.setOpacity(opacity);
  }

  public void setShadowRadius(FastShadowView view, float radius) {
    view.setRadius(radius);
  }

  public void setShadowOffset(FastShadowView view, ReadableMap offset) {
    if (offset == null) {
      view.resetOffset();
    } else {
      view.setOffset(
        (float) offset.getDouble("width"),
        (float) offset.getDouble("height")
      );
    }
  }

  public void setCornerRadii(FastShadowView view, ReadableMap borderRadius) {
    view.setCornerRadii(new float[]{
      (float) borderRadius.getDouble("topLeft"),
      (float) borderRadius.getDouble("topRight"),
      (float) borderRadius.getDouble("bottomRight"),
      (float) borderRadius.getDouble("bottomLeft")
    });
  }
}
