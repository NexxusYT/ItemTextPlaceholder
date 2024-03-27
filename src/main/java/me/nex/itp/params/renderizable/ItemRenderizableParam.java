package me.nex.itp.params.renderizable;

import me.nex.itp.PlaceholderTextRenderer;
import me.nex.itp.params.PlaceholderParams;
import net.minecraft.block.StairsBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public final class ItemRenderizableParam {

    private static final Matrix4f SCALING_MATRIX = new Matrix4f().scaling(1.0F, -1.0F, 1.0F);

    public static boolean renderItemPlaceholder(
            MinecraftClient minecraftClient,
            @NotNull PlaceholderParams placeholderParams,
            @NotNull VertexConsumerProvider vertexConsumerProvider
    ) {
        final String type = placeholderParams.type();
        final String value = placeholderParams.value();

        if (!type.equalsIgnoreCase("i")) {
            return false;
        }

        if (value.contains(":")) {
            final String[] split = value.split(":");
            if (split.length != 2) {
                return false;
            }

            final float sizeMultiplier;
            try {
                sizeMultiplier = ItemRenderizableParam.clamp(
                        Float.parseFloat(split[1]),
                        -10.0f,
                        10.0f
                );
            } catch (NumberFormatException e) {
                return false;
            }
            return ItemRenderizableParam.tryRender(
                    minecraftClient, vertexConsumerProvider,
                    sizeMultiplier, split[0]
            );
        }
        return ItemRenderizableParam.tryRender(minecraftClient, vertexConsumerProvider, 1.0f, value);
    }

    private static boolean tryRender(
            MinecraftClient minecraftClient,
            VertexConsumerProvider vertexConsumerProvider,
            float sizeMultiplier,
            String itemIndexString
    ) {
        final int itemIndex;
        try {
            itemIndex = Integer.parseInt(itemIndexString);
        } catch (NumberFormatException e) {
            return false;
        }

        return ItemRenderizableParam.render(minecraftClient, vertexConsumerProvider,
                PlaceholderTextRenderer.CONTROLLER.getMatrixStack(), sizeMultiplier, itemIndex
        );
    }

    private static boolean render(
            MinecraftClient minecraftClient,
            VertexConsumerProvider vertexConsumerProvider,
            MatrixStack matrixStack,
            float sizeMultiplier,
            int itemIndex
    ) {
        final Item item = Registries.ITEM.get(itemIndex);
        if (item.equals(Items.AIR)) {
            return false;
        }

        final ItemStack itemStack = item.getDefaultStack();
        final BakedModel bakedModel = minecraftClient.getItemRenderer().getModel(itemStack, null, null, 0);


        matrixStack.multiplyPositionMatrix(ItemRenderizableParam.SCALING_MATRIX);
        matrixStack.scale(26.0f * sizeMultiplier, 26.0f * Math.abs(sizeMultiplier), 0.1f);

        if (bakedModel.hasDepth()) {
            final float degrees20 = (float) (20.0f * Math.PI / 180f);
            final float degrees45 = (float) (45.0f * Math.PI / 180f);

            matrixStack.peek().getPositionMatrix().rotateAffineXYZ(degrees20, degrees45, 0f);
        } else matrixStack.scale(1.5f, 1.5f, 1.5f);

        if (itemStack.getItem() instanceof BlockItem blockItem
                && blockItem.getBlock() instanceof StairsBlock
                || itemStack.getItem().equals(Items.LECTERN)) {
            matrixStack.peek().getPositionMatrix().rotateAffineXYZ(0f, (float) (Math.PI / 2.0f), 0f);
        }

        minecraftClient.getItemRenderer().renderItem(itemStack,
                ModelTransformationMode.NONE, false,
                matrixStack, vertexConsumerProvider,
                15728880, OverlayTexture.DEFAULT_UV, bakedModel
        );
        return true;
    }

    private static float clamp(float value, float min, float max) {
        return value < min ? min : Math.min(value, max);
    }
}
