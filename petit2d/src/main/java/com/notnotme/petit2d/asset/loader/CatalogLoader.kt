package com.notnotme.petit2d.asset.loader

import android.content.res.AssetManager
import com.google.gson.Gson
import com.notnotme.petit2d.asset.Catalog
import com.notnotme.petit2d.asset.base.AssetLoader
import com.notnotme.petit2d.model.TextureRegion


class CatalogLoader : AssetLoader<Catalog> {
    private val gson        : Gson                      = Gson()
    private val catalogs    : HashMap<String, Catalog>  = hashMapOf()

    override fun load(assetManager: AssetManager, path: String): Catalog {
        return catalogs[path] ?: run {
            assetManager.open(path).use { stream ->
                val json = stream.readBytes().decodeToString()
                val catalog = gson.fromJson(json, JsonCatalog::class.java)
                val regions = hashMapOf<String, TextureRegion>()

                for (entry in catalog.frames) {
                    val name = entry.key
                    val region = entry.value

                    val xScale = 1.0f / catalog.size.w.toFloat()
                    val yScale = 1.0f / catalog.size.h.toFloat()

                    val x = xScale * region.x.toFloat()
                    val y = yScale * region.y.toFloat()
                    val w = xScale * region.w.toFloat()
                    val h = yScale * region.h.toFloat()


                    regions[name] = TextureRegion.obtain().apply {
                        width = region.w
                        height = region.h
                        s = x
                        t = y
                        p = x + w
                        q = y + h
                    }
                }

                Catalog(regions).also {
                    catalogs[path] = it
                }
            }
        }
    }

    override fun get(path: String): Catalog {
        return catalogs[path] ?: throw IndexOutOfBoundsException("$path not found in catalogs")
    }

    override fun unload(path: String) {
        catalogs.remove(path)?.apply {
            regions.values.forEach(TextureRegion::recycle)
            regions.clear()
        }
    }

    companion object {
        private data class Region       (val x: Int, val y: Int, val w: Int, val h: Int)
        private data class TextureSize  (val w: Int, val h: Int)
        private data class JsonCatalog  (val frames: Map<String, Region>, val size: TextureSize)
    }
}
