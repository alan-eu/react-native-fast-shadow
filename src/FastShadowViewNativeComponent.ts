import type { HostComponent, ViewProps } from 'react-native'
import codegenNativeComponent from 'react-native/Libraries/Utilities/codegenNativeComponent'
import { Int32, Float } from 'react-native/Libraries/Types/CodegenTypes'

export interface FastShadowViewProps extends ViewProps {
  shadowColor?: Int32
  shadowOpacity?: Float
  shadowRadius?: Float
  shadowOffset?: {
    width: Float
    height: Float
  }
  cornerRadii?: {
    topLeft: Float
    topRight: Float
    bottomLeft: Float
    bottomRight: Float
  }
}

export default codegenNativeComponent<FastShadowViewProps>(
  'FastShadowView'
) as HostComponent<FastShadowViewProps>
