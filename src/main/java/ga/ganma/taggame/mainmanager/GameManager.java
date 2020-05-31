package ga.ganma.taggame.taggame.mainmanager;

import ga.ganma.taggame.taggame.Taggame;
import ga.ganma.taggame.taggame.item.ItemHandler;
import ga.ganma.taggame.taggame.item.MaterialclickTypeItemAPI;
import ga.ganma.taggame.taggame.item.PlayerclickTypeItemAPI;
import ga.ganma.taggame.taggame.item.items.agreitem.HackingItem;
import ga.ganma.taggame.taggame.item.items.agreitem.JumpUpItem;
import ga.ganma.taggame.taggame.item.items.agreitem.MotionSensorItem;
import ga.ganma.taggame.taggame.item.items.commonItem.SpeedItem;
import ga.ganma.taggame.taggame.item.items.runwayitem.*;
import ga.ganma.taggame.taggame.myException.AgreNotFoundException;
import ga.ganma.taggame.taggame.myException.NoTimeSetException;
import ga.ganma.taggame.taggame.myException.RunawayNotFoundException;
import ga.ganma.taggame.taggame.scoreboard.TagMainScoreboard;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class GameManager {

    private int time;
    private final HashMap<Player, Position> playerPositionHashMap = new HashMap<>();
    private boolean isstart;
    private boolean ismove;
    private static final List<Material> agreItemlist = new ArrayList<>();
    private static final List<Material> runwayItemlist = new ArrayList<>();
    private static final ItemHandler ih = new ItemHandler(Taggame.getPlugin());
    private GameRunneble runnable;

    public GameManager(){
        time = 480;
        isstart = false;
        ismove = true;

        registerItem(Material.GLOWSTONE_DUST,new GlowItem(),Position.RUNAWAY);
        registerItem(Material.FEATHER, new SpeedItem(),Position.RUNAWAY);
        registerItem(Material.BONE, new HideItem(),Position.RUNAWAY);
        registerItem(Material.SUGAR, new BreathingItem(),Position.RUNAWAY);
        registerItem(Material.BIRCH_BUTTON,new HanakusoItem(),Position.RUNAWAY);
        registerItem(Material.DIAMOND,new FakeplayerItem(),Position.RUNAWAY);
        //ih.register(Material.COOKIE, new RespawnItem());

        registerItem(Material.BOOK, new HackingItem(),Position.AGRE);
        registerItem(Material.GLOWSTONE, new ga.ganma.taggame.taggame.item.items.agreitem.GlowItem(),Position.AGRE);
        registerItem(Material.CLOCK,new MotionSensorItem(Taggame.getPlugin()),Position.AGRE);
        registerItem(Material.RABBIT_FOOT, new JumpUpItem(),Position.AGRE);
        registerItem(Material.SLIME_BALL, new ga.ganma.taggame.taggame.item.items.agreitem.RespawnItem(),Position.AGRE);
    }

    public int getTime() {
        return time;
    }

    public HashSet<Player> getRunawayplayer() {
        HashSet<Player> runawayplayerList = new HashSet<>();
        for (Player p : Bukkit.getOnlinePlayers()){
            if(playerPositionHashMap.containsKey(p)){
                if(playerPositionHashMap.get(p) == Position.RUNAWAY){
                    runawayplayerList.add(p);
                }
            }
        }
        return runawayplayerList;
    }

    public HashSet<Player> getAgrePlayer() {
        HashSet<Player> agrePlayerList = new HashSet<>();
        for(Player p:Bukkit.getOnlinePlayers()){
            if(playerPositionHashMap.containsKey(p)){
                if(playerPositionHashMap.get(p) == Position.AGRE){
                    agrePlayerList.add(p);
                }
            }
        }
        return agrePlayerList;
    }

    public HashSet<Player> getAdminPlayer() {
        HashSet<Player> adminPlayerList = new HashSet<>();
        for(Player p:Bukkit.getOnlinePlayers()){
            if(playerPositionHashMap.containsKey(p)){
                if(playerPositionHashMap.get(p) == Position.ADMIN){
                    adminPlayerList.add(p);
                }
            }
        }
        return adminPlayerList;
    }

    public void setAdminPlayer(HashSet<Player> adminPlayer){
        adminPlayer.forEach(
                (key) -> playerPositionHashMap.put(key,Position.ADMIN)
        );
    }

    public void setAdminPlayer(Player player){
        playerPositionHashMap.put(player,Position.ADMIN);
    }

    public void setAgrePlayer(HashSet<Player> agrePlayer) {
        agrePlayer.forEach(
                (key) -> playerPositionHashMap.put(key,Position.AGRE)
        );
    }

    public void setAgrePlayer(Player player){
        playerPositionHashMap.put(player,Position.AGRE);
        ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
        ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
        ItemStack slimeball = new ItemStack(Material.SLIME_BALL);

        player.getInventory().clear();
        player.removePotionEffect(PotionEffectType.GLOWING);
        player.removePotionEffect(PotionEffectType.SPEED);
        player.removePotionEffect(PotionEffectType.JUMP);
        player.removePotionEffect(PotionEffectType.WATER_BREATHING);
        player.removePotionEffect(PotionEffectType.DOLPHINS_GRACE);
        player.removePotionEffect(PotionEffectType.BLINDNESS);
        player.setPlayerListName(ChatColor.RED + "[鬼]" + ChatColor.RESET + player.getName());

        slimeball.setItemMeta(ih.itemclickgetExecutor(Material.SLIME_BALL).getItemMeta());

        PlayerInventory pi = player.getInventory();
        pi.setHelmet(helmet);
        pi.setChestplate(chestplate);
        pi.setItem(4,slimeball);
        if (isstarting()){
            List<Integer> list = new ArrayList<>();
            for (int a = 0;a < agreItemlist.size();a++){
                list.add(a);
            }
            Collections.shuffle(list);
            ItemStack is1 = new ItemStack(agreItemlist.get(list.get(0)));
            is1.setItemMeta(ih.itemclickgetExecutor(agreItemlist.get(list.get(0))).getItemMeta());
            pi.setItem(0,is1);
        }
        player.setWalkSpeed(0.23f);
    }

    public void setRunawayplayer(HashSet<Player> runawayplayer) {
        runawayplayer.forEach(
                (key) -> playerPositionHashMap.put(key,Position.RUNAWAY)
        );
    }

    public void setRunawayplayer(Player player){
        playerPositionHashMap.put(player,Position.RUNAWAY);
        player.getInventory().clear();
        player.removePotionEffect(PotionEffectType.GLOWING);
        player.removePotionEffect(PotionEffectType.WATER_BREATHING);
        player.removePotionEffect(PotionEffectType.JUMP);
        player.removePotionEffect(PotionEffectType.SPEED);
        player.removePotionEffect(PotionEffectType.DOLPHINS_GRACE);
        player.removePotionEffect(PotionEffectType.BLINDNESS);
        player.setPlayerListName(ChatColor.AQUA + "[逃げ]" + ChatColor.RESET + player.getName());

        player.setWalkSpeed(0.2f);
    }

    public boolean setTime(int time) {
            this.time = time;
            return true;
        }

    public void start(Location loc) throws AgreNotFoundException, RunawayNotFoundException, NoTimeSetException {
        GameManager manager = Taggame.getgamemanager();

        //各設定項目の未設定の例外送出
        if (!manager.isAgredecided()){
            throw new AgreNotFoundException();
        }

        if (!manager.isrunawaydecided()){
            throw new RunawayNotFoundException();
        }

        if (manager.getTime() <= 0){
            throw new NoTimeSetException();
        }

        Bukkit.getOnlinePlayers().forEach(
                (player) -> {
                    if(!manager.getAdminPlayer().contains(player)) {
                        player.teleport(loc);
                        player.setGameMode(GameMode.ADVENTURE);
                        //player.setSneaking(true);
                        player.getInventory().clear();
                    }

                    if(manager.getAgrePlayer().contains(player)){
                        ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
                        ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
                        ItemStack slimeball = new ItemStack(Material.SLIME_BALL);
                        slimeball.setItemMeta(ih.itemclickgetExecutor(Material.SLIME_BALL).getItemMeta());
                        PlayerInventory pi = player.getInventory();

                        pi.setHelmet(helmet);
                        pi.setChestplate(chestplate);
                        pi.setItem(4,slimeball);
                        List<Integer> list = new ArrayList<>();
                        for (int a = 0;a < agreItemlist.size();a++){
                            list.add(a);
                        }
                        PotionEffect pe = new PotionEffect(PotionEffectType.BLINDNESS,20*60,1);
                        player.addPotionEffect(pe);
                        Collections.shuffle(list);
                        ItemStack is1 = new ItemStack(agreItemlist.get(list.get(0)));
                        is1.setItemMeta(ih.itemclickgetExecutor(agreItemlist.get(list.get(0))).getItemMeta());
                        ItemStack is2 = new ItemStack(agreItemlist.get(list.get(4)));
                        is2.setItemMeta(ih.itemclickgetExecutor(agreItemlist.get(list.get(4))).getItemMeta());
                        player.getInventory().setItem(0,is1);
                        player.getInventory().setItem(1,is2);
                    }

                    if (manager.getRunawayplayer().contains(player)){
                        List<Integer> list = new ArrayList<>();
                        for (int a = 0;a < runwayItemlist.size();a++){
                            list.add(a);
                        }
                        Collections.shuffle(list);
                        ItemStack is1 = new ItemStack(runwayItemlist.get(list.get(0)));
                        is1.setItemMeta(ih.itemclickgetExecutor(runwayItemlist.get(list.get(0))).getItemMeta());
                        player.getInventory().setItem(0,is1);
                    }

                }
        );
        ismove = false;

        runnable = new GameRunneble(getTime());
        runnable.runTaskTimer(Taggame.getPlugin(),0,20);
    }

    public boolean isstarting(){
        return isstart;
    }

    public boolean ismoveing(){
        return ismove;
    }

    public boolean isAgredecided(){
        return playerPositionHashMap.containsValue(Position.AGRE);
    }

    public boolean isrunawaydecided(){
        return playerPositionHashMap.containsValue(Position.RUNAWAY);
    }

    public boolean isadmindecided(){
        return playerPositionHashMap.containsValue(Position.ADMIN);
    }

    public String getPlayerPosition(Player p) {
        if (playerPositionHashMap.containsKey(p)) {
            switch (playerPositionHashMap.get(p)){
                case AGRE:
                    return ChatColor.DARK_RED +  "鬼";

                case RUNAWAY:
                    return ChatColor.DARK_AQUA + "逃げ";

                case ADMIN:
                    return ChatColor.GREEN + "運営";
            }
        }

        return "ぬる";
    }

    public void end(){
        Taggame.registerGamemanager();
        Location loc = Taggame.getPlugin().getConfig().getLocation("lobbylocation");
        Bukkit.getOnlinePlayers().forEach(
                (player) ->{
                    if(loc != null) {
                        player.teleport(loc);
                    }
                    else {
                        player.teleport(Taggame.getPlugin().getConfig().getLocation("spawnlocation"));
                    }
                    player.setFoodLevel(20);
                    player.setSneaking(false);
                    Taggame.getgamemanager().setRunawayplayer(player);
                    player.setWalkSpeed(0.2f);
                }
        );
        Bukkit.getOnlinePlayers().forEach(
                TagMainScoreboard::registerScoreboard
        );
        setstarted(false);
    }

    public void playerquit(Player p){
        playerPositionHashMap.remove(p);
    }

    public void Reducetime(){
        if(this.time >= 0){
            time--;
        }
    }

    public void setstarted(boolean bool){
        this.isstart = bool;
    }

    public void setmove(boolean bool){
        this.ismove = bool;
    }

    public GameRunneble getRunnable() {
        return runnable;
    }

    public void registerItem(Material material, MaterialclickTypeItemAPI api, Position position){
        switch (position){
            case ADMIN:
                return;

            case RUNAWAY:
                runwayItemlist.add(material);
                break;

            case AGRE:
                agreItemlist.add(material);
                break;
        }

        ih.register(material,api);
    }

    public void registerItem(Material material, PlayerclickTypeItemAPI api, Position position){
        switch (position){
            case ADMIN:
                return;

            case RUNAWAY:
                runwayItemlist.add(material);
                break;

            case AGRE:
                agreItemlist.add(material);
                break;
        }

        ih.register(material,api);
    }
}

enum Position{
    AGRE, RUNAWAY, ADMIN
}