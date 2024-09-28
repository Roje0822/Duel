package kr.chide1.duel.listener;

import kr.chide1.duel.manager.PlayerDuelManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EndDuelListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player loser = event.getEntity();
        PlayerDuelManager duelManager = PlayerDuelManager.getInstance();

        if (!duelManager.hasPlayerDuel(loser)) return;

        Player winner = duelManager.getEnemy(loser);

        duelManager.endDuel(winner, loser);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player loser = event.getPlayer();
        PlayerDuelManager duelManager = PlayerDuelManager.getInstance();

        if (!duelManager.hasPlayerDuel(loser)) return;

        Player winner = duelManager.getEnemy(loser);

        duelManager.endDuel(winner, loser);
    }
}
