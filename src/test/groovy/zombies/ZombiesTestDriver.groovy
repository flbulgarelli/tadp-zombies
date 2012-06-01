package zombies

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test

import zombies.Persona;

class ZombiesTestDriver {
  
  class GritableMock {
    boolean gritado = false
    void escucharGrito() {
      gritado = true
    }
  }
  
  Persona zombie1
  Persona personaSana
  Persona personaDebil

  @Before
  void setup() {
    zombie1 = new Persona(energia: 150)
    personaSana = new Persona(energia: 60)
    personaDebil = new Persona(energia: 4)
    
    zombie1.volverZombie()
  }
  
  
  @Test
  void 'cuando un personaje grita sus perseguidores lo escuchan'() {
    GritableMock perseguidor1 = new GritableMock()
    GritableMock perseguidor2 = new GritableMock()
    personaSana.with { 
      perseguirPor(perseguidor1)
      perseguirPor(perseguidor2)
    }
    personaSana.gritar()
    
    assertTrue(perseguidor1.gritado)
    assertTrue(perseguidor2.gritado)
  }


  @Test
  void 'cuando un zombie muerde vuelve a su victima zombie'() {
    zombie1.morder(personaSana)
    assertPuedeMorder(personaSana, personaDebil)
  }

  @Test(expected = RuntimeException)
  void lasPersonasNoPuedenMorder() {
    personaSana.morder(personaDebil)
  }
  
  @Test
  void 'los personajes se pueden mover en ambas direcciones'() {
    //Marcar esto
    personaSana.with {
      caminarDerecha()
      caminarDerecha()
      caminarIzquierda()
    }
    assert 10 == personaSana.posicionX
  }
  
  @Test
  void 'los zombies se mueven a la mitad de velocidad que las personas'() {
    zombie1.caminarDerecha()
    assert 5 == zombie1.posicionX
  }
  
  @Test(expected = RuntimeException)
  void 'las personas no puede correr si no tienen energia suficiente'() {
    personaDebil.correrDerecha()
  }
  
  @Test(expected = RuntimeException)
  void 'las personas no pueden gritar si no tienen energia suficiente'() {
    personaDebil.gritar()
  }
  
  @Test(expected = RuntimeException)
  void 'las personas no pueden trotar si no tienen energia suficiente'() {
    personaDebil.trotarDerecha()
  }
  
  @Test(expected = RuntimeException)
  void 'las personas no pueden caminar si no tienen energia suficiente'() {
    personaDebil.caminarDerecha()
  }

  @Test
  void 'gritarle a un zombie reduce su energia'() {
    zombie1.escucharGrito()
    
    assert 100 == zombie1.energia
  }
  
  @Test(expected=RuntimeException)
  void 'las personas son sordas'() {
    personaSana.escucharGrito()
  }


  void assertPuedeMorder(Persona mordedor, Persona mordido) {
    try {
      mordedor.morder(mordedor)
    } catch (Exception e) {
      fail()
    }
  }
}
