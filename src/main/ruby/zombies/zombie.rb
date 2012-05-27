module Zombie
  def escuchar_grito
    _disminuir_energia(50)
  end

  def _mover(cuanto)
    @posicion.mover(cuanto / 2)
  end

  def morder(otro)
    otro.volver_zombie
  end
end

