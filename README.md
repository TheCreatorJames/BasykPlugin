# BasykPlugin
One of [Minecraft](https://minecraft.net/)'s best features is its multiplayer functionality. Custom Server Software like [Bukkit](http://bukkit.org/) and [Spigot](https://www.spigotmc.org/) make it much easier to add new functionality to multiplayer (Commands, Gameplay, Etc).

This plugin allows me to add new functionality to Minecraft inside of the game, by scripting right into the world. Using this tiny plugin, anyone can add new commands to the world in seconds, without having to search for a pre-existing, pre-compiled plugin.

The language is stack-based, and uses [reverse polish notation](https://en.wikipedia.org/wiki/Reverse_Polish_notation) to execute the code. An example of a program is : 
```
{
    Engine|me;    //Makes the Engine Select You As the Current Player
    20 Player|SetHealth; 
} =Heal; pop; 
```

A more complex program might be like this :
```
{
    Engine|SetPlayer; //Pulls Variable off the Stack and Sets the Player.
    //The variable is pushed onto the stack prior to executing the function.
    
    2 Player|GetY + Player|SetY;  
    $f 10 delay; //execute every .5 seconds (1 tick = 1/20 of a second)
} =f; pop; f;
```


### How to use it in game.
Write code into books. Summon a book with the chat command `#Code` and you can run the book code with `#Run` or punching with the book in hand. 

You can execute functions in a command line by putting a `# Code Here`.

You can also punch with a book in your hand. 


### Todo

Permissions need to be added somehow. Sign features need to be fleshed out. 

Documentation needs to be written for how to write loops, if elses, etc. 