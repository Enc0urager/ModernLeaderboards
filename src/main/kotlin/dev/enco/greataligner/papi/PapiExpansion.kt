package dev.enco.greataligner.papi

import dev.enco.greataligner.config.Config
import dev.enco.greataligner.math.Calculator
import dev.enco.greataligner.repository.LbRepository
import dev.enco.greataligner.utils.Colorizer
import me.clip.placeholderapi.PlaceholderAPI
import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.ChatColor
import org.bukkit.OfflinePlayer
import java.text.MessageFormat

class PapiExpansion(
    private val calculator: Calculator,
    private val config: Config,
    private val lbRepository: LbRepository
) : PlaceholderExpansion() {
    override fun getIdentifier(): String = "greataligner"
    override fun getAuthor(): String = "Encourager"
    override fun getVersion(): String = "1.0"

    override fun onRequest(player: OfflinePlayer?, params: String): String? {
        val (placeStr, lb) = params.split("_", limit = 2).takeIf { it.size == 2 } ?: return null
        val place = placeStr.toIntOrNull() ?: return "Illegal lb pattern"

        val base = "%ajlb_lb_${lb}_${place}_alltime"
        fun ph(suffix: String) = PlaceholderAPI.setPlaceholders(player, "${base}_$suffix%")

        return formatEntry(player, place, lb, ph("prefix"), ph("name"), ph("value"))
    }

    private fun formatEntry(player: OfflinePlayer?, place: Int, lb: String, prefix: String, name: String, value: String): String {
        val format = config.getLbFormat(lb) ?: return "Format not found"
        val placeText = config.getPlaceChar(place)

        val totalW =
            calculator.getStringWidth(placeText, false) +
                    calculator.getStringWidth(ChatColor.stripColor(prefix).orEmpty(), true) +
                    calculator.getStringWidth(name, false) +
                    calculator.getStringWidth(value, false)

        val maxW = lbRepository.get(lb).let { current ->
            (current + config.spaceExtensionStep)
                .takeIf { totalW + config.minSpaceWidth > current }
                ?: current
        }.also { lbRepository.put(lb, it) }

        val pixelsNeeded = maxW - totalW
        val spaces = config.spaceSymbol.repeat((pixelsNeeded / config.spaceSymbolWidth).coerceAtLeast(0))

        val result = MessageFormat.format(format, placeText, name, prefix, value, spaces, pixelsNeeded)
        return PlaceholderAPI.setPlaceholders(player, Colorizer.colorize(result))
    }
}