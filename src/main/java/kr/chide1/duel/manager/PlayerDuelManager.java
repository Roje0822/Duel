package kr.chide1.duel.manager;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import kr.chide1.duel.duel.PlayerStatus;
import kr.chide1.duel.repository.PlayerStatusRepository;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerDuelManager {

    private final Map<UUID, UUID> playerEnemyMap = new HashMap<>();

    private final Map<Player, Player> playerApplyMap = new HashMap<>();

    private static PlayerDuelManager instance;

    public static PlayerDuelManager getInstance() {
        if (instance == null) instance = new PlayerDuelManager();
        return instance;
    }

    private PlayerDuelManager() {
    }

    public void applyDuel(Player player, Player target) {
        playerApplyMap.put(target, player);
    }

    public boolean hasPlayerDuel(Player player) {
        return playerEnemyMap.containsKey(player.getUniqueId());
    }

    public Player getApplyPlayer(Player target) {
        return playerApplyMap.get(target);
    }

    public boolean hasApplyPlayer(Player target) {
        return playerApplyMap.containsKey(target);
    }

    public void acceptDuel(Player player, Player target) {
        playerEnemyMap.put(player.getUniqueId(), target.getUniqueId());
        playerEnemyMap.put(target.getUniqueId(), player.getUniqueId());
        playerApplyMap.remove(target);
    }

    public void refuseDuel(Player target) {
        playerApplyMap.remove(target);
    }

    public void startDuel(Player player, Player target) {
        System.out.println(player.getName() + target.getName() + "듀얼 스타트 1");

        // NMS 로 특정 플레이어에게만 보이는 발광 <= 원함
        // TODO: 2024-08-25 발광효과

//        GlowUtil.addGlow(player, target, GlowUtil.GlowColor.BLUE);
//        GlowUtil.addGlow(target, player, GlowUtil.GlowColor.RED);

        ProtectedRegion region = getSpawnRegion(player.getWorld());

        if (region != null) {
            DefaultDomain members = region.getMembers();
            members.addPlayer(player.getUniqueId());
            members.addPlayer(target.getUniqueId());
            region.setMembers(members);
        }

        playerApplyMap.remove(target);
    }

    public Player getEnemy(Player player) {
        return Bukkit.getPlayer(playerEnemyMap.get(player.getUniqueId()));
    }

    public void endDuel(Player winner, Player loser) {
        ProtectedRegion region = getSpawnRegion(winner.getWorld());

//        GlowUtil.removeGlow(winner);
//        GlowUtil.removeGlow(loser);

        if (region != null) {
            DefaultDomain members = region.getMembers();
            members.removePlayer(winner.getUniqueId());
            members.removePlayer(loser.getUniqueId());
            region.setMembers(members);
            save(winner.getWorld());
        }

        playerEnemyMap.remove(winner.getUniqueId());
        playerEnemyMap.remove(loser.getUniqueId());

        calculateDuel(winner, loser);

        PlayerStatusRepository statusRepository = PlayerStatusRepository.getInstance();
        statusRepository.addWinCount(winner);
        statusRepository.addLoseCount(loser);
    }

    private void calculateDuel(Player winner, Player loser) {
        PlayerStatusRepository statusRepository = PlayerStatusRepository.getInstance();
        PlayerStatus winnerStatus = statusRepository.getPlayerStatus(winner);
        PlayerStatus loserStatus = statusRepository.getPlayerStatus(loser);

        double winnerWinPercent = 1 / (1 + Math.pow(10, (double) (loserStatus.getPoint() - winnerStatus.getPoint()) / 400));
        double loserLosePercent = 1 / (1 + Math.pow(10, (double) (winnerStatus.getPoint() - loserStatus.getPoint()) / 400));

        statusRepository.addPoint(winner, (int) ((1 - winnerWinPercent) * 40));
        statusRepository.removePoint(loser, (int) ((1 - loserLosePercent) * 40));

        System.out.println((int) (1 - winnerWinPercent) * 40);
        System.out.println((int) (1 - loserLosePercent) * 40);

    }

    public Integer getDuelAmount() {
        return playerEnemyMap.size() / 2;
    }

    public ProtectedRegion getSpawnRegion(World world) {
        RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = regionContainer.get(BukkitAdapter.adapt(world));
        return Objects.requireNonNull(regions).getRegion("spawn");
    }

    @SneakyThrows
    public void save(World world) {
        RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = regionContainer.get(BukkitAdapter.adapt(world));
        regions.saveChanges();
    }
}
