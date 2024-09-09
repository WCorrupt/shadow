package wcorrupt.itemname;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.WeakHashMap;

public class ShadowstrikeKatana extends JavaPlugin implements Listener {

    private static final String SHADOWSTRIKE_TAG = "shadowstrike_katana";
    private final WeakHashMap<UUID, Long> cooldowns = new WeakHashMap<>();
    private static final double MAX_TELEPORT_DISTANCE = 10.0;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getLogger().info(ChatColor.GREEN + "Shadowstrike Katana Plugin Enabled!");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info(ChatColor.RED + "Shadowstrike Katana Plugin Disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("getkatana")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                giveShadowstrikeKatana(player);
                player.sendMessage(ChatColor.LIGHT_PURPLE + "You have been given the Shadowstrike Katana!");
            } else {
                sender.sendMessage("This command can only be used by a player.");
            }
            return true;
        }
        return false;
    }

    private void giveShadowstrikeKatana(Player player) {
        ItemStack katana = new ItemStack(Material.IRON_SWORD);
        ItemMeta meta = katana.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(applyGradient("ꜱʜᴀᴅᴏᴡꜱᴛʀɪᴋᴇ ᴋᴀᴛᴀɴᴀ", "#432E71", "#CAA9C6"));
            meta.getPersistentDataContainer().set(new NamespacedKey(this, SHADOWSTRIKE_TAG), PersistentDataType.BYTE, (byte) 1);

            List<String> lore = new ArrayList<>();
            lore.add(applyGradient("« ᴇɴᴄʜᴀɴᴛᴍᴇɴᴛꜱ »", "#432E71", "#CAA9C6"));
            lore.add(applyGradient("", "#432E71", "#CAA9C6"));
            lore.add(applyGradient("ꜱʜᴀʀᴘɴᴇꜱꜱ ᴠ", "#432E71", "#CAA9C6"));
            lore.add(applyGradient("ꜰɪʀᴇ ᴀꜱᴘᴇᴄᴛ ɪɪ", "#432E71", "#CAA9C6"));
            lore.add(applyGradient("ᴜɴʙʀᴇᴀᴋɪɴɢ ɪɪɪ", "#432E71", "#CAA9C6"));
            lore.add(applyGradient("ᴍᴇɴᴅɪɴɢ", "#432E71", "#CAA9C6"));
            lore.add(applyGradient("ꜱʜᴀᴅᴏᴡ ꜱᴛʀɪᴋᴇ", "#432E71", "#CAA9C6"));
            lore.add(applyGradient("", "#432E71", "#CAA9C6"));
            lore.add(applyGradient("✧━━━━━━━━━━━━◈━━━━━━━━━━━━✧", "#432E71", "#CAA9C6"));
            lore.add(applyGradient("ꜰᴏʀɢᴇᴅ ɪɴ ᴛʜᴇ ꜱʜᴀᴅᴏᴡꜱ.", "#432E71", "#CAA9C6"));
            lore.add(applyGradient("ᴀ ʙʟᴀᴅᴇ ᴏꜰ ᴅᴀʀᴋ ᴡʜɪꜱᴘᴇʀꜱ.", "#432E71", "#CAA9C6"));
            lore.add(applyGradient("", "#432E71", "#CAA9C6"));
            lore.add(applyGradient("→ ᴛᴇʟᴇᴘᴏʀᴛ ʙᴇʜɪɴᴅ ᴇɴᴇᴍɪᴇꜱ.", "#432E71", "#CAA9C6"));
            lore.add(applyGradient("⏳ 30 ꜱᴇᴄᴏɴᴅ ᴄᴏᴏʟᴅᴏᴡɴ.", "#432E71", "#CAA9C6"));
            lore.add(applyGradient("", "#432E71", "#CAA9C6"));
            lore.add(applyGradient("⚔ ᴜꜱᴇ ɪᴛ ᴡɪꜱᴇʟʏ, ᴡᴀʀʀɪᴏʀ.", "#432E71", "#CAA9C6"));
            lore.add(applyGradient("✧━━━━━━━━━━━━◈━━━━━━━━━━━━✧", "#432E71", "#CAA9C6"));

            meta.setLore(lore);

            meta.addEnchant(Enchantment.SHARPNESS, 5, true);
            meta.addEnchant(Enchantment.FIRE_ASPECT, 2, true);
            meta.addEnchant(Enchantment.UNBREAKING, 3, true);
            meta.addEnchant(Enchantment.MENDING, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

            katana.setItemMeta(meta);
            player.getInventory().addItem(katana);
        }
    }

    @EventHandler
    public void onPlayerUseShadowstrike(PlayerInteractEvent event) {
        if (!event.hasItem()) return;
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (item == null || !item.hasItemMeta()) return;

        ItemMeta meta = item.getItemMeta();
        if (meta != null && meta.getPersistentDataContainer().has(new NamespacedKey(this, SHADOWSTRIKE_TAG), PersistentDataType.BYTE)) {
            if (event.getAction().toString().contains("RIGHT_CLICK") && isOffCooldown(player)) {
                if (teleportBehindTarget(player)) {
                    setCooldown(player);
                }
            }
        }
    }

    private boolean isOffCooldown(Player player) {
        return !cooldowns.containsKey(player.getUniqueId()) || (System.currentTimeMillis() - cooldowns.get(player.getUniqueId())) >= 10000;
    }

    private void setCooldown(Player player) {
        player.setCooldown(Material.IRON_SWORD, 200);
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
    }

    private boolean teleportBehindTarget(Player player) {
        RayTraceResult result = player.getWorld().rayTraceEntities(
                player.getEyeLocation(), player.getEyeLocation().getDirection(), MAX_TELEPORT_DISTANCE, 0.1, entity -> entity instanceof Player && entity != player);

        if (result != null && result.getHitEntity() instanceof Player) {
            Player target = (Player) result.getHitEntity();

            Vector direction = target.getLocation().getDirection().multiply(-1);
            direction.setY(0);
            player.teleport(target.getLocation().add(direction.normalize().multiply(2)));
            player.setRotation(target.getLocation().getYaw(), player.getLocation().getPitch());
            return true;
        }

        return false;
    }

    private String applyGradient(String text, String startColor, String endColor) {
        StringBuilder gradientText = new StringBuilder();
        int length = text.length();

        int[] startRGB = hexToRGB(startColor);
        int[] endRGB = hexToRGB(endColor);

        for (int i = 0; i < length; i++) {
            double ratio = (double) i / (length - 1);
            int red = (int) (startRGB[0] + ratio * (endRGB[0] - startRGB[0]));
            int green = (int) (startRGB[1] + ratio * (endRGB[1] - startRGB[1]));
            int blue = (int) (startRGB[2] + ratio * (endRGB[2] - startRGB[2]));

            String hexColor = String.format("§x§%1X§%1X§%1X§%1X§%1X§%1X",
                    (red >> 4) & 0xF, red & 0xF,
                    (green >> 4) & 0xF, green & 0xF,
                    (blue >> 4) & 0xF, blue & 0xF);

            gradientText.append(hexColor).append(text.charAt(i));
        }

        return gradientText.toString();
    }

    private int[] hexToRGB(String hex) {
        return new int[]{
                Integer.valueOf(hex.substring(1, 3), 16),
                Integer.valueOf(hex.substring(3, 5), 16),
                Integer.valueOf(hex.substring(5, 7), 16)
        };
    }
}
