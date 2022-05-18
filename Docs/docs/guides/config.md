# Config

Location: `config/wurmtweaks.cfg`

`cacheConversions` is a `true` or `false` config option that caches the item names for lookups when used in scripts to improve performance greatly, using a small amount of extra memory

`debug` is a `true` or `false` config option that enables wurmtweaks debug mode, this is primary used for development as it adds some extra information to logs about its internal workings.

`initialScript` is a variable string designed to signify what wurmtweaks will load on startup, such as running a script or multiple scripts, depending on this option. To run a single script in the `Wurmtweaks_2` folder you simply add the script name to this line, to run multiple create a file ending with .ws with a list of all the files you wish to run within. To download scripts from the internet on startup simple do the same thing as before just use the download link, ending with an .py (for single scripts) or .ws for multiple scripts or a list of other scripts to download.