package archives.tater.myarm;

import archives.tater.myarm.datagen.ItemTagGenerator;
import archives.tater.myarm.datagen.LangGenerator;
import archives.tater.myarm.datagen.ModelGenerator;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class MyArmDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        var pack = fabricDataGenerator.createPack();
        pack.addProvider(ItemTagGenerator::new);
        pack.addProvider(ModelGenerator::new);
        pack.addProvider(LangGenerator::new);
	}
}
