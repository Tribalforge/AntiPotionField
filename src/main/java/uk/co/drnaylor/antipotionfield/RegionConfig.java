package uk.co.drnaylor.antipotionfield;

import uk.co.drnaylor.antipotionfield.configaccessor.ConfigAccessor;

public class RegionConfig {

	private ConfigAccessor _regionConfig; 
	
	public ConfigAccessor getRegionConfig()
	{
		return _regionConfig;
	}
	
	public RegionConfig(String filename)
	{
		_regionConfig = new ConfigAccessor(AntiPotionField.plugin, filename);
	}
}
