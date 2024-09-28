package kr.chide1.duel.repository;

import kr.chide1.duel.PeroDuel;
import kr.chide1.duel.duel.PlayerStatus;
import kr.chide1.duel.manager.PlayerDuelManager;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;

public class PlayerStatusRepository {

    private final Map<UUID, PlayerStatus> playerStatusMap = new HashMap<>();
    private final File directory = new File(PeroDuel.getInstance().getDataFolder(), "data");
    private final Logger logger = PeroDuel.getInstance().getLogger();
    private static PlayerStatusRepository instance;

    public static PlayerStatusRepository getInstance() {
        if (instance == null) instance = new PlayerStatusRepository();
        return instance;
    }

    @SneakyThrows
    public void save() {
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                logger.warning(directory.getPath() + " 파일 생성도중 오류가 발생했습니다.");
                return;
            }
        }

        for (UUID uuid : playerStatusMap.keySet()) {
            PlayerStatus playerStatus = playerStatusMap.get(uuid);
            File file = new File(directory, uuid + ".yml");
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);

            yaml.set("point", playerStatus.getPoint());
            yaml.set("lose", playerStatus.getLoseCount());
            yaml.set("win", playerStatus.getWinCount());

            yaml.save(file);
        }
    }

    public void load() {
        if (!directory.exists() || directory.listFiles() == null) return;

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
            UUID uuid = UUID.fromString(file.getName().replace(".yml", ""));

            int point = yaml.getInt("point");
            int lose = yaml.getInt("lose");
            int win = yaml.getInt("win");

            PlayerStatus playerStatus = new PlayerStatus(point, win, lose);
            playerStatusMap.put(uuid, playerStatus);
        }
    }

    public void addPoint(Player player, int amount) {
        PlayerStatus playerStatus = playerStatusMap.get(player.getUniqueId());
        int current = playerStatus.getPoint();
        playerStatus.setPoint(current + amount);
        playerStatusMap.put(player.getUniqueId(), playerStatus);
    }

    public void removePoint(Player player, int amount) {
        PlayerStatus playerStatus = playerStatusMap.get(player.getUniqueId());
        int current = playerStatus.getPoint();
        playerStatus.setPoint(current - amount);
        playerStatusMap.put(player.getUniqueId(), playerStatus);
    }

    public void addWinCount(Player player) {
        PlayerStatusRepository statusRepository = PlayerStatusRepository.getInstance();
        PlayerStatus status = statusRepository.getPlayerStatus(player);
        status.setWinCount(status.getWinCount() + 1);
    }

    public void addLoseCount(Player player) {
        PlayerStatusRepository statusRepository = PlayerStatusRepository.getInstance();
        PlayerStatus status = statusRepository.getPlayerStatus(player);
        status.setLoseCount(status.getLoseCount() + 1);
    }

    public void resetPoint(Player player) {
        playerStatusMap.remove(player.getUniqueId());
        PlayerStatus playerStatus = new PlayerStatus(1000, 0, 0);
        playerStatusMap.put(player.getUniqueId(), playerStatus);
    }

    public boolean hasNoData(Player player) {
        return !playerStatusMap.containsKey(player.getUniqueId());
    }

    public PlayerStatus getPlayerStatus(Player player) {
        return playerStatusMap.get(player.getUniqueId());
    }
}
