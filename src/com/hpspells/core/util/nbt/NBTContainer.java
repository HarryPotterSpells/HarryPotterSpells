package com.hpspells.core.util.nbt;

import com.hpspells.core.util.SVPBypass;

import java.util.List;
import java.util.Queue;

// HPS modifications: change static values class to SVPBypass, remove language calls, removed getName
public abstract class NBTContainer {

    static final Class class_NBTBase = SVPBypass.getCurrentNMSClass("NBTBase");
    static final Class class_NBTTagCompound = SVPBypass.getCurrentNMSClass("NBTTagCompound");

    abstract public NBTBase getTag();

    public NBTBase getCustomTag(){
        return getTag();
    }

    abstract public void setTag(NBTBase base);

    public void setCustomTag(NBTBase base){
        setTag(base.clone());
    }

    public void removeTag() {
        setTag(new NBTTagCompound());
    }

    public boolean removeTag(Object... values) {
        NBTQuery q = new NBTQuery(values);
        return removeTag(q);
    }

    public boolean setTag(NBTBase base, Object... values) {
        NBTQuery q = new NBTQuery(values);
        return setTag(q,base);
    }

    public abstract List<String> getTypes();

    private NBTBase getQueryTag(NBTQuery query, NBTBase tag) {
        if (query == null) return tag;
        Queue<Object> queue = query.getQueue();
        NBTBase current = tag;
        while (true) {
            Object t = queue.poll();
            if (t == null || current == null) return current;
            if (current instanceof NBTTagCompound && t instanceof String) {
                NBTTagCompound compound = (NBTTagCompound) current;
                String key = (String) t;
                if (!compound.has(key)) return null;
                current = compound.get(key);
            } else if (current instanceof NBTTagList && t instanceof Integer) {
                NBTTagList list = (NBTTagList) current;
                int index = (Integer) t;
                if (index == -1) index = list.size() - 1;
                current = list.get(index);
            } else if (current instanceof NBTTagNumericArray && t instanceof Integer) {
                NBTTagNumericArray array = (NBTTagNumericArray) current;
                int index = (Integer) t;
                if (index == -1) index = array.size() - 1;
                current = NBTBase.getByValue(array.get(index));
            } else throw new RuntimeException();
        }
    }
    public NBTBase getTag(Object... values) {
        NBTQuery q = new NBTQuery(values);
        return getTag(q);
    }
    public NBTBase getTag(NBTQuery query){
        return getQueryTag(query, getTag());
    }
    public NBTBase getCustomTag(NBTQuery query){
        return getQueryTag(query, getCustomTag());
    }
    public NBTBase getCustomTag(Object... values) {
        NBTQuery q = new NBTQuery(values);
        return getCustomTag(q);
    }

    public boolean setTag(NBTQuery query, NBTBase base) {
        if (query == null || query.isEmpty()) {
            setTag(base);
            return true;
        }
        Queue<Object> queue = query.getQueue();
        NBTBase root = getTag();
        if (root == null) {
            Object z = queue.peek();
            if (z instanceof String) root = new NBTTagCompound();
            else if (z instanceof Integer) root = new NBTTagList();
        } else {
            root = root.clone();
        }
        NBTBase current = root;
        while (true) {
            if (queue.size() == 1) {
                Object t = queue.poll();
                if (current instanceof NBTTagCompound && t instanceof String) {
                    NBTTagCompound compound = (NBTTagCompound) current;
                    String key = (String) t;
                    compound.set(key, base.clone());
                    setTag(root);
                    return true;
                } else if (current instanceof NBTTagList && t instanceof Integer) {
                    NBTTagList list = (NBTTagList) current;
                    int index = (Integer) t;
                    if (index == -1) list.add(base.clone());
                    else list.set(index, base.clone());
                    setTag(root);
                    return true;
                } else if (current instanceof NBTTagNumericArray && t instanceof Integer) {
                    NBTTagNumericArray array = (NBTTagNumericArray) current;
                    int index = (Integer) t;
                    if (index == -1) index = array.size();
                    if (!(base instanceof NBTTagNumeric))
                        throw new RuntimeException();
                    Number value = ((NBTTagNumeric) base).get();
                    array.set(index, value);
                    setTag(root);
                    return true;
                } else {
                    throw new RuntimeException();
                }
            }
            Object t = queue.poll();
            if (current instanceof NBTTagCompound && t instanceof String) {
                NBTTagCompound compound = (NBTTagCompound) current;
                String key = (String) t;
                if (!compound.has(key)) {
                    Object z = queue.peek();
                    if (z instanceof String) current = compound.nextCompound(key);
                    else if (z instanceof Integer) current = compound.nextList(key);
                } else {
                    current = compound.get(key);
                }
            } else if (current instanceof NBTTagList && t instanceof Integer) {
                NBTTagList list = (NBTTagList) current;
                int index = (Integer) t;
                if (index == -1) index = list.size()-1;
                NBTBase b = null;
                if (!list.isEmpty()) {
                    b = NBTBase.getDefault(list.getSubTypeId());
                } else {
                    Object z = queue.peek();
                    if (z instanceof String) b = new NBTTagCompound();
                    else if (z instanceof Integer) b = new NBTTagList();
                }
                while (list.size() <= index) {
                    list.add(b.clone());
                }
                current = list.get(index);
            } else throw new RuntimeException();
        }
    }

    public boolean setCustomTag(NBTQuery query, NBTBase base) {
        if (query == null || query.isEmpty()) {
            setCustomTag(base);
            return true;
        }
        Queue<Object> queue = query.getQueue();
        NBTBase root = getCustomTag();
        if (root == null) {
            Object z = queue.peek();
            if (z instanceof String) root = new NBTTagCompound();
            else if (z instanceof Integer) root = new NBTTagList();
        } else {
            root = root.clone();
        }
        NBTBase current = root;
        while (true) {
            if (queue.size() == 1) {
                Object t = queue.poll();
                if (current instanceof NBTTagCompound && t instanceof String) {
                    NBTTagCompound compound = (NBTTagCompound) current;
                    String key = (String) t;
                    compound.set(key, base.clone());
                    setCustomTag(root);
                    return true;
                } else if (current instanceof NBTTagList && t instanceof Integer) {
                    NBTTagList list = (NBTTagList) current;
                    int index = (Integer) t;
                    if (index == -1) list.add(base.clone());
                    else list.set(index, base.clone());
                    setCustomTag(root);
                    return true;
                } else if (current instanceof NBTTagNumericArray && t instanceof Integer) {
                    NBTTagNumericArray array = (NBTTagNumericArray) current;
                    int index = (Integer) t;
                    if (index == -1) index = array.size();
                    if (!(base instanceof NBTTagNumeric))
                        throw new RuntimeException();
                    Number value = ((NBTTagNumeric) base).get();
                    array.set(index, value);
                    setCustomTag(root);
                    return true;
                } else {
                    throw new RuntimeException();
                }
            }
            Object t = queue.poll();
            if (current instanceof NBTTagCompound && t instanceof String) {
                NBTTagCompound compound = (NBTTagCompound) current;
                String key = (String) t;
                if (!compound.has(key)) {
                    Object z = queue.peek();
                    if (z instanceof String) current = compound.nextCompound(key);
                    else if (z instanceof Integer) current = compound.nextList(key);
                } else {
                    current = compound.get(key);
                }
            } else if (current instanceof NBTTagList && t instanceof Integer) {
                NBTTagList list = (NBTTagList) current;
                int index = (Integer) t;
                if (index == -1) index = list.size()-1;
                NBTBase b = null;
                if (!list.isEmpty()) {
                    b = NBTBase.getDefault(list.getSubTypeId());
                } else {
                    Object z = queue.peek();
                    if (z instanceof String) b = new NBTTagCompound();
                    else if (z instanceof Integer) b = new NBTTagList();
                }
                while (list.size() <= index) {
                    list.add(b.clone());
                }
                current = list.get(index);
            } else throw new RuntimeException();
        }
    }

    public boolean removeTag(NBTQuery query) {
        if (query == null || query.isEmpty()) {
            removeTag();
            return true;
        }
        NBTBase root = getTag();
        if (root == null) {
            return false;
        } else {
            root = root.clone();
        }
        NBTBase current = root;
        Queue<Object> queue = query.getQueue();
        while (true) {
            if (queue.size() == 1) {
                Object t = queue.poll();
                if (current instanceof NBTTagCompound && t instanceof String) {
                    NBTTagCompound compound = (NBTTagCompound) current;
                    String key = (String) t;
                    boolean b = compound.remove(key);
                    setTag(root);
                    return b;
                } else if (current instanceof NBTTagList && t instanceof Integer) {
                    NBTTagList list = (NBTTagList) current;
                    int index = (Integer) t;
                    if (index == -1) index = list.size() - 1;
                    boolean b = list.remove(index)!=null;
                    setTag(root);
                    return b;
                } else if (current instanceof NBTTagNumericArray && t instanceof Integer) {
                    NBTTagNumericArray array = (NBTTagNumericArray) current;
                    int index = (Integer) t;
                    if (index == -1) index = array.size() - 1;
                    boolean b = array.remove(index);
                    setTag(root);
                    return b;
                } else throw new RuntimeException();
            }
            Object t = queue.poll();
            if (current == null) return false;
            if (current instanceof NBTTagCompound && t instanceof String) {
                NBTTagCompound compound = (NBTTagCompound) current;
                String key = (String) t;
                if (!compound.has(key)) return false;
                current = compound.get(key);
            } else if (current instanceof NBTTagList && t instanceof Integer) {
                NBTTagList list = (NBTTagList) current;
                int index = (Integer) t;
                if (index == -1) index = list.size() - 1;
                current = list.get(index);
            } else throw new RuntimeException();
        }
    }
    public void removeCustomTag(NBTQuery query) {
        removeTag(query);
    }
    public void removeCustomTag() {
        removeTag();
    }
    public void removeCustomTag(Object... values) {
        removeTag(values);
    }

}
