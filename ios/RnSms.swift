import MessageUI
import MobileCoreServices
import UIKit

@objc(RnSms)
class RnSms: UIViewController, MFMessageComposeViewControllerDelegate {
    func messageComposeViewController(
        _ controller: MFMessageComposeViewController, didFinishWith result: MessageComposeResult
    ) {
        controller.dismiss(animated: true)
    }
    
    @objc public func sendSMS(
        _ addresses: [String],
        _ message: String,
        _ options: (NSDictionary?),
        _ resolve: @escaping RCTPromiseResolveBlock,
        rejecter reject: @escaping RCTPromiseRejectBlock
    ) {
        guard MFMessageComposeViewController.canSendText() else {
            reject("NO_SMS_APP", "No messaging application available", nil)
            return
        }
        
        DispatchQueue.main.async {
            let messageComposeViewController = MFMessageComposeViewController()
            messageComposeViewController.recipients = addresses
            messageComposeViewController.body = message
            messageComposeViewController.messageComposeDelegate = self
            
            if let options = options, let attachments = options["attachments"] as? [[String: Any]] {
                for attachment in attachments {
                    // Extract attachment information
                    guard let mimeType = attachment["mimeType"] as? String,
                          let uri = attachment["uri"] as? String,
                          let filename = attachment["filename"] as? String,
                          let url = URL(string: uri),
                          let attachmentData = try? Data(contentsOf: url),
                          let utiRef = UTTypeCreatePreferredIdentifierForTag(
                            kUTTagClassMIMEType,
                            mimeType as CFString,
                            nil
                          )
                    else {
                        // Handle any missing or invalid data in the attachment
                        reject("ATTACHMENT", "Invalid attachment data", nil)
                        return
                    }

                    // Get the UTI (Uniform Type Identifier) from the MIME type
                    let typeIdentifier = utiRef.takeRetainedValue() as String

                    // Add attachment to the message compose view controller
                    if messageComposeViewController.addAttachmentData(attachmentData, typeIdentifier: typeIdentifier, filename: filename) {
                        // Successfully added attachment
                    } else {
                        // Failed to attach file
                        reject("ATTACHMENT", "Failed to attach file: \(uri)", nil)
                        return
                    }
                }
            }
            
            var rootViewController = UIApplication.shared.keyWindow?.rootViewController
            while rootViewController?.presentedViewController != nil {
                rootViewController = rootViewController?.presentedViewController
            }
            rootViewController?.present(messageComposeViewController, animated: true, completion: nil)
            resolve(true)
        }
    }
}
