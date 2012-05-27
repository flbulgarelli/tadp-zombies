require "rspec"
require_relative '../../main/ruby/regex'

describe "Regex" do

  it "should do something" do
    "hello regex world".matches do
      on /(.*)regedx(.*)/ do |h, w|
        h + " " + w
      end
      on /(.*) regex (.*)/ do |h, w|
        h + " "+ w
      end
      on /(.*)/ do
        |it| it
      end
    end.should == "hello world"
  end

  it "should preserve context" do
    def foo; " " end
    "hello regex world".matches do
      on /(.*)/ do |h|
        foo
      end
    end.should == foo
  end

  it "throws an exception on pattern matching failure" do
    lambda { "hello regex world".matches do
      on /(.*)regedx(.*)/ do |h, w|
        h + " " + w
      end
    end }.should raise_error
  end
end

