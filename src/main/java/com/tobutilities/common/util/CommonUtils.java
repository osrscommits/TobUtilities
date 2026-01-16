package com.tobutilities.common.util;

import com.tobutilities.common.enums.Region;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginManager;

public class CommonUtils
{

	public static Region getRegionByRegionId(int regionId)
	{
		switch (regionId)
		{
			case 12613:
				return Region.MAIDEN;
			case 13125:
				return Region.BLOAT;
			case 13122:
				return Region.NYLOCAS;
			case 13123:
			case 13379:
				return Region.SOTETSEG;
			case 12612:
				return Region.XARPUS;
			case 12611:
				return Region.VERZIK;
			default:
				return Region.UNKNOWN;
		}
	}

	public static int getRegionID(Client client) {
		Player player = client.getLocalPlayer();
		final LocalPoint playerLocation = player.getLocalLocation();
		WorldPoint playerLocationPoint = WorldPoint.fromLocalInstance(client, playerLocation);
		return playerLocationPoint.getRegionID();
	}

	public static void checkForLegacyGPUAndPrintWarning(ClientThread clientThread, PluginManager pluginManager, Client client, String message) {
		for (Plugin plugin : pluginManager.getPlugins()) {
			if (plugin.getName().equals("GPU (legacy)") && pluginManager.isPluginActive(plugin)) {
				clientThread.invoke(() -> client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", message, ""));
			}
		}
	}
}
