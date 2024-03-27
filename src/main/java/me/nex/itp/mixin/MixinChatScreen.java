package me.nex.itp.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatScreen.class)
public class MixinChatScreen {

    @Inject(method = "sendMessage", at = @At("HEAD"), cancellable = true)
    private void injectOnSendMessage(String chatText, boolean addToHistory, CallbackInfoReturnable<Boolean> cir) {
        final MinecraftClient client = MinecraftClient.getInstance();
        final ClientPlayNetworkHandler networkHandler = client.getNetworkHandler();

        if (networkHandler == null || chatText.isEmpty()) {
            cir.setReturnValue(false);
            return;
        }

        if (addToHistory) {
            client.inGameHud.getChatHud().addToMessageHistory(chatText);
        }

        cir.setReturnValue(true);
        if (chatText.startsWith("/")) {
            final String command = StringUtils.normalizeSpace(chatText.substring(1));
            networkHandler.sendChatCommand(command);
            return;
        }
        networkHandler.sendChatMessage(chatText);
    }
}
