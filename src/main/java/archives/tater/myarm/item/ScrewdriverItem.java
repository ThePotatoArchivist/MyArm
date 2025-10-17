package archives.tater.myarm.item;

import archives.tater.myarm.MyArm;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class ScrewdriverItem extends Item {
    public ScrewdriverItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        return tryDisarm(user, entity);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (hand == Hand.MAIN_HAND ^ user.getMainArm() == Arm.RIGHT) return TypedActionResult.fail(user.getStackInHand(hand));
        return new TypedActionResult<>(tryDisarm(user, user), user.getStackInHand(hand));
    }

    private static @NotNull ActionResult tryDisarm(PlayerEntity user, LivingEntity entity) {
        if (!(entity instanceof PlayerEntity player) || !MyArm.isDisarmable(player) || entity.hasAttached(MyArm.DISARMED)) return ActionResult.FAIL;

        user.getWorld().playSoundFromEntity(user, entity, SoundEvents.BLOCK_COPPER_TRAPDOOR_OPEN, entity.getSoundCategory(), 1f, 0.7f);

        entity.setAttached(MyArm.DISARMED, Unit.INSTANCE);

        player.dropItem(MyArm.ARM.getDefaultStack(), false, true);

        return ActionResult.SUCCESS;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        tooltip.add(LORE_TEXT);
    }

    public static final String LORE_TRANSLATION = "item.myarm.screwdriver.lore";
    private static final Text LORE_TEXT = Text.translatable(LORE_TRANSLATION).formatted(Formatting.DARK_GRAY);
}
