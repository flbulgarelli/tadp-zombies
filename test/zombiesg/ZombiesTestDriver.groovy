package zombiesg

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test

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
  void cuandoUnPersonajeGritaSusPerseguidoresLoEscuchan() {
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
  void cuandoUnZombieMuerdeVuelveASuVictimaZombie() {
    zombie1.morder(personaSana)
    assertPuedeMorder(personaSana, personaDebil)
  }

  @Test(expected = RuntimeException)
  void lasPersonasNoPuedenMorder() {
    personaSana.morder(personaDebil)
  }
  
  @Test
  void losPersonajesSePuedenMoverEnAmbasDirecciones() {
    //Marcar esto
    personaSana.with {
      caminar(Direccion.DERECHA)
      caminar(Direccion.DERECHA)
      caminar(Direccion.IZQUIERDA)
    }
    assertEquals(10, personaSana.posicionX)
  }
  
  @Test
  void losZombiesSeMuevenALaMitadDeVelocidadQueLasPersonas() {
    zombie1.caminar(Direccion.DERECHA)
    assertEquals(5, zombie1.posicionX)
  }
  
  @Test(expected = RuntimeException)
  void lasPersonasNoPuedeCorrerSiNoTienenEnergiaSuficiente() {
    personaDebil.correr(Direccion.DERECHA)
  }
  
  @Test(expected = RuntimeException)
  void lasPersonasNoPuedeGritarSiNoTienenEnergiaSuficiente() {
    personaDebil.gritar()
  }
  
  @Test(expected = RuntimeException)
  void lasPersonasNoPuedenTrotarSiNoTienenEnergiaSuficiente() {
    personaDebil.trotar(Direccion.DERECHA)
  }
  
  @Test(expected = RuntimeException)
  void lasPersonasNoPuedenCaminarSiNoTienenEnergiaSuficiente() {
    personaDebil.caminarDerecha(Direccion.DERECHA)
  }

  @Test
  void gritarleAUnZombieReduceSuEnergia() {
    zombie1.escucharGrito()
    
    assertEquals(100, zombie1.energia)
  }
  
  @Test(expected=RuntimeException)
  void lasPersonasSonSordas() {
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
