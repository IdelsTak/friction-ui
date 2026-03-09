package com.github.idelstak.friction.ui.effect;

import com.github.idelstak.friction.ui.mvu.*;
import java.util.*;

public interface RunEffect extends AutoCloseable {

  void run(List<Effect> effects);

  @Override
  void close();
}
