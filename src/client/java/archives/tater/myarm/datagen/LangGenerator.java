package archives.tater.myarm.datagen;

import archives.tater.myarm.MyArm;
import archives.tater.myarm.item.ScrewdriverItem;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

import net.minecraft.registry.RegistryWrapper.WrapperLookup;

import java.util.concurrent.CompletableFuture;

public class LangGenerator extends FabricLanguageProvider {
    public LangGenerator(FabricDataOutput dataOutput, CompletableFuture<WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add(MyArm.ARM, "Brass Arm");
        translationBuilder.add(MyArm.SCREWDRIVER, "Brass Screwdriver");
        translationBuilder.add(ScrewdriverItem.LORE_TRANSLATION, "Might fit someone's... ???");
        translationBuilder.add(MyArm.COMMAND_SUCCESS, "Be more careful with it next time");
        translationBuilder.add(MyArm.COMMAND_FAIL, "You have both of yours already");
    }
}
