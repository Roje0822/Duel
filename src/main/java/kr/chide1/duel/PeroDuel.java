package kr.chide1.duel;

import kr.chide1.duel.command.DuelCommand;
import kr.chide1.duel.listener.EndDuelListener;
import kr.chide1.duel.listener.PlayerFightListener;
import kr.chide1.duel.listener.SetupPlayerListener;
import kr.chide1.duel.repository.PlayerStatusRepository;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class PeroDuel extends JavaPlugin {

    @Getter
    private static PeroDuel instance;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        new DuelCommand("결투", this);

        getServer().getPluginManager().registerEvents(new PlayerFightListener(), this);
        getServer().getPluginManager().registerEvents(new EndDuelListener(), this);
        getServer().getPluginManager().registerEvents(new SetupPlayerListener(), this);

        PlayerStatusRepository.getInstance().load();
    }

    @Override
    public void onDisable() {
        PlayerStatusRepository.getInstance().save();
    }
}
