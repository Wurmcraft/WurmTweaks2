# Items
apple = "<apple>"
diamond = "<diamond>"
god_apple = "<golden_apple@1>"

# Shapeless Recipes
ShapelessRecipe("<glass>", "<golden_apple@1> <diamond>")
ShapelessRecipe(god_apple, " ".join([apple, diamond]))
ShapelessRecipe(apple, diamond)

# Shaped Recipes
ShapedRecipe(diamond, ["XAX", "A A", "XAX"], " ".join(["X", "<glass>", "A", apple]))
ShapedRecipe(god_apple, ["A A", "XAX"], "A <apple> X <glass>")

# Furnace Recipes
FurnaceRecipe(apple, "<stone>", 5)
FurnaceRecipe(diamond, god_apple)

# Brewing Recipes
BrewingRecipe(god_apple, "<potion^{Potion:\"minecraft:water\"}>", "<bow>")

# Ore Dictionary
OreDictionary(apple, "fruitApple")
OreDictionary(diamond, "stone")

# Item Manager