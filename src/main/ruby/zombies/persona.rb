class Persona
  attr_reader :energia

  def initialize(energia)
    @perseguidores = []
    @posicion = Posicion.new(0)
    @energia = energia
  end

  def energia_para_caminar;  5 end
  def energia_para_correr;  60 end
  def energia_para_gritar;  60 end
  def energia_para_trotar;  15 end

  def method_missing(name, *args)
    name.to_s.matches do
      on /(.*)_(izquierda|derecha)/ do |accion,direccion|
        _realizar_accion(accion, *([direccion] + args))
      end
      on /correr|gritar|caminar|trotar/ do
        _realizar_accion(name, *args)
      end
      fallback do
        raise NoMethodError, "no method #{name.to_s} in #{self.class}"
      end
    end
  end

  def _realizar_accion(name, *args)
    energia_requerida = send "energia_para_#{name}"
    raise "No hay suficiente energi para #{name}" unless
        _tiene_suficiente_energia(energia_requerida)
    send "_#{name}", *args
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
    _mover(cuanto.send(direccion))
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