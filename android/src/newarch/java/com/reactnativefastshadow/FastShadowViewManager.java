package com.reactnativefastshadow;

import android.graphics.Color;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import com.facebook.react.uimanager.ViewManagerDelegate;
import com.facebook.react.uimanager.ViewGroupManager;

import com.facebook.react.module.annotations.ReactModule;

import com.facebook.react.viewmanagers.FastShadowViewManagerDelegate;
import com.facebook.react.viewmanagers.FastShadowViewManagerInterface;


@ReactModule(name = FastShadowViewManagerImpl.NAME)
public class FastShadowViewManager extends ViewGroupManager<FastShadowView>
    implements FastShadowViewManagerInterface<FastShadowView> {

  private final FastShadowViewManagerDelegate mDelegate;
  private final FastShadowViewManagerImpl mFastShadowViewManagerImpl;

  public FastShadowViewManager() {
    mDelegate = new FastShadowViewManagerDelegate<>(this);
    mFastShadowViewManagerImpl = new FastShadowViewManagerImpl();
  }

  @Override
  protected ViewManagerDelegate<FastShadowView> getDelegate() {
    return mDelegate;
  }

  @Override
  public void onDropViewInstance(@NonNull FastShadowView view) {
    super.onDropViewInstance(view);
    view.releaseShadow();
  }

  @Override
  @NonNull
  public String getName() {
    return FastShadowViewManagerImpl.NAME;
  }

  @Override
  public FastShadowView createViewInstance(ThemedReactContext context) {
    return mFastShadowViewManagerImpl.createViewInstance(context);
  }

  @Override
  @ReactProp(name = "shadowColor", customType = "Color", defaultInt = Color.BLACK)
  public void setShadowColor(FastShadowView view, int color) {
    mFastShadowViewManagerImpl.setShadowColor(view, color);
  }

  @Override
  @ReactProp(name = "shadowOpacity", defaultFloat = 0)
  public void setShadowOpacity(FastShadowView view, float opacity) {
    mFastShadowViewManagerImpl.setShadowOpacity(view, opacity);
  }

  @Override
  @ReactProp(name = "shadowRadius", defaultFloat = 3)
  public void setShadowRadius(FastShadowView view, float radius) {
    mFastShadowViewManagerImpl.setShadowRadius(view, radius);
  }

  @Override
  @ReactProp(name = "shadowOffset")
  public void setShadowOffset(FastShadowView view, ReadableMap offset) {
    mFastShadowViewManagerImpl.setShadowOffset(view, offset);
  }

  @Override
  @ReactProp(name = "cornerRadii")
  public void setCornerRadii(FastShadowView view, ReadableMap borderRadius) {
    mFastShadowViewManagerImpl.setCornerRadii(view, borderRadius);
  }
}
