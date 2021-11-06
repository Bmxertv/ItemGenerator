package de.bmxertv.itemgenerator.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.UUID;

public class ItemBuilder {

    private ItemStack itemStack;
    private ItemMeta itemMeta;

    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material, 1);
        this.itemMeta = this.itemStack.getItemMeta();
    }

    public ItemBuilder(Material material, int amount) {
        this.itemStack = new ItemStack(material, amount);
        this.itemMeta = this.itemStack.getItemMeta();
    }

    public ItemBuilder(Material material, int amount, int data) {
        this.itemStack = new ItemStack(material, amount);
        this.itemMeta = this.itemStack.getItemMeta();
        setData(data);
    }

    public ItemBuilder setName(String s) {
        this.itemMeta.setDisplayName(color(s));
        return this;
    }

    public ItemBuilder setNoName() {
        this.itemMeta.setDisplayName(" ");
        return this;
    }

    public ItemBuilder setData(int i) {
        this.itemStack.setDurability((short) i);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder setLore(String... s) {
        Arrays.asList(s).replaceAll(r -> r.replace('&', '§'));
        this.itemMeta.setLore(Arrays.asList(s));
        return this;
    }

    public ItemBuilder setGlowing() {
        this.itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        this.itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ItemBuilder setGlowing(boolean b) {
        if (b) {
            setGlowing();
            return this;
        } else {
            this.itemMeta.removeEnchant(Enchantment.ARROW_DAMAGE);
            return this;
        }
    }

    public ItemBuilder addEnchantment(Enchantment e, int i) {
        this.itemMeta.addEnchant(e, 1, true);
        return this;
    }

    public ItemBuilder addFlag(ItemFlag... f) {
        this.itemMeta.addItemFlags(f);
        return this;
    }

    public ItemBuilder setSkullOwner(String owner) {
        SkullMeta meta = (SkullMeta) this.itemMeta;
        meta.setOwner(owner);
        return this;
    }

    /**
     * https://minecraft-heads.com/custom-heads
     */
    public ItemBuilder setSkull(String value) {
        if (value.isEmpty()) return this;
        SkullMeta meta = (SkullMeta) this.itemMeta;
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
        gameProfile.getProperties().put("textures", new Property("textures", value));
        try {
            Field field = meta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(meta, gameProfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public ItemStack create() {
        this.itemStack.setItemMeta(this.itemMeta);
        return this.itemStack;
    }

    private String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }


}
