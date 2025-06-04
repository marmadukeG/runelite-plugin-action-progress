package com.github.calebwhiting.runelite.plugins.actionprogress.detect;

import com.github.calebwhiting.runelite.data.Magic;
import com.github.calebwhiting.runelite.plugins.actionprogress.Action;
import com.github.calebwhiting.runelite.plugins.actionprogress.ActionProgressConfig;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.ItemContainer;
import net.runelite.api.MenuAction;
import net.runelite.api.gameval.*;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.Widget;
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
		ItemContainer inventory = this.client.getItemContainer(InventoryID.INV);
		if (inventory == null) {
			return;
		}
		// There is not a space in the middle of String Jewellery, it is a '\u00A0' afaik
		// We are matching "String[standard ASCII plus the whitespace codepoints]Jewellery"
		if(!evt.getMenuTarget().matches(".*String\\p{Z}Jewellery.*")){
			return;
		}

		for (Magic.StringJewellerySpell stringJewellerySpell : Magic.StringJewellerySpell.values()) {
			Magic.Spell spell = stringJewellerySpell.getSpell();
			Widget widget = this.client.getWidget(spell.getWidgetId());
			if (widget != null && widget.getBorderType() == 0) {
				int itemId = stringJewellerySpell.getJewelleryItemId();
				if (inventory.count(itemId) <= 0) {
					continue;
				}

				int amount = Math.min(inventory.count(itemId), spell.getAvailableCasts(this.client));
				this.actionManager.setAction(Action.MAGIC_STRING_JEWELLERY, amount, itemId);
				break;
			}
		}
	}
}
