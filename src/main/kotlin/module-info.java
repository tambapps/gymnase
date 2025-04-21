module com.tambapps.marcel.gymnase {
  requires javafx.controls;
  requires javafx.fxml;
  requires kotlin.stdlib;
  requires java.prefs;

  // fx libraries
  requires org.fxmisc.richtext;
  requires reactfx;

  // marcel
  requires marcel.compiler;
  requires marcel.extensions;
  requires marcel.lexer;
  requires marcel.parser;
  requires marcel.repl;
  requires marcel.semantic.core;
  requires marcel.semantic.processor;
  requires marcel.semantic.transformer;
  requires marcel.stdlib;

  opens com.tambapps.marcel.gymnase to javafx.fxml;
  opens com.tambapps.marcel.gymnase.node to javafx.fxml;
  opens com.tambapps.marcel.gymnase.service to javafx.fxml;
  exports com.tambapps.marcel.gymnase;
}