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

// contra mis principios, es mutable
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

  public void caminar(Direccion direccion) {
    int energiaRequerida = getEnergiaParaCaminar();
    if (!tieneSuficienteEnergia(energiaRequerida)) {
      throw new RuntimeException("No hay suficiente energia para caminar");
    }
    mover(10, direccion);
    disminuirEnergia(energiaRequerida);
  }

  public void trotar(Direccion direccion) {
    int energiaNecesaria = getEnergiaParaTrotar();
    if (!tieneSuficienteEnergia(energiaNecesaria)) {
      throw new RuntimeException("No hay suficiente energia para trotar");
    }
    mover(20, direccion);
    disminuirEnergia(energiaNecesaria);
  }

  public void correr(Direccion direccion) {
    int energiaRequerida = getEnergiaParaCorrer();
    if (!tieneSuficienteEnergia(energiaRequerida)) {
      throw new RuntimeException("No hay suficiente energia para correr");
    }
    mover(40, direccion);
    disminuirEnergia(energiaRequerida);
  }

  public void gritar() {
    if (!tieneSuficienteEnergia(getEnergiaParaGritar())) {
      throw new RuntimeException("No hay suficiente energia para gritar");
    }
    System.out.println("AHHHHHH");

    for (Gritable perseguidor : perseguidores)
      perseguidor.escucharGrito();

    disminuirEnergia(getEnergiaParaGritar());
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

}
