# 3D Grafikus rendszerek - 3. Házi feladat
## Kiinduló projekt
A kiinduló projekt egy Gradle segítségével buildelhető WebGL alapú 3D grafikus alkalmazás, amely GLSL shadereket és Kotlin nyelvű logikát használ a rendereléshez.
Némely matematikai alapművelet és geomtria előre meg van írva. Ezeket kell a feladatnak megfelelően kiegészíteni vagy befejezni. A program fordítása során a Kotlin/JS transzpiláció a Kotlin kódot JavaScript kódra fordítja, amit a böngésző képes futtatni.

## Megvalósított feladatok
Írjon egy 3D autóverseny-játékot. Legyen egy irányítható avatar és egy pálya. Minden elem legyen textúrázott, egyéb árnyalás nem szükséges.

- Pálya: végtelen sík

- Kormány: legyenek az avataron kerekek, amik egyrészt forognak a haladásnak megfelelően, másrészt vannak köztük kormányozhatók, amik a függőleges tengely körül ki tudnak fordulni.

- Heli: a kamera egy helikopter-szerű, fizikailag szimulált (de meg nem jelenített) objektumhoz legyen kötve. A kamerát rugószerű erő és forgatónyomaték tartsa az avatar fölött-mögött, és az avatar felé fordulva. Az erők és forgatónyomatékok lehetnek viszonylag nagyok, kellő lendület- és perdületcsillaptás mellett.

- Árnyék: minden objektum (kivéve a talaj) legyen fekete színben, egy globális (nem függőleges) fényirány mentén a talajra vetítve és kirajzolva.

- Az autó irányítása az nyíl billentyűk segítségével történik.