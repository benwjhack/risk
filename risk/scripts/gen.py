import random, xml, xml.etree
import etree.ElementTree as ee
default = ee.parse('info/tiles/default.xml').getroot()
tiles = ee.parse('info/tiles/tiles.xml').getroot().findall("tile")
rtiles = [None for _ in range(len(tiles))]
default_land = 1
with open("saves/world.txt", "w") as file:
    width = 100
    height = 100
    bigness = height * width
    world = [[default.attrib['code'] for _ in range(height)] for _ in range(width)]
    for i in tiles:
        if "default" in i.attrib and i.attrib["default"] == "yes":
            default_land = i.attrib["code"]
    for i in range(bigness / 10):
        xl = random.randint(0, width)
        yl = random.randint(0, height)
        for x in range(xl, xl + 3):
            for y in range(yl, yl + 3):
                code = default_land
                for i in tiles:
                    if "common" in i.attrib:
                        if random.randint(0, 10 - int(i.attrib["common"])) == 1:
                            code = i.attrib["code"]
                try:
                    world[x][y] = code
                except IndexError:
                    pass
    invworld = [[0 for _ in range(width)] for _ in range(height)]
    for y in range(height):
        for x in range(width):
            file.write(str(world[x][y]))
            if x == width-1:
                file.write("\n\r")
            else:
                file.write(" ")
    print "DONE"
