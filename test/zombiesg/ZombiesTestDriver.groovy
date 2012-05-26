package zombiesg;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ZombiesTestDriver {
  
  class GritableMock {
    boolean gritado = false;
    public void escucharGrito() {
      gritado = true;
    }
  }
  
  private Persona zombie1;
  private Persona personaSana;
  private Persona personaDebil;

  @Before
  public void setup() {
    zombie1 = new Persona(150);
    
    personaSana = new Persona(60);
    personaDebil = new Persona(4);
    
    zombie1.volverZombie()
  }
  
  
  @Test
  public void cuandoUnPersonajeGritaSusPerseguidoresLoEscuchan() {
    GritableMock perseguidor1 = new GritableMock();
    GritableMock perseguidor2 = new GritableMock();
    personaSana.perseguirPor(perseguidor1);
    personaSana.perseguirPor(perseguidor2);
    
    
    personaSana.gritar();
    
    assertTrue(perseguidor1.gritado);
    assertTrue(perseguidor2.gritado);
  }


  @Test
  public void cuandoUnZombieMuerdeVuelveASuVictimaZombie() {
    zombie1.morder(personaSana);
    assertPuedeMorder(personaSana, personaDebil);
  }

  @Test(expected = RuntimeException.class)
  public void lasPersonasNoPuedenMorder() {
    personaSana.morder(personaDebil);
  }
  
  @Test
  public void losPersonajesSePuedenMoverEnAmbasDirecciones() {
    personaSana.caminar(Direccion.DERECHA);
    personaSana.caminar(Direccion.DERECHA);
    personaSana.caminar(Direccion.IZQUIERDA);

    assertEquals(10, personaSana.posicionX);
  }
  
  @Test
  public void losZombiesSeMuevenALaMitadDeVelocidadQueLasPersonas() {
    zombie1.caminar(Direccion.DERECHA);
    
    assertEquals(5, zombie1.posicionX);
  }
  
  @Test(expected = RuntimeException.class)
  public void lasPersonasNoPuedeCorrerSiNoTienenEnergiaSuficiente() {
    personaDebil.correr(Direccion.DERECHA);
  }
  
  @Test(expected = RuntimeException.class)
  public void lasPersonasNoPuedeGritarSiNoTienenEnergiaSuficiente() {
    personaDebil.gritar();
  }
  
  @Test(expected = RuntimeException.class)
  public void lasPersonasNoPuedenTrotarSiNoTienenEnergiaSuficiente() {
    personaDebil.trotar(Direccion.DERECHA);
  }
  
  @Test(expected = RuntimeException.class)
  public void lasPersonasNoPuedenCaminarSiNoTienenEnergiaSuficiente() {
    personaDebil.caminar(Direccion.DERECHA);
  }

  @Test
  public void gritarleAUnZombieReduceSuEnergia() throws Exception {
    zombie1.escucharGrito();
    
    assertEquals(100, zombie1.energia);
  }
  
  @Test(expected=RuntimeException.class)
  public void lasPersonasSonSordas() throws Exception {
    personaSana.escucharGrito();
  }


  public void assertPuedeMorder(Persona mordedor, Persona mordido) {
    try {
      mordedor.morder(mordedor);
    } catch (Exception e) {
      fail();
    }
  }
}
