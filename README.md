PaintWithMe
===========

A simple Java application that allows P2P collaboration when painting.

Compile all the files like so:
**`    javac PaintWithMe.java Canvas.java Link.java`** <br>
Then start it with:
**`    java PaintWithMe myPort host otherPort`**



######Example######
You start with:
**`    java PaintWithMe 62000 localhost 62001`**<br>
And then the other client should start it with:
**`    java PaintWithMe 62001 localhost 62000`**

You now have two clients on localhost that can draw together.
