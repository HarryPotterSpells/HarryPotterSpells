package com.hpspells.core.util.nbt;

import org.bukkit.ChatColor;

import static org.bukkit.ChatColor.*;

// HPS modifications: changed enum names, removed runtime exception message
public enum NBTType {
    END((byte) 0, "end", "\u24CD", WHITE),
    BYTE((byte) 1, "byte", "\u24B7", RED),//b
    SHORT((byte) 2, "short", "\u24C8", YELLOW),//s
    INT((byte) 3, "int", "\u24BE", BLUE),//i
    LONG((byte) 4, "long", "\u24C1", AQUA),//l
    FLOAT((byte) 5, "float", "\u24BB", DARK_PURPLE),//f
    DOUBLE((byte) 6, "double", "\u24B9", LIGHT_PURPLE),//d
    BYTE_ARRAY((byte) 7, "byte[]", ChatColor.BOLD + "\u24B7", DARK_RED),//b
    STRING((byte) 8, "string", "\u24C9", GREEN),
    LIST((byte) 9, "list", "\u2630", DARK_GRAY),
    COMPOUND((byte) 10, "compound", "\u27B2", GRAY), //27B2
    INT_ARRAY((byte) 11, "int[] ", ChatColor.BOLD + "\u24BE", DARK_BLUE),//i
    ;
    public final String name;
    public final String prefix;
    public final byte type;
    public final ChatColor color;

    NBTType(byte type, String name, String prefix, ChatColor color) {
        this.type = type;
        this.name = name;
        this.prefix = prefix;
        this.color = color;
    }

    public static NBTType fromByte(byte b) {
        for (NBTType t : values()) if (t.type == b) return t;
        return END;
    }

    public static NBTType fromBase(NBTBase base) {
        if (base == null) return END;
        return fromByte(base.getTypeId());
    }

    public static NBTType fromString(String name) {
        String s = name.toLowerCase();
        for (NBTType t : values()) if (s.equalsIgnoreCase(t.name)) return t;
        for (NBTType t : values()) if (t.name.toLowerCase().startsWith(s)) return t;
        return END;
    }

    public NBTBase getDefault() {
        return NBTBase.getDefault(type);
    }

    public NBTBase parse(String s) {
        switch (this) {
            case STRING: {
                return new NBTTagString(s);
            }
            case BYTE: {
                Byte v = null;
                try {
                    v = Byte.parseByte(s);
                } catch (Throwable ignored) {
                }
                if (v == null) try {
                    v = (byte) Long.parseLong(s);
                } catch (Throwable ignored) {
                }
                if (v == null) try {
                    v = (byte) Double.parseDouble(s);
                } catch (Throwable ignored) {
                }
                if (v == null) throw new RuntimeException();
                return new NBTTagByte(v);
            }
            case SHORT: {
                Short v = null;
                try {
                    v = Short.parseShort(s);
                } catch (Throwable ignored) {
                }
                if (v == null) try {
                    v = (short) Long.parseLong(s);
                } catch (Throwable ignored) {
                }
                if (v == null) try {
                    v = (short) Double.parseDouble(s);
                } catch (Throwable ignored) {
                }
                if (v == null) throw new RuntimeException();
                return new NBTTagShort(v);
            }
            case INT: {
                Integer v = null;
                try {
                    v = Integer.parseInt(s);
                } catch (Throwable ignored) {
                }
                if (v == null) try {
                    v = (int) Long.parseLong(s);
                } catch (Throwable ignored) {
                }
                if (v == null) try {
                    v = (int) Double.parseDouble(s);
                } catch (Throwable ignored) {
                }
                if (v == null) throw new RuntimeException();
                return new NBTTagInt(v);
            }
            case LONG: {
                Long v = null;
                try {
                    v = Long.parseLong(s);
                } catch (Throwable ignored) {
                }
                if (v == null) try {
                    v = (long) Double.parseDouble(s);
                } catch (Throwable ignored) {
                }
                if (v == null) throw new RuntimeException();
                return new NBTTagLong(v);
            }
            case DOUBLE: {
                Double v = null;
                try {
                    v = Double.parseDouble(s);
                } catch (Throwable ignored) {
                }
                if (v == null) throw new RuntimeException();
                return new NBTTagDouble(v);
            }
            case FLOAT: {
                Float v = null;
                try {
                    v = Float.parseFloat(s);
                } catch (Throwable ignored) {
                }
                if (v == null) try {
                    v = (float) (double) Double.parseDouble(s);
                } catch (Throwable ignored) {
                }
                if (v == null) throw new RuntimeException();
                return new NBTTagFloat(v);
            }
            case BYTE_ARRAY: {
                if (!s.matches("\\[((-?[0-9]+|#-?[0-9a-fA-F]+)(,(?!\\])|(?=\\])))*\\]")) {
                    throw new RuntimeException();
                }
                String sp = s.substring(1, s.length() - 1);
                if (sp.isEmpty()) return new NBTTagByteArray();
                String[] ss = sp.split(",");
                byte[] v = new byte[ss.length];
                for (int i = 0; i < v.length; i++) {
                    Byte t = null;
                    String x = ss[i];
                    if (x.startsWith("#")) try {
                        t = (byte) Long.parseLong(x.substring(1), 16);
                    } catch (Throwable ignored) {
                    }
                    try {
                        t = Byte.parseByte(x);
                    } catch (Throwable ignored) {
                    }
                    if (t == null) try {
                        t = (byte) (long) Long.parseLong(x);
                    } catch (Throwable ignored) {
                    }
                    if (t == null) throw new RuntimeException();
                    v[i] = t;
                }
                return new NBTTagByteArray(v);
            }
            case INT_ARRAY: {
                if (!s.matches("\\[((-?[0-9]+|#-?[0-9a-fA-F]+)(,(?!\\])|(?=\\])))*\\]")) {
                    throw new RuntimeException();
                }
                String sp = s.substring(1, s.length() - 1);
                if (sp.isEmpty()) return new NBTTagIntArray();
                String[] ss = sp.split(",");
                int[] v = new int[ss.length];
                for (int i = 0; i < v.length; i++) {
                    Integer t = null;
                    String x = ss[i];
                    if (x.startsWith("#")) try {
                        t = (int) Long.parseLong(x.substring(1), 16);
                    } catch (Throwable ignored) {
                    }
                    try {
                        t = Integer.parseInt(x);
                    } catch (Throwable ignored) {
                    }
                    if (t == null) try {
                        t = (int) (long) Long.parseLong(x);
                    } catch (Throwable ignored) {
                    }
                    if (t == null) throw new RuntimeException();
                    v[i] = t;
                }
                return new NBTTagIntArray(v);
            }
            default: {
                throw new RuntimeException();
            }
        }
    }
}
