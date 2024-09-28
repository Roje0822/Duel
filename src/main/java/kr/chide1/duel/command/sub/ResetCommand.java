package kr.chide1.duel.command.sub;

import kr.chide1.duel.PeroDuel;
import kr.chide1.duel.repository.PlayerStatusRepository;
import kr.chide1.rojecore.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class ResetCommand implements SubCommand {
    @Override
    public String getCommandName() {
        return "초기화";
    }

    @Override
    public String getUsage() {
        return "<닉네임>";
    }

    @Override
    public String getPermission() {
        return "op";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        // 결투 초기화 닉
        if (!sender.isOp()) {
            sender.sendMessage("\uE509 권한이 없습니다");
            return;
        }
        if (args.length != 2) {
            sender.sendMessage("\uE509 잘못된 명령어 사용입니다");
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);

        if (target == null) {
            sender.sendMessage("\uE509 존재하지 않는 플레이어 입니다");
            return;
        }

        PlayerStatusRepository statusRepository = PlayerStatusRepository.getInstance();
        statusRepository.resetPoint(target);
        sender.sendMessage("\uE509 " + target.getName() + "님의 mmr/전적을 초기화 시켰습니다");
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
