import { requireNativeComponent, ViewProps } from 'react-native';

export type FastShadowViewProps = ViewProps & {
  cornerRadii: {
    topLeft: number;
    topRight: number;
    bottomLeft: number;
    bottomRight: number;
  };
};

export const FastShadowView =
  requireNativeComponent<FastShadowViewProps>('FastShadowView');
