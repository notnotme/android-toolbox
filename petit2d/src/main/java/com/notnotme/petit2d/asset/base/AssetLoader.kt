package com.notnotme.petit2d.asset.base

import android.content.res.AssetManager


interface AssetLoader<T : Asset> {
    fun load(assetManager: AssetManager, path: String): T
    fun get(path: String): T
    fun unload(path: String)
}
