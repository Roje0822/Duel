package kr.chide1.duel.command.sub;

import kr.chide1.duel.duel.PlayerStatus;
import kr.chide1.duel.repository.PlayerStatusRepository;
import kr.chide1.rojecore.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class InformationCommand implements SubCommand {
    @Override
    public String getCommandName() {
        return "정보";
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
        if (!(sender instanceof Player player)) {
            sender.sendMessage("버킷에서는 사용 할 수 없습니다");
            return;
        }

        PlayerStatus status = PlayerStatusRepository.getInstance().getPlayerStatus(player);
        player.sendMessage("포인트 : " + status.getPoint());
        player.sendMessage("승 : " + status.getWinCount());
        player.sendMessage("패 : " + status.getLoseCount());
    }

    @Override
    public List<String> getCompletions(CommandSender commandSender, String[] strings) {
        return null;
    }
}
