package io.yukkuric.useless_mod_ex.mixin;

import com.sorrowmist.useless.content.machines.advanced_alloy_furnace.layout.AdvancedAlloyFurnaceLayout;
import com.sorrowmist.useless.content.machines.advanced_alloy_furnace.recipe.AlloyFurnaceRecipeCalculator;
import com.sorrowmist.useless.content.recipe.AdvancedAlloyFurnaceRecipe;
import com.sorrowmist.useless.content.recipe.MoldMatcher;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin({AlloyFurnaceRecipeCalculator.class})
public abstract class AlloyFurnaceRecipeCalculatorMoldMixin {
    @Shadow
    @Final
    private ItemStackHandler itemHandler;

    @Inject(
            method = {"canConsumeRecipeInputs(Lcom/sorrowmist/useless/content/recipe/AdvancedAlloyFurnaceRecipe;II)Z"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private void uselessStretcher$multiMold(AdvancedAlloyFurnaceRecipe recipe, int parallel, int machineTier, CallbackInfoReturnable<Boolean> cir) {
        if (recipe == null || recipe.molds().size() <= 1) return;
        ItemStack mold = this.itemHandler.getStackInSlot(AdvancedAlloyFurnaceLayout.MOLD_SLOT);
        if (!MoldMatcher.matches(recipe.molds(), List.of(mold))) {
            cir.setReturnValue(false);
        }
    }

    @Redirect(
            method = {"canConsumeRecipeInputs(Lcom/sorrowmist/useless/content/recipe/AdvancedAlloyFurnaceRecipe;II)Z"},
            at = @At(value = "INVOKE", target = "Ljava/util/List;size()I")
    )
    private int uselessStretcher$singleMoldSlot(List<Ingredient> molds) {
        // 同 AlloyFurnaceRecipeManagerMoldMixin：槽位只有一个，模具是否齐全由上面的注入判定。
        return Math.min(molds.size(), 1);
    }
}