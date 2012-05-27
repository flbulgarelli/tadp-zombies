require "rspec"
require_relative '../../main/ruby/zombies'

class GritableMock
  attr_accessor :gritado
  def initialize
    @gritado = false
  end
  def escuchar_grito
    @gritado = true
  end
end

describe "zombies" do

  before(:each) do
    @zombie1 = Persona.new(150)
    @persona_sana = Persona.new(60)
    @persona_debil = Persona.new(4)
    @zombie1.volver_zombie
    end

  describe "direccion" do
    it "tiene metodos izquierda y derecha" do
      1.izquierda.should == - 1.derecha
    end
  end

  describe "personaje" do
    it "es escuchado por sus perseguidores cuando grita" do
      perseguidor1 = GritableMock.new
      perseguidor2 = GritableMock.new
      @persona_sana.instance_eval do
        perseguir_por(perseguidor1)
        perseguir_por(perseguidor2)
      end
      @persona_sana.gritar

      perseguidor1.gritado.should == true
      perseguidor2.gritado.should == true
    end

    it "se puede mover en ambas direcciones" do
      #Marcar esto
      @persona_sana.instance_eval do
        caminar_derecha
        caminar_derecha
        caminar_izquierda
      end
      @persona_sana.posicion_x.should == 10
    end
  end

  describe "persona" do
    it "no puede morder" do
      lambda { @persona_sana.morder(@persona_debil) }.should raise_error
    end

    it "es sorda" do
      lambda { @persona_sana.escuchar_grito }.should raise_error
    end

    it "no puede correr sin suficiente energia" do
      lambda { @persona_debil.correr_derecha }.should raise_error
    end

    it "no puede gritar sin suficiente energia" do
      lambda { @persona_debil.gritar }.should raise_error
    end

    it "no puede trotar sin suficiente energia" do
      lambda { @persona_debil.trotar }.should raise_error
    end


  end

  describe "zombie" do
    it "vuelve zombie a su victima cuando la muerde" do
      def assert_puede_morder(mordedor, mordido)
        mordedor.morder(mordido)
      end

      @zombie1.morder(@persona_sana)
      assert_puede_morder(@persona_sana, @persona_debil)
    end

    it "se mueve a la mitad de velocidad de una persona" do
      @zombie1.caminar_derecha
      @zombie1.posicion_x.should == 5
    end

    it "pierde energia si se le grita" do
      @zombie1.escuchar_grito
      @zombie1.energia.should == 100
    end

  end

end










