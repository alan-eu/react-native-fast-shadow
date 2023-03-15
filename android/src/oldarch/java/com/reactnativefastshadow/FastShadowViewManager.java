package com.reactnativefastshadow;

import android.graphics.Color;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.views.view.ReactViewGroup;
import com.facebook.react.views.view.ReactViewManager;

public class FastShadowViewManager extends ReactViewManager {

  @Override
  @NonNull
  public String getName() {
    return FastShadowViewManagerImpl.NAME;
  }

  @Override
  public FastShadowView createViewInstance(ThemedReactContext context) {
    return FastShadowViewManagerImpl.createViewInstance(context);
  }

  @Override
  public void onDropViewInstance(@NonNull ReactViewGroup view) {
    super.onDropViewInstance(view);
    FastShadowViewManagerImpl.onDropViewInstance(view);
  }

  @ReactProp(name = "shadowColor", customType = "Color", defaultInt = Color.BLACK)
  public void setShadowColor(FastShadowView view, int color) {
    FastShadowViewManagerImpl.setShadowColor(view, color);
  }

  @ReactProp(name = "shadowOpacity", defaultFloat = 0)
  public void setShadowOpacity(FastShadowView view, float opacity) {
    FastShadowViewManagerImpl.setShadowOpacity(view, opacity);
  }

  @ReactProp(name = "shadowRadius", defaultFloat = 3)
  public void setShadowRadius(FastShadowView view, float radius) {
    FastShadowViewManagerImpl.setShadowRadius(view, radius);
  }

  @ReactProp(name = "shadowOffset")
  public void setShadowOffset(FastShadowView view, ReadableMap offset) {
    FastShadowViewManagerImpl.setShadowOffset(view, offset);
  }

  @ReactProp(name = "cornerRadii")
  public void setCornerRadii(FastShadowView view, ReadableMap borderRadius) {
    FastShadowViewManagerImpl.setCornerRadii(view, borderRadius);
  }
}
