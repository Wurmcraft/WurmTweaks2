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
table = Item("<crafting_table")
table.removeRecipe() # Removes Crafting Recipes
stackSize = table.stackSize()
table.stackSize(37)
count = table.count()
stackedTables = table.count(count + 5) # <6x:crafting_table>
# Specialty
sword = Item("<diamond_sword")
sword.noRepair()
sword.harvestLevel("pickaxe", 3) # Sword can now mine diamonds
#sword.harvestSpeed(8)
#sword.harvestSpeed("<stone>", 50)
