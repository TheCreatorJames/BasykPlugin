package creatorjames.Basyk.StackObjects;


public class StackString extends StackObject {

	private String str; 
	
	/***
	 * Empty Constructor
	 */
	public StackString()
	{
		this("");
	}
	
	/***
	 * Copy Constructor
	 * @param ss
	 */
	public StackString(StackString ss)
	{
		this(ss.GetString());
	}
	
	
	/***
	 * Constructs a StackString from a String.
	 * @param str
	 */
	public StackString(String str)
	{
		this.str = str;
	}
	
	/***
	 * Sets the String
	 * @param str
	 */
	public void SetString(String str)
	{
		this.str = str;
	}
	
	
	/**
	 * Gets the String
	 * @return
	 */
	public String GetString()
	{
		return str;
	}
	
	
	/****
	 * Adds the strings on the stack together.
	 * @param stack
	 * @return
	 */
	private boolean AddString(Stacker stack)
	{	
		if(stack.FromTop(1) instanceof StackString)
		{
			stack.Pop();
			StackString x = (StackString)stack.Pop();
			
			SetString(x.GetString() + GetString());
			stack.Push(this);
			
			return true;
		}
		
		return false;
	}
	
	
	/****
	 * Checks if the strings are equal.
	 * @param stack
	 * @param invert
	 * @return
	 */
	private boolean EqualsString(Stacker stack, boolean invert)
	{
		if(stack.FromTop(1) instanceof StackString)
		{
			stack.Pop();
			StackString x = (StackString)stack.Pop();
			//finish this.
			if(x.GetString().equals(GetString()))
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
	
	
	/***
	 * Attempts to turn the string into a number.
	 * @param stack
	 * @return
	 */
	private boolean Numberify(Stacker stack)
	{
		stack.Pop();
		
		if(GetString().matches("^-?[\\d\\.]+$"))
		{
			stack.Push(new StackNumber(Double.parseDouble(GetString())));
			
			return true;
		}
		
		return false;
	}
	
	
	@Override
	public boolean Execute(Stacker stack, String command) {
		/*
		switch(command)
		{
		case "==":
			return EqualsString(stack, false);
		case "!=":
			return EqualsString(stack, true);
		case "+":
			return AddString(stack);
		case "number":
			return Numberify(stack);
		case "function":
			stack.Pop();
			stack.Push(new StackFunction(GetString()));
			return true;
		}
		*/
		
		
		if(command.equals("=="))
		{
			return EqualsString(stack, false);
		}
		else
		if(command.equals("!="))
		{
			return EqualsString(stack, true);
		}
		else
		if(command.equals("+"))
		{
			return AddString(stack);	
		}
		else
		if(command.equals("number"))
		{
			return Numberify(stack);
		}
		else
		if(command.equals("function"))
		{
			stack.Pop();
			stack.Push(new StackFunction(GetString()));
			return true;
		}
		
		return false;
	}

	@Override
	public StackObject Clone() {
		return new StackString(this);
	}
	
	
	public String toString()
	{
		return this.GetString() + " [String]";
	}

	@Override
	public String Serialize() {
		// TODO Auto-generated method stub
		return "{" + GetString() + "} string ";
	}

}
