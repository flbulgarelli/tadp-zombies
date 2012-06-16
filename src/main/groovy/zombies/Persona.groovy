package zombies
import regex.*

class Persona {
  
  static { 
    Regex.require()
    Direccion.require()
  }
 
  def posicionX = 0
  def energia
  def perseguidores = []

  def energiaParaCaminar = 5
  def energiaParaCorrer  = 60 
  def energiaParaGritar  = 60 
  def energiaParaTrotar  = 15 
  
  def methodMissing(String name, args) {
    name.match {
      when(/(.*)(Izquierda|Derecha)/) {
        accion, sentido -> _realizarAccion(accion, sentido.toLowerCase())
      }
      when(/correr|gritar|caminar|trotar/) {
        _realizarAccion(name, args)
      }
      fallback {
        throw new MissingMethodException(name, this.class, args)
      }
    }
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
  
  def _mover(cuanto) {
    posicionX += cuanto
  }
  
  def _mover(cuanto, direccion) {
    _mover(cuanto."${direccion}"())
  }
  
  def perseguirPor(perseguidor) {
    perseguidores << perseguidor
  }
  
  def volverZombie() {
    this.metaClass {
      mixin(Zombie) 
    }
  }
  
  
}