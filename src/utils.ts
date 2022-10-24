import { ColorValue, Platform, ViewStyle } from 'react-native';

export type ShadowParams = {
  color?: ColorValue;
  opacity?: number;
  radius?: number;
  offset?: [number, number];
};

/**
 * Utility function to ensure that shadows look the same accross all platforms (iOS, android, web).
 * Rationale: for some reasons, the shadow radius on iOS looks like too large by a factor of 2.
 * To keep shadows consistent, this function will divide the shadow radius by 2 on iOS.
 */
export function shadowStyle({
  color,
  opacity,
  radius,
  offset,
}: ShadowParams): ViewStyle {
  return {
    shadowColor: color,
    shadowOpacity: opacity,
    shadowRadius:
      radius && Platform.select({ ios: radius / 2, default: radius }),
    shadowOffset: offset && { width: offset[0], height: offset[1] },
  };
}
