/*
 * Copyright (c) 2020
 * Coded by FebanHD
 * Do not output this code as your own
 */

package de.febanhd.gameapi.api.items;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

@Getter
public class ClickableItemStack extends ItemStack {

    public final static ArrayList<ClickableItemStack> stacks = Lists.newArrayList();
    private final ClickableItemStackListener listener;

    public ClickableItemStack(Material material, ClickableItemStackListener listener) {
        super(material);
        stacks.add(this);
        this.listener = listener;
    }
}
