package ga.ganma.taggame.taggame.item.items.runwayitem;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import ga.ganma.taggame.taggame.Taggame;
import ga.ganma.taggame.taggame.item.MaterialclickTypeItemAPI;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class FakeplayerItem implements MaterialclickTypeItemAPI, Listener {

    private static final HashMap<EntityPlayer, Boolean> npclist = new HashMap<>();

    public FakeplayerItem(){
        Bukkit.getPluginManager().registerEvents(this,Taggame.getPlugin());
        new BukkitRunnable(){
            @Override
            public void run() {
                if(!Taggame.getgamemanager().isstarting()){
                    npclist.forEach(
                            (player,bool) -> {
                                removeNPCpacket(player);
                                setNpclist(player, false);
                            }
                    );
                }
            }
        }.runTaskTimer(Taggame.getPlugin(),0,20);
    }

    @Override
    public void run(Player useplayer) {
        useplayer.getInventory().clear(useplayer.getInventory().getHeldItemSlot());
        createNPC(useplayer,useplayer.getName());
        useplayer.sendMessage(Taggame.prefix + "あなたのホログラムを作成しました。");
    }

    @Override
    public ItemMeta getItemMeta() {
        ItemStack is = new ItemStack(Material.SLIME_BALL);
        ItemMeta im = is.getItemMeta();
        if (im != null) {
            im.setDisplayName("ホログラム");
            List<String> list = new ArrayList<>();
            list.add("ホログラムを生成し鬼を惑わします。");
            list.add("鬼が近づいたら爆発して20秒間発光と盲目になります。");
            im.setLore(list);
        }
        return im;
    }

    public void createNPC(Player p,String skin) {
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer world = ((CraftWorld) Bukkit.getWorld(p.getWorld().getName())).getHandle();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), skin);
        EntityPlayer npc = new EntityPlayer(server, world, gameProfile, new PlayerInteractManager(world));
        npc.setLocation(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), p.getLocation().getYaw(), p.getLocation().getPitch());
            String[] name = getskin(p,skin);
            gameProfile.getProperties().put("textures",new Property("textures",name[0],name[1]));
            addNPCpacket(npc);
            setNpclist(npc,true);
    }

        public void addNPCpacket (EntityPlayer npc){
            Bukkit.getOnlinePlayers().forEach(
                    (player) -> {
                        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
                        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
                        connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
                        connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc,(byte) (npc.yaw * 256/360)));
                        DataWatcher dw = npc.getDataWatcher();
                        dw.set(new DataWatcherObject<>(16,DataWatcherRegistry.a), (byte)127);
                        connection.sendPacket(new PacketPlayOutEntityMetadata(npc.getId(),dw,true));
                    }
            );
            npc.setSneaking(true);
        }

    public void removeNPCpacket (EntityPlayer npc) {
        Bukkit.getOnlinePlayers().forEach(
                (player) -> {
                    PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
                    connection.sendPacket(new PacketPlayOutEntityDestroy(npc.getId()));
                }
        );
    }

        public void joinaddNPCpacket (Player p) {
            npclist.forEach(
                    (npc,bool) -> {
                        PlayerConnection connection = ((CraftPlayer) p).getHandle().playerConnection;
                        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
                        connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
                        connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte) (npc.yaw * 256 / 360)));
                        DataWatcher dw = npc.getDataWatcher();
                        dw.set(new DataWatcherObject<>(16,DataWatcherRegistry.a), (byte)127);
                        connection.sendPacket(new PacketPlayOutEntityMetadata(npc.getId(),dw,true));
                    }
            );
        }

        public String[] getskin(Player player,String name) {
            /*try {
                URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
                InputStreamReader reader = new InputStreamReader(url.openStream());
                String uuid = new JsonParser().parse(reader).getAsJsonObject().get("id").getAsString();

                URL url2 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid
                        + "?unsigned=false");
                InputStreamReader reader2 = new InputStreamReader(url2.openStream());
                JsonObject property = new JsonParser().parse(reader2).getAsJsonObject().get("properties")
                        .getAsJsonArray().get(0).getAsJsonObject();
                String texture = property.get("value").getAsString();
                String signature = property.get("signature").getAsString();
                return new String[]{texture, signature};
            } catch (IOException e) { */
                EntityPlayer p = ((CraftPlayer)player).getHandle();
                GameProfile profile = p.getProfile();
                Property property = profile.getProperties().get("textures").iterator().next();
                String texture = property.getValue();
                String signature = property.getSignature();
                return new String[] {texture,signature};
           // }
        }

        @EventHandler
        public void getJoinPlayer(PlayerJoinEvent e){
        joinaddNPCpacket(e.getPlayer());
        }

        @EventHandler
    public void getPlayermoveEvent(PlayerMoveEvent e) {
            if (Taggame.getgamemanager().isstarting()) {
                if (!npclist.isEmpty()) {
                    npclist.forEach(
                            (npc,bool) -> {
                                if (bool) {
                                    Location location = ((Entity) npc).getBukkitEntity().getLocation();
                                    if (e.getTo().distance(location) <= 4) {
                                        if (Taggame.getgamemanager().getAgrePlayer().contains(e.getPlayer())) {
                                            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 20, 1));
                                            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20 * 20, 1));
                                            e.getPlayer().getWorld().createExplosion(e.getPlayer().getLocation().clone(), 0);
                                            e.getPlayer().sendMessage(Taggame.prefix + "あなたは" + npc.getName() + "に騙されました！");
                                            Bukkit.getPlayer(npc.getName()).sendMessage(Taggame.prefix + "鬼があなたのホログラムに騙されました！");
                                            removeNPCpacket(npc);
                                            setNpclist(npc, false);
                                        }
                                    }
                                }
                            }
                    );
                }
            }
        }

        public void setNpclist(EntityPlayer npc,boolean bool){
        npclist.put(npc,bool);
        }

        public static HashMap<EntityPlayer, Boolean> getentityplayerhashmap (){
        return npclist;
        }
    }
