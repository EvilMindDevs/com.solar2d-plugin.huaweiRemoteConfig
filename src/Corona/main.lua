local remoteConfig = require "plugin.huaweiRemoteConfig"
local widget = require( "widget" )
local json = require("json")


local TAG = "remoteConfig"

local header = display.newText( "Huawei Remote Config", display.contentCenterX, 60, native.systemFont, 10 )
header:setFillColor( 255, 255, 255 )

local function listener( event )
    print(TAG, json.prettify(event))
end

remoteConfig.init( listener )

-- Remote Config
local applyDefault = widget.newButton(
    {
        left = 65,
        top = 110,
        id = "applyDefault",
        label = "applyDefault",
        onPress = function()
            local defaultValues = {
                    a = "text",
                    b = 1,
                    c = 2.5,
                    d = 12512,
                    e = true
                }
            remoteConfig.applyDefault(defaultValues)
        end,
        width = 190,
        height = 30
    }
)

local fetch = widget.newButton(
    {
        left = 65,
        top = 140,
        id = "fetch",
        label = "fetch",
        onPress = remoteConfig.fetch,
        width = 190,
        height = 30
    }
)

local getValueAsBoolean = widget.newButton(
    {
        left = 65,
        top = 170,
        id = "getValueAsBoolean",
        label = "getValueAsBoolean",
        onPress = function()
            print(TAG, "getValueAsBoolean =>" .. tostring(remoteConfig.getValueAsBoolean("e")))
        end,
        width = 190,
        height = 30
    }
)

local getValueAsDouble = widget.newButton(
    {
        left = 65,
        top = 200,
        id = "getValueAsDouble",
        label = "getValueAsDouble",
        onPress = function()
            print(TAG, "getValueAsDouble =>" .. remoteConfig.getValueAsDouble("c"))
        end,
        width = 190,
        height = 30
    }
)

local getValueAsLong = widget.newButton(
    {
        left = 65,
        top = 230,
        id = "getValueAsLong",
        label = "getValueAsLong",
        onPress = function()
            print(TAG, "getValueAsLong =>" .. remoteConfig.getValueAsLong("d"))
        end,
        width = 190,
        height = 30
    }
)

local getValueAsString = widget.newButton(
    {
        left = 65,
        top = 260,
        id = "getValueAsString",
        label = "getValueAsString",
        onPress = function()
            print(TAG, "getValueAsString =>" .. remoteConfig.getValueAsString("a"))
        end,
        width = 190,
        height = 30
    }
)

local getValueAsByteArray = widget.newButton(
    {
        left = 65,
        top = 290,
        id = "getValueAsByteArray",
        label = "getValueAsByteArray",
        onPress = function()
            print(TAG, "getValueAsByteArray =>" .. remoteConfig.getValueAsByteArray("key"))
        end,
        width = 190,
        height = 30
    }
)

local getMergedAll = widget.newButton(
    {
        left = 65,
        top = 320,
        id = "getMergedAll",
        label = "getMergedAll",
        onPress = function()
            print(TAG, " getMergedAll() => ", json.prettify(remoteConfig.getMergedAll()));
        end,
        width = 190,
        height = 30
    }
)

local getSource = widget.newButton(
    {
        left = 65,
        top = 350,
        id = "getSource",
        label = "getSource",
        onPress = function()
            print(TAG, remoteConfig.getSource("abc3"))
        end,
        width = 190,
        height = 30
    }
)

local clearAll = widget.newButton(
    {
        left = 65,
        top = 380,
        id = "clearAll",
        label = "clearAll",
        onPress = remoteConfig.clearAll,
        width = 190,
        height = 30
    }
)

local setDeveloperMode = widget.newButton(
    {
        left = 65,
        top = 410,
        id = "setDeveloperMode",
        label = "setDeveloperMode",
        onPress = function()
            remoteConfig.setDeveloperMode(true)
        end,
        width = 190,
        height = 30
    }
)
