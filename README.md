# react-native-fast-shadow

🌖 **Fast and high quality** Android shadows for React Native

## Why

React Native only supports shadows on Android through the [elevation](https://reactnative.dev/docs/view-style-props#elevation-android) prop but achieving the desired effect is often impossible as it only comes with very few presets. Third-party libraries have been created to circumvent this but when used on many views, they can make you app slower or significantly increase its memory consumption.

## Features
* 💆‍♀️ **Easy to use:** Drop-in replacement for the `<View>` component
* ⚡️ **Performant:** Shadows can be applied to a large number of views without any signicant performance impact. It has been optimized for low memory consumption and fast rendering
* 🎛 **Customizable:** Supports all the regular shadow props, `shadowRadius`, `shadowColor`, `shadowOpacity` and `shadowOffset`
* 📱 **Universal:** Works on all platforms. On iOS and Web, `<ShadowedView>` is just an alias of `<View>`

## Getting started

```sh
npm install react-native-fast-shadow
# or
yarn add react-native-fast-shadow
```

**Usage:**

```jsx
import { ShadowedView } from 'react-native-fast-shadow';

<ShadowedView
  style={{
    shadowOpacity: 0.4,
    shadowRadius: 12,
    shadowOffset: {
      width: 5,
      height: 3,
    },
  }}
>
  <Image source={require('./kitten.png')} style={{ borderRadius: 30 }} />
</ShadowedView>
```

<img width="198" src="https://user-images.githubusercontent.com/20420653/197513322-81c46d07-2f44-463b-86ef-86a4ad856146.png">

The `shadowStyle()` utility can be used to make it easier to create shadow styles and to **keep shadows consistent** accross platforms.
It will create the same `style` prop as above, but will divide the shadow radius by 2 on iOS (as for some reasons, iOS shadows are too large by a factor of 2 when compared to design tools or to CSS's box-shadow model):

```jsx
import { ShadowedView, shadowStyle } from 'react-native-fast-shadow';

<ShadowedView
  style={shadowStyle({
    opacity: 0.4,
    radius: 12,
    offset: [5, 3],
  })}
>
  <Image source={require('./kitten.png')} style={{ borderRadius: 30 }} />
</ShadowedView>
```
 
## How it works under the hood

On Android, shadow drawables are generated with the following process (see [ShadowFactory.java](https://github.com/alan-eu/react-native-fast-shadow/blob/main/android/src/main/java/com/reactnativefastshadow/ShadowFactory.java) for more details):
1. The shape of the view (rectangle with rounded corners) is painted in black on a canvas
2. A gaussian blur is applied to the canvas with the given `shadowRadius` using the Renderscript API
3. The drawable is then converted to a [NinePatchDrawable](https://developer.android.com/reference/android/graphics/drawable/NinePatchDrawable) to ensure that corners of the shadow won't be distorted when the view is resized. This way, we can generate only a small shadow drawable and **reuse it** for all views with the same border and blur radii.
4. Finally, the drawable is rendered behind the view content: it is tinted with `shadowColor`/`shadowOpacity` and offseted according to `shadowOffset` 

**How NinePatchDrawable works** (notice how the corners are not streched when the drawable is resized):

<img width="240" src="https://user-images.githubusercontent.com/20420653/197518195-2e13d80e-2a24-4e1c-ae53-444306733c83.gif">

## Troubleshooting

React-native-fast-shadow comes with the following limitations:
* **It only works with rounded rectangles:** Unlike the iOS `<View>` implementation, `<ShadowedView>` won't work with freeform views. It expects its descendant views to be a rounded rectangle (up to a circle). **Solutions:** For `<Text>` elements, you can use [textShadowRadius](https://reactnative.dev/docs/text-style-props.html#textshadowradius). For complex shapes, [react-native-androw](https://github.com/folofse/androw) is your best option.
* **\<ShadowedView\> expects its child view to fill it:** It's up to you to make sure that `<ShadowedView>` and its children have the same size, otherwise the shadow will be larger than the content (you can think of `<ShadowedView>` as a view with a background color).
* **Corner radii can be inferred incorrectly:** We use `<ShadowedView>`'s style or the style of its direct child to infer the corner radii to apply. If your view hierarchy is more complex, corner radii might not be inferred correctly. **Solution:** rework your view hierarchy or pass the `borderRadius` directly to the `style` prop of `<ShadowedView>`.
* **Shadow radius is limited to 25 or below:** This limitation comes from Renderscript's [blur effect](https://developer.android.com/reference/android/renderscript/ScriptIntrinsicBlur). **Solution:** Let us know if this is an issue for you. This can probably be worked around by downscaling the shadow bitmap before applying the blur effect.

## Benchmark

The following table compares the memory consumption of `react-native-fast-shadow`, [react-native-androw](https://github.com/folofse/androw) and [react-native-shadow-2](https://github.com/SrBrahma/react-native-shadow-2) when rendering 100 150x200pt `<Image>` on a Pixel 2 with a 12pt radius shadow. The app was built using the debug variant and Hermes.

| No shadow | react-native-shadow-2 | react-native-androw | react-native-fast-shadow |
|-|-|-|-|
| 117MB (ref) | 430MB **(+313MB)** | 403MB **(+286MB)** | 123MB **(+6MB)** |
