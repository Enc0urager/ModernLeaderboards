package dev.enco.greataligner.config

import com.google.common.collect.ImmutableMap
import dev.enco.greataligner.Main
import dev.enco.greataligner.repository.CharRepository
import dev.enco.greataligner.utils.Colorizer
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration

class Config(
    private val plugin : Main,
    private val charRepository: CharRepository
) {
    private lateinit var config: FileConfiguration
    lateinit var lbFormats : ImmutableMap<String, String>
    var customPlaceChars = Int2ObjectOpenHashMap<String>()
    lateinit var spaceSymbol : String
    var spaceSymbolWidth : Int = 2
    var startMaxWidth : Int = 150
    var minSpaceWidth : Int = 10
    var spaceExtensionStep : Int = 5

    fun getPlaceChar(place : Int) : String = customPlaceChars.getOrDefault(place, place.toString())
    fun getLbFormat(lb : String) : String? = lbFormats[lb]

    init {
        plugin.saveDefaultConfig()
        this.load()
    }

    fun load() {
        config = plugin.config
        lbFormats = loadLbFormats(config.getConfigurationSection("formats")!!)
        spaceSymbolWidth = config.getInt("space-symbol-width")
        spaceSymbol = config.getString("space-symbol")!!
        startMaxWidth = config.getInt("start-max-width")
        minSpaceWidth = config.getInt("min-space-width")
        spaceExtensionStep = config.getInt("space-extension-step")
        loadCustomPlaceChars(config.getConfigurationSection("custom-place-chars")!!)
        loadCustomCharWidth(config.getConfigurationSection("custom-chars-width")!!)
    }

    private fun loadLbFormats(section: ConfigurationSection): ImmutableMap<String, String> {
        val builder = ImmutableMap.builder<String, String>()
        for (key in section.getKeys(false)) {
            val value = Colorizer.colorize(section.getString(key)!!)
            builder.put(key, value)
        }
        return builder.build();
    }

    private fun loadCustomPlaceChars(section: ConfigurationSection) {
        for (key in section.getKeys(false)) {
            val str = Colorizer.colorize(section.getString(key)!!)
            val key = key.toInt()
            customPlaceChars.put(key, str)
        }
    }

    private fun loadCustomCharWidth(section: ConfigurationSection) {
        for (key in section.getKeys(false)) {
            val width = section.getInt(key)
            charRepository.setCharsWidth(key, width)
        }
    }
}