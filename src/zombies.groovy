import java.awt.Point;

Integer.metaClass.getDerecha = { delegate }
Integer.metaClass.getIzquiera = { -delegate }

class Posicion {
  def x;
  def mover(cuanto){
    x += cuanto
  }
}

class Persona implements GroovyInterceptable {
  def energia

  def invokeMethod(String name, args){
    if(name in ['correr', 'gritar'])
      disminuirEnergia()
    this.class.metaClass.getMetaMethod(name).invoke(args)
  }
  
  def disminuirEnergia() {
    assert suficienteEnergia, 'No hay suficiente estamina'
    energia--
  }

  def getSuficienteEnergia() {
    energia > 0
  }

  def volverZombie() {
    metaClass.mixin(Zombie)
  }
  
  def caminar() {
    mover(10)
  }
  
  def correr() {
    mover(20)
  }
}
Persona.mixin(Posicion)

class Zombie {
  def morder(otro){
    otro.volverZombie()
  }
  def correr() {
    super.correr()
  }
}

arnaldo = new Persona(stamina: 50)
andrea = new Persona(stamina: 60)
analia = new Persona(stamina: 20)

arnaldo.volverZombie()
andrea.morder(analia)

arnaldo.morder(analia)
analia.morder(arnaldo)

arnaldo.mover(10.derecha)








