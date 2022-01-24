# Quantum Singularities

This mod is based on the Quantum Singularities as first seen in CuboidDroid's Support Mod, which was specifically written for my first modpack, Cuboid Outpost.

Since Cuboid Outpost was released, I've had several enquiries about whether the Singularity Resource/Power generators were available separately, so I've decided to re-jig them a bit to make them available in a slightly more "generic" form for consumption in other modpacks. 

This mod encapsulates the singularities and the Quantum Collapser, as is required by the Singularity Resource Generators and Singularity Power Generators mods, which are being implemented separately so that other modpack makers can choose whether or not to include them at a more granular level (for example, if you just want to use quantum singularities stand-alone as a crafting ingredient in your pack recipes).

Primarily what this means is that the Quantum Collapsers use a standard (vanilla style) tier progression rather than the special ores from Cuboid Outpost, namely:

* **Iron** Quantum Collapser
* **Gold** Quantum Collapser
* **Diamond** Quantum Collapser
* **Emerald** Quantum Collapser
* **Netherite** Quantum Collapser

The only real difference between them is the speed at which they collapse input materials into Quantum Singularities. I have also tweaked the textures and colours of the Quantum Collapsers to match their vanilla tier.

The main changes to the mod implementation center around configuration and setup.

## Using this mod in your own mod pack

You're welcome to use this mod in your own mod pack if you choose.

When running an instance of Minecraft with this mod installed for the first time, a set of default quantum singularities will be generated and placed into the `config/cuboiddroid/cuboidqs/singularities` folder. By default, these will all be disabled. To enable them in your pack, just change the `enabled` value to `true` in the json file(s) you want to enable. If you want to add additional singularities, the easiest way to achieve that is to copy an existing file, change its name, and tweak the content.

### Default Quantum Singularities

Currently the following default quantum singularity files get generated:

* `coal_qs.json` -  Quantum Singularity.
* `cobblestone_qs.json` - Cobblestone Quantum Singularity.
* `diamond_qs.json` - Diamond Quantum Singularity.
* `dirt_qs.json` - Dirt Quantum Singularity.
* `emerald_qs.json` - Emerald Quantum Singularity.
* `glowstone_qs.json` - Glowstone Quantum Singularity.
* `gold_qs.json` - Gold Quantum Singularity.
* `gravel_qs.json` - Gravel Quantum Singularity.
* `iron_qs.json` - Iron Quantum Singularity.
* `lapis_qs.json` - Lapis Quantum Singularity.
* `redstone_qs.json` - Redstone Quantum Singularity.
* `sand_qs.json` - Sand Quantum Singularity.

### Quantum Singularity JSON file structure

Here is an example of the Iron Quantum Singularity, which requires 640 iron ingots as input and takes 20 seconds to collapse into a singularity (in the Iron-tier collapser):

``` json
{
  "enabled": true,
  "name": "iron_qs",
  "colors": [
    "ececec",
    "b1b0b0"
  ],
  "input": "#forge:ingots/iron",
  "inputCount": 640,
  "workTicks": 600,
  "tier": 1
}
```

The values in the JSON files are as follows:

* **`enabled`** - if `true`, then this singularity will be enabled (available to craft, and shows in JEI if installed). If `false` then hidden/not available in-game. Note that the default is false - you will need to set this to true for it to show up in-game.
* **`name`** - must be unique across all quantum singularities. The recommended naming convention is `{input_material}_qs`. The name for it can be provided in a lang file using the key `item.cuboidqs.{input_material}_qs` - so in the above example, the `lang/en_us.json` file contains a key for `item.cuboidqs.iron_qs` with a value of `Iron Quantum Singularity`
* **`colors`** - this is an array with two hex colours in it. The first is the tint for the foreground layer of the Quantum Singularity item, and the second is the tint for the background layer of the Quantum Singularity item.
* **`input`** - a resource key for an item or tag. If using a tag, specify with a prefix of `#`. An example of a tag could be `#forge:ingots/iron` and an example of using a specific item instead might be `minecraft:iron_ingot`
* **`inputCount`** - The total number of items to be consumed in order to produce the Quantum Singularity for it. For example, 640 would mean 10 full stacks.
* **`workTicks`** - The number of ticks it takes at the first tier of collapser (Iron Quantum Collapser) to produce the singularity once all of the items have been provided. Higher tier collapsers will be shorter than this, based on the mod config settings.
* **`tier`** - the minimum tier of collapser needed to create the singularity. Defaults to 1 (iron), but can also be set to 2 for gold, 3 for diamond, 4 for emerald or 5 for netherite

### Creating additional Quantum Singularities

Additional singularities can be created by adding new .json files to the `config/cuboiddroid/cuboidqs/singularities` folder, and adding an appropriate language key. It doesn't matter what the file is called, but it is recommended that you follow the same convention as making the filename match the "name" field in the JSON - i.e. `{input_material}_qs.json`

### Configuring the Quantum Collapsers

In addition to the JSON files for the singularities, there is a TOML file for configuration of the Quantum Collapsers in `config/cuboiddroid/cuboidqs` called `cuboidqs.toml`. In that file you can tweak the speed of the various tiers of collapser, as well as a few other settings.

The default speed settings for the different tiers of collapser are as follows:

* Iron Quantum Collapser - 1.0x
* Gold Quantum Collapser - 1.2x
* Diamond Quantum Collapser - 1.5x
* Emerald Quantum Collapser - 2.5x
* Netherite Quantum Collapser - 5.0x

## FAQ

### I need help configuring this mod in my pack. Where can I ask questions?

If you need assistance getting things running in your modpack, then reach out to me on [my Discord](https://discord.gg/j9zWKFuBtU) and look for the channel dedicated to this mod, and I'll do what I can to help.

### I've found a bug or have a suggestion for improvement, where can I tell you about these?

If you find bugs, have suggestions or requests for enhancement, please use the [Issue Tracker](https://github.com/CuboidDroid/cuboidqtmsngl/issues) on GitHub for this mod.

### Can I use this mod in my own mod pack?

Yes. Please go ahead!

### Where did you get the idea for this mod?

The Quantum Singularity idea and implementation were heavily inspired by the Singularities and Compressor from [BlakeBr0's Extended Crafting](https://www.curseforge.com/minecraft/mc-mods/extended-crafting) mod, which is aimed at a more end-game scenario, whereas I created these primarily as a way to make the starting game slightly different (when used in conjunction with Singularity Resource Generators) in my Cuboid Outpost mod pack.

### How can I support you in making more mods / mod packs?

If I'm totally honest, I just do this for fun in my very limited spare time, so I'm really not expecting any financial support. A nice "thank you" on [my Discord](https://discord.gg/j9zWKFuBtU) if more than sufficient!

However, I have learnt that sometimes people just want to express their thanks in other ways, so in response to people asking for it, I've gone ahead and made it possible to support me financially anyway. 

On the CurseForge page for this mod you should find a button to [donate using PayPal](https://www.paypal.com/donate/?hosted_button_id=3NREHL7EUD5JG) if that's your preference for a one-off donation / thank you, or if you'd like to really encourage me to keep at it, you can also support me on a monthly basis via [Patreon](https://www.patreon.com/cuboiddroid).

Let me say it again though - I don't expect any financial compensation - a "thank you" is sufficient!

---
###  

I hope you find using this mod (and/or the companion mods for Resource Generators and Power Generators) simple and easy, and wish you all the best in building your next successful modpack!

