# Huawei Remote Config Solar2d Plugin

This plugin was created based on Huawei Remote Config. Please [check](https://developer.huawei.com/consumer/en/agconnect/remote-configuration/) for detailed information about Huawei Remote Config. 

In order to use the Huawei Remote Config, you must first create an account from developer.huawei.com. And after logging in with your account, and then you must create a project in the huawei console in order to use HMS kits.

## Project Setup

To use the plugin please add following to `build.settings`

```lua
{
    plugins = {
        ["plugin.huaweiRemoteConfig"] = {
            publisherId = "com.solar2d",
        },
    },
}
```

And then you have to create keystore for your app. And you must generate sha-256 bit fingerprint from this keystore using the command here. You have to define this fingerprint to your project on the huawei console.

And you must add the keystore you created while building your project. 
Also you need to give the package-name of the project you created on Huawei Console.
And you need to put `agconnect-services.json` file into `main.lua` directory.

After all the configuration processes, you must define the plugin in main.lua.

```lua
local remoteConfig = require "plugin.huaweiRemoteConfig"

local function listener(event)
    print(event) -- (table)
end

remoteConfig.init(listener) -- sets listener and HMS plugin
```

We should call all methods through remoteConfig object. And you can take result informations from listener.

## Methods in the Plugin

### applyDefault
Used to add a default values.

```lua
    local defaultValues = {
            a = "text",
            b = 1,
            c = 2.5,
            d = 12512
            e = true
        }
    remoteConfig.applyDefault(defaultValues)

    --Result 
    --[[(Listener) Table {
              isError = true|false
              message = text
              type = applyDefault (text)
              provider = Huawei RemoteConfig (text)
        } 
    ]]--
```

### fetch
Used to pull the values from cloud. If you don`t know to add parameter in cloud, [check](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-remoteconfig-android-obtainconfig-0000001054997460#h1-1592307038995) here

```lua
    remoteConfig.fetch()
    remoteConfig.fetch(long intervalSeconds)

    --Result 
    --[[(Listener) Table {
              isError = true|false
              message = text
              type = fetch (text)
              provider = Huawei RemoteConfig (text)
        } 
    ]]--
```

### getValueAsBoolean
Returns the value of the boolean type for a key.

```lua
    local value = remoteConfig.getValueAsBoolean("key")

    --Result (Boolean)
    --[[(Listener) Table {
              isError = true|false
              message = text
              type = getValueAsBoolean (text)
              provider = Huawei RemoteConfig (text)
        } 
    ]]--
```

### getValueAsDouble
Returns the value of the double type for a key.

```lua
    local value = remoteConfig.getValueAsDouble("key")

    --Result (Double)
    --[[(Listener) Table {
              isError = true|false
              message = text
              type = getValueAsDouble (text)
              provider = Huawei RemoteConfig (text)
        } 
    ]]--
```

### getValueAsLong
Returns the value of the long type for a key.

```lua
    local value = remoteConfig.getValueAsLong("key")

    --Result (long)
    --[[(Listener) Table {
              isError = true|false
              message = text
              type = getValueAsLong (text)
              provider = Huawei RemoteConfig (text)
        } 
    ]]--
```

### getValueAsString
Returns the value of the string type for a key.

```lua
    local value = remoteConfig.getValueAsString("key")

    --Result (string)
    --[[(Listener) Table {
              isError = true|false
              message = text
              type = getValueAsString (text)
              provider = Huawei RemoteConfig (text)
        } 
    ]]--
```

### getValueAsByteArray
Returns the value of the byte array type for a key.

```lua
    local value = remoteConfig.getValueAsByteArray("key")

    --Result (byte[])
    --[[(Listener) Table {
              isError = true|false
              message = text
              type = getValueAsByteArray (text)
              provider = Huawei RemoteConfig (text)
        } 
    ]]--
```


### getMergedAll
Used to get all values obtained in Remote Config.

```lua
    remoteConfig.getMergedAll()
    print(json.prettify(remoteConfig.getMergedAll()))
    
    --Result (Json)
    --[[(Listener) Table {
              isError = true|false
              message = text
              type = getMergedAll (text)
              provider = Huawei RemoteConfig (text)
        } 
    ]]--
```

### getSource
Used to find the source of a value.(STATIC, DEFAULT, REMOTE)

```lua
    local value = remoteConfig.getSource("key")
    
    --Result (String)
    --[[(Listener) Table {
              isError = true|false
              message = text
              type = getSource (text)
              provider = Huawei RemoteConfig (text)
        } 
    ]]--
```

### clearAll
Used to clear all cached data which fetched from Remote Configuration and default values

```lua
    remoteConfig.clearAll()
    
    --Result 
    --[[(Listener) Table {
              isError = true|false
              message = text
              type = clearAll (text)
              provider = Huawei RemoteConfig (text)
        } 
    ]]--
```

### setDeveloperMode
Used to receive data unlimited times from the cloud, you can set Developer Mode.

```lua
    remoteConfig.setDeveloperMode(true)
    
    --Result 
    --[[(Listener) Table {
              isError = true|false
              message = text
              type = setDeveloperMode (text)
              provider = Huawei RemoteConfig (text)
        } 
    ]]--
```


## References
HMS RemoteConfig https://developer.huawei.com/consumer/en/agconnect/remote-configuration/

## License
MIT
