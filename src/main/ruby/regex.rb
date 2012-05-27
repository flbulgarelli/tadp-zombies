class String

  def matches(&block)
    caller = eval 'self', block.binding
    binder = Binder.new(self, caller)
    binder.instance_eval(&block)
    binder.eval()
  end

  class Binder
    attr_accessor :result

    def initialize(value, caller)
      @result = []
      @value = value
      @caller = caller
    end

    def fallback(&action)
      return unless @result.empty?
      action.(@value)
    end

    def on(expr, &action)
      return unless @result.empty?
      match = @value.scan(expr)
      unless match.empty?
        @result = [action.(*match[0])]
      end
    end

    def method_missing(name, *args)
      @caller.__send__(name, *args)
    end

    def eval()
      raise "Pattern matching failure" if @result.empty?
      @result[0]
    end
  end


end