package com.github.calebwhiting.runelite.plugins.actionprogress.detect;

import com.github.calebwhiting.runelite.data.Magic;
import com.github.calebwhiting.runelite.plugins.actionprogress.Action;
import com.github.calebwhiting.runelite.plugins.actionprogress.ActionProgressConfig;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.runelite.api.*;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.eventbus.Subscribe;

@Singleton
public class StringJewellerySpellDetector extends ActionDetector
{
	@Inject private ActionProgressConfig config;

	@Inject private Client client;

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked evt)
	{
		if (!this.config.magicStringJewellery()) {
			return;
		}
		if (evt.getMenuAction() != MenuAction.CC_OP) {
			return;
		}
		ItemContainer inventory = this.client.getItemContainer(InventoryID.INVENTORY);
		if (inventory == null) {
			return;
		}
		if (!evt.getMenuTarget().equals("<col=00ff00>String Jewellery</col>")) {
			return;
		}
		int availableCasts = Magic.LunarSpell.STRING_JEWELLERY.getAvailableCasts(this.client);
		int totalItems = 0;
		int jewelleryItemId = 0;
        for (Magic.StringJewellerySpell stringJewellerySpell : Magic.StringJewellerySpell.values()) {
			int itemId = stringJewellerySpell.getJewelleryItemId();
			int availableItems = inventory.count(itemId);
			if (availableItems == 0) {
				continue;
			}
			jewelleryItemId = itemId;
			totalItems += availableItems;
		}

		int amount = Math.min(totalItems, availableCasts);
		this.actionManager.setAction(Action.MAGIC_STRING_JEWELLERY, amount, jewelleryItemId);
	}
}
