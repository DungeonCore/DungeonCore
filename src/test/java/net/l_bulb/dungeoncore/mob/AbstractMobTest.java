package net.l_bulb.dungeoncore.mob;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import net.l_bulb.dungeoncore.common.event.player.PlayerCustomMobSpawnEvent;

public class AbstractMobTest {

  private static final String DUMMY_NAME = "dummy_name";

  @Spy
  AbstractMobImplemention mob = new AbstractMobImplemention();

  @Mock
  Entity mockEntity;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testIsThisMob() {
    // setup
    when(mob.getEntityType()).thenReturn(EntityType.ZOMBIE);
    when(mockEntity.getType()).thenReturn(EntityType.ZOMBIE);

    when(mob.getName()).thenReturn(DUMMY_NAME);
    when(mockEntity.getCustomName()).thenReturn(DUMMY_NAME);

    // 実行
    boolean actual = mob.isThisMob(mockEntity);

    // 検証
    assertThat(actual).isEqualTo(true);
  }

  class AbstractMobImplemention extends AbstractMob<Zombie> {

    @Override
    public String getName() {
      return null;
    }

    @Override
    public void onSpawn(PlayerCustomMobSpawnEvent e) {}

    @Override
    public void onAttack(LivingEntity mob, LivingEntity target, EntityDamageByEntityEvent e) {}

    @Override
    public void onDamage(LivingEntity mob, Entity damager, EntityDamageByEntityEvent e) {}

    @Override
    public void onOtherDamage(EntityDamageEvent e) {}

    @Override
    public void onDeathPrivate(EntityDeathEvent e) {}

    @Override
    public EntityType getEntityType() {
      return null;
    }
  }

}
