package dev.pieman.multiplayerbutton.mixin;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.SafetyScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = PauseScreen.class, priority = 9999)
public abstract class AddButtonMixin extends Screen {
	protected AddButtonMixin(Component title) {
		super(title);
	}
	
	@ModifyVariable(
		method = "createPauseMenu",
		at = @At(value = "INVOKE",
			target = "Lnet/minecraft/client/gui/layouts/GridLayout$RowHelper;addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;)Lnet/minecraft/client/gui/layouts/LayoutElement;",
			ordinal = 4,
			shift = At.Shift.AFTER
		))
	private GridLayout.RowHelper addMultiplayerButtonMultiplayer4(GridLayout.RowHelper adder) {
		addMultiplayerButton(adder);
		return adder;
	}
	
	@ModifyVariable(
		method = "createPauseMenu",
		at = @At(value = "INVOKE",
			target = "Lnet/minecraft/client/gui/layouts/GridLayout$RowHelper;addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;)Lnet/minecraft/client/gui/layouts/LayoutElement;",
			ordinal = 3,
			shift = At.Shift.AFTER
		))
	private GridLayout.RowHelper addMultiplayerButtonMultiplayer3(GridLayout.RowHelper adder) {
		addMultiplayerButton(adder);
		return adder;
	}

	@Unique
	private void addMultiplayerButton(GridLayout.RowHelper adder) {
		Button multiplayerButton = this.addRenderableWidget(Button.builder(Component.translatable("menu.multiplayer"), (button) -> {
			assert this.minecraft != null;
			if (this.minecraft.options.skipMultiplayerWarning) {
				minecraft.setScreen(new JoinMultiplayerScreen(this));
			} else {
				minecraft.setScreen(new SafetyScreen(this));
			}
		}).width(204).build());

		adder.addChild(multiplayerButton, 2);
	}
}
