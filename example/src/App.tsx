import * as React from 'react';

import { StyleSheet, View } from 'react-native';
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
  },
  box: {
    width: 120,
    height: 120,
    marginVertical: 20,
    shadowColor: 'red',
    shadowOpacity: 0.8,
    shadowRadius: 3,
    shadowOffset: { width: 0, height: -3 },
    backgroundColor: 'white',
  },
});
