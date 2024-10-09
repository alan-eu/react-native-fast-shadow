package com.reactnativefastshadow;

import android.graphics.Color;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.views.view.ReactViewGroup;
import com.facebook.react.views.view.ReactViewManager;

public class FastShadowViewManager extends ReactViewManager {

  public static final String NAME = "FastShadowView";

  @Override
  @NonNull
  public String getName() {
    return FastShadowViewManager.NAME;
  }

  @Override
  public FastShadowView createViewInstance(ThemedReactContext context) {
    return new FastShadowView(context);
  }

  @Override
  public void onDropViewInstance(@NonNull ReactViewGroup view) {
    super.onDropViewInstance(view);
    ((FastShadowView) view).releaseShadow();
  }

  @ReactProp(name = "shadowColor", customType = "Color", defaultInt = Color.BLACK)
  public void setShadowColor(FastShadowView view, int color) {
    view.setColor(color);
  }

  @ReactProp(name = "shadowOpacity", defaultFloat = 0)
  public void setShadowOpacity(FastShadowView view, float opacity) {
    view.setOpacity(opacity);
  }

  @ReactProp(name = "shadowRadius", defaultFloat = 3)
  public void setShadowRadius(FastShadowView view, float radius) {
    view.setRadius(radius);
  }

  @ReactProp(name = "shadowOffset")
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

  @ReactProp(name = "cornerRadii")
  public void setCornerRadii(FastShadowView view, ReadableMap borderRadius) {
    view.setCornerRadii(new float[]{
      (float) borderRadius.getDouble("topLeft"),
      (float) borderRadius.getDouble("topRight"),
      (float) borderRadius.getDouble("bottomRight"),
      (float) borderRadius.getDouble("bottomLeft")
    });
  }
}
