package com.logicalgeekboy.logical_zoom.mixin

import com.logicalgeekboy.logical_zoom.LogicalZoom
import com.logicalgeekboy.logical_zoom.LogicalZoom.Companion.isZooming
import com.logicalgeekboy.logical_zoom.LogicalZoom.Companion.manageSmoothCamera
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.GameRenderer
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer::class)
class LogicalZoomMixin {
    @Inject(method = ["getFov(Lnet/minecraft/client/render/Camera;FZ)F"], at = [At("RETURN")], cancellable = true)
    fun getZoomLevel(callbackInfo: CallbackInfoReturnable<Float?>) {
        if (isZooming) {
            val fov: Float = callbackInfo.getReturnValue()!!
            callbackInfo.setReturnValue(fov * LogicalZoom.zoomLevel)
        }

        manageSmoothCamera()
    }
}
