package creatorjames.Basyk.StackObjects;


public class StackNumber extends StackObject 
{

	private double value;
	public StackNumber()
	{
		this(0);
	}
	
	public StackNumber(StackNumber sn)
	{
		this(sn.GetValue());
	}
	
	public StackNumber(double value)
	{
		this.value = value;
	}
	
	
	public double GetValue()
	{
		return value;
	}
	
	public void SetValue(double value)
	{
		this.value = value;
	}

	private void Add(StackNumber n)
	{
		this.value += n.GetValue();
	}
	
	private void Multiply(StackNumber n)
	{
		this.value *= n.GetValue();
	}
	
	private void Divide(StackNumber n)
	{
		this.value /= n.GetValue();
	
	}
	
	private void Subtract(StackNumber n)
	{
		this.value -= n.GetValue();
	}
	
	public void Modulo(StackNumber n)
	{
		this.value %= n.GetValue();
	}
	
	/**
	 * performs the operation
	 * @param stack
	 * @param com
	 * @return
	 */
	private boolean Operation(Stacker stack, String com)
	{

		if(stack.FromTop(1) instanceof StackNumber)
		{
			stack.Pop();
			StackNumber z = (StackNumber)stack.Peek();
			/*
			switch(com)
			{
				case "+":
					z.Add(this);
					break;
				case "-":
					z.Subtract(this);
					break;
				case "*":
					z.Multiply(this);
					break;
				case "/":
					z.Divide(this);
					break;
				case "%":
					z.Modulo(this);
					break;
			}
			*/
			
			if(com.equals("+"))
			{
				z.Add(this);
				
			}
			else
			if(com.equals("-"))
			{
				z.Subtract(this);
			}
			else
			if(com.equals("/"))
			{
				z.Divide(this);
				
			}
			else
			if(com.equals("*"))
			{
				z.Multiply(this);
				
			}
			else
			if(com.equals("%"))
			{
				z.Modulo(this);
			}
			
			return true;
		}
		
		
		return false;
	}
	
	
	/**
	 * rounds the number
	 * @return
	 */
	private boolean Round()
	{
		this.value = Math.round(value);
		return true;
	}
	
	
	/**
	 * floors the number to the lowest value.
	 * @return
	 */
	private boolean Floor()
	{
		this.value = Math.floor(value);
		return true;
	}
	
	
	/**
	 * Turns the number into a string.
	 * @param stack
	 * @return
	 */
	private boolean Stringify(Stacker stack)
	{
		stack.Pop();
		stack.Push(new StackString(""+GetValue()));
		return true;
	}
	
	
	/**
	 * Checks if the numbers are equal
	 * @param stack
	 * @param v
	 * @return
	 */
	private boolean EqualTo(Stacker stack, boolean v)
	{
		
		if(stack.FromTop(1) instanceof StackNumber)
		{
			
			stack.Pop();
			if(((StackNumber)stack.Pop()).GetValue() == GetValue())
			{
				stack.Push(new StackBoolean(true && !v));
			}
			else 
			{
				stack.Push(new StackBoolean(false || v));
			}
		
			return true;
		}
		
		
		return false;
	}
	
	
	/**
	 * Checks if less than (also used for >=)
	 * @param stack
	 * @param invert
	 * @return
	 */
	private boolean LessThan(Stacker stack, boolean invert)
	{
		
		if(stack.FromTop(1) instanceof StackNumber)
		{
			stack.Pop();
			if(GetValue() < ((StackNumber)stack.Pop()).GetValue())
			{
				stack.Push(new StackBoolean(true && !invert));
			}
			else 
			{
				stack.Push(new StackBoolean(false || invert));
			}
		
			return true;
		}
		
		
		return false;
	}
	
	
	/**
	 * Checks if greater than (also used for <=)
	 * @param stack
	 * @param invert
	 * @return
	 */
	private boolean GreaterThan(Stacker stack, boolean invert)
	{
		
		if(stack.FromTop(1) instanceof StackNumber)
		{
			stack.Pop();
			if(GetValue() > ((StackNumber)stack.Pop()).GetValue())
			{
				stack.Push(new StackBoolean(!invert));
			}
			else 
			{
				stack.Push(new StackBoolean(invert));
			}
		
			return true;
		}
		
		
		return false;
	}
	
	
	
	
	@Override
	public boolean Execute(Stacker stack, String command) {
		// TODO Auto-generated method stub
		
		/*
		switch(command)
		{	
		case "<":
			
		case ">":
			
		case ">=":
		
		case "<=":
			
		case "!=":
			
		case "==":
			
		case "+":
		
		case "-":
		
		case "*":
			return Operation(stack, command);
		case "/":
			return Operation(stack, command);
		case "%":
			return Operation(stack, command);
		case "round":
			
		case "floor":
			return Floor();
		case "string":
			return Stringify(stack);
		}
		*/
		
		if(command.equals("neg") || command.equals("negative"))
		{
			this.value *= -1;
			return true;
		}
		else
		if(command.equals("<"))
		{
			return LessThan(stack, false);
		}
		else
		if(command.equals("<="))
		{
			return GreaterThan(stack, true);
		}
		else
		if(command.equals(">"))
		{
			return GreaterThan(stack, false);
		}
		else
		if(command.equals(">="))
		{
			return LessThan(stack, true);
		}
		else
		if(command.equals("=="))
		{
			return EqualTo(stack, false);
		}
		else
		if(command.equals("!="))
		{
			return EqualTo(stack, true);
		}
		else
		if(command.equals("-") || command.equals("+") || command.equals("*") || command.equals("%") || command.equals("/"))
		{	
			return Operation(stack, command);
				
		}
		else 
		if(command.equals("round"))
		{
			return Round();
		}
		else
		if(command.equals("floor"))
		{
			return Floor();
		}
		else
		if(command.equals("string"))
		{
			return Stringify(stack);
		}
		
		return false;
	}
	
	@Override
	public StackObject Clone() {
		// TODO Auto-generated method stub
		return new StackNumber(this);
	}
	
	
	public String toString()
	{
		return "" + this.GetValue() + " [Number]";
	}

	@Override
	public String Serialize() {
		// TODO Auto-generated method stub
		int dValue = (int)(value*10000);
		
		String extra = "";
		
		if(dValue < 0) extra = "neg ";
		return Math.abs(dValue) + " 10000 / " +extra;
	}

}
