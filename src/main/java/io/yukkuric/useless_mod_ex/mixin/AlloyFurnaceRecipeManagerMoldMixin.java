package io.yukkuric.useless_mod_ex.mixin;

import com.sorrowmist.useless.content.recipe.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin({AlloyFurnaceRecipeManager.class})
public abstract class AlloyFurnaceRecipeManagerMoldMixin {
    @Inject(
            method = {"matchesMold"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private void uselessStretcher$matchesMold(AdvancedAlloyFurnaceRecipe recipe, ItemStack mold, CallbackInfoReturnable<Boolean> cir) {
        if (recipe == null || recipe.molds().size() <= 1) return;
        cir.setReturnValue(mold != null && MoldMatcher.matches(recipe.molds(), List.of(mold)));
    }

    @Redirect(
            method = {"selectBestRecipe(Ljava/lang/Iterable;Lcom/sorrowmist/useless/content/recipe/AlloyFurnaceRecipeManager$RecipeLookupContext;Lcom/sorrowmist/useless/content/recipe/AlloyFurnaceRecipeManager$LookupSnapshot;)Lcom/sorrowmist/useless/content/recipe/AdvancedAlloyFurnaceRecipe;"},
            at = @At(value = "INVOKE", target = "Ljava/util/List;size()I")
    )
    private int uselessStretcher$singleMoldSlot(List<Ingredient> molds) {
        // 单方块机器只有一个模具槽，这里判定的只是物理槽位数。万象模具可以用这一个槽覆盖全部模具需求，
        // 因此多模具配方不能在这一步就被丢掉，是否真的匹配交给 matchesMold 判定。
        return Math.min(molds.size(), 1);
    }
}