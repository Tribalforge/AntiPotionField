package uk.co.drnaylor.antipotionfield;

import uk.co.drnaylor.antipotionfield.configaccessor.ConfigAccessor;

public class RegionConfig {

    private ConfigAccessor _regionConfig;

    /**
     * Creates an object that interfaces with a config file.
     * 
     * @param filename Name of the file to interface with.
     */
    public RegionConfig(String filename) {
        _regionConfig = new ConfigAccessor(AntiPotionField.plugin, filename);
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
