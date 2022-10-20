import * as React from 'react';

import { Platform, StyleSheet, View } from 'react-native';
import { ShadowedView } from 'react-native-fast-shadow';

export default function App() {
  return (
    <View style={styles.container}>
      <ShadowedView style={styles.box} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: 'white',
  },
  box: {
    width: 200,
    height: 150,
    marginVertical: 20,
    borderRadius: 40,
    borderTopLeftRadius: 10,
    shadowColor: 'red',
    shadowOpacity: 0.8,
    shadowRadius: Platform.select({ ios: 10, android: 25 }),
    shadowOffset: { width: 1, height: 1 },
    backgroundColor: 'yellow',
  },
});
