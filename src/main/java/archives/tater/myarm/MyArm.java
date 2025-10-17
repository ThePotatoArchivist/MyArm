package archives.tater.myarm;

import archives.tater.myarm.item.ArmItem;
import archives.tater.myarm.item.ScrewdriverItem;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.Unit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import static net.minecraft.server.command.CommandManager.literal;

@SuppressWarnings("UnstableApiUsage")
public class MyArm implements ModInitializer {
	public static final String MOD_ID = "myarm";

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static Item register(Identifier id, Item item) {
        return Registry.register(Registries.ITEM, id, item);
    }

    private static final UUID OWNER = UUID.fromString("23ba4b73-93d9-4013-bae9-018f4981f9d7");

    public static boolean isDisarmable(PlayerEntity player) {
        return player.getUuid().equals(OWNER);
    }

    public static final AttachmentType<Unit> DISARMED = AttachmentRegistry.create(id("disarmed"), builder -> builder
            .copyOnDeath()
            .persistent(Unit.CODEC)
            .syncWith(PacketCodec.unit(Unit.INSTANCE), AttachmentSyncPredicate.all())
    );

    public static final Item ARM = register(id("arm"), new ArmItem(new Item.Settings()
            .maxCount(1)
            .attributeModifiers(AttributeModifiersComponent.builder()
                    .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(Item.BASE_ATTACK_DAMAGE_MODIFIER_ID, 1, Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                    .add(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(Item.BASE_ATTACK_SPEED_MODIFIER_ID, -2f, Operation.ADD_VALUE), AttributeModifierSlot.MAINHAND)
                    .build())
            .fireproof()
            .rarity(Rarity.EPIC)
    ));

    public static final Item SCREWDRIVER = register(id("screwdriver"), new ScrewdriverItem(new Settings()
            .maxCount(1)
    ));

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(literal("could_you_give_me_a_hand")
                    .requires(source -> source.getPlayer() == null || isDisarmable(source.getPlayer()))
                    .executes(context -> {
                        var player = context.getSource().getPlayer();
                        if (player == null || !player.hasAttached(DISARMED)) {
                            context.getSource().sendError(Text.literal("You have both of yours already"));
                            return 0;
                        }
                        context.getSource().sendMessage(Text.literal("Be more careful with it next time"));
                        player.removeAttached(DISARMED);
                        return 1;
                    }));
        });
	}
}