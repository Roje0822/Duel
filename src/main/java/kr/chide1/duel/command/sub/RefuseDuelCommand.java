package kr.chide1.duel.command.sub;

import kr.chide1.duel.manager.PlayerDuelManager;
import kr.chide1.rojecore.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class RefuseDuelCommand implements SubCommand {
    @Override
    public String getCommandName() {
        return "거절";
    }

    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player target)) {
            sender.sendMessage("버킷에서는 사용 할 수 없습니다");
            return;
        }

        PlayerDuelManager duelManager = PlayerDuelManager.getInstance();

        if (!duelManager.hasApplyPlayer(target)) {
            target.sendMessage("\uE509 §c받은 결투 신청이 없습니다");
            return;
        }

        Player player = duelManager.getApplyPlayer(target);
        duelManager.refuseDuel(target);

        player.sendMessage("\uE509 상대방이 결투를 취소 했습니다");
        target.sendMessage("\uE509 결투를 취소 했습니다");
    }

    @Override
    public List<String> getCompletions(CommandSender commandSender, String[] strings) {
        return null;
    }
}
