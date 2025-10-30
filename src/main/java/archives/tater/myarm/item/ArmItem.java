package archives.tater.myarm.item;

import archives.tater.myarm.MyArm;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

@SuppressWarnings("UnstableApiUsage")
public class ArmItem extends Item {
    public ArmItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        var stack = user.getStackInHand(hand);
        if (!MyArm.isDisarmable(user) || !MyArm.getArm(user).isEmpty()) return TypedActionResult.fail(stack);
        user.playSound(SoundEvents.BLOCK_COPPER_TRAPDOOR_CLOSE, 1f, 0.7f);
        user.setAttached(MyArm.ARM_ITEM, stack);
        return TypedActionResult.success(ItemStack.EMPTY);
    }
}
