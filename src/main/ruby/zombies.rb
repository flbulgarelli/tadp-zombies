class Numeric
  def derecha
    self
  end
  def izquierda
    -self
  end
end

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

class Posicion

  def initialize(x)
    @x = x
  end

  def mover(cuanto)
    @x += cuanto
  end
end

class Persona

  def initialize(energia)
    @perseguidores = []
    @posicion = Posicion.new(0)
    @energia = energia
  end

  def energia_para
    {:caminar => 5,
     :correr => 60,
     :gritar => 60,
     :trotar => 15}
  end

  def method_missing(name, *args)
    if [:correr, :gritar, :caminar, :trotar].include?  name
      _realizar_accion(name, args)
    elsif name.to_s.start_with?('energia_para')
      energia_para[name.to_s.delete('energia_para')]
    else
      raise NoMethodError.new(name, self.class)
    end
  end

  def _realizar_accion(name, *args)
    energia_requerida = energia_para[name]
    if !_tiene_suficiente_energia(energia_requerida)
      raise Exception.new("No hay suficiente energi para #{name}")
    end
    send "_#{name}", args
    _disminuir_energia(energia_requerida)
  end

  def _caminar(direccion)
    _mover_hacia(10, direccion)
  end

  def _trotar(direccion)
    _mover_hacia(20, direccion)
  end

  def _correr(direccion)
    _mover_hacia(40, direccion)
  end

  def _gritar
    puts "AHHHHHH"
    @perseguidores.each &:escuchar_grito
  end

  def _tiene_suficiente_energia(energia_requerida)
    @energia >= energia_requerida
  end

  def _disminuir_energia(energia_requerida)
    @energia -= energia_requerida
  end

  def _mover(cuanto)
    @posicion.mover(cuanto)
  end

  def _mover_hacia(cuanto, direccion)
    mover(cuanto.send(direccion))
  end

  def posicion_x
    @posicion.x
  end

  def perseguir_por(perseguidor)
    @perseguidores << perseguidor
  end

  def volver_zombie
    class << self
      include Zombie
    end
  end
end