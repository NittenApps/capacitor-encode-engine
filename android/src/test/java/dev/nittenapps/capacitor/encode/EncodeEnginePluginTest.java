package dev.nittenapps.capacitor.encode;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.util.Base64;

import com.getcapacitor.JSObject;
import com.getcapacitor.PluginCall;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EncodeEnginePluginTest {
    private final EncodeEnginePlugin plugin = new EncodeEnginePlugin();

    @Test
    public void checkDigits() {
        String input = "ANAC14947170425062650";
        String digits = EncodeEngine.checkDigits(input);
        assertEquals("JX", digits);
    }

    @Test
    public void plugin_checkDigits() {
        PluginCall mockCall = mock(PluginCall.class);
        when(mockCall.getString("input")).thenReturn("160425222835");

        plugin.checkDigits(mockCall);

        verify(mockCall, times(1)).resolve(argThat(argument -> {
            try {
                JSObject expected = new JSObject();
                expected.put("value", "1234");
                return expected.toString().equals(argument.toString());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }));
    }

    @Test
    public void plugin_decode() {
        mockStatic(Base64.class);
        when(Base64.encode(any(byte[].class), anyInt())).thenAnswer(invocation -> {
            // Implement desired behavior, e.g., use java.util.Base64
            return java.util.Base64.getEncoder().encode((byte[])invocation.getArgument(0));
        });

        PluginCall mockCall = mock(PluginCall.class);
        when(mockCall.getString("key")).thenReturn("CHUC303880608251528026NA");
        when(mockCall.getString("value")).thenReturn(
                "x0lFLk5jNm1grYWHQB3b\\/K2ge\\/YNGC\\/N1f7C1YV2SW7jq5XokFa0P+xu07J+04I4VVOluyas+TVDsdux0ysUB1VPVyWfdGjCJzy3mWqBPAIMSMtRQEZ\\/kCAZe92gfyQNWhsEGuhB2XhuzjtSYbZYSXuMcjBexmvv2I0PI34VR1Op8txHp\\/YXEWXoRgyZovfvWfPsxOA\\/h07XbC9G11fmomGyuHJVINt8f1TSuEHGj4NpHgPk5Mgda+W41GzDNfkPqGzd\\/4TrhfT8IpZO7K9UsoYFrzQ97LgpCihqB5ZkAqpVRrlNolmZqnvuMMnD2\\/XRC0Ei3j69KwN3Qtvr6Et67ScLC+We\\/5aU0QYILo7rbyM0jb1\\/1kD3CT4TxNMtuX4nSYkPtvg29EwJ+WVh22+aicTJ7Y30PGrTBLfWiwuZ2DuQ53gZnDRXVZimbKmRmJzn75h54RQF\\/q4TjlS3WHCssrRZj1u0zfKRPiRnyQ6DEZ1nvi03o6wW4t7iGb3oMTcqQMx0oQkwCPUTL\\/zzNLqKOiQNyiKL7PkSk2aZBiNVytExvWK+WS4rdUn+Gn62ApVNbe68zxJeJ38oGKgksX5afRfDhMpghbl7EdjnEefPKKsBKaGWvbAktJvu0p3M286reRlQGMbNdjlu926Bs99SGxEcFAimonhRe0njuTo88XAUD5+QsfdhGQ==");

        plugin.decode(mockCall);

        verify(mockCall, times(1)).resolve(argThat(argument -> {
            try {
                JSObject expected = new JSObject();
                expected.put("value", "1234");
                return expected.toString().equals(argument.toString());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }));
    }
}
