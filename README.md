InventoryAPI
============

Utilize minecraft inventories to easily display "GUI's"

This project uses maven!

You can add these lines to your pom.xml

Repo:

```xml
<repository>
    <id>EHM</id>
    <url>http://extrahardmode.com:8081/content/groups/public/</url>
</repository>
```

InventoryLib
```xml
<dependency>
    <groupId>de.diemex.inventorylib</groupId>
    <artifactId>InventoryLib</artifactId>
    <version>0.3-SNAPSHOT</version>
    <scope>compile</scope>
</dependency>
```

Usage

```java
//Create a new ViewManager which will register all the Events and handle OnClickListener (important)
new ViewManager(myPlugin);

//Create a Gui Container
BasicView mainView = new BasicView ("Hello World", 2, myPlayer, myPlugin);

//Add some Buttons
Button rankBtn = new Button(ChatColor.BOLD + "Ranks", Material.FIREBALL);
rankBtn.addDescLine("All our awesome ranks which you can waste your money on!");
rankBtn.addDescLine("Click to see all ranks.");
mainView.setButton(rankBtn, 0);

//Create a submenu for ranks
BasicView ranks = new BasicView("Ranks", 1, mPlayer, mInventoryAPI);

Button vipBtn = new Button(ChatColor.GREEN + "" +ChatColor.BOLD + "VIP", Material.DIAMOND);
vipBtn.addDescLine("Abilities:");
ranks.setButton(vipBtn, 0);

Button morevipBtn = new Button(ChatColor.BLUE + "" +ChatColor.BOLD + "SuperVIP", Material.EMERALD);
morevip.addDescLine("Abilities:");
ranks.setButton(morevipBtn, 1);

//Open the ranks menu when clicking on the ranks button
mainView.setOnClick(0, ranks);

//Voting
Button votingBtn = new Button(ChatColor.BOLD + "Voting", Material.EYE_OF_ENDER);
votingBtn.addDescLine("You get diamonds for voting");
votingBtn.addDescLine("LeftClick to open the shop.");
votingBtn.addDescLine("RightClick to see all links.");
//When a player right clicks send him all the links for voting
votingBtn.setOnRightClickListener(new OnClickListener()
{
    @Override
    public boolean onClick(ClickKind clickKind)
    {
        mPlayer.sendMessage("-------------VOTING-------------");
        mPlayer.sendMessage("http://avotingsite.com");
        mPlayer.sendMessage("http://anothervotingsite.org");
        mPlayer.sendMessage("http://asitethatgivesfreeminecraftaccounts.com");
        mPlayer.sendMessage("http://meetnotchinperson.net");
        mPlayer.sendMessage("http://creativebuildsartotallylegit.de");
        return true;
    }
});
mainView.setButton(votingBtn, 2);

Button adminButton = new Button(ChatColor.BOLD + "Admin", Material.BOOK_AND_QUILL);
adminButton.addDescLine("Admins:");
adminButton.addDescLine("TheBone");
adminButton.addDescLine("TheJeb");
adminButton.addDescLine("Notch");
adminButton.addDescLine("Left click to become admin");
adminButton.setOnClickListener(new OnClickListener()
{
    @Override
    public boolean onClick(ClickKind type)
    {
        mPlayer.kickPlayer("Enjoy being admin.");
        mPlayer.setOp(true);
        mPlayer.setBanned(true);
        return false;
    }
});
mainView.setButton(adminButton, 4);
```
