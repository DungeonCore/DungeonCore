package net.l_bulb.dungeoncore.item.system.lore;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

public class ItemLoreTokenTest {
  @Spy
  ItemLoreToken token = new ItemLoreToken("test");

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testSize() {
    // 検証
    assertThat(token.size()).isEqualTo(0);
  }

  @Test
  public void testSize2() {
    token.addLore("dummy");
    // 検証
    assertThat(token.size()).isEqualTo(1);
  }

}
