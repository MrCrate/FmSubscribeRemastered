/* Decompiler 11ms, total 279ms, lines 77 */
package ru.feymer.fmsubscribe.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FmSubscribeTabCompleter implements TabCompleter {
   @Nullable
   public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
      if (args.length == 1) {
         return Arrays.asList("give", "take", "info", "reload");
      } else {
         Collection onlinePlayers;
         ArrayList onlinePlayerName;
         Iterator var7;
         Player onlinePlayer;
         String name;
         if (args[0].equalsIgnoreCase("give")) {
            if (args.length == 2) {
               onlinePlayers = Bukkit.getOnlinePlayers();
               onlinePlayerName = new ArrayList();
               var7 = onlinePlayers.iterator();

               while(var7.hasNext()) {
                  onlinePlayer = (Player)var7.next();
                  name = onlinePlayer.getName();
                  onlinePlayerName.add(name);
               }

               return onlinePlayerName;
            }

            if (args.length == 3) {
               return Arrays.asList("10", "20", "30", "40", "50", "forever");
            }
         } else if (args[0].equalsIgnoreCase("take")) {
            if (args.length == 2) {
               onlinePlayers = Bukkit.getOnlinePlayers();
               onlinePlayerName = new ArrayList();
               var7 = onlinePlayers.iterator();

               while(var7.hasNext()) {
                  onlinePlayer = (Player)var7.next();
                  name = onlinePlayer.getName();
                  onlinePlayerName.add(name);
               }

               return onlinePlayerName;
            }
         } else if (args[0].equalsIgnoreCase("info") && args.length == 2) {
            onlinePlayers = Bukkit.getOnlinePlayers();
            onlinePlayerName = new ArrayList();
            var7 = onlinePlayers.iterator();

            while(var7.hasNext()) {
               onlinePlayer = (Player)var7.next();
               name = onlinePlayer.getName();
               onlinePlayerName.add(name);
            }

            return onlinePlayerName;
         }

         return new ArrayList();
      }
   }
}
