package zombiesj;

import java.util.Collection;
import java.util.LinkedList;

/*
 * 
 * 1. Todos los personajes estan en linea recta. 2. en cada turno, yo muevo un
 * personaje, caminando, corriendo, o trotando. 3. Si donde me detengo hay un
 * zombie, este me muerde (eso lo resuelve el motor) 4. Si por donde pasé habia
 * zombies, estos me siguen. (no importa de que forma) 5. Como muevo un personae
 * por vez, puede ser que los zombies me alancen eventualmente. (esto lo maneja
 * el motor) 6. Cuando un personaje grita, mata a los zombies que lo siguen. 7.
 * cuando un personaje muerde a otro, lo vuelve zombie
 */

enum Estado {
  PERSONA {
    public void escucharGrito(Personaje personaje) {
      throw new RuntimeException("Las personas no saben escuchar gritos!");
    }

    public void mover(Posicion posicion, int cuanto, Direccion direccion) {
      posicion.mover(cuanto, direccion);
    }

    public void morder(Personaje otro) {
      throw new RuntimeException("Las personas no saben morder!");
    }

  },
  ZOMBIE {
    public void escucharGrito(Personaje personaje) {
      personaje.disminuirEnergia(50);
    }

    public void mover(Posicion posicion, int cuanto, Direccion direccion) {
      posicion.mover(cuanto / 2, direccion);
    }

    public void morder(Personaje otro) {
      otro.volverZombie();
    }
  };

  public abstract void escucharGrito(Personaje personaje);

  public abstract void mover(Posicion posicion, int cuanto, Direccion direccion);

  public abstract void morder(Personaje otro);
}

enum Direccion {
  IZQUIERDA {
    public int mover(int x, int cuanto) {
      return x - cuanto;
    }
  },
  DERECHA {
    public int mover(int x, int cuanto) {
      return x + cuanto;
    }
  };
  public abstract int mover(int x, int cuanto);
}

class Posicion {
  private int x;

  public Posicion(int x) {
    this.x = x;
  }

  public void mover(int cuanto, Direccion direccion) {
    x = direccion.mover(x, cuanto);
  }

  public int getX() {
    return x;
  }
}

interface Gritable {

  void escucharGrito();
  
}


public class Personaje implements Gritable {

  private Posicion posicion = new Posicion(0);
  private int energia;
  private Collection<Gritable> perseguidores = new LinkedList<Gritable>();
  private Estado estado = Estado.PERSONA;

  public Personaje(int energia) {
    this.energia = energia;
  }

  public int getEnergiaParaCaminar() {
    return 5;
  }

  public int getEnergiaParaCorrer() {
    return 60;
  }

  public int getEnergiaParaGritar() {
    return 60;
  }

  public int getEnergiaParaTrotar() {
    return 15;
  }

  public void caminar(final Direccion direccion) {
    new Accion(getEnergiaParaCaminar(), "caminar") {
      protected void realizarAccion() {
        mover(10, direccion);
      }
    }.realizar();
  }

  public void trotar(final Direccion direccion) {
    new Accion(getEnergiaParaCaminar(), "trotar") {
      protected void realizarAccion() {
        mover(20, direccion);
      }
    }.realizar();
  }

  public void correr(final Direccion direccion) {
    new Accion(getEnergiaParaCorrer(), "trotar") {
      protected void realizarAccion() {
        mover(40, direccion);
      }
    }.realizar();
  }

  public void gritar() {
    new Accion(getEnergiaParaGritar(), "gritar") {
      protected void realizarAccion() {
        System.out.println("AHHHHHH");

        for (Gritable perseguidor : perseguidores)
          perseguidor.escucharGrito();
      }
    }.realizar();
  }

  public void perseguirPor(Gritable perseguidor) {
    perseguidores.add(perseguidor);
  }

  public void escucharGrito() {
    estado.escucharGrito(this);
  }

  public void volverZombie() {
    estado = Estado.ZOMBIE;
  }

  public void morder(Personaje otro) {
    estado.morder(otro);
  }

  protected boolean tieneSuficienteEnergia(int energiaRequerida) {
    return getEnergia() >= energiaRequerida;
  }

  protected void disminuirEnergia(int energiaRequerida) {
    energia -= energiaRequerida;
  }

  protected void mover(int cuanto, Direccion direccion) {
    estado.mover(posicion, cuanto, direccion);
  }

  public int getPosicionX() {
    return posicion.getX();
  }

  public int getEnergia() {
    return energia;
  }
  
  abstract class Accion {
    
    private final int energiaRequerida;
    private final String nombreAccion;

    public Accion(int energiaRequerida, String nombreAccion) {
      this.energiaRequerida = energiaRequerida;
      this.nombreAccion = nombreAccion;
    }

    public void realizar() {
      if (!tieneSuficienteEnergia(energiaRequerida)) {
        throw new RuntimeException("No hay suficiente energia para " + nombreAccion);
      }
      realizarAccion();
      disminuirEnergia(energiaRequerida);
    }
    protected abstract void realizarAccion();
  }

}
