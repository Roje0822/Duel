package kr.chide1.duel.command.sub;

import kr.chide1.duel.PeroDuel;
import kr.chide1.duel.manager.PlayerDuelManager;
import kr.chide1.rojecore.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class AcceptDuelCommand implements SubCommand {
    @Override
    public String getCommandName() {
        return "수락";
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
        duelManager.acceptDuel(player, target);

        target.sendMessage("\uE509 결투를 승낙하였습니다");
        target.sendMessage("\uE509 잠시 후 결투가 시작됩니다");
        player.sendMessage("\uE509 " + target.getName() + "님이 결투를 승낙하였습니다");
        player.sendMessage("\uE509 잠시 후 결투가 시작됩니다");

        PeroDuel plugin = PeroDuel.getInstance();

        new BukkitRunnable() {
            int timer = 5;

            @Override
            public void run() {
                if (timer == 0) {
                    duelManager.startDuel(player, target);
                    player.sendTitle("결투가 시작되었습니다", "", 10, 30, 10);
                    target.sendTitle("결투가 시작되었습니다", "", 10, 30, 10);
                    this.cancel();
                    return;
                }
                player.sendTitle(timer + "초 후 시작합니다", "", 10, 30, 10);
                target.sendTitle(timer + "초 후 시작합니다", "", 10, 30, 10);
                timer--;
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    @Override
    public List<String> getCompletions(CommandSender sender, String[] args) {
        return null;
    }
}
