package kr.chide1.duel.command.sub;

import kr.chide1.duel.PeroDuel;
import kr.chide1.duel.duel.PlayerStatus;
import kr.chide1.duel.repository.PlayerStatusRepository;
import kr.chide1.rojecore.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class PointCommand implements SubCommand {
    @Override
    public String getCommandName() {
        return "포인트";
    }

    @Override
    public String getUsage() {
        return "포인트 추가/차감";
    }

    @Override
    public String getPermission() {
        return "op";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        // 결투 포인트 추가/차감 닉 양
        if (!sender.isOp()) {
            sender.sendMessage("\uE509 권한이 없습니다");
            return;
        }
        if (args.length != 4) {
            sender.sendMessage("\uE509 잘못된 명령어 사용입니다");
            return;
        }

        Player target = Bukkit.getPlayer(args[2]);
        int amount = Integer.parseInt(args[3]);

        if (target == null) {
            sender.sendMessage("\uE509 존재하지 않는 플레이어 입니다");
            return;
        }

        PlayerStatusRepository statusRepository = PlayerStatusRepository.getInstance();

        if (args[0].equals("추가")) {
            statusRepository.addPoint(target, amount);
            sender.sendMessage("\uE509 " + target.getName() + "님께 " + amount + "만큼 추가했습니다");
        } else if (args[0].equals("차감")) {
            statusRepository.removePoint(target, amount);
            sender.sendMessage("\uE509 " + target.getName() + "님께 " + amount + "만큼 차감했습니다");
        } else {
            sender.sendMessage("\uE509 /결투 포인트 추가/차감 닉 양");
        }

    }

    @Override
    public List<String> getCompletions(CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 2) {
            completions.add("추가");
            completions.add("차감");
        } else if (args.length == 3) {
            PeroDuel.getInstance().getServer().getOnlinePlayers().forEach(player -> {
                completions.add(player.getName());
            });
        } else if (args.length == 4) {
            completions.add("<amount>");
        }
        return StringUtil.copyPartialMatches(args[args.length - 1], completions, new ArrayList<>());
    }
}
