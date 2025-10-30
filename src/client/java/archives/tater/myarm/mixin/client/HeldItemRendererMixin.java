package archives.tater.myarm.mixin.client;

import archives.tater.myarm.MyArm;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {
    @WrapOperation(
            method = "updateHeldItems",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getMainHandStack()Lnet/minecraft/item/ItemStack;")
    )
    private ItemStack useArmItem(ClientPlayerEntity instance, Operation<ItemStack> original) {
        var item = original.call(instance);
        if (!MyArm.isDisarmable(instance) || instance.getMainArm() != Arm.LEFT || !item.isEmpty()) return item;
        var armItem = MyArm.getArm(instance);
        if (ItemStack.areItemsAndComponentsEqual(armItem, MyArm.ARM.getDefaultStack())) return item;
        return armItem;
    }

    @WrapOperation(
            method = "updateHeldItems",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getOffHandStack()Lnet/minecraft/item/ItemStack;")
    )
    private ItemStack useArmItem2(ClientPlayerEntity instance, Operation<ItemStack> original) {
        var item = original.call(instance);
        if (!MyArm.isDisarmable(instance) || instance.getMainArm() == Arm.LEFT || !item.isEmpty()) return item;
        var armItem = MyArm.getArm(instance);
        if (ItemStack.areItemsAndComponentsEqual(armItem, MyArm.ARM.getDefaultStack())) return item;
        return armItem;
    }
}
