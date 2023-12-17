import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'rn-sms' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const RnSms = NativeModules.RnSms
  ? NativeModules.RnSms
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export async function sendSMS(
  addresses: string[],
  message: string,
  options?: {
    attachments: { filename: string; mimeType: string; uri: string }[];
  }
): Promise<boolean> {
  return await RnSms.sendSMS(addresses, message, options);
}
