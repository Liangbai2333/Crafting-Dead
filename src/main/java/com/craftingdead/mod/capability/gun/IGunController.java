package com.craftingdead.mod.capability.gun;

import java.util.Optional;
import java.util.Set;
import com.craftingdead.mod.capability.action.IAction;
import com.craftingdead.mod.item.AttachmentItem;
import com.craftingdead.mod.item.GunItem;
import com.craftingdead.mod.item.Color;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.INBTSerializable;

public interface IGunController extends IAction, INBTSerializable<CompoundNBT> {

  void tick(Entity entity, ItemStack itemStack);

  void setTriggerPressed(Entity entity, ItemStack itemStack, boolean triggerPressed);

  boolean isReloading();

  int getTotalReloadDurationTicks();

  int getReloadDurationTicks();

  void stopReloading();

  void startReloading(Entity entity, ItemStack itemStack);

  float getAccuracy(Entity entity, ItemStack itemStack);

  ItemStack getMagazineStack();

  void setMagazineStack(ItemStack stack);

  int getAmmo();

  void setAmmo(int ammo);

  Set<AttachmentItem> getAttachments();

  default float getAttachmentMultiplier(AttachmentItem.MultiplierType multiplierType) {
    return this
        .getAttachments()
        .stream()
        .map(attachment -> attachment.getMultiplier(multiplierType))
        .reduce(1.0F, (x, y) -> x * y);
  }

  void setAttachments(Set<AttachmentItem> attachments);

  ItemStack getPaint();

  Optional<GunItem> getGun();

  void setPaint(ItemStack paint);

  /**
   * Color of the gun
   */
  Optional<Color> getColor();

  boolean isAcceptedPaintOrAttachment(ItemStack itemStack);

  void toggleFireMode(Entity entity);

  @Override
  default boolean isActive(ClientPlayerEntity playerEntity) {
    return this.isReloading();
  }

  @Override
  default ITextComponent getText(ClientPlayerEntity playerEntity) {
    return new TranslationTextComponent("action.reload");
  }

  @Override
  default float getProgress(ClientPlayerEntity playerEntity) {
    return (float) (this.getTotalReloadDurationTicks() - this.getReloadDurationTicks())
        / this.getTotalReloadDurationTicks();
  }
}
