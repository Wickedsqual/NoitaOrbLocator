# NoitaOrbLocator
This Tool is a little Helper to fetch the Player and GreaterChest Location for the Game Noita.

The purpose of this Tool is to help People who don't want to use Mods to have an easier Time to find the correct location for the GreateerOrbChest

Details:

All infos are read from the save00 folder. (C:\Users\[USER]\AppData\LocalLow\Nolla_Games_Noita)

1. It reads the Current World Seed from the latest "_stats" file.
  Then it calculates the GreaterChestLocation with the external Tool findorb11.exe (https://github.com/kaliuresis/findorb11/)

2. It reads the CurrentPlayerLocation from the "player.xml" file

3. It calculates and shows the difference between the 2 values.


A filewatcher checks the player.xml fore changes, so if Noita is restarted, is automatically updated the player location in this application
