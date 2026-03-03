package dev.enco.greataligner.command

import dev.enco.greataligner.config.Config
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class ReloadCommand(
    private val config : Config
) : CommandExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>?
    ): Boolean {
        config.load()
        sender.sendMessage("Reloaded")
        return true;
    }
}