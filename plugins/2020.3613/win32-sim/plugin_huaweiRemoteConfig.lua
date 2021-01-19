local Library = require "CoronaLibrary"

local lib = Library:new{ name='plugin.huaweiRemoteConfig', publisherId='com.solar2d' }

local placeholder = function()
	print( "WARNING: The '" .. lib.name .. "' library is not available on this platform." )
end


lib.init = placeholder
lib.applyDefault = placeholder
lib.fetch = placeholder
lib.getValueAsBoolean = placeholder
lib.getValueAsDouble = placeholder
lib.getValueAsLong = placeholder
lib.getValueAsString = placeholder
lib.getValueAsByteArray = placeholder
lib.getMergedAll = placeholder
lib.getSource = placeholder
lib.clearAll = placeholder
lib.setDeveloperMode = placeholder

-- Return an instance
return lib