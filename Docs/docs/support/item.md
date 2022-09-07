# Item Functions

# General Usage

Initialization for a specific item: `Item("{item}")`

- Where `{item}` is replaced with an item

To use a specific function use `Item("{item}").function()`

Example: `Item("<diamond_sword>").count()`

## Remove Recipe (Crafting Table)

Function: `.removeRecipe()`

This will remove all the recipes for the provided item from the crafting registry /
crafting table.

## Stack Size / Count

Function `.stackSize()` or `.count()`

Note: They both do the same thing, just synonyms.

This will return a number with the amount of items in the provided item.

## Stack Size / Count (Changing)

Function `.stackSize({num})` or `.count({num})`

- Where {num} is replaced with a number

This will return a string with the amount of the items changes to represent this change

Example:

`sword = Item("<iron_sword>")` = 1 count of iron sword

`sword = sword.stackSize(3)`

sword = `<3x:iron_sword>` = 3 count of iron sword's

## No Repair

Function `.noRepair()`

Sets the item unable to be repaired in an anvil

# Harvest Level

Function `.harvestLevel("{tool}" {num})`

- Where `{tool}` is replaced with a valid type of harvest level tool (`pickaxe`, `axe`
  , `shovel`)
- Where `{num}` is replaced with a number

This changes the harvest level of item to the provided tool

# Harvest Speed

Function `.harvestSpeed("{block}" {num})`

- Where `{block}` is replaced with a valid block (**Optional**, unfilled it will work for every block)
- Where `{num}` is replaced with a number

This changes the speed at which the provided tool will mine a specific block

Note: This will not change the harvest level of the tool to allow it to actually allow it
to mine it, it just speeds up the mining of the provided block (invalid or not)

