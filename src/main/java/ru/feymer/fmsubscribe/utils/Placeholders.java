/* Decompiler 13ms, total 349ms, lines 52 */
package ru.feymer.fmsubscribe.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import ru.feymer.fmsubscribe.FmSubscribe;

public class Placeholders extends PlaceholderExpansion {
   @NotNull
   public String getIdentifier() {
      return "fmsubscribe";
   }

   @NotNull
   public String getAuthor() {
      return "feymerwtf";
   }

   @NotNull
   public String getVersion() {
      return ((FmSubscribe)JavaPlugin.getPlugin(FmSubscribe.class)).getDescription().getVersion();
   }

   public String onPlaceholderRequest(Player player, @NotNull String identifier) {
      String oldData;
      if (identifier.equalsIgnoreCase("has")) {
         oldData = Data.getData(player);
         return !oldData.isEmpty() ? Utils.getString("placeholders.has.yes-sub") : Utils.getString("placeholders.has.no-sub");
      } else if (identifier.equalsIgnoreCase("end")) {
         oldData = Data.getData(player);
         if (!oldData.isEmpty()) {
            if (oldData.equalsIgnoreCase("forever")) {
               return Utils.getString("placeholders.end.forever");
            } else {
               LocalDate futureData = LocalDate.parse(oldData, DateTimeFormatter.ISO_DATE);
               LocalDate currentDate = LocalDate.now();
               long daysUntil = ChronoUnit.DAYS.between(currentDate, futureData);
               return Utils.getString("placeholders.end.remainder").replace("%days%", String.valueOf(daysUntil));
            }
         } else {
            return Utils.getString("placeholders.end.no-sub");
         }
      } else {
         return identifier;
      }
   }
}
