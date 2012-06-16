package zombies

class Zombie {
  def escucharGrito() {
    _disminuirEnergia(50)
  }

  def _mover(cuanto) {
    setPosicionX(posicionX + cuanto / 2)
  }

  def morder(otro) {
    otro.volverZombie()
  }
}