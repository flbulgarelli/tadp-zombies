package zombiesg

import java.util.Collection
import java.util.LinkedList

class Zombie {
  void escucharGrito() {
    _disminuirEnergia(50)
  }

  void _mover(cuanto, direccion) {
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
      _realizarAccion(name, args);
    else 
      throw new MissingMethodException(name, this.class, args);
  }
  
  def _realizarAccion(name, args) {
    int energiaRequerida = this."energiaPara${name.capitalize()}"
    if (!_tieneSuficienteEnergia(energiaRequerida)) {
      throw new RuntimeException("No hay suficiente energia para ${name}")
    }
    invokeMethod("_${name}", args)
    _disminuirEnergia(energiaRequerida)
  }

  void _caminar(Direccion direccion) {
    _mover(10, direccion)
  }

  void _trotar(Direccion direccion) {
    _mover(20, direccion)
  }

  void _correr(Direccion direccion) {
    _mover(40, direccion)
  }

  void _gritar() {
    println "AHHHHHH"
    perseguidores.each { it.escucharGrito() }
  }

  def _tieneSuficienteEnergia(energiaRequerida) {
    energia >= energiaRequerida
  }

  def _disminuirEnergia(energiaRequerida) {
    energia -= energiaRequerida
  }
  
  def _mover(cuanto, direccion) {
    posicion.mover(cuanto, direccion)
  }
  
  int getPosicionX() {
    posicion.x
  }
  
  def perseguirPor(perseguidor) {
    perseguidores << perseguidor
  }
  
  def volverZombie() {
    this.metaClass { mixin(Zombie) }
  }
}
