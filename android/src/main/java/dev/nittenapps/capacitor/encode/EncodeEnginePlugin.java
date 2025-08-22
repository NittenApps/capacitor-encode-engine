package dev.nittenapps.capacitor.encode;

import androidx.annotation.NonNull;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "EncodeEngine")
public class EncodeEnginePlugin extends Plugin {
    @PluginMethod
    public void checkDigits(@NonNull PluginCall call) {
        String input = call.getString("input");

        JSObject ret = new JSObject();
        ret.put("value", EncodeEngine.checkDigits(input));
        call.resolve(ret);
    }

    @PluginMethod
    public void decode(@NonNull PluginCall call) {
        String key = call.getString("key");
        String value = call.getString("value");

        JSObject ret = new JSObject();
        ret.put("value", EncodeEngine.decode(key, value));
        call.resolve(ret);
    }

    @PluginMethod
    public void encode(@NonNull PluginCall call) {
        String key = call.getString("key");
        String value = call.getString("value");

        JSObject ret = new JSObject();
        ret.put("value", EncodeEngine.encode(key, value));
        call.resolve(ret);
    }

    @PluginMethod
    public void sign(@NonNull PluginCall call) {
        String value = call.getString("value");
        assert value != null;

        JSObject ret = new JSObject();
        ret.put("value", EncodeEngine.sign(bridge.getContext(), value));
        call.resolve(ret);
    }
}
