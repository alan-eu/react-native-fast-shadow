import React, { useEffect } from 'react';

import { Image, View, ScrollView, Text } from 'react-native';
import { ShadowedView, shadowStyle } from 'react-native-fast-shadow';
import Animated, {
  useSharedValue,
  withTiming,
  useAnimatedStyle,
  withRepeat,
} from 'react-native-reanimated';

const AnimatedShadowedView = Animated.createAnimatedComponent(ShadowedView);

export default function App() {
  const animatedWidth = useSharedValue(30);
  const animatedHeight = useSharedValue(50);

  useEffect(() => {
    animatedWidth.value = 30;
    animatedWidth.value = withRepeat(
      withTiming(100, {
        duration: 1000,
      }),
      -1,
      true
    );
    animatedHeight.value = 50;
    animatedHeight.value = withRepeat(
      withTiming(100, {
        duration: 1500,
      }),
      -1,
      true
    );
  }, []);

  const animatedStyle = useAnimatedStyle(() => ({
    width: `${animatedWidth.value}%`,
    height: `${animatedHeight.value}%`,
  }));

  const uiManager = (global as any)?.nativeFabricUIManager ? 'Fabric' : 'Paper';

  return (
    <ScrollView style={{ backgroundColor: 'white' }}>
      <Text style={{fontSize: 18, fontWeight: 'bold'}}>This View is {uiManager}</Text>
      <View style={{ margin: 16 }}>
        <View style={{ flexDirection: 'row', marginBottom: 20 }}>
          <ShadowedView
            style={{
              width: 150,
              height: 200,
              borderRadius: 20,
              borderBottomRightRadius: 50,
              backgroundColor: '#dbfff2',
              borderColor: '#7af0c5',
              marginRight: 20,
              ...shadowStyle({
                opacity: 0.4,
                radius: 12,
                offset: [5, 3],
              }),
            }}
          />
          <ShadowedView
            style={{
              width: 60,
              height: 60,
              borderRadius: 30,
              backgroundColor: '#d6dbff',
              ...shadowStyle({
                color: '#221db2',
                opacity: 0.8,
                radius: 10,
                offset: [0, 3],
              }),
            }}
          />
        </View>

        <View
          style={{
            width: 250,
            height: 200,
            marginBottom: 20,
          }}
        >
          <AnimatedShadowedView
            style={[
              animatedStyle,
              {
                borderRadius: 20,
                borderTopStartRadius: 50,
                backgroundColor: '#ffe8e9',
                ...shadowStyle({
                  opacity: 0.3,
                  radius: 25,
                  offset: [5, 3],
                }),
              },
            ]}
          />
        </View>

        <ShadowedView
          style={{
            alignSelf: 'flex-start',
            ...shadowStyle({
              color: '#221db2',
              opacity: 0.4,
              radius: 25,
              offset: [5, 3],
            }),
            marginBottom: 20,
          }}
        >
          <Image
            style={{ borderRadius: 60 }}
            source={require('./image1.png')}
          />
        </ShadowedView>
      </View>
    </ScrollView>
  );
}
