package me.caps123987.monitorapi.tests;

import me.caps123987.monitorapi.displays.InteractiveDisplay;
import me.caps123987.monitorapi.displays.RenderMode;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class comm implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        commandSender.sendMessage("dtest...");
        InteractiveDisplay display = new InteractiveDisplay(RenderMode.ALL_PLAYERS_ALL_DISPLAYS);

        display.enableHeader();
        display.setHeader(Component.text("This is a Header              :D"));
        display.setMainTextLines(Arrays.asList("&bHello World!"
                                              ,"this is a test message"));
        display.setAlignment(TextDisplay.TextAlignment.LEFT);

        display.create(((Player)commandSender).getLocation());

        return true;
    }
}
