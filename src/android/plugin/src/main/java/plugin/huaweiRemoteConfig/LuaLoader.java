//
//  LuaLoader.java
//  TemplateApp
//
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

// This corresponds to the name of the Lua library,
// e.g. [Lua] require "plugin.library"
package plugin.huaweiRemoteConfig;

import android.content.Context;
import android.util.Log;

import com.ansca.corona.CoronaActivity;
import com.ansca.corona.CoronaEnvironment;
import com.ansca.corona.CoronaLua;
import com.ansca.corona.CoronaRuntime;
import com.ansca.corona.CoronaRuntimeListener;
import com.ansca.corona.CoronaRuntimeTask;
import com.huawei.agconnect.config.AGConnectServicesConfig;
import com.huawei.agconnect.config.LazyInputStream;
import com.huawei.agconnect.remoteconfig.AGConnectConfig;
import com.huawei.agconnect.remoteconfig.ConfigValues;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;
import com.naef.jnlua.NamedJavaFunction;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


@SuppressWarnings("WeakerAccess")
public class LuaLoader implements JavaFunction, CoronaRuntimeListener {

    private int fListener;

    public static final String TAG = "Huawei Remote Config";

    public static AGConnectConfig config = null;

    /**
     * This corresponds to the event name, e.g. [Lua] event.name
     */
    private static final String EVENT_NAME = "Huawei Remote Config";
    private static final String VERSION = "1.0.0";

    public static final String applyDefault = "applyDefault";
    public static final String fetch = "fetch";
    public static final String getValueAsBoolean = "getValueAsBoolean";
    public static final String getValueAsDouble = "getValueAsDouble";
    public static final String getValueAsLong = "getValueAsLong";
    public static final String getValueAsString = "getValueAsString";
    public static final String getValueAsByteArray = "getValueAsByteArray";
    public static final String getMergedAll = "getMergedAll";
    public static final String getSource = "getSource";
    public static final String clearAll = "clearAll";
    public static final String setDeveloperMode = "setDeveloperMode";

    @SuppressWarnings("unused")
    public LuaLoader() {
        // Initialize member variables.
        fListener = CoronaLua.REFNIL;

        // Set up this plugin to listen for Corona runtime events to be received by methods
        // onLoaded(), onStarted(), onSuspended(), onResumed(), and onExiting().
        CoronaEnvironment.addRuntimeListener(this);
    }

    @Override
    public int invoke(LuaState L) {
        // Register this plugin into Lua with the following functions.
        NamedJavaFunction[] luaFunctions = new NamedJavaFunction[]{
                new init(),
                new applyDefault(),
                new fetch(),
                new getValueAsBoolean(),
                new getValueAsDouble(),
                new getValueAsLong(),
                new getValueAsString(),
                new getValueAsByteArray(),
                new getMergedAll(),
                new getSource(),
                new clearAll(),
                new setDeveloperMode(),
                new getMergedAll(),
        };
        String libName = L.toString(1);
        L.register(libName, luaFunctions);

        // Returning 1 indicates that the Lua require() function will return the above Lua library.
        return 1;
    }


    @Override
    public void onLoaded(CoronaRuntime runtime) {
    }

    @Override
    public void onStarted(CoronaRuntime runtime) {
        Log.i(EVENT_NAME, "Started v"+VERSION);
    }


    @Override
    public void onSuspended(CoronaRuntime runtime) {
    }


    @Override
    public void onResumed(CoronaRuntime runtime) {
    }

    @Override
    public void onExiting(CoronaRuntime runtime) {
        // Remove the Lua listener reference.
        CoronaLua.deleteRef(runtime.getLuaState(), fListener);
        fListener = CoronaLua.REFNIL;
    }

    @SuppressWarnings("unused")
    public void dispatchEvent(final String message) {
        CoronaEnvironment.getCoronaActivity().getRuntimeTaskDispatcher().send(new CoronaRuntimeTask() {
            @Override
            public void executeUsing(CoronaRuntime runtime) {
                LuaState L = runtime.getLuaState();

                CoronaLua.newEvent(L, EVENT_NAME);

                L.pushString(message);
                L.setField(-2, "message");

                try {
                    CoronaLua.dispatchEvent(L, fListener, 0);
                } catch (Exception ignored) {
                }
            }
        });
    }

    @SuppressWarnings("unused")
    public void dispatchEvent(final Boolean isError, final String message, final String type, final String provider, final JSONObject data) {
        CoronaEnvironment.getCoronaActivity().getRuntimeTaskDispatcher().send(new CoronaRuntimeTask() {
            @Override
            public void executeUsing(CoronaRuntime runtime) {
                LuaState L = runtime.getLuaState();

                CoronaLua.newEvent(L, EVENT_NAME);

                L.pushString(message);
                L.setField(-2, "message");

                L.pushBoolean(isError);
                L.setField(-2, "isError");

                L.pushString(type);
                L.setField(-2, "type");

                L.pushString(provider);
                L.setField(-2, "provider");

                L.pushString(data.toString());
                L.setField(-2, "data");
                try {
                    CoronaLua.dispatchEvent(L, fListener, 0);
                } catch (Exception ignored) {
                }

            }
        });
    }

    @SuppressWarnings("unused")
    public void dispatchEvent(final Boolean isError, final String message, final String type, final String provider) {
        CoronaEnvironment.getCoronaActivity().getRuntimeTaskDispatcher().send(new CoronaRuntimeTask() {
            @Override
            public void executeUsing(CoronaRuntime runtime) {
                LuaState L = runtime.getLuaState();

                CoronaLua.newEvent(L, EVENT_NAME);

                L.pushString(message);
                L.setField(-2, "message");

                L.pushBoolean(isError);
                L.setField(-2, "isError");

                L.pushString(type);
                L.setField(-2, "type");

                L.pushString(provider);
                L.setField(-2, "provider");

                try {
                    CoronaLua.dispatchEvent(L, fListener, 0);
                } catch (Exception ignored) {
                }
            }
        });
    }

    @SuppressWarnings("unused")
    private class init implements NamedJavaFunction {

        @Override
        public String getName() {
            return "init";
        }

        @Override
        public int invoke(LuaState L) {
            int listenerIndex = 1;

            if (CoronaLua.isListener(L, listenerIndex, EVENT_NAME)) {
                fListener = CoronaLua.newRef(L, listenerIndex);
            }

            CoronaActivity activity = CoronaEnvironment.getCoronaActivity();
            AGConnectServicesConfig config = AGConnectServicesConfig.fromContext(activity);
            config.overlayWith(new LazyInputStream(activity) {
                public InputStream get(Context context) {
                    try {
                        Log.i(TAG, "agconnect-services.json was read");
                        return context.getAssets().open("agconnect-services.json");
                    } catch (IOException e) {
                        Log.i(TAG, "agconnect-services.json reading Exception " + e);
                        return null;
                    }
                }
            });
            return 0;
        }
    }

    public void getInstance() {
        if (config == null) config = AGConnectConfig.getInstance();
    }

    @SuppressWarnings("unused")
    public class applyDefault implements NamedJavaFunction {

        @Override
        public String getName() {
            return applyDefault;
        }

        @Override
        public int invoke(LuaState luaState) {

            CoronaActivity activity = CoronaEnvironment.getCoronaActivity();
            if (activity == null) {
                return 0;
            }

            getInstance();

            Map<String, Object> map = new HashMap<>();
//            if (luaState.isString(1)) {
//                dispatchEvent(true, "applyDefault (Table) expected, got " + luaState.typeName(1), applyDefault, TAG);
//            } else
            if (luaState.isTable(1)) {
                for (luaState.pushNil(); luaState.next(1); luaState.pop(1)) {
                    String key = luaState.toString(-2);
                    map.put(luaState.toString(-2), luaState.toJavaObject(-1, Object.class));
                }
            } else {
                dispatchEvent(true, "applyDefault (Table) expected, got " + luaState.typeName(1), applyDefault, TAG);
                return 0;
            }

            try {
                config.applyDefault(map);
                dispatchEvent(false, "applyDefault(); is succesful", applyDefault, TAG);
            } catch (Exception e) {
                dispatchEvent(true, "applyDefault(); Error " + e.getMessage(), applyDefault, TAG);
            }

            return 0;
        }
    }

    @SuppressWarnings("unused")
    public class fetch implements NamedJavaFunction {

        @Override
        public String getName() {
            return fetch;
        }

        @Override
        public int invoke(LuaState luaState) {

            CoronaActivity activity = CoronaEnvironment.getCoronaActivity();
            if (activity == null) {
                return 0;
            }

            getInstance();

            Task task;
            if (luaState.isNumber(1)) {
                task = config.fetch(luaState.checkInteger(1));
            } else {
                task = config.fetch();
            }

            task.addOnSuccessListener(new OnSuccessListener<ConfigValues>() {
                @Override
                public void onSuccess(ConfigValues configValues) {
                    config.apply(configValues);
                    dispatchEvent(false, "Remote Config Fetch Success", fetch, TAG);
                }
            });
            task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    dispatchEvent(true, "Remote Config Fetch Error " + e.getMessage(), fetch, TAG);
                }
            });
            return 0;
        }
    }

    @SuppressWarnings("unused")
    public class getValueAsBoolean implements NamedJavaFunction {

        @Override
        public String getName() {
            return getValueAsBoolean;
        }

        @Override
        public int invoke(LuaState luaState) {

            CoronaActivity activity = CoronaEnvironment.getCoronaActivity();
            if (activity == null) {
                return 0;
            }

            getInstance();

            String value = null;
            if (luaState.isString(1)) {
                value = luaState.checkString(1);
            } else {
                dispatchEvent(true, "getValueAsBoolean (String) expected, got " + luaState.typeName(1), getValueAsBoolean, TAG);
                return 0;
            }

            try {
                luaState.pushBoolean(config.getValueAsBoolean(value));
                dispatchEvent(false, "getValueAsBoolean(" + value + "); is succesful", getValueAsBoolean, TAG);
            } catch (Exception e) {
                dispatchEvent(true, "getValueAsBoolean(" + value + "); Error " + e.getMessage(), getValueAsBoolean, TAG);
            }

            return 1;
        }
    }

    @SuppressWarnings("unused")
    public class getValueAsDouble implements NamedJavaFunction {

        @Override
        public String getName() {
            return getValueAsDouble;
        }

        @Override
        public int invoke(LuaState luaState) {

            CoronaActivity activity = CoronaEnvironment.getCoronaActivity();
            if (activity == null) {
                return 0;
            }

            getInstance();

            String value = null;
            if (luaState.isString(1)) {
                value = luaState.checkString(1);
            } else {
                dispatchEvent(true, "getValueAsDouble (String) expected, got " + luaState.typeName(1), getValueAsDouble, TAG);
                return 0;
            }

            try {
                luaState.pushNumber(config.getValueAsDouble(value));
                dispatchEvent(false, "getValueAsDouble(" + value + "); is succesful", getValueAsDouble, TAG);
            } catch (Exception e) {
                dispatchEvent(true, "getValueAsDouble(" + value + "); Error " + e.getMessage(), getValueAsDouble, TAG);
            }

            return 1;
        }
    }

    @SuppressWarnings("unused")
    public class getValueAsLong implements NamedJavaFunction {

        @Override
        public String getName() {
            return getValueAsLong;
        }

        @Override
        public int invoke(LuaState luaState) {

            CoronaActivity activity = CoronaEnvironment.getCoronaActivity();
            if (activity == null) {
                return 0;
            }

            getInstance();

            String value = null;
            if (luaState.isString(1)) {
                value = luaState.checkString(1);
            } else {
                dispatchEvent(true, "getValueAsLong (String) expected, got " + luaState.typeName(1), getValueAsLong, TAG);
                return 0;
            }

            try {
                luaState.pushNumber(config.getValueAsLong(value));
                dispatchEvent(false, "getValueAsLong(" + value + "); is succesful", getValueAsLong, TAG);
            } catch (Exception e) {
                dispatchEvent(true, "getValueAsLong(" + value + "); Error " + e.getMessage(), getValueAsLong, TAG);
            }

            return 1;
        }
    }

    @SuppressWarnings("unused")
    public class getValueAsString implements NamedJavaFunction {

        @Override
        public String getName() {
            return getValueAsString;
        }

        @Override
        public int invoke(LuaState luaState) {

            CoronaActivity activity = CoronaEnvironment.getCoronaActivity();
            if (activity == null) {
                return 0;
            }

            getInstance();

            String value = null;
            if (luaState.isString(1)) {
                value = luaState.checkString(1);
            } else {
                dispatchEvent(true, "getValueAsString (String) expected, got " + luaState.typeName(1), getValueAsString, TAG);
                Log.i(TAG, "getValueAsString (String) expected, got " + luaState.typeName(1));
                return 0;
            }

            try {
                luaState.pushString(config.getValueAsString(value));
                dispatchEvent(false, "getValueAsString(" + value + "); is succesful", getValueAsString, TAG);
            } catch (Exception e) {
                dispatchEvent(true, "getValueAsString(" + value + "); Error " + e.getMessage(), getValueAsString, TAG);
            }


            return 1;
        }
    }

    @SuppressWarnings("unused")
    public class getValueAsByteArray implements NamedJavaFunction {

        @Override
        public String getName() {
            return getValueAsByteArray;
        }

        @Override
        public int invoke(LuaState luaState) {

            CoronaActivity activity = CoronaEnvironment.getCoronaActivity();
            if (activity == null) {
                return 0;
            }

            getInstance();

            String value = null;
            if (luaState.isString(1)) {
                value = luaState.checkString(1);
            } else {
                dispatchEvent(true, "getValueAsByteArray (String) expected, got " + luaState.typeName(1), getValueAsByteArray, TAG);
                Log.i(TAG, "getValueAsByteArray (String) expected, got " + luaState.typeName(1));
                return 0;
            }

            try {
                luaState.pushString(config.getValueAsByteArray(value));
                dispatchEvent(false, "getValueAsByteArray(" + value + "); is succesful", getValueAsByteArray, TAG);
            } catch (Exception e) {
                dispatchEvent(true, "getValueAsByteArray(" + value + "); Error " + e.getMessage(), getValueAsByteArray, TAG);
            }

            return 1;
        }
    }

    @SuppressWarnings("unused")
    public class getMergedAll implements NamedJavaFunction {

        @Override
        public String getName() {
            return getMergedAll;
        }

        @Override
        public int invoke(LuaState luaState) {

            CoronaActivity activity = CoronaEnvironment.getCoronaActivity();
            if (activity == null) {
                return 0;
            }

            getInstance();

            JSONObject jsonObject = new JSONObject();
            try {
                Map<String, Object> map = config.getMergedAll();
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    jsonObject.put(entry.getKey(), entry.getValue());
                }
                dispatchEvent(false, "getMergedAll(); is succesful", getMergedAll, TAG);
            } catch (Exception e) {
                dispatchEvent(true, "getMergedAll(); Error " + e.getMessage(), getMergedAll, TAG);
            }

            luaState.pushString(jsonObject.toString());
            return 1;
        }
    }

    @SuppressWarnings("unused")
    public class getSource implements NamedJavaFunction {

        @Override
        public String getName() {
            return getSource;
        }

        @Override
        public int invoke(LuaState luaState) {

            CoronaActivity activity = CoronaEnvironment.getCoronaActivity();
            if (activity == null) {
                return 0;
            }

            getInstance();

            String value = null;
            if (luaState.isString(1)) {
                value = luaState.checkString(1);
            } else {
                dispatchEvent(true, "getSource (String) expected, got " + luaState.typeName(1), getSource, TAG);
                return 0;
            }

            try {
                luaState.pushString(config.getSource(value).toString());
                dispatchEvent(false, "getSource(" + value + "); is succesful ", getSource, TAG);
            } catch (Exception e) {
                dispatchEvent(true, "getSource(" + value + "); Error " + e.getMessage(), getSource, TAG);
            }

            return 1;
        }
    }

    @SuppressWarnings("unused")
    public class clearAll implements NamedJavaFunction {

        @Override
        public String getName() {
            return clearAll;
        }

        @Override
        public int invoke(LuaState luaState) {

            CoronaActivity activity = CoronaEnvironment.getCoronaActivity();
            if (activity == null) {
                return 0;
            }

            getInstance();

            try {
                config.clearAll();
                dispatchEvent(false, "clearAll(); is succesful ", clearAll, TAG);
            } catch (Exception e) {
                dispatchEvent(true, "clearAll(); Error " + e.getMessage(), clearAll, TAG);
            }

            return 0;
        }
    }

    @SuppressWarnings("unused")
    public class setDeveloperMode implements NamedJavaFunction {

        @Override
        public String getName() {
            return setDeveloperMode;
        }

        @Override
        public int invoke(LuaState luaState) {

            CoronaActivity activity = CoronaEnvironment.getCoronaActivity();
            if (activity == null) {
                return 0;
            }

            getInstance();

            Boolean value = null;
            if (luaState.isBoolean(1)) {
                value = luaState.checkBoolean(1);
            } else {
                dispatchEvent(true, "setDeveloperMode (Boolean) expected, got " + luaState.typeName(1), setDeveloperMode, TAG);
                return 0;
            }

            try {
                config.setDeveloperMode(value);
                dispatchEvent(false, "setDeveloperMode(" + value + ") is successful", setDeveloperMode, TAG);
            } catch (Exception e) {
                dispatchEvent(true, "setDeveloperMode(" + value + "); Error " + e.getMessage(), setDeveloperMode, TAG);
            }

            return 0;
        }
    }
}
