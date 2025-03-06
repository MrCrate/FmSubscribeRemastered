package ru.feymer.fmsubscribe.commands;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;
import ru.feymer.fmsubscribe.FmSubscribe;
import ru.feymer.fmsubscribe.utils.Data;
import ru.feymer.fmsubscribe.utils.Utils;

import java.time.LocalDateTime;
import java.util.List;

public class FmSubscribeCommand implements CommandExecutor {
    private static Chat chat = null;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        setupChat();

        if (args.length == 0) {
            sendNoArgsMessage(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "give":
                return handleGive(sender, args);
            case "take":
                return handleTake(sender, args);
            case "info":
                return handleInfo(sender, args);
            case "reload":
                return handleReload(sender, args);
            default:
                sendNoArgsMessage(sender);
                return true;
        }
    }

    private boolean handleGive(CommandSender sender, String[] args) {
      if (args.length != 3) {
          sendNoArgsMessage(sender);
          return true;
      }
  
      if (!sender.hasPermission("fmsubscribe.give")) {
          Utils.sendMessage(sender, Utils.getString("messages.no-permission"));
          return true;
      }
  
      Player player = Bukkit.getPlayer(args[1]);
      if (player == null) {
          Utils.sendMessage(sender, Utils.getString("messages.null-player"));
          return true;
      }
  
      String oldData = Data.getData(player);
      if (!oldData.isEmpty()) {
          if (oldData.equalsIgnoreCase("forever")) {
              Utils.sendMessage(sender, Utils.getString("messages.already-has-forever-sub").replace("%player%", player.getName()));
          } else {
              Utils.sendMessage(sender, Utils.getString("messages.already-has-sub").replace("%player%", player.getName())
                      .replace("%expiration%", oldData));
          }
          return true;
      }
  
      String durationInput = args[2];
      String expirationDate;
  
      if (durationInput.equalsIgnoreCase("forever")) {
          expirationDate = "forever";
      } else {
          try {
              int days = Integer.parseInt(durationInput);
              expirationDate = LocalDateTime.now().plusDays(days).toString().substring(0, 10);
          } catch (NumberFormatException e) {
              Utils.sendMessage(sender, Utils.getString("messages.incorrect-date"));
              return true;
          }
      }
  
      Data.setData(player, expirationDate);
      Data.save();
      executeCommands("settings.commands-on-give", player);
  
      Utils.sendMessage(sender, Utils.getString(expirationDate.equals("forever") ? "messages.give-forever" : "messages.give-duration")
              .replace("%player%", player.getName())
              .replace("%duration%", durationInput));
      return true;
  }

    private boolean handleTake(CommandSender sender, String[] args) {
        if (args.length != 2) {
            sendNoArgsMessage(sender);
            return true;
        }

        if (!sender.hasPermission("fmsubscribe.take")) {
            Utils.sendMessage(sender, Utils.getString("messages.no-permission"));
            return true;
        }

        Player player = Bukkit.getPlayer(args[1]);
        if (player == null) {
            Utils.sendMessage(sender, Utils.getString("messages.null-player"));
            return true;
        }

        String oldData = Data.getData(player);
        if (oldData.isEmpty()) {
            Utils.sendMessage(sender, Utils.getString("messages.no-exists"));
            return true;
        }

        executeCommands("settings.commands-on-take", player);
        Data.removeData(player);

        String playerPrefix = chat.getPlayerPrefix(player);
        int symbolLength = Utils.getString("settings.symbol").length();
        String newPrefix = playerPrefix.substring(0, playerPrefix.length() - symbolLength);

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " meta setprefix \"" + Utils.getString("settings.prefix-2").replace("%prefix%", newPrefix));

        Utils.sendMessage(sender, Utils.getString("messages.take").replace("%player%", player.getName()));
        return true;
    }

    private boolean handleInfo(CommandSender sender, String[] args) {
        if (args.length != 2) {
            sendNoArgsMessage(sender);
            return true;
        }

        if (!sender.hasPermission("fmsubscribe.info")) {
            Utils.sendMessage(sender, Utils.getString("messages.no-permission"));
            return true;
        }

        Player player = Bukkit.getPlayer(args[1]);
        if (player == null) {
            Utils.sendMessage(sender, Utils.getString("messages.null-player"));
            return true;
        }

        String subscriptionData = Data.getData(player);
        if (subscriptionData.isEmpty()) {
            Utils.sendMessage(sender, Utils.getString("messages.no-sub"));
        } else {
            Utils.sendMessage(sender, Utils.getString("messages.yes-sub"));
        }

        return true;
    }

    private boolean handleReload(CommandSender sender, String[] args) {
        if (args.length != 1) {
            sendNoArgsMessage(sender);
            return true;
        }

        if (!sender.hasPermission("fmsubscribe.reload")) {
            Utils.sendMessage(sender, Utils.getString("messages.no-permission"));
            return true;
        }

        FmSubscribe.getInstance().reloadConfig();
        Data.reload();
        Utils.sendMessage(sender, Utils.getString("messages.reload"));
        return true;
    }

    private void executeCommands(String path, Player player) {
        List<String> commands = Utils.getStringList(path);
        for (String cmd : commands) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", player.getName()));
        }
    }

    private void sendNoArgsMessage(CommandSender sender) {
        for (String message : Utils.getStringList("messages.no-args")) {
            Utils.sendMessage(sender, message);
        }
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