package zombiesg

import java.util.Collection
import java.util.LinkedList

class Zombie {
  void escucharGrito() {
    disminuirEnergia(50)
  }

  void mover(posicion, cuanto, direccion) {
    posicion.mover(cuanto / 2, direccion)
  }

  void morder(otro) {
    otro.volverZombie()
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

class Persona {

  Posicion posicion = new Posicion(0)
  int energia
  def perseguidores = []

  Persona(int energia) {
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

  def perseguirPor(perseguidor) {
    perseguidores.add(perseguidor)
  }

  def volverZombie() {
    this.metaClass { mixin(Zombie) }
  }
  

  protected tieneSuficienteEnergia(energiaRequerida) {
    energia >= energiaRequerida
  }

  protected disminuirEnergia(energiaRequerida) {
    energia -= energiaRequerida
  }
  
  protected mover(posicion, cuanto, direccion) {
    posicion.mover(cuanto, direccion)
  }

  int getPosicionX() {
    posicion.x
  }
}
