package com.koirdsuzu.bungeeshortcut;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class BungeeShortcut extends Plugin {
    private Map<String, String> shortcuts = new HashMap<>();

    @Override
    public void onEnable() {
        loadConfig();
        registerShortcuts();
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new BungeeShortcutCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void loadConfig() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            try {
                try (java.io.InputStream in = getResourceAsStream("config.yml");
                     java.io.FileOutputStream out = new java.io.FileOutputStream(configFile)) {
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = in.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                    }
                }
            } catch (IOException e) {
                getLogger().warning("config.ymlの作成に失敗しました: " + e.getMessage());
            }
        }
        try {
            Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
            shortcuts.clear();
            Configuration section = config.getSection("shortcuts");
            for (String key : section.getKeys()) {
                shortcuts.put(key, section.getString(key));
            }
        } catch (IOException e) {
            getLogger().warning("config.ymlの読み込みに失敗しました: " + e.getMessage());
        }
    }

    private void registerShortcuts() {
        // 既存のショートカットコマンドを解除（BungeeCordは動的解除が難しいので注意）
        for (String shortcut : shortcuts.keySet()) {
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new ShortcutCommand(shortcut, shortcuts.get(shortcut)));
        }
    }

    private class ShortcutCommand extends Command {
        private final String realCommand;
        private final String shortcutName;
        public ShortcutCommand(String name, String realCommand) {
            super(name);
            this.realCommand = realCommand;
            this.shortcutName = name;
        }
        @Override
        public void execute(CommandSender sender, String[] args) {
            if (!hasPermission(sender)) {
                sender.sendMessage(new TextComponent("§c権限がありません: bungeeshortcut.shortcut." + shortcutName));
                return;
            }
            StringBuilder cmd = new StringBuilder(realCommand);
            for (String arg : args) {
                cmd.append(" ").append(arg);
            }
            ProxyServer.getInstance().getPluginManager().dispatchCommand(sender, cmd.toString());
        }
        @Override
        public boolean hasPermission(CommandSender sender) {
            return sender.hasPermission("bungeeshortcut.shortcut." + shortcutName);
        }
        @Override
        public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
            // 引数補完はしない
            return Collections.emptyList();
        }
    }

    private class BungeeShortcutCommand extends Command {
        public BungeeShortcutCommand() {
            super("bungeeshortcut", null, "bsc");
        }
        @Override
        public void execute(CommandSender sender, String[] args) {
            if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
                sender.sendMessage(new TextComponent("§a--- BungeeShortcut Help ---"));
                sender.sendMessage(new TextComponent("§e/shortcut名 [引数] ... : ショートカットコマンドを実行"));
                sender.sendMessage(new TextComponent("§e/bungeeshortcut reload : 設定をリロード"));
                sender.sendMessage(new TextComponent("§e/bungeeshortcut help : このヘルプを表示"));
                sender.sendMessage(new TextComponent("§a登録ショートカット一覧:"));
                for (String key : shortcuts.keySet()) {
                    sender.sendMessage(new TextComponent("§b/" + key + " §7→ §f" + shortcuts.get(key)));
                }
            } else if (args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("bungeeshortcut.commands.reload")) {
                    sender.sendMessage(new TextComponent("§c権限がありません: bungeeshortcut.commands.reload"));
                    return;
                }
                loadConfig();
                registerShortcuts();
                sender.sendMessage(new TextComponent("§aBungeeShortcut: 設定をリロードしました。"));
            } else {
                sender.sendMessage(new TextComponent("§c使い方: /bungeeshortcut <help|reload>"));
            }
        }
        @Override
        public boolean hasPermission(CommandSender sender) {
            // helpは全員OK
            return true;
        }
        @Override
        public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
            if (args.length == 1) {
                if (sender.hasPermission("bungeeshortcut.commands.reload")) {
                    return java.util.Arrays.asList("help", "reload");
                } else {
                    return java.util.Arrays.asList("help");
                }
            }
            return Collections.emptyList();
        }
    }
}
