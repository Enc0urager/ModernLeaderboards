package dev.enco.greataligner

import dev.enco.greataligner.command.ReloadCommand
import dev.enco.greataligner.config.Config
import dev.enco.greataligner.math.Calculator
import dev.enco.greataligner.papi.PapiExpansion
import dev.enco.greataligner.repository.CharRepository
import dev.enco.greataligner.repository.LbRepository
import org.bstats.bukkit.Metrics
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {
    override fun onEnable() {
        val charRepository = CharRepository()
        val config = Config(this, charRepository)
        val lbRepository = LbRepository(config)
        val calculator = Calculator(charRepository)
        PapiExpansion(calculator, config, lbRepository).also { it.register() }
        getCommand("greataligner")?.setExecutor(ReloadCommand(config))
        Metrics(this, 29887)
    }
}