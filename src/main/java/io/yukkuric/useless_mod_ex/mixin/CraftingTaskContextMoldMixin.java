package io.yukkuric.useless_mod_ex.mixin;

import com.sorrowmist.useless.content.machines.advanced_alloy_furnace.ae.CraftingTaskContext;
import com.sorrowmist.useless.content.recipe.AdvancedAlloyFurnaceRecipe;
import com.sorrowmist.useless.content.recipe.MoldMatcher;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({CraftingTaskContext.class})
public interface CraftingTaskContextMoldMixin {
    @Shadow ItemStackHandler getItemHandler();
    @Shadow int getMoldSlot();
    @Inject(
      method = {"getTaskAvailability"},
      at = {@At("HEAD")},
      cancellable = true
   )
   default void uselessStretcher$multiMold(AdvancedAlloyFurnaceRecipe recipe, CallbackInfoReturnable<CraftingTaskContext.TaskAvailability> cir) {
      if (recipe == null || recipe.molds().size() <= 1) return;
      ItemStackHandler itemHandler = this.getItemHandler();
      int moldSlot = this.getMoldSlot();
      if (itemHandler == null || moldSlot < 0 || moldSlot >= itemHandler.getSlots()) return;
      ItemStack mold = itemHandler.getStackInSlot(moldSlot);
      if (MoldMatcher.matches(recipe.molds(), List.of(mold))) {
         cir.setReturnValue(CraftingTaskContext.TaskAvailability.ready());
      }
   }
}