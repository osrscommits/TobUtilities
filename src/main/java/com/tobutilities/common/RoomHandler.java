package com.tobutilities.common;

import com.google.inject.Provides;
import com.tobutilities.TobUtilitiesConfig;
import com.tobutilities.TobUtilitiesPlugin;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginManager;
import net.runelite.client.ui.overlay.OverlayManager;

public class RoomHandler
{
	@Inject
	protected Client client;
	@Inject
	private OverlayManager overlayManager;
	@Inject
	private ConfigManager configManager;
	@Inject
	protected PluginManager pluginManager;

	@Inject
	protected ClientThread clientThread;

	protected TobUtilitiesConfig config;

	protected TobUtilitiesPlugin plugin;

	@Provides
	TobUtilitiesConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TobUtilitiesConfig.class);
	}

	@Inject
	protected RoomHandler(TobUtilitiesPlugin plugin, TobUtilitiesConfig config, Client client)
	{
		this.plugin = plugin;
		this.config = config;
		this.client = client;
	}

}
