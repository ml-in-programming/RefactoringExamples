# RefactoringExamples

## JHotDraw 5.1

| Member                                              | Move to        |
|-----------------------------------------------------|----------------|
| DrawApplet.readFromStorableInput(String)            | StorableInput  |
| DrawApplication.readFromStorableInput(String)       | StorableInput  |
| DrawApplication.saveAsStorableOutput(String)        | StorableOutput |
| DrawApplication.createColorMenu(String, String)     | CommandMenu    |
| DrawApplication.createArrowMenu()                   | CommandMenu    |
| DrawApplication.createFontMenu()                    | CommandMenu    |
| DrawApplication.createFontStyleMenu()               | CommandMenu    |
| DrawApplication.createFontSizeMenu()                | CommandMenu    |

## ThreeSwords

| Member                                                        | Move to       |
|---------------------------------------------------------------|---------------|
| PlayerData.getItems(Inventory)                                | Inventory     |
| PlayerData.getType(Item)                                      | Item          |
| ItemWeapon.fireProjectile(Point, int, String, boolean, Being) | Being         |
| Entity.collidesWith(Sprite, Sprite, boolean)                  | Sprite        |
| Game.playerData                                               | Player        |
| Game.addText(ConsoleWindow)                                   | ConsoleWindow |
| Game.getPlayerData()                                          | Player        |
| Resistance.checkResistance(Being, String)                     | Being         |

## FTP4J
| Member                             | Move to                 |
|------------------------------------|-------------------------|
| FTPClient.changeDirectoryUp()      | FTPCommunicationChannel |
| FTPClient.createDirectory(String)  | FTPCommunicationChannel |
| FTPClient.fileSize(String)         | FTPCommunicationChannel |
| FTPClient.help()                   | FTPCommunicationChannel |
| FTPClient.deleteFile(String)       | FTPCommunicationChannel |
| FTPClient.sendCustomCommand(String)| FTPCommunicationChannel |
| FTPClient.disconnect(boolean)      | FTPCommunicationChannel |
| FTPClient.changeAccount(String)    | FTPCommunicationChannel |
| FTPClient.deleteDirectory(String)  | FTPCommunicationChannel |
| FTPClient.noop()                   | FTPCommunicationChannel |
| FTPClient.changeDirectory(String)  | FTPCommunicationChannel |
| FTPClient.sendSiteCommand(String)  | FTPCommunicationChannel |
| FTPClient.rename(String,String)    | FTPCommunicationChannel |
| FTPClient.serverStatus()           | FTPCommunicationChannel |
