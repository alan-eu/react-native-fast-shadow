# react-native-fast-shadow

🌖 **Fast** and **high quality** Android shadows for React Native

## Why

React Native only supports shadows on Android through the [elevation](https://reactnative.dev/docs/view-style-props#elevation-android) prop but it is hard to achieve the effect you want 

## Features
* 💆‍♀️ **Easy to use:** Drop-in replacement for the `<View>` component
* 🎛 **Customizable:** Supports all the regular shadow props: `shadowRadius`, `shadowColor`, `shadowOpacity` and `shadowOffset`
* ⚡️ **Performant:** Shadows can be applied to a large number of views without any signicant performance impact. It is optimized for low memory consumption and fast rendering

## Getting started

```sh
npm install react-native-fast-shadow
or
yarn add react-native-fast-shadow
```

**Usage:**

```jsx
import { ShadowedView } from "react-native-fast-shadow";

<ShadowedView
  style={{
    shadowColor: 'black',
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


## Usage

```js
import { FastShadowView } from "react-native-fast-shadow";

// ...

<FastShadowView color="tomato" />
```

