package com.tobutilities.sote;

import com.tobutilities.TobUtilitiesConfig;
import com.tobutilities.TobUtilitiesPlugin;
import com.tobutilities.bloat.BloatConstants;
import com.tobutilities.common.RoomHandler;
import com.tobutilities.common.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.client.callback.RenderCallback;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;

import javax.inject.Inject;

@Slf4j
public class SoteHandler extends RoomHandler implements RenderCallback {
    private boolean hideSotetsegWalls;

    @Inject
    protected SoteHandler(TobUtilitiesPlugin plugin, TobUtilitiesConfig config, Client client)
    {
        super(plugin, config, client);
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event) {
        if (event.getGroup().equals("tobutilities")) {
            switch (event.getKey()) {
                case "hideSoteWalls":
                    hideSotetsegWalls = config.hideSotetsegWalls();
                    onRoomEntry();
            }
        }
    }

    @Override
    public boolean drawObject(Scene scene, TileObject object)
    {
        if (!(object instanceof GameObject)) {
            return RenderCallback.super.drawObject(scene, object);
        }
        GameObject gameObject = (GameObject) object;
        if (hideSotetsegWalls && SoteConstants.SOTE_WALL_IDS.contains(gameObject.getId())) {
            return false;
        }

        return RenderCallback.super.drawObject(scene, object);
    }

    public void onRoomEntry()
    {
        if (hideSotetsegWalls) {
            CommonUtils.checkForLegacyGPUAndPrintWarning(
                    clientThread,
                    pluginManager,
                    client,
                    "Sotetseg wall hiding does not work with legacy GPU; " +
                            "either switch to the updated GPU plugin, or disable wall hiding " +
                            "in the bloat options in ToB Utilities."
            );
        }
    }

    public void startUp()
    {
        hideSotetsegWalls = config.hideSotetsegWalls();
    }
}
