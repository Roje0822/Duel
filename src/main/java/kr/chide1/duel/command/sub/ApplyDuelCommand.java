package kr.chide1.duel.command.sub;

import kr.chide1.duel.PeroDuel;
import kr.chide1.duel.manager.PlayerDuelManager;
import kr.chide1.rojecore.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class ApplyDuelCommand implements SubCommand {
    @Override
    public String getCommandName() {
        return "신청";
    }

    @Override
    public String getUsage() {
        return "<닉네임>";
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
        if (args.length != 2) {
            player.sendMessage("\uE509 §c잘못된 명령어 사용입니다");
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        PlayerDuelManager duelManager = PlayerDuelManager.getInstance();

        if (duelManager.hasPlayerDuel(player)) {
            player.sendMessage("\uE509 §c지금은 결투를 신청할 수 없는 상태입니다");
            return;
        }
        if (target == null) {
            player.sendMessage("\uE509 §c존재 하지 않는 플레이어 입니다");
            return;
        }
        if (player == target) {
            player.sendMessage("\uE509 §c자신에게는 결투 신청을 할 수 없습니다");
            return;
        }
        if (duelManager.hasPlayerDuel(target)) {
            player.sendMessage("\uE509 §c" + target.getName() + "님은 결투 신청을 받을 수 없는 상태입니다");
            return;
        }
        if (duelManager.getDuelAmount() >= 6) {
            player.sendMessage("\uE509 §c진행 되고 있는 듀얼이 3팀 이상입니다");
            return;
        }
        // TODO: 2024-08-25 같은 상대는 30분 후에 되게 막기

        duelManager.applyDuel(player, target);
        player.sendMessage("\uE509 §e" + target.getName() + "§f님에게 결투를 신청하였습니다");
        target.sendMessage("\uE509 §e" + player.getName() + "§f님에게 결투를 신청받았습니다");
        target.sendMessage("\uE509 \"/결투 수락\" 으로 수락하기 \"/결투 거절\" 로 거절하기");

        // TODO: 2024-08-25 전적 채팅에 띄우기
        PeroDuel plugin = PeroDuel.getInstance();
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (duelManager.hasApplyPlayer(target)) {
                duelManager.refuseDuel(target);
                player.sendMessage("\uE509 요청이 만료되었습니다");
                target.sendMessage("\uE509 요청이 만료되었습니다");
            }
        }, 20 * 20);
    }

    @Override
    public List<String> getCompletions(CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 2) {
            PeroDuel.getInstance().getServer().getOnlinePlayers().forEach(player -> {
                completions.add(player.getName());
            });
        }
        return StringUtil.copyPartialMatches(args[args.length - 1], completions, new ArrayList<>());
    }
}
