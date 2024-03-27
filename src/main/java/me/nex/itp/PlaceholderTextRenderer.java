package me.nex.itp;

import me.nex.itp.controller.PlaceholderController;
import me.nex.itp.controller.PlaceholderControllerImpl;
import me.nex.itp.params.CharacterVisitorParams;
import me.nex.itp.params.PlaceholderParams;
import me.nex.itp.params.renderizable.ItemRenderizableParam;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.CharacterVisitor;
import net.minecraft.text.OrderedText;
import org.joml.Matrix4f;

import java.util.stream.Collectors;

public final class PlaceholderTextRenderer {

    public static final PlaceholderController CONTROLLER = new PlaceholderControllerImpl();

    public static boolean accept(OrderedText orderedText, CharacterVisitor defaultVisitor, float y, int color, int light, Matrix4f matrix, VertexConsumerProvider vertexConsumerProvider) {
        final MinecraftClient minecraftClient = MinecraftClient.getInstance();

        PlaceholderTextRenderer.CONTROLLER.reset();

        orderedText.accept((index, style, codePoint) -> {
            final char character = (char) codePoint;

            if (character == '$') {
                /* Toggle inside placeholder definition */
                PlaceholderTextRenderer.CONTROLLER.togglePlaceholderDefinition();
                PlaceholderTextRenderer.CONTROLLER.addParam(CharacterVisitorParams.of(index, codePoint, style));

                if (!PlaceholderTextRenderer.CONTROLLER.isPlaceholderDefinition()) {
                    final String contentInformation = PlaceholderTextRenderer.CONTROLLER.getParams().stream()
                            .map(CharacterVisitorParams::codePoint)
                            .map(i -> (char) i.intValue())
                            .map(String::valueOf)
                            .collect(Collectors.joining(""));

                    if (!PlaceholderUtilities.isValidPlaceholderDefinition(contentInformation)) {
                        PlaceholderTextRenderer.CONTROLLER.emptyPlaceholderDefinition(minecraftClient, defaultVisitor);
                        return true;
                    }

                    final MatrixStack matrixStack = PlaceholderTextRenderer.CONTROLLER.getMatrixStack();

                    matrixStack.push();
                    matrixStack.multiplyPositionMatrix(matrix);
                    matrixStack.translate(PlaceholderTextRenderer.CONTROLLER.getOffset(), y, 0.0f);

                    final PlaceholderParams placeholderParams = PlaceholderUtilities.getPlaceholderParams(contentInformation);


                    final boolean success = ItemRenderizableParam.renderItemPlaceholder(minecraftClient, placeholderParams, vertexConsumerProvider);
                    if (!success) {
                        PlaceholderTextRenderer.CONTROLLER.emptyPlaceholderDefinition(minecraftClient, defaultVisitor);
                    }

                    matrixStack.pop();
                }
                return true;
            }

            if (!PlaceholderTextRenderer.CONTROLLER.isPlaceholderDefinition()) {
                PlaceholderTextRenderer.CONTROLLER.addOffset(PlaceholderUtilities.getAdvance(minecraftClient, style.getFont(), codePoint));
                return defaultVisitor.accept(index, style, codePoint);
            }

            PlaceholderTextRenderer.CONTROLLER.addParam(CharacterVisitorParams.of(index, codePoint, style));
            if (Character.isWhitespace(codePoint)) {
                PlaceholderTextRenderer.CONTROLLER.emptyPlaceholderDefinition(minecraftClient, defaultVisitor);
            }
            return true;
        });

        if (PlaceholderTextRenderer.CONTROLLER.isPlaceholderDefinition()
                && !PlaceholderTextRenderer.CONTROLLER.getParams().isEmpty()) {
            PlaceholderTextRenderer.CONTROLLER.emptyPlaceholderDefinition(minecraftClient, defaultVisitor);
        }
        return true;
    }
}
