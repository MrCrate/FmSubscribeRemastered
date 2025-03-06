package ru.feymer.fmsubscribe;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.feymer.fmsubscribe.commands.FmSubscribeCommand;
import ru.feymer.fmsubscribe.commands.FmSubscribeTabCompleter;
import ru.feymer.fmsubscribe.tasks.Clear;
import ru.feymer.fmsubscribe.tasks.UpdatePrefix;
import ru.feymer.fmsubscribe.utils.Data;
import ru.feymer.fmsubscribe.utils.Hex;
import ru.feymer.fmsubscribe.utils.Placeholders;

public final class FmSubscribe extends JavaPlugin {

    private static FmSubscribe instance;

    @Override
    public void onEnable() {
        instance = this;

        // Вывод сообщения о запуске плагина
        sendStartupMessage();

        // Регистрация команд и таб-комплитера
        registerCommands();

        // Загрузка конфигурации и данных
        saveDefaultConfig();
        Data.load();

        // Регистрация PlaceholderAPI
        new Placeholders().register();

        // Запуск асинхронных задач
        startTasks();
    }

    @Override
    public void onDisable() {
        // Вывод сообщения о выключении плагина
        sendShutdownMessage();
    }

    /**
     * Выводит сообщение о запуске плагина в консоль.
     */
    private void sendStartupMessage() {
        String pluginName = getDescription().getName();
        String version = getDescription().getVersion();

        Bukkit.getConsoleSender().sendMessage(Hex.color(""));
        Bukkit.getConsoleSender().sendMessage(Hex.color("&a» &fПлагин &a" + pluginName + " &fвключился&f!"));
        Bukkit.getConsoleSender().sendMessage(Hex.color("&a» &fВерсия: &av" + version));
        Bukkit.getConsoleSender().sendMessage(Hex.color(""));
    }

    /**
     * Выводит сообщение о выключении плагина в консоль.
     */
    private void sendShutdownMessage() {
        String pluginName = getDescription().getName();
        String version = getDescription().getVersion();

        Bukkit.getConsoleSender().sendMessage(Hex.color(""));
        Bukkit.getConsoleSender().sendMessage(Hex.color("&a» &fПлагин &a" + pluginName + " &fвыключился&f!"));
        Bukkit.getConsoleSender().sendMessage(Hex.color("&a» &fВерсия: &av" + version));
        Bukkit.getConsoleSender().sendMessage(Hex.color(""));
    }

    /**
     * Регистрирует команды и таб-комплитер.
     */
    private void registerCommands() {
        getCommand("fmsubscribe").setExecutor(new FmSubscribeCommand());
        getCommand("fmsubscribe").setTabCompleter(new FmSubscribeTabCompleter());
    }

    /**
     * Запускает асинхронные задачи.
     */
    private void startTasks() {
        Clear clearTask = new Clear();
        clearTask.runTaskTimerAsynchronously(this, 0L, 60L);

        UpdatePrefix updatePrefixTask = new UpdatePrefix();
        updatePrefixTask.runTaskTimerAsynchronously(this, 0L, 60L);
    }

    /**
     * Возвращает экземпляр плагина.
     *
     * @return Экземпляр плагина.
     */
    public static FmSubscribe getInstance() {
        return instance;
    }
}