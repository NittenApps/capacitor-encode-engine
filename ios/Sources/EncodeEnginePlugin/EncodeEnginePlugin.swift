import Foundation
import Capacitor

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(EncodeEnginePlugin)
public class EncodeEnginePlugin: CAPPlugin, CAPBridgedPlugin {
    public let identifier = "EncodeEnginePlugin"
    public let jsName = "EncodeEngine"
    public let pluginMethods: [CAPPluginMethod] = [
        CAPPluginMethod(name: "decode", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "encode", returnType: CAPPluginReturnPromise)
    ]
    private let implementation = EncodeEngine()

    @objc func checkDigits(_ call: CAPPluginCall) {
        let input = call.getString("input") ?? ""

        call.resolve([
            "value": implementation.checkDigits(input)
        ])
    }

    @objc func decode(_ call: CAPPluginCall) {
        let key = call.getString("key") ?? ""
        let value = call.getString("value") ?? ""

        call.resolve([
            "value": implementation.decode(key, value)
        ])
    }

    @objc func encode(_ call: CAPPluginCall) {
        let key = call.getString("key") ?? ""
        let value = call.getString("value") ?? ""

        call.resolve([
            "value": implementation.encode(key, value)
        ])
    }
}
