package com.logicalgeekboy.logical_zoom

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW

class LogicalZoom : ClientModInitializer {
    override fun onInitializeClient() {
        keyBinding =
            KeyBinding("key.logical_zoom.zoom", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_C, "category.logical_zoom.zoom")

        currentlyZoomed = false
        originalSmoothCameraEnabled = false

        KeyBindingHelper.registerKeyBinding(keyBinding)
    }

    companion object {
        private var currentlyZoomed = false
        private var keyBinding: KeyBinding? = null
        private var originalSmoothCameraEnabled = false
        private val mc: MinecraftClient = MinecraftClient.getInstance()

        @kotlin.jvm.JvmField
        val zoomLevel: Float = 0.23.toFloat()

        @kotlin.jvm.JvmStatic
        val isZooming: Boolean
            get() = keyBinding!!.isPressed()

        @kotlin.jvm.JvmStatic
        fun manageSmoothCamera() {
            if (zoomStarting()) {
                zoomStarted()
                enableSmoothCamera()
            }

            if (zoomStopping()) {
                zoomStopped()
                resetSmoothCamera()
            }
        }

        private val isSmoothCamera: Boolean
            get() = mc.options.smoothCameraEnabled

        private fun enableSmoothCamera() {
            mc.options.smoothCameraEnabled = true
        }

        private fun disableSmoothCamera() {
            mc.options.smoothCameraEnabled = false
        }

        private fun zoomStarting(): Boolean {
            return isZooming && !currentlyZoomed
        }

        private fun zoomStopping(): Boolean {
            return !isZooming && currentlyZoomed
        }

        private fun zoomStarted() {
            originalSmoothCameraEnabled = isSmoothCamera
            currentlyZoomed = true
        }

        private fun zoomStopped() {
            currentlyZoomed = false
        }

        private fun resetSmoothCamera() {
            if (originalSmoothCameraEnabled) {
                enableSmoothCamera()
            } else {
                disableSmoothCamera()
            }
        }
    }
}
