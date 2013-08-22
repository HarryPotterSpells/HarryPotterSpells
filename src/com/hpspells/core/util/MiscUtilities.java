package com.hpspells.core.util;

import org.bukkit.inventory.ItemStack;

import java.util.Random;

import static com.hpspells.core.util.SVPBypass.getMethod;

/**
 * A class containing a mix of effects
 */
public class MiscUtilities {
    private static Class<?> cbItemStack = SVPBypass.getCurrentCBClass("inventory.CraftItemStack"), nmsItemStack = SVPBypass.getCurrentNMSClass("ItemStack"), nmsTagCompound = SVPBypass.getCurrentNMSClass("NBTTagCompound"), nmsTagList = SVPBypass.getCurrentNMSClass("NBTTagList");

    /**
     * Makes an {@link ItemStack} glow as if enchanted <br>
     * Based on stirante's {@code addGlow} method in his <a href="https://github.com/SocialCraft/PrettyScaryLib/blob/master/src/com/stirante/PrettyScaryLib/EnchantGlow.java">EnchantGlow</a> class
     *
     * @param item the item stack to make glow
     * @return the glowing item stack
     * @throws Exception if an error occurred whilst making the item glow
     */
    public static ItemStack makeGlow(ItemStack item) throws Exception {
        Object nmsStack = getMethod(cbItemStack, "asNMSCopy").invoke(cbItemStack, item);
        Object tag = null;

        if (!((Boolean) getMethod(nmsItemStack, "hasTag").invoke(nmsStack))) {
            tag = nmsTagCompound.newInstance();
            getMethod(nmsItemStack, "setTag").invoke(nmsStack, tag);
        }

        if (tag == null)
            tag = getMethod(nmsItemStack, "getTag").invoke(nmsStack);

        Object ench = nmsTagList.newInstance();
        getMethod(nmsTagCompound, "set").invoke(tag, "ench", ench);
        getMethod(nmsItemStack, "setTag").invoke(nmsStack, tag);
        return (ItemStack) getMethod(cbItemStack, "asCraftMirror").invoke(cbItemStack, nmsStack);
    }

    /**
     * Makes an {@link ItemStack} stop glowing as if enchanted <br>
     * Based on stirante's {@code removeGlow} method in his <a href="https://github.com/SocialCraft/PrettyScaryLib/blob/master/src/com/stirante/PrettyScaryLib/EnchantGlow.java">EnchantGlow</a> class
     *
     * @param item the item stack to make stop glowing
     * @return the non-glowing item stack
     * @throws Exception if an error occurred whilst making the item glow
     */
    public static ItemStack stopGlow(ItemStack item) throws Exception {
        Object nmsStack = getMethod(cbItemStack, "asNMSCopy").invoke(cbItemStack, item);
        Object tag = null;

        if (!((Boolean) getMethod(nmsItemStack, "hasTag").invoke(nmsStack)))
            return item;

        tag = getMethod(nmsItemStack, "getTag").invoke(nmsStack);
        getMethod(nmsTagCompound, "set").invoke(tag, "ench", null);
        getMethod(nmsItemStack, "setTag").invoke(nmsStack, tag);
        return (ItemStack) getMethod(cbItemStack, "asCraftMirror").invoke(cbItemStack, nmsStack);
    }

    /**
     * @param min minimum random value that can be generated
     * @param max maximum random value that can be generated
     * @return an EXCLUSIVE random number in the range. Value ranges are (min, max)
     */
    public static float randomBetween(float min, float max) {
        Random random = new Random();
        return (random.nextFloat() * (max - min)) + min;
    }

    /**
     * @param min minimum random value that can be generated
     * @param max maximum random value that can be generated
     * @return an INCLUSIVE random number in the range. Value ranges are [min, max]
     */
    public static int randomBetween(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

}
