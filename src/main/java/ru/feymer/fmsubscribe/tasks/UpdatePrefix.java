package ru.feymer.fmsubscribe.tasks;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.scheduler.BukkitRunnable;
import ru.feymer.fmsubscribe.FmSubscribe;
import ru.feymer.fmsubscribe.utils.Data;
import ru.feymer.fmsubscribe.utils.Utils;

public class UpdatePrefix extends BukkitRunnable {
   private static Chat chat = null;

   public void run() {
      this.setupChat();
      Bukkit.getOnlinePlayers().forEach((player) -> {
         String oldData = Data.getData(player);
         String playerPrefix = chat.getPlayerPrefix(player);
         if (!oldData.isEmpty()) {
            int symbol_length = Utils.getString("settings.symbol").length();
            playerPrefix = playerPrefix.substring(0, playerPrefix.length() - symbol_length);
            final String groupPrefix = chat.getGroupPrefix(player.getWorld(), chat.getPrimaryGroup(player)); // Объявляем как final
            if (groupPrefix.substring(0, groupPrefix.length() - 3).equalsIgnoreCase(playerPrefix)) {
               return;
            }

            final String groupPrefixx = chat.getGroupPrefix(player.getWorld(), chat.getPrimaryGroup(player)); // Объявляем как final
            String symbol = Utils.getString("settings.symbol");
            Bukkit.getScheduler().runTask(FmSubscribe.getInstance(), () -> {
               Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " meta setprefix \"" + Utils.getString("settings.prefix-1").replace("%prefix%", groupPrefixx).replace("%symbol%", symbol) + "\"");
            });
         } else {
            playerPrefix = playerPrefix.substring(0, playerPrefix.length() - 3);
            final String prefix = chat.getGroupPrefix(player.getWorld(), chat.getPrimaryGroup(player)); // Объявляем как final
            if (prefix.substring(0, prefix.length() - 3).equalsIgnoreCase(playerPrefix)) {
               return;
            }

            final String groupPrefix = chat.getGroupPrefix(player.getWorld(), chat.getPrimaryGroup(player)); // Объявляем как final
            Bukkit.getScheduler().runTask(FmSubscribe.getInstance(), () -> {
               Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " meta setprefix \"" + Utils.getString("settings.prefix-2").replace("%prefix%", groupPrefix) + "\"");
            });
         }
      });
   }

   private void setupChat() {
      RegisteredServiceProvider<Chat> rsp = FmSubscribe.getInstance().getServer().getServicesManager().getRegistration(Chat.class);
      chat = rsp.getProvider();
   }
}