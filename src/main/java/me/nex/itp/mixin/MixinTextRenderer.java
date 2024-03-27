package me.nex.itp.mixin;

import me.nex.itp.PlaceholderTextRenderer;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.text.CharacterVisitor;
import net.minecraft.text.OrderedText;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TextRenderer.class)
public class MixinTextRenderer {

    @Shadow
    @Final
    public int fontHeight;

    @Redirect(
            method = "drawLayer(Lnet/minecraft/text/OrderedText;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/font/TextRenderer$TextLayerType;II)F",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/text/OrderedText;accept(Lnet/minecraft/text/CharacterVisitor;)Z"
            ))
    private boolean itp$drawLayer(OrderedText instance, CharacterVisitor characterVisitor, OrderedText text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumerProvider, TextRenderer.TextLayerType layerType, int underlineColor, int light) {
        return PlaceholderTextRenderer.accept(instance, characterVisitor, y, color, light, matrix, vertexConsumerProvider);
    }

    @Redirect(
            method = "drawWithOutline",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/text/OrderedText;accept(Lnet/minecraft/text/CharacterVisitor;)Z"
            ))
    private boolean itp$drawWithOutline(OrderedText instance, CharacterVisitor characterVisitor, OrderedText text, float x, float y, int color, int outlineColor, Matrix4f matrix, VertexConsumerProvider vertexConsumers, int light) {
        return PlaceholderTextRenderer.accept(instance, characterVisitor, y, color, light, matrix, vertexConsumers);
    }
}
