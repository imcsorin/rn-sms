#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(RnSms, NSObject)

RCT_EXTERN_METHOD(sendSMS
                  :(NSArray<NSString> *) addresses
                  :(NSString *) message
                  :(NSDictionary *) options
                  :(RCTPromiseResolveBlock) resolve
                  rejecter: (RCTPromiseRejectBlock) reject)

+ (BOOL)requiresMainQueueSetup
{
  return NO;
}

@end
