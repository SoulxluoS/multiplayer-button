package dev.pieman.multiplayerbutton.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.GenericMessageScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(JoinMultiplayerScreen.class)
public class SaveWorldOnServerJoin extends Screen {

    @Shadow
    @Final
    private Screen lastScreen;

    protected SaveWorldOnServerJoin(Component title) {
        super(title);
    }

    @Inject(at = @At(value = "HEAD"), method = "method_19912(Lnet/minecraft/client/gui/components/Button;)V", cancellable = true)
    private void modifyCancelButton(CallbackInfo ci) {
        assert minecraft != null;
        if (minecraft.isLocalServer()) {
            ci.cancel();
            minecraft.setScreen(lastScreen);
        } else {
            if (lastScreen instanceof PauseScreen) {
                ci.cancel();
                minecraft.disconnect(new GenericMessageScreen(Component.translatable("menu.savingLevel")), false);
                minecraft.setScreen(new TitleScreen());
            }
        }
    }

    @Inject(at = @At(value = "HEAD"), method = "join")
    private void addMultiplayerButtonSinglePlayer(ServerData entry, CallbackInfo ci) {
        boolean bl1 = Minecraft.getInstance().isLocalServer();
        assert minecraft != null;
        if (minecraft.level != null) {
            assert Minecraft.getInstance().level != null;
            Minecraft.getInstance().level.disconnect(ClientLevel.DEFAULT_QUIT_MESSAGE);
            if (bl1)
                Minecraft.getInstance().disconnectWithSavingScreen();
        }
    }
}
