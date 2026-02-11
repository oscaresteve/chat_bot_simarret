package edu.alumno.oscaresteve.chat_bot_simarret.event;

import org.springframework.context.ApplicationEvent;

public class ChisteContadoEvent extends ApplicationEvent {
  public ChisteContadoEvent(Object source) {
    super(source);
  }
}
