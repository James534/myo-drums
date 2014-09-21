scriptId = 'com.thalmic.mousetest'

-- -- This allows a user to control their cursor using the Myo.
-- -- Gestures:
-- -- Wave left: Left click
-- -- Wave right: Right click
-- -- Fist: Click and drag

-- -- Helpers

-- -- Forces waveOut = waveRight, waveIn = waveLeft regardless of arm
function conditionallySwapWave(pose)
    if myo.getArm() == "left" then
        if pose == "waveIn" then
            pose = "waveOut"
        elseif pose == "waveOut" then
            pose = "waveIn"
        end
    end
    return pose
end

-- -- Handle gestures from Myo

function onPoseEdge(pose, edge)
    pose = conditionallySwapWave(pose)

end

-- Other callbacks

function onPeriodic()
	myo.debug("XDirection = " .. myo.getXDirection())
	myo.debug("GetPitch = " .. myo.getPitch())
	myo.debug("GetYaw = " .. myo.getYaw())
	drum = 1
	if myo.getYaw() < 0.5 then
		drum = 1
	elseif myo.getYaw() < 1.0 then
		drum = 2
	elseif myo.getYaw() < 1.5 then
		drum = 3
	else
		drum = 4
	end
	
	x_dir = myo.getXDirection()
	pitch = myo.getPitch() 
	true_pitch = pitch	
	if x_dir == 'towardWrist' then
		true_pitch = -pitch
	end

	if true_pitch < -0.1 then
		if drum == 1 then
			myo.keyboard("a", "press")
		elseif drum == 2 then
			myo.keyboard("s", "press")
		elseif drum == 3 then
			myo.keyboard("d", "press")
		else
			myo.keyboard("f", "press")
		end
	end
	myo.debug("DRUM = " .. drum)

end

-- Here we decide if we want to control the new active app.
function onForegroundWindowChange(app, title)
    myo.debug("onForegroundWindowChange: " .. app .. ", " .. title)

    local wantActive = false
    activeApp = ""

    if platform == "MacOS" then
        if app == "net.java.openjdk.cmd" then --
            -- TextEdit on MacOS
            wantActive = true
            activeApp = "Drums"
        end
    elseif platform == "Windows" then
        -- Powerpoint on Windows
        wantActive = string.match(title, " %- Eclipse$") or
                     string.match(title, "^Eclipse %- ") or
                     string.match(title, " %- Eclipse$")
        activeApp = "Eclipse"
    end
    return wantActive
end

function activeAppName()
    return "Output Everything"
end

function onActiveChange(isActive)
    myo.debug("onActiveChange")
end

-- Functionality
-- -- Repeat previous sound -- Playback -- open Hand (Background on Gui changes backward)
-- -- Y-axis Resolution for 4 drums and 4 cymbols - Gui + (Vibrate - short)?
-- -- Z-axis (hardness of a hit measured by Acceleration) to Volume 4 levels?
--  -- Possibly fist = Start recording sound for playback 1 (Vibrate medium, background on gui changes forward)
--
