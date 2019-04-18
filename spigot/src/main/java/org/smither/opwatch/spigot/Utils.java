package org.smither.opwatch.spigot;

import com.google.common.collect.Sets;
import org.bukkit.Material;

import java.util.HashSet;
import java.util.Set;

public class Utils {
    private static Set<Material> signSet =
            Sets.newHashSet(
                    Material.SIGN,
                    Material.WALL_SIGN,
                    Material.LEGACY_SIGN,
                    Material.LEGACY_WALL_SIGN,
                    Material.LEGACY_SIGN_POST);

    public static Set<Material> getSignSet() {
        return new HashSet<>(signSet);
    }
}
