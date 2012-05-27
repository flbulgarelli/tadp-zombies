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
      _realizar_accion(name, *args)
    elsif name.to_s.start_with?('energia_para')
      energia_para[name.to_s.delete('energia_para')]
    else
      raise NoMethodError, name
    end
  end

  def _realizar_accion(name, *args)
    energia_requerida = energia_para[name]
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