package com.notnotme.petit2d.asset.loader

import android.content.res.AssetManager
import android.graphics.BitmapFactory
import com.notnotme.petit2d.asset.Texture
import com.notnotme.petit2d.asset.base.AssetLoader
import com.notnotme.petit2d.glkt.createTexture
import com.notnotme.petit2d.glkt.deleteTexture


class TextureLoader : AssetLoader<Texture> {
    private val textures    : HashMap<String, Texture>  = hashMapOf()

    override fun load(assetManager: AssetManager, path: String): Texture {
        return textures[path] ?: run {
            assetManager.open(path).use { stream ->
                BitmapFactory.decodeStream(stream).let { bitmap ->
                    val texture = createTexture(bitmap)
                    bitmap.recycle()
                    Texture(texture).also {
                        textures[path] = it
                    }
                }
            }
        }
    }

    override fun get(path: String): Texture {
        return textures[path] ?: throw IndexOutOfBoundsException("$path not found in textures")
    }

    override fun unload(path: String) {
        textures.remove(path)?.apply {
            deleteTexture(texture)
            texture = 0
        }
    }
}
