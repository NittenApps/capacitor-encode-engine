import CommonCrypto
import Foundation
import SwiftyRSA

@objc public class EncodeEngine: NSObject {
    private static let CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private static let MODULUS = 1271
    private static let RADIX = 36
    private static let DOUBLE_DIGIT = true

    @objc public func checkDigits(_ input: String) -> String {
        let input = input.uppercased()

        var p: Int = 0
        for char in input {
            guard let val = CHARS.indexOf(char) else { return nil }
            p = ((p + val) * RADIX) % MODULUS
        }

        if DOUBLE_DIGIT {
            p = (p * RADIX) % MODULUS
        }

        let checksum = (MODULUS - p + 1) % MODULUS
        if DOUBLE_DIGIT {
            let second = checksum % RADIX
            let first = (checksum - (checksum % RADIX)) / RADIX
            return String(CHARS.charAt(first)!) + String(CHARS.charAt(second)!)
        }

        return String(CHARS.charAt(checksum)!)
    }

    @objc public func decode(_ key: String, _ value: String) -> String? {
        guard let keyData = convertKey(key),
              let encryptedData = Data(base64Encoded: value),
              let decryptedData = crypt(operation: kCCDecrypt, algorithm: kCCAlgorithmAES,
                                        options: kCCOptionPKCS7Padding, key: Data(keyData),
                                        data: encryptedData) else { return nil }
        return String(data: decryptedData, encoding: .utf8)
    }

    @objc public func encode(_ key: String, _ value: String) -> String? {
        guard let keyData = convertKey(key),
              let encryptedData = crypt(operation: kCCEncrypt, algorithm: kCCAlgorithmAES,
                                        options: kCCOptionPKCS7Padding, key: Data(keyData),
                                        data: value.data(using: .utf8)!) else { return nil }
        return encryptedData.base64EncodedString()
    }

    private func convertKey(_ key: String) -> [UInt8]? {
        guard let data = key.data(using: .utf8) else { return nil }
        return Array(data)
    }

    private func crypt(operation: Int, algorithm: Int, options: Int, key: Data, data: Data) -> Data? {
        let hashedKey = generateKey(key)
        let iv = generateIV(hashedKey)
        return hashedKey.withUnsafeBytes { keyUnsafeRawBufferPointer in
            return data.withUnsafeBytes { dataUnsafeRawBufferPointer in
                return iv.withUnsafeBytes { ivUnsafeRawBufferPointer in
                    let dataOutSize = data.count + kCCBlockSizeAES128
                    let dataOut = UnsafeMutableRawPointer.allocate(byteCount: dataOutSize, alignment: 1)
                    defer { dataOut.deallocate() }
                    var dataOutMoved: Int = 0
                    let status = CCCrypt(CCOperation(operation), CCAlgorithm(algorithm), CCOptions(options),
                                         keyUnsafeRawBufferPointer.baseAddress, hashedKey.count,
                                         ivUnsafeRawBufferPointer.baseAddress,
                                         dataUnsafeRawBufferPointer.baseAddress, data.count, dataOut, dataOutSize,
                                         &dataOutMoved)
                    guard status == kCCSuccess else {
                        switch status {
                        case Int32(kCCParamError):
                            print("kCCParamError")
                        case Int32(kCCBufferTooSmall):
                            print("kCCBufferTooSmall")
                        case Int32(kCCMemoryFailure):
                            print("kCCMemoryFailure")
                        case Int32(kCCAlignmentError):
                            print("kCCAlignmentError")
                        case Int32(kCCDecodeError):
                            print("kCCDecodeError")
                        case Int32(kCCUnimplemented):
                            print("kCCUnimplemented")
                        case Int32(kCCOverflow):
                            print("kCCOverflow")
                        case Int32(kCCRNGFailure):
                            print("kCCRNGFailure")
                        case Int32(kCCUnspecifiedError):
                            print("kCCUnspecifiedError")
                        case Int32(kCCCallSequenceError):
                            print("kCCCallSequenceError")
                        case Int32(kCCKeySizeError):
                            print("kCCKeySizeError")
                        case Int32(kCCInvalidKey):
                            print("kCCInvalidKey")
                        default:
                            break
                        }
                        return nil
                    }
                    return Data(bytes: dataOut, count: dataOutMoved)
                }
            }
        }
    }

    private func generateIV(_ key: Data) -> Data {
        let bytes = UnsafeMutableBufferPointer<UInt8>.allocate(capacity: kCCKeySizeAES128)
        defer { bytes.deallocate() }
        key.copyBytes(to: bytes, count: min(key.count, kCCKeySizeAES128))
        return Data(bytes)
    }

    private func generateKey(_ key: Data) -> Data {
        let hash = sha256(key)
        let bytes = UnsafeMutableBufferPointer<UInt8>.allocate(capacity: kCCKeySizeAES256)
        defer { bytes.deallocate() }
        hash.copyBytes(to: bytes, count: min(hash.count, kCCKeySizeAES256))
        return Data(bytes)
    }

    private func sha256(_ data: Data) -> Data {
        var hash = [UInt8](repeating: 0,  count: Int(CC_SHA256_DIGEST_LENGTH))
        data.withUnsafeBytes {
            _ = CC_SHA256($0.baseAddress, CC_LONG(data.count), &hash)
        }
        return Data(hash)
    }
}
