package creatorjames.Basyk;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import creatorjames.Basyk.StackObjects.StackBoolean;
import creatorjames.Basyk.StackObjects.StackFunction;
import creatorjames.Basyk.StackObjects.StackNumber;
import creatorjames.Basyk.StackObjects.StackObject;
import creatorjames.Basyk.StackObjects.StackString;
import creatorjames.Basyk.StackObjects.Stacker;

public class BasykParser 
{
	private BasykExtension basykExtension;
	private static HashMap<String, Integer> operatorsX;
	
	
	//Statics//
	/***
	 * This method adds space between certain characters. This makes it more user friendly to script in this
	 * language. It's already 'unique' enough. Why hassle them with whitespacing manually? I hate that.
	 * @param expression
	 * @return
	 */
	public static String AddSpace(String expression)
	{
		expression = expression.replace('\t', ' ').replace('\n', ' ');
		String chars = "\\( \\) \\[ \\] \\+ \\- \\* \\=\\= \\/ \\% \\|\\| \\&\\& \\{ \\} \\;";
		
		String[] chars2 = chars.split(" ");
		String res = expression;
		for(String z : chars2)
		{
			//small exception
			
			{
				res = res.replaceAll("(?<=[^\\\\])"+z, " " + z + " ");
			}
		}
		
		res = res.replaceAll("\\\\", "");
		return res.replaceAll("[ ]+", " ");
	}
	
	
	
	/***
	 * This is a test method to be refractored out.
	 */
	private static void MiscellaneousCommands()
	{
		String commands = "echo,Game|Activate";
		
		String[] spl = commands.split(",");
		
		for(String n : spl)
		{
			operatorsX.put(n, -1);
		}
	}
	
	/**
	 * This converts from infix to reverse polish notation. 
	 * This makes the parsing easier, while keeping it more logical for newcomers.
	 * Sure, they do have to think a little stack based, but...
	 * oh well :)
	 * @param expression
	 * @return
	 */
	public static String ConvertToRPN(String expression)
	{
		//I'll just require it to be written as RPN.
		return expression.replaceAll(";", "");
		/*
		//establish operator priority
		if(operatorsX == null)
		{
			operatorsX = new HashMap<String, Integer>();
	

			operatorsX.put("parse", 5);
			
			operatorsX.put("*", 4);
			operatorsX.put("/", 4);
			operatorsX.put("%", 4);
			
			
			operatorsX.put("+", 3);
			operatorsX.put("-", 3);
			
			operatorsX.put("string", 2);
			operatorsX.put("number", 2);
			

			operatorsX.put("<", 1);
			operatorsX.put("<=", 1);
			operatorsX.put(">", 1);
			operatorsX.put(">=", 1);
			operatorsX.put("==", 1);
			operatorsX.put("!=", 1);
			
			
			operatorsX.put("||", 0);
			operatorsX.put("&&", 0);
			
			MiscellaneousCommands();
			
		}
		
		//System.out.println(expression);
		
		//rpn conversion.
		String result = "";
		
		Stack<String> operators = new Stack<String>();
		Queue<String> operands = new LinkedList<String>();

		Queue<String> replacements = new LinkedList<String>();
		//Queue<String> replacements2=  new LinkedList<String>();

		
		
		
		boolean mode = false; 	//false is parenthesis
								//true is braces
		
		
		String let = "("; 
		String letB = ")";
		
		//This parses parenthesis as well as the braces. 
		while(expression.indexOf("(") != -1 || expression.indexOf("{") != -1)
		{
		
			//finds which one is best to parse by.
			if(expression.indexOf("(") < expression.indexOf("{"))
			{
				mode = (expression.indexOf("(") == -1);
			}
			else
			{
				mode = !(expression.indexOf("{") == -1);	
			}
			
			if(mode)
			{
				let = "{";
				letB = "}";
			}
			else
			{
				let = "(";
				letB = ")";
			}
			
			
			int pos = expression.indexOf(let);
			int pos2 = expression.indexOf(letB, pos+1);
			
			int tpos = pos;
			while(expression.indexOf(let, tpos+1) < pos2 && expression.indexOf(let, tpos+1) != -1)
			{
				pos2 = expression.indexOf(letB, pos2+1);
				tpos = expression.indexOf(let, tpos+1);
			}
			
			
			String rp = expression.substring(pos, pos2+1);
			
			//if it is braces, it retains those braces.
			if(!mode)
				replacements.offer(ConvertToRPN(expression.substring(pos+1, pos2)));
			else
				replacements.offer(" { " + ConvertToRPN(expression.substring(pos+1, pos2)) +" } ");
			
			
			expression = expression.substring(0, pos) + "_#R#_" + expression.substring(pos2+1);
		}
		
		

		String[] tokens = expression.split(" ");
		
		int level = -10;
		//parses through all of the tokens.
		for(String tok : tokens)
		{	
			if(operatorsX.containsKey(tok))
			{
				if(level > operatorsX.get(tok))
				{
					while(operands.size() != 0)
					{
						result += operands.poll() + " ";
					}
					
					while(operators.size() != 0)
					{
						result += operators.pop() + " ";
					}


				}
				operators.push(tok);
				level = operatorsX.get(tok);	
			}
			else if (tok.equals("_#R#_"))
			{
				//push something like a variable?
				if(replacements.size() != 0)
				operands.offer(replacements.poll());
				else operands.offer(tok);
			}
			
			else if(tok.equals(";"))
			{
				//force flush
				while(operands.size() != 0)
				{
					result += operands.poll() + " ";
				}
				
				while(operators.size() != 0)
				{
					result += operators.pop() + " ";
				}
			}
			else
			{
				//push variable.
				operands.offer(tok);
			}
		
		}
		
		
		
		//flushes
		while(operands.size() != 0)
		{
			result += operands.poll() + " ";
		}
		
		while(operators.size() != 0)
		{
			result += operators.pop() + " ";
		}
	
		while(replacements.size() != 0)
		{
			result.replace("_#R#_", replacements.poll());
		}

		if(result.contains("null"))
		{
			System.out.println(result + "\n" +  expression);
			StackObject x = new StackNumber();
			StackBoolean z = (StackBoolean)x;
			
		}
		
		return result.replaceAll("[ ]+", " ");
		*/
	}
	//End Statics//
	
	private Stacker stack;
	private HashMap<String, StackObject> variables;
	
	public BasykParser()
	{
		this.stack = new Stacker();
		this.variables = new HashMap<String, StackObject>();
		basykExtension = new BasykExtension();
		Clear();
	}
	
	
	/***
	 * Clears all variables and such from the imaginary stack and imaginary heap.
	 */
	private void Clear()
	{
		stack.Clear();
		variables.clear();
		
		//Yes. I seriously wrote the if else using my own scripting language. Enjoy! :D
		Parse("{ =answerOne del =functionTwo del =functionOne del ( answerOne ) { functionOne } if ( answerOne false == ) { functionTwo } if ~answerOne ~functionOne ~functionTwo } =ifelse del", null);
		Parse("{ =duplicator $duplicator ~duplicator } =dup pop", null);
		Parse("{ { = } string swap +; function; exe; } =lset pop", null);
		Parse("{ { $ } string swap +; function; exe; } =lget pop", null);
	}
	
	/**
	 * Gets the Stack from the Parser.
	 * @return
	 */
	public Stacker GetStack()
	{
		return stack;
	}

	
	private int loopCount;
	private int loops;
	
	
	public HashMap <String, StackObject> GetVariables()
	{
		return variables;
	}
	
	/**
	 * Allows you to parse the expression, opting out of RPN conversion if you've already done so.
	 * @param expression
	 * @param rpn
	 */
	public void Parse(String expression, final Player player)
	{
		expression = AddSpace(expression);
		expression = ConvertToRPN(expression);
	
		//System.out.println(expression);
		
		
		//I'll refractor this a little later ):
		//There is no such thing as self-documenting code. And I promise I will document this soon.
		
		
		String[] tokens = expression.split("\\s+");
		
		int functionMode = 0;
		String functionText = "";
		
		
		for(String tok : tokens)
		{
			
			if(tok.startsWith("" +((char)167)))
			{
				if(tok.length() == 2) continue;
				tok = tok.substring(2);
			}
			
			if(tok.length() == 0)
			{
				continue;
			}
			
			
			
			if(tok.equals("{"))
			{
				if(functionMode != 0)
				{
					functionText += "{ ";
				}
				
				functionMode++;	
			}
			else
			if(tok.equals("}"))
			{
				functionMode--;
				
				if(functionMode == 0)
				{
					stack.Push(new StackFunction(functionText));
					functionText = "";
				}
				else
				{
					functionText += "} ";
				}
				
			}
			else
			if(functionMode != 0)
			{
				functionText += tok + " ";
			}
			else
			if(tok.equals("parse") || tok.equals("exe"))
			{
				if(stack.GetSize() != 0)
				{
					if(stack.Peek() instanceof StackFunction)
					{
						//then do something
						Parse(((StackFunction)stack.Pop()).GetFunction(), player);
						
					}
				}
			}
			else
			if(tok.equals("while"))
			{
				if(stack.GetSize() >= 2)
				{
					if(stack.Peek() instanceof StackFunction)
					{
						if(stack.FromTop(1) instanceof StackFunction)
						{
							StackFunction x = (StackFunction)stack.Pop(); //true/false
							StackFunction y = (StackFunction)stack.Pop(); //method
							Parse(x.GetFunction(), player);
							loops++;
							while(stack.Peek() instanceof StackBoolean && ((StackBoolean)stack.Pop()).GetValue() && loopCount < 256*256)
							{
								loopCount++;
								Parse(y.GetFunction(), player);
								Parse(x.GetFunction(),  player);
							}
							loops--;
							
							if(loops == 0) loopCount = 0;
						
							
						}
					}
				}
			}
			else
			if(tok.equals("delay"))
			{

				if(stack.GetSize() >= 2)
				{
					if(stack.Peek() instanceof StackNumber)
					{
						if(stack.FromTop(1) instanceof StackFunction)
						{
							StackNumber x = (StackNumber)stack.Pop(); //number of ticks till.
							final StackFunction y = (StackFunction)stack.Pop(); //method
							
							BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
							scheduler.scheduleSyncDelayedTask(BasykPlugin.GetInstance(), new Runnable() 
							{
					            public void run() 
					            {
					            	Parse(y.GetFunction(),  player);
					            }
							}, (long)x.GetValue());
						}
					}
				}
			}
			else
			if(tok.equals("repeat") || tok.equals("Repeat"))
			{
				if(stack.GetSize() >= 2)
				{
					if(stack.Peek() instanceof StackNumber)
					{
						if(stack.FromTop(1) instanceof StackFunction)
						{
							StackNumber x = (StackNumber)stack.Pop(); //number of times to repeat
							StackFunction y = (StackFunction)stack.Pop(); //method
							//Parse(x.GetFunction(), true);
							loops++;
							for(int i = 0; i < x.GetValue() && loopCount < 256*256; i++)
							{
								loopCount++;
								Parse(y.GetFunction(), player);
							}
							loops--;
							
							if(loops == 0) loopCount = 0;
						}
					}
				}
			}
			else
			if(tok.equals("if"))
			{
				if(stack.GetSize() >= 2)
				{
					if(stack.Peek() instanceof StackFunction)
					{
						if(stack.FromTop(1) instanceof StackBoolean)
						{
							StackFunction x = (StackFunction)stack.Pop();
							if(((StackBoolean)stack.Pop()).GetValue())
							{
								Parse(x.GetFunction(), player);
							}
						}
					}
					else
					if(stack.Peek() instanceof StackBoolean)
					{
						if(stack.FromTop(1) instanceof StackFunction)
						{
							if(((StackBoolean)stack.Pop()).GetValue())
							{
								Parse(((StackFunction)stack.Pop()).GetFunction(), player);
							} else stack.Pop(); //remove the function anyway
						}
					}
				}
					
			}
			//add most of your code under here.
			else
			if(basykExtension.GameExtension(tok, player, this))
			{
				//do nothing.
			}
			else
			if(tok.equals("clear"))
			{
				Clear();
			}
			else
			if(tok.equals("swap") )
			{ 
				//heh
				if(stack.GetSize() >= 2)
				{
					StackObject uno = stack.Pop();
					StackObject dos = stack.Pop();
					stack.Push(uno);
					stack.Push(dos);
				}
			}
			else
			if(tok.equals("@size"))
			{
				stack.Push(new StackNumber(stack.GetSize()));
			}
			else
			if(tok.equals("pop") || tok.equals("del"))
			{
				if(stack.GetSize() != 0)
				stack.Pop();
			}
			else
			if(tok.equalsIgnoreCase("true"))
			{
				stack.Push(new StackBoolean(true));
			}
			else
			if(tok.equalsIgnoreCase("false"))
			{
				stack.Push(new StackBoolean(false));
			}
			else
			if(tok.startsWith("=") && !tok.equals("=="))
			{
				if(stack.GetSize() != 0)
				{
					variables.put(tok.substring(1), stack.Peek().Clone());
				}
			}
			else
			if(tok.startsWith("$"))
			{
				if(variables.containsKey(tok.substring(1)))
				{
					stack.Push(variables.get(tok.substring(1)).Clone());	
				}
			}
			else
			if(tok.startsWith("~"))
			{
				if(variables.containsKey(tok.substring(1)))
				{
					variables.remove(tok.substring(1));	
				}
			}
			else
			/*if(tok.startsWith("\"") && tok.endsWith("\"")) // I might add this back in, temporarily removing.
			{
				
				tok = tok.replaceAll("(?<=[^\\^])\\^_"," -- ").replaceAll("_", " ").replace(" -- ", "_");
				stack.Push(new StackString(tok.substring(1, tok.length()-1)));
			}
			else*/
			if(tok.matches("^-?\\d+$"))
			{
				stack.Push(new StackNumber(Integer.parseInt(tok)));
			}
			else
			{
				try 
				{
					if(variables.containsKey(tok))	
					{
						stack.Push(variables.get(tok).Clone());
						
						if(stack.Peek() instanceof StackFunction)
						{
							Parse(((StackFunction)stack.Pop()).GetFunction(), player);	
						}
					}
					else
					if(!stack.Peek().Execute(stack, tok))
					{
						//System.out.println("Error on token : " + tok);
						player.sendMessage("Error on token : " + tok);
					}
				} 
				catch(Exception ex)
				{
					//System.out.println("Apparent error on token : " + tok);
					player.sendMessage("Error on token : " + tok);
				}
			}
			
		}
		
	}
	
	
	
	
	
	
	
}
