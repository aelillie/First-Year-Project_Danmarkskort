Group G: Danmarkskort
============
###How to open the program:
1. Open the program by doubleclicking the Danmarkskort.jar file
2. If this doesn't work, you can try to run it through cmdprompt:
   - Open cmdprompt
   - Navigate to the folder containing the jarfile and write:
   
      C:\Users\Johndoe\DesktopMystuff\Danmarkskort> **java -jar *filename*.jar**

3. The program uses ~1.7gb to run. This amount of space must be available in order to run it. If it fails to run, you can try type the following in cmdpromt, in an attempt to allocate the needed memory:
      C:\Users\Johndoe\DesktopMystuff\Danmarkskort> **java -jar -Xmx2g *filename*.jar**


###How to use the program:
1. Once the loading screen is done, the program should initially display a map of Denmark

2. Explore the map by dragging the mouse around, or use the arrowkeys

3. To zoom in/out you can use mousescroll or the '+' / '-' keys

4. In the right hand side of the programscreen you can see some function buttons. They are as follows from top to buttom:
   - Option button (Here you can load files, save files as .bin, and toggle/untoggle icons on the map)
   - Maptype button (Here you can chose between three different maptypes; Standard map, colorblind map and transport map)
   - '+' (zoom in)
   - '-' (zoom out)
   - - Fullscreen button 

5. In the top left side of the screen you can see a search bar, which you can use to search for addresses. The program will suggest addresses as you type. You can use the arrow keys or the mouse to navigate between the search suggestions, and subsequently hit enter to do a search. This will mark and pan to the selected location

6. Beneath the searchbar is the Route Plan button. Click it to open the route plan

7. Inside the Route Plan you can start by selecting your method of transportation; by car, bicycle or foot (default by car)

8. Then you can type your start address in the search field marked with a green 'A' and the destination address in the field marked by a red 'B'. These two search fields have the same features as the address search described in section '6' above.

9. Furthermore you can select wether you want to travel by the fastest route, or by the shortest route (default is fastest)

10. Once two addresses has been selected you can click find route in order to find the route

11. If fastest path is chosen it will draw a dark-blue path between point A and B (if shortest, pink)  and open a direction info panel, that shows info about the route

12. In the direction panel you can see distance to destination, time to destination, as well as directions for how to get from A to B. In some cases (typically in small cities) it will incorrectly say that the shortest path is faster than the fastest path. This is a bug!
