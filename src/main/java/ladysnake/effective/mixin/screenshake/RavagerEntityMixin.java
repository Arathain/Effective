package ladysnake.effective.mixin.screenshake;

import com.sammy.ortus.handlers.ScreenshakeHandler;
import com.sammy.ortus.systems.rendering.particle.Easing;
import com.sammy.ortus.systems.screenshake.PositionedScreenshakeInstance;
import com.sammy.ortus.systems.screenshake.ScreenshakeInstance;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.RavagerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RavagerEntity.class)
public class RavagerEntityMixin extends HostileEntity {
	protected RavagerEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "roar", at = @At("HEAD"))
	public void roar(CallbackInfo ci) {
		ScreenshakeInstance roarScreenShake = new PositionedScreenshakeInstance(10, this.getPos(), 20f, 0f, 25f, Easing.CIRC_IN_OUT).setIntensity(0.0f, 1.0f, 0.0f);
		ScreenshakeHandler.addScreenshake(roarScreenShake);
	}
}
