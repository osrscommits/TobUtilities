package com.tobutilities.common.metronome;

import com.tobutilities.TobUtilitiesConfig;
import com.tobutilities.TobUtilitiesPlugin;
import com.tobutilities.common.enums.Region;
import static com.tobutilities.common.util.CommonUtils.getRegionByRegionId;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.Random;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.tobutilities.common.util.CommonUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.client.eventbus.Subscribe;

@Slf4j
@Singleton
public class MetronomeService

{
	@Inject
	private Client client;

	@Inject
	private TobUtilitiesPlugin plugin;

	@Inject
	private TobUtilitiesConfig config;

	@Getter
	@Setter
	private boolean metronomeDisplayed = true;
	@Getter
	@Setter
	private Color currentColor = Color.WHITE;
	@Getter
	@Setter
	private int currentColorIndex = 0;
	private final Random random = new Random();


	@Inject
	public MetronomeService(Client client, TobUtilitiesPlugin plugin, TobUtilitiesConfig config)
	{
		this.client = client;
		this.plugin = plugin;
		this.config = config;
	}

	@Subscribe
	public void onGameTick(GameTick tick)
	{
		int regionTickCount;
		Region newTickRegion = CommonUtils.getRegionByRegionId(CommonUtils.getRegionID(client));

		if (newTickRegion != plugin.region)
		{
			if (newTickRegion.getWaveNumber() > plugin.region.getWaveNumber())
			{
				// raid has advanced to next room
				plugin.region = newTickRegion;
				if (isCurrentRegionMetronomeEnabled())
				{
					//randomize color index to avoid potential instance timer sync for player who starts the instance
					currentColorIndex = random.nextInt(plugin.region.getTickCount() + 1);
					//enable metronome if applicable
					setMetronomeDisplayed(true);
				}
			}
			else
			{
				//In event of either wipe or after treasure room
				plugin.region = newTickRegion;
			}
		}

		if (plugin.region.getTickCount() == 0)
		{
			//Player is in a region where metronome isn't used
			setMetronomeDisplayed(false);
			return;
		}

		regionTickCount = plugin.region.getTickCount();

		if (getCurrentColorIndex() >= regionTickCount)
		{
			setCurrentColorIndex(0);
		}

		int colorIndex = getCurrentColorIndex() + 1;
		setCurrentColorIndex(colorIndex);

		switch (colorIndex)
		{
			case 1:
				setCurrentColor(config.getTick1Color());
				break;
			case 2:
				setCurrentColor(config.getTick2Color());
				break;
			case 3:
				setCurrentColor(config.getTick3Color());
				break;
			case 4:
				setCurrentColor(config.getTick4Color());
				break;
			case 5:
				setCurrentColor(config.getTick5Color());
				break;
			case 6:
				setCurrentColor(config.getTick6Color());
				break;
			case 7:
				setCurrentColor(config.getTick7Color());
				break;
		}
	}


	public boolean isCurrentRegionMetronomeEnabled()
	{
		Region region = plugin.region;
		switch (region)
		{
			case BLOAT:
				return config.enableBloatMetronome();
			case NYLOCAS:
				return config.enableNyloMetronome();
			case SOTETSEG:
				return config.enableSoteMetronome();
			case XARPUS:
				return config.enableXarpusMetronome();
			case VERZIK:
				return config.enableVerzikMetronome();
			default:
				return false;
		}
	}

	public void keyPressed(KeyEvent e)
	{
		if (config.metronomeResetHotkey().matches(e))
		{
			currentColorIndex = 0;
			e.consume();
		}
	}
}
