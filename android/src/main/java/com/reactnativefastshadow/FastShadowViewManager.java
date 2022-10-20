package com.reactnativefastshadow;

import android.graphics.Color;
import android.view.View;

import androidx.annotation.NonNull;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

public class FastShadowViewManager extends SimpleViewManager<FastShadowView> {
  public static final String REACT_CLASS = "FastShadowView";

  @Override
  @NonNull
  public String getName() {
    return REACT_CLASS;
  }

  @Override
  @NonNull
  public FastShadowView createViewInstance(ThemedReactContext reactContext) {
    return new FastShadowView(reactContext);
  }

  @ReactProp(name = "radius")
  public void setRadius(FastShadowView view, float radius) {
    view.setRadius(radius);
  }

  @ReactProp(name = "color")
  public void setColor(FastShadowView view, String color) {
    view.setColor(Color.parseColor(color));
  }
}
