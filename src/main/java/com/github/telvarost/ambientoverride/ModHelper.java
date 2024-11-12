package com.github.telvarost.ambientoverride;

import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Null;
import net.modificationstation.stationapi.api.util.Namespace;

public class ModHelper {
    @Entrypoint.Namespace
    public static final Namespace NAMESPACE = Null.get();

//    public static float PostProcess(float red, float green, float blue) {
//        return red * ModHelperFields.fogRedMultiplier;
//    }

    public static class ModHelperFields {
        public static Float fogDensityMultiplier = 2.0F;
        public static Float fogRedMultiplier     = 2.0F;
        public static Float fogGreenMultiplier   = 1.0F;
        public static Float fogBlueMultiplier    = 0.5F;
    }
}
