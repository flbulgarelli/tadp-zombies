package zombiesg

import java.util.Collection
import java.util.LinkedList


enum Estado {
  PERSONA {
    void escucharGrito(personaje) {
      throw new RuntimeException("Las personas no saben escuchar gritos!")
    }

    void mover(posicion, cuanto, direccion) {
      posicion.mover(cuanto, direccion)
    }

    void morder(otro) {
      throw new RuntimeException("Las personas no saben morder!")
    }

  },
  ZOMBIE {
    void escucharGrito(personaje) {
      personaje.disminuirEnergia(50)
    }

    void mover(posicion, cuanto, direccion) {
      posicion.mover(cuanto / 2, direccion)
    }

    void morder(otro) {
      otro.volverZombie()
    }
  }
}

enum Direccion {
  IZQUIERDA {
    int mover(x, cuanto) {
      x - cuanto
    }
  },
  DERECHA {
    int mover(x, cuanto) {
      x + cuanto
    }
  }
}

// contra mis principios, es mutable
class Posicion {
  int x

  Posicion(x) {
    this.x = x
  }

  void mover(cuanto, direccion) {
    x = direccion.mover(x, cuanto)
  }
}

interface Gritable {
  void escucharGrito()
}

class Personaje implements Gritable {

  Posicion posicion = new Posicion(0)
  int energia
  Collection<Gritable> perseguidores = []
  Estado estado = Estado.PERSONA

  Personaje(int energia) {
    this.energia = energia
  }

  int getEnergiaParaCaminar() {
    5
  }

  int getEnergiaParaCorrer() {
    60
  }

  int getEnergiaParaGritar() {
    60
  }

  int getEnergiaParaTrotar() {
    15
  }

  void caminar(Direccion direccion) {
    int energiaRequerida = energiaParaCaminar
    if (!tieneSuficienteEnergia(energiaRequerida)) {
      throw new RuntimeException("No hay suficiente energia para caminar")
    }
    mover(posicion, 10, direccion)
    disminuirEnergia(energiaRequerida)
  }

  void trotar(Direccion direccion) {
    int energiaNecesaria = energiaParaTrotar
    if (!tieneSuficienteEnergia(energiaNecesaria)) {
      throw new RuntimeException("No hay suficiente energia para trotar")
    }
    mover(posicion, 20, direccion)
    disminuirEnergia(energiaNecesaria)
  }

  void correr(Direccion direccion) {
    int energiaRequerida = energiaParaCorrer
    if (!tieneSuficienteEnergia(energiaRequerida)) {
      throw new RuntimeException("No hay suficiente energia para correr")
    }
    mover(posicion, 40, direccion)
    disminuirEnergia(energiaRequerida)
  }

  void gritar() {
    def energiaRequerida = energiaParaTrotar
    if (!tieneSuficienteEnergia(energiaRequerida)) {
      throw new RuntimeException("No hay suficiente energia para gritar")
    }
    println "AHHHHHH"

    perseguidores.each { it.escucharGrito() }

    disminuirEnergia(energiaRequerida)
  }

  void perseguirPor(Gritable perseguidor) {
    perseguidores.add(perseguidor)
  }

  void escucharGrito() {
    estado.escucharGrito(this)
  }

  void volverZombie() {
    estado = Estado.ZOMBIE
  }

  void morder(Personaje otro) {
    estado.morder(otro)
  }

  protected tieneSuficienteEnergia(energiaRequerida) {
    energia >= energiaRequerida
  }

  protected disminuirEnergia(energiaRequerida) {
    energia -= energiaRequerida
  }

  protected mover(posicion, cuanto, direccion) {
    estado.mover(posicion, cuanto, direccion)
  }

  int getPosicionX() {
    posicion.x
  }
}
