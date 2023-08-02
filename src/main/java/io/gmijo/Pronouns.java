package io.gmijo;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class Pronouns extends JavaPlugin implements Listener {

    public static HashMap<UUID, String> pronounAssociations;
    private static FileConfiguration config;

    @Override
    public void onEnable() {
        pronounAssociations = new HashMap<>();
        config = getConfig();
        saveDefaultConfig();
        loadAssociations();
        getCommand("prounounce").setExecutor(new PronounsCommand(this));
        getCommand("spawnextremearmorstand").setExecutor(new SpawnExtremeArmorStandCommandExecutor());
        Bukkit.getPluginManager().registerEvents(this, this);
        startSync(this);

    }

    private void startSync(Pronouns pl) {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(pl, new Runnable() {
            @Override
            public void run() {
                for(Player player : Bukkit.getOnlinePlayers()){
                    if (pronounAssociations.containsKey(player.getUniqueId())) {

                        ArmorStand armorStand = getArmorStand(player);
                        if (armorStand != null) {
                            Location location = player.getLocation();
                            armorStand.teleport(location.add(0, config.getDouble("heigh"), 0)); // Adjust the height offset (2 blocks) as desired
                        }

                    }
                }
            }
        }, 240L, 1L);
    }

    @Override
    public void onDisable() {
        saveAssociations();
    }

    private void loadAssociations() {
        for (String uuidString : config.getKeys(false)) {
            if(uuidString.length() != 36){
                continue;
            }
            UUID uuid = UUID.fromString(uuidString);
            String pronouns = config.getString(uuidString);
            pronounAssociations.put(uuid, pronouns);
        }
    }
    //test
    public void saveAssociations() {
        for (UUID uuid : pronounAssociations.keySet()) {
            config.set(uuid.toString(), pronounAssociations.get(uuid));
        }
        saveConfig();
    }
    public HashMap<UUID, String> getPronounAssociations() {
        return pronounAssociations;
    }


    // Event listeners
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (pronounAssociations.containsKey(player.getUniqueId())) {
            String pronouns = pronounAssociations.get(player.getUniqueId());
            spawnArmorStand(player, pronouns);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (pronounAssociations.containsKey(player.getUniqueId())) {
            despawnArmorStand(player);
        }
    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        Player player = event.getPlayer();
        if (pronounAssociations.containsKey(player.getUniqueId())) {
            despawnArmorStand(player);
        }
    }
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event){
        Player player = event.getPlayer();
        if (pronounAssociations.containsKey(player.getUniqueId())) {
            spawnArmorStand(player, pronounAssociations.get(player.getUniqueId()));
        }
    }
    /*@EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (pronounAssociations.containsKey(player.getUniqueId())) {
            ArmorStand armorStand = getArmorStand(player);
            if (armorStand != null) {
                Location location = player.getLocation();
                armorStand.teleport(location.add(0, config.getDouble("heigh"), 0)); // Adjust the height offset (2 blocks) as desired
            }
        }
    }

     */

    @EventHandler
    public void onGameModeSwitch(PlayerGameModeChangeEvent event){
        Player player = event.getPlayer();
        switch(event.getNewGameMode()){
            case SPECTATOR: {
                despawnArmorStand(player);
                break;
            }
            case CREATIVE:
            case SURVIVAL:
            case ADVENTURE:{
                if (pronounAssociations.containsKey(player.getUniqueId())) {
                    spawnArmorStand(player, pronounAssociations.get(player.getUniqueId()));
                }
            }
        }
    }


    public static void spawnArmorStand(Player player, String pronouns) {
        Location location = player.getLocation();
        ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location.add(0, config.getDouble("heigh"), 0), EntityType.ARMOR_STAND);
        armorStand.setInvisible(true);
        armorStand.setSmall(true);
        armorStand.setBasePlate(false);
        armorStand.setInvulnerable(true);
        armorStand.setCustomName(pronouns);
        armorStand.setCustomNameVisible(true);
        armorStand.setGravity(false);
    }

    public static void despawnArmorStand(Player player) {
        ArmorStand armorStand = getArmorStand(player);
        if (armorStand != null) {
            armorStand.remove();
        }
    }

    private static ArmorStand getArmorStand(Player player) {
        Location location = player.getLocation();
        for (ArmorStand armorStand : location.getWorld().getEntitiesByClass(ArmorStand.class)) {
            if (armorStand.getCustomName() != null &&
                    armorStand.getCustomName().equals(pronounAssociations.get(player.getUniqueId()))) {
                return armorStand;
            }
        }
        return null;
    }
}
