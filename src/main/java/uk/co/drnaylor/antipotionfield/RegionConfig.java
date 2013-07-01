package uk.co.drnaylor.antipotionfield;

import uk.co.drnaylor.antipotionfield.configaccessor.ConfigAccessor;

public class RegionConfig {

    private final ConfigAccessor _regionConfig;

    /**
     * Creates an object that interfaces with a config file.
     *
     */
    public RegionConfig() {
        _regionConfig = new ConfigAccessor(AntiPotionField.getPlugin(), "regions.yml");
    }

    /**
     * Get the configuration object represented by this object.
     *
     * @return ConfigAccessor object containing the object.
     */
    public ConfigAccessor getRegionConfig() {
        return _regionConfig;
    }
}
