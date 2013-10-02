package stanhebben.minetweaker.api.value;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import stanhebben.minetweaker.api.Tweaker;
import stanhebben.minetweaker.api.TweakerExecuteException;
import stanhebben.minetweaker.base.actions.SetFuelItemAction;
import stanhebben.minetweaker.base.actions.SetLocalizedStringAction;

public class TweakerItemSub extends TweakerItem {
	private int id;
	private int meta;
	private Item item;
	private ItemStack stack;
	
	public TweakerItemSub(int id, int meta) throws TweakerExecuteException {
		this.id = id;
		this.meta = meta;
		if (Item.itemsList[id] == null) {
			throw new TweakerExecuteException("No item with id " + id);
		}
		item = Item.itemsList[id];
		if (!item.getHasSubtypes()) {
			throw new TweakerExecuteException("Item has no subitems");
		}
		stack = new ItemStack(id, 1, meta);
	}
	
	@Override
	public int getItemId() {
		return id;
	}

	@Override
	public int getItemSubId() {
		return meta;
	}
	
	@Override
	public boolean isSubItem() {
		return true;
	}

	@Override
	public ItemStack make(int amount) {
		if (amount == 1) {
			return stack;
		} else {
			return new ItemStack(id, amount, meta);
		}
	}

	@Override
	public String getName() {
		return item.getUnlocalizedName(stack);
	}

	@Override
	public String getDisplayName() {
		//#ifdef MC152
		//+return item.getLocalizedName(stack);
		//#else
		return item.getItemStackDisplayName(stack);
		//#endif
	}

	@Override
	public void setDisplayName(String value) {
		Tweaker.apply(new SetLocalizedStringAction(item.getUnlocalizedName(stack) + ".name", "en_US", value));
	}

	@Override
	public void setDisplayName(String lang, String value) {
		Tweaker.apply(new SetLocalizedStringAction(item.getUnlocalizedName(stack) + ".name", lang, value));
	}
	
	@Override
	public int getMaxDamage() {
		return stack.getMaxDamage();
	}

	@Override
	public int getFuelValue() {
		return TileEntityFurnace.getItemBurnTime(stack);
	}

	@Override
	public void setFuelValue(int value) throws TweakerExecuteException {
		Tweaker.apply(new SetFuelItemAction(this, value));
	}
	
	@Override
	public String toIdString() {
		return id + ":" + meta;
	}

	@Override
	public String toString() {
		return "<" + id + ":" + meta + ">";
	}
}
