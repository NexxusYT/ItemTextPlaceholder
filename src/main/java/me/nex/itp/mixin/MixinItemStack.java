package me.nex.itp.mixin;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class MixinItemStack {
    
    @Shadow
    public abstract Item getItem();

    @Inject(method = "getTooltip", at = @At(value = "RETURN"))
    private void injectOnGetTooltip(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir) {
        if (!context.isAdvanced()) {
            return;
        }

        final List<Text> tooltip = cir.getReturnValue();
        final Item item = this.getItem();

        final int itemId = Registries.ITEM.getRawId(item);
        tooltip.add(Text.literal(
                "#%s".formatted(itemId)
        ).formatted(Formatting.DARK_GRAY));
    }
}
