package com.reactnativefastshadow;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

public class ShadowCache {
  private Map<String, Shadow> cache = new HashMap<>();
  private ShadowFactory factory = new ShadowFactory();

  public Shadow getOrCreateShadow(Context context, int width, int height, float[] borderRadii, float blurRadius) {
    ShadowSpecs specs = factory.getShadowSpecs(width, height, borderRadii, blurRadius);
    String cacheKey = specs.getCacheKey();
    Shadow shadow = cache.get(cacheKey);
    if (shadow == null) {
      shadow = factory.createShadow(context, specs);
      if (shadow == null) {
        return null;
      }
      cache.put(cacheKey, shadow);
    }
    shadow.retain();
    return shadow;
  }

  public void releaseShadow(Shadow shadow) {
    if (shadow != null) {
      boolean recycled = shadow.release();
      if (recycled) {
        cache.remove(shadow.getSpecs().getCacheKey());
      }
    }
  }
}
