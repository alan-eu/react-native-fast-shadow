package com.reactnativefastshadow;

import android.graphics.Color;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReadableMap;
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

  @Override
  public void onDropViewInstance(@NonNull FastShadowView view) {
    super.onDropViewInstance(view);
    view.releaseShadow();
  }

  @ReactProp(name = "color")
  public void setColor(FastShadowView view, String color) {
    view.setColor(Color.parseColor(color));
  }

  @ReactProp(name = "radius")
  public void setRadius(FastShadowView view, float radius) {
    view.setRadius(radius);
  }

  @ReactProp(name = "borderRadii")
  public void setBorderRadii(FastShadowView view, ReadableMap borderRadius) {
    view.setBorderRadii(new float[]{
      (float) borderRadius.getDouble("topLeft"),
      (float) borderRadius.getDouble("topRight"),
      (float) borderRadius.getDouble("bottomRight"),
      (float) borderRadius.getDouble("bottomLeft")
    });
  }
}
