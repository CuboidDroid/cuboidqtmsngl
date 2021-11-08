# Cuboid Quantum Singularities

This mod is based on the Quantum Singularities as first seen in CuboidDroid's Support Mod, which was specifically written for my first modpack, Cuboid Outpost.

Since Cuboid Outpost was released, I've had several enquiries about whether the Singularity Resource/Power generators were available separately, so I've decided to re-jig them a bit to make them available in a slightly more "generic" form for consumption in other modpacks. This mod encapsulates the singularities and the Quantum Collapser, as is required by the Cuboid Resource Generators and Cuboid Power Generators mods, which have been implemented separately so that other modpack makers can choose whether or not to include them at a more granular level.

Primarily what this means is that the Quantum Collapsers use a standard tier progression rather than the special ores from Cuboid Outpost, namely:

* Iron Quantum Collapser
* Gold Quantum Collapser
* Diamond Quantum Collapser
* Emerald Quantum Collapser
* Netherite Quantum Collapser

The only real difference between them is the speed at which they collapse input materials into Quantum Singularities and the power they consume. I have also tweaked the textures and colours of the Quantum Collapsers to match their tier.

The main changes to the mod implementation center around configuration and setup.

When running an instance with this mod installed for the first time, a set of default quantum singularities will be generated and placed into the `config/cuboiddroid/cuboidqtmsngl/singularities` folder. By default, these will all be disabled. To enable them in your pack, just change the `enabled` value to `true` in the json file(s) you want to enable. If you want to add additional singularities, the easiest way to achieve that is to copy an existing file, change its name, and tweak the content.

Here is an example of the Iron Quantum Singularity, which requires 640 iron ingots as input:

``` json
{
  "enabled": false,
  "input": "#forge:ingots/iron",
  "outputKey": "iron_qs",
  "timeMultiplier": 1,
  "energyMultiplier": 1,
  "foreground": "#DDDDDD",
  "background": "#888888"
}
```

The values in the JSON file are as follows:

* **enabled** - if `true`, then this singularity will be enabled (available to craft, and shows in JEI if installed). If `false` then not available in-game.
* **input** - a resource key for an item or tag. If using a tag, specify with a prefix of `#`
* **outputKey** - must be unique across all quantum singularities. The recommended naming convention is `{input_material}_qs`. These will be available as items with resource location of `cuboidqtmsngl:{input_material}_qs` - so for the iron example above, the item will have a resource location of `cuboidqtmsngl:iron_qs` and the name for it can be provided in a lang file using the key `item.cuboidqtmsngl.iron_qs`
* **timeMultiplier** - multiplies the base time of the Quantum Collapser in use by this amount to determine the total time for producing this singularity.
* **energyMultiplier** -  multiplies the base energy used of the Quantum Collapser in use by this amount to determine the total energy consumed when producing this singularity.
* **foreground** - the RGB colour to tint the foreground layer of the Quantum Singularity item.
* **background** - the RGB colour to tint the background layer of the Quantum Singularity item.

In addition to the JSON files for the singularities, there is a TOML file for configuration of the Quantum Collapsers in `config/cuboiddroid/cuboidqtmsngl` called `cuboidqtmsngl-common.toml`, which has the following settings:

```
  [collapser.iron]
  ...
  ...
  ...
```
