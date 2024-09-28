package kr.chide1.duel.command;

import kr.chide1.duel.command.sub.*;
import kr.chide1.rojecore.command.BasedCommand;
import kr.chide1.rojecore.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DuelCommand extends BasedCommand {
    public DuelCommand(String commandName, JavaPlugin plugin) {
        super(commandName, plugin);
        registerSubCommand("신청", new ApplyDuelCommand());
        registerSubCommand("수락", new AcceptDuelCommand());
        registerSubCommand("거절", new RefuseDuelCommand());
        registerSubCommand("포인트", new PointCommand());
        registerSubCommand("초기화", new ResetCommand());
        registerSubCommand("정보", new InformationCommand());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            for (SubCommand subCommand : getAvailableSubCommands(sender)) {
                sender.sendMessage("\uE509 /결투 " + subCommand.getCommandName() + " " + subCommand.getUsage());
            }
            return true;
        }

        if (getSubCommandMap().containsKey(args[0])) {
            execute(args[0], sender, args);
            return true;
        }

        sender.sendMessage("\uE509 §c 존재하지 않는 명령어 입니다.");
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return getAvailableSubCommands(sender).stream()
                    .map(SubCommand::getCommandName)
                    .collect(Collectors.toList());
        } else if (getSubCommandMap().containsKey(args[0])) {
            return getCompletions(args[0], sender, args);
        }
        return Collections.emptyList();
    }

}
