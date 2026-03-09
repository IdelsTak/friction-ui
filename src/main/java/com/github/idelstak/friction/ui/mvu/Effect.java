package com.github.idelstak.friction.ui.mvu;

public sealed interface Effect {

  record LoadList() implements Effect {
  }

  record LoadDetail(String id) implements Effect {
  }
}
