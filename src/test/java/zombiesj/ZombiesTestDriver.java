package zombiesj;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import zombiesj.Direccion;
import zombiesj.Gritable;
import zombiesj.Personaje;

public class ZombiesTestDriver {

  private Personaje zombie1;
  private Personaje personaSana;
  private Personaje personaDebil;

  @Before
  public void setup() {
    zombie1 = new Personaje(150);
    
    personaSana = new Personaje(60);
    personaDebil = new Personaje(4);
    
    zombie1.volverZombie();
  }
  
  
  @Test
  public void cuandoUnPersonajeGritaSusPerseguidoresLoEscuchan() {
    class GritableMock implements Gritable {
      boolean gritado = false;
      public void escucharGrito() {
        gritado = true;
      }
    }
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

    assertEquals(10, personaSana.getPosicionX());
  }
  
  @Test
  public void losZombiesSeMuevenALaMitadDeVelocidadQueLasPersonas() {
    zombie1.caminar(Direccion.DERECHA);
    
    assertEquals(5, zombie1.getPosicionX());
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
    
    assertEquals(100, zombie1.getEnergia());
  }
  
  @Test(expected=RuntimeException.class)
  public void lasPersonasSonSordas() throws Exception {
    personaSana.escucharGrito();
  }


  public void assertPuedeMorder(Personaje mordedor, Personaje mordido) {
    try {
      mordedor.morder(mordedor);
    } catch (Exception e) {
      fail();
    }
  }
}
