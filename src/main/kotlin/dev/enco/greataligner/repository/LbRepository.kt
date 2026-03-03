package dev.enco.greataligner.repository

import dev.enco.greataligner.config.Config
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap

class LbRepository(config: Config) {
    val lbPixels = Object2IntOpenHashMap<String>().apply { defaultReturnValue(config.startMaxWidth) }
    fun get(lb : String) : Int = lbPixels.getInt(lb)
    fun put(lb : String, value : Int) = lbPixels.put(lb, value)
}