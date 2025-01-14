package com.hpspells.core.util;

import org.bukkit.inventory.ItemStack;

import java.util.Map;
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
    @SuppressWarnings("deprecation")
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

    /**
     * Gets a random {@code String} from a map with a weighted probability
     *
     * @param probabilities a map of all the strings and {@link Double} probabilities adding up to 1.0
     *
     * @return the chosen string or {@code "Invalid Probability"} if the map was invalid
     */
    public static String getStringFromProbability(Map<String, Object> probabilities) {
        double probability = Math.random(), cumulativeProbability = 0.0;

        for (Map.Entry<String, Object> entry : probabilities.entrySet()) {
            cumulativeProbability += (Double) entry.getValue();

            if (probability <= cumulativeProbability) {
                return entry.getKey();
            }
        }

        return "Invalid Probability";
    }
    
    /**
     * Takes an object and tries to convert to a Double in the case it is an Integer or a String.
     * Will log an error if toHandle is not a valid double.
     * 
     * @param toHandle The value to handle
     * @return the double value of toHandle or null
     * @throws NumberFormatException if not a valid number
     */
    public static Double handleDouble(Object toHandle) throws NumberFormatException {
        if (toHandle instanceof Double) {
            return (Double) toHandle;
        } else if (toHandle instanceof Integer || toHandle instanceof String) {
            return Double.parseDouble(toHandle.toString());
        }
        return null;
    }

}
