package com.github.telvarost.ambientoverride;

import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Null;
import net.modificationstation.stationapi.api.util.Namespace;

public class ModHelper {
    @Entrypoint.Namespace
    public static final Namespace NAMESPACE = Null.get();

    public static class ModHelperFields {
        /** - Target fog values */
        public static Float targetFogDensity     = Config.config.baseFogIntensity;
        public static Float targetFogRed         = 1.0F;
        public static Float targetFogGreen       = 1.0F;
        public static Float targetFogBlue        = 1.0F;

        /** - Current fog values */
        public static Float fogDensityMultiplier = Config.config.baseFogIntensity;
        public static Float fogRedMultiplier     = 1.0F;
        public static Float fogGreenMultiplier   = 1.0F;
        public static Float fogBlueMultiplier    = 1.0F;
    }
}
