package zombiesg

import java.util.Collection
import java.util.LinkedList

//TODO me faltan reqs para un eigen method y un mixin estatico, y/o multimetodos. 
//instance eval podremos verlo en la otra clase 
class Zombie {
  def escucharGrito() {
    _disminuirEnergia(50)
  }

  def _mover(cuanto, direccion) {
    posicion.mover(cuanto / 2, direccion)
  }

  def morder(otro) {
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
  def x
  def mover(cuanto, direccion) {
    x = direccion.mover(x, cuanto)
  }
}

class Persona {

  Posicion posicion = new Posicion(x: 0)
  def energia
  def perseguidores = []

  def getEnergiaParaCaminar() { 5 }

  def getEnergiaParaCorrer() { 60 }

  def getEnergiaParaGritar() { 60 }

  def getEnergiaParaTrotar() { 15 }
  
  def methodMissing(String name, args){
    if(name in ['correr', 'gritar', 'caminar', 'trotar']) 
      _realizarAccion(name, args);
    else 
      throw new MissingMethodException(name, this.class, args);
  }
  
  def _realizarAccion(name, args) {
    def energiaRequerida = this."energiaPara${name.capitalize()}"
    if (!_tieneSuficienteEnergia(energiaRequerida)) {
      throw new RuntimeException("No hay suficiente energia para ${name}")
    }
    invokeMethod("_${name}", args)
    _disminuirEnergia(energiaRequerida)
  }

  def _caminar(direccion) {
    _mover(10, direccion)
  }

  def _trotar(direccion) {
    _mover(20, direccion)
  }

  def _correr(direccion) {
    _mover(40, direccion)
  }

  def _gritar() {
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
  
  def getPosicionX() {
    posicion.x
  }
  
  def perseguirPor(perseguidor) {
    perseguidores << perseguidor
  }
  
  def volverZombie() {
    this.metaClass { mixin(Zombie) }
  }
}
