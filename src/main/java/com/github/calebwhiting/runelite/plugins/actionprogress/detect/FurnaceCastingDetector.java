package com.github.calebwhiting.runelite.plugins.actionprogress.detect;

import com.github.calebwhiting.runelite.data.Crafting;
import com.github.calebwhiting.runelite.plugins.actionprogress.Action;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.events.WidgetClosed;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.gameval.VarbitID;
import net.runelite.client.eventbus.Subscribe;

import static net.runelite.api.gameval.AnimationID.HUMAN_FURNACE;


/**
 * Detects actions initiated from the furnace casting interface (Gold/Silver products)
 */
@Slf4j
public class FurnaceCastingDetector extends ActionDetector
{
	/**
	 * Indicates how many items are to be created in the crafting dialogue.
	 */
	private static final int VAR_FURNACE_MAKE_AMOUNT = 2224;

	@Inject private Client client;

	@Override
	public void setup()
	{
		this.registerAction(Action.CRAFT_CAST_GOLD_AND_SILVER, Crafting.SILVER_AND_GOLD_ITEMS);
	}

	/**
	 * Consumes Widget Closed event and runs if the widget was our gold or silver casting
	 * @param evt Widget Closed Event
	 */
	@Subscribe
	@Singleton
	public void onWidgetClosed(WidgetClosed evt){

		// Gold Casting
		if(evt.getGroupId() == InterfaceID.CRAFTING_GOLD) {
			// Get the player's active animation, if there is no animation this also fails
			Player me = this.client.getLocalPlayer();
			if (me.getAnimation() != HUMAN_FURNACE) {
				return;
			}

			// action count is a player value while last type is a varbit, we may need to update VAR_FURNACE_MAKE_AMOUNT
			int actionCount = this.client.getVarpValue(VAR_FURNACE_MAKE_AMOUNT);
			int lastType =	this.client.getVarbitValue(VarbitID.CRAFTING_GOLD_ITEM_LASTTYPE) - 1; // minus 1 for array trav

			// error check so we don't out of bounds in case they ever change the method by which they count
			if (lastType >= 0) {
				this.setActionByItemId(Crafting.GOLD_ITEMS[lastType], actionCount);
			}
		}
		// Silver Casting
		else if(evt.getGroupId() == InterfaceID.SILVER_CRAFTING) {
			// Get the player's active animation, if there is no animation this also fails
			Player me = this.client.getLocalPlayer();
			if (me.getAnimation() != HUMAN_FURNACE) {
				return;
			}

			// action count is a player value while last type is a varbit, we may need to update VAR_FURNACE_MAKE_AMOUNT
			int actionCount = this.client.getVarpValue(VAR_FURNACE_MAKE_AMOUNT);
			int lastType =	this.client.getVarbitValue(VarbitID.CRAFTING_SILVER_ITEM_LASTTYPE) - 1; // minus 1 for array trav

			// error check so we don't out of bounds in case they ever change the method by which they count
			if (lastType >= 0) {
				this.setActionByItemId(Crafting.SILVER_ITEMS[lastType], actionCount);
			}
		}
	}
}