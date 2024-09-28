package kr.chide1.duel.listener;

import kr.chide1.duel.repository.PlayerStatusRepository;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class SetupPlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerStatusRepository statusRepository = PlayerStatusRepository.getInstance();

        if (statusRepository.hasNoData(player)) {
            statusRepository.resetPoint(player);
        }
    }
}
