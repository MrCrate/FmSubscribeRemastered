package ru.feymer.fmsubscribe.tasks;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.scheduler.BukkitRunnable;
import ru.feymer.fmsubscribe.FmSubscribe;
import ru.feymer.fmsubscribe.utils.Data;
import ru.feymer.fmsubscribe.utils.Utils;

import java.time.LocalDateTime;
import java.util.List;

public class Clear extends BukkitRunnable {
    private static Chat chat = null;

    @Override
    public void run() {
        setupChat();

        Bukkit.getOnlinePlayers().forEach(player -> {
            String oldData = Data.getData(player);
            if (!oldData.isEmpty() && !oldData.equalsIgnoreCase("forever")) {
                LocalDateTime expirationTime = LocalDateTime.parse(oldData + "T00:00:00");

                if (!expirationTime.isAfter(LocalDateTime.now())) {
                    final String prefix = chat.getPlayerPrefix(player).substring(0, chat.getPlayerPrefix(player).length() - Utils.getString("settings.symbol").length());

                    Bukkit.getScheduler().runTask(FmSubscribe.getInstance(), () -> {
                        List<String> commands = Utils.getStringList("settings.commands-on-take");
                        for (String command : commands) {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
                        }
                    });

                    Data.removeData(player);

                    Bukkit.getScheduler().runTask(FmSubscribe.getInstance(), () -> {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " meta setprefix \"" + Utils.getString("settings.prefix-2").replace("%prefix%", prefix) + "\"");
                    });
                }
            }
        });
    }

    private void setupChat() {
        if (chat == null) {
            RegisteredServiceProvider<Chat> rsp = FmSubscribe.getInstance().getServer().getServicesManager().getRegistration(Chat.class);
            if (rsp != null) {
                chat = rsp.getProvider();
            }
        }
    }
}