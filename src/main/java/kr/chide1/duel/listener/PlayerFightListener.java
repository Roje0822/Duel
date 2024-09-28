package kr.chide1.duel.listener;

import kr.chide1.duel.manager.PlayerDuelManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerFightListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player attacker)) return;
        if (!(event.getEntity() instanceof Player victim)) return;

        PlayerDuelManager duelManager = PlayerDuelManager.getInstance();

        if (!duelManager.hasPlayerDuel(attacker)) return;
        System.out.println(123);

        Player enemy = duelManager.getEnemy(attacker);

        if (enemy != victim) {
            attacker.sendMessage("\uE509 §c그 사람은 상대가 아닙니다.");
            event.setCancelled(true);
        }
    }
}
