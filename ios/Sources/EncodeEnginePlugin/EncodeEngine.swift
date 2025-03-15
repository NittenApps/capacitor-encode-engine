import Foundation

@objc public class EncodeEngine: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
