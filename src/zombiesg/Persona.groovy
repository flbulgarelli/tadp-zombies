package zombiesg

import java.util.Collection
import java.util.LinkedList

class Zombie {
  void escucharGrito() {
    disminuirEnergia(50)
  }

  void mover(cuanto, direccion) {
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
  void mover(cuanto, direccion) {
    x = direccion.mover(x, cuanto)
  }
}

class Persona {

  Posicion posicion = new Posicion(x: 0)
  int energia
  def perseguidores = []

  int getEnergiaParaCaminar() { 5 }

  int getEnergiaParaCorrer() { 60 }

  int getEnergiaParaGritar() { 60 }

  int getEnergiaParaTrotar() { 15 }
  
  def methodMissing(String name, args){
    if(name in ['correr', 'gritar', 'caminar', 'trotar']) 
      realizarAccion(name, args);
    else 
      throw new MissingMethodException(name, this.class, arguments);
  }
  
  def realizarAccion(name, args) {
    int energiaRequerida = this."energiaPara${name.capitalize()}"
    if (!tieneSuficienteEnergia(energiaRequerida)) {
      throw new RuntimeException("No hay suficiente energia para ${name}")
    }
    invokeMethod("_${name}", args)
    disminuirEnergia(energiaRequerida)
  }

  void _caminar(Direccion direccion) {
    mover(10, direccion)
  }

  void _trotar(Direccion direccion) {
    mover(20, direccion)
  }

  void _correr(Direccion direccion) {
    mover(40, direccion)
  }

  void _gritar() {
    println "AHHHHHH"
    perseguidores.each { it.escucharGrito() }
  }

  def perseguirPor(perseguidor) {
    perseguidores << perseguidor
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
  
  protected mover(cuanto, direccion) {
    posicion.mover(cuanto, direccion)
  }
  
  int getPosicionX() {
    posicion.x
  }
}
