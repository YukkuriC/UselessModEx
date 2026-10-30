package io.yukkuric.useless_mod_ex.mixin;

import com.sorrowmist.useless.content.blocks.multiblock.OmniversalAlloyFurnaceStructure;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({OmniversalAlloyFurnaceStructure.class})
public abstract class OmniversalAlloyFurnaceStructureAirMixin {
    @Inject(
            method = {"matches(Lcom/sorrowmist/useless/content/blocks/multiblock/OmniversalAlloyFurnaceStructure$Part;Lnet/minecraft/world/level/block/state/BlockState;)Z"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private static void uselessStretcher$anyBlockAtAirSlots(OmniversalAlloyFurnaceStructure.Part part, BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (part == OmniversalAlloyFurnaceStructure.Part.AIR) {
            cir.setReturnValue(true);
        }
    }
}
