class Posicion
  attr_accessor :x
  def initialize(x)
    @x = x
  end

  def mover(cuanto)
    @x += cuanto
  end
end



