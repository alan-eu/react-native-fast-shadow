// @flow
import type { ViewProps } from 'react-native/Libraries/Components/View/ViewPropTypes';
import type { HostComponent } from 'react-native';
import codegenNativeComponent from 'react-native/Libraries/Utilities/codegenNativeComponent';

export type FastShadowViewProps = $ReadOnly<{|
  ...ViewProps,
  cornerRadii: $ReadOnly<{|
    topLeft: Int32,
    topRight: Int32,
    bottomLeft: Int32,
    bottomRight: Int32,
  |}>,
|}>;

export default (codegenNativeComponent<FastShadowViewProps>(
  'FastShadowView'
): HostComponent<FastShadowViewProps>);
