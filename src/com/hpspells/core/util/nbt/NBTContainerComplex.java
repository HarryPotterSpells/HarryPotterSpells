package com.hpspells.core.util.nbt;

import java.util.ArrayList;
import java.util.List;

public class NBTContainerComplex extends NBTContainer {

    private final NBTContainer container;
    private final NBTQuery query;

    public NBTContainerComplex(NBTContainer container, NBTQuery query) {
        this.container = container;
        this.query = query;
    }

    public NBTContainer getObject() {
        return this.container;
    }

    @Override
    public List<String> getTypes() {
        return new ArrayList<String>();
    }

    public NBTQuery getQuery() {
        return this.query;
    }

    @Override
    public NBTBase getTag() {
        return container.getTag(getQuery());
    }

    @Override
    public void setTag(NBTBase base) {
        container.setTag(getQuery(), base);
    }

    @Override
    public void removeTag() {
        container.removeTag(getQuery());
    }
}
