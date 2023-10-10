# The Rasterizer
Here will be the rasterizer code.

### Current status of Rasterizer :

* all testing done with a .PPM file.
* Renderer has ```setPixel(int x, int y, Color someColor)``` function
* Renderer has ```drawLine(Pixel p0, Pixel p1, Color color)``` and ```drawTriangle(Pixel p0, Pixel p1, Pixel p2, Color color)``` functions 
* [More functionality will be appended here]

### Development Diary : 

Converting C++ to Java is hell. Java doesn't let you do anything. I miss ``` const vec2& ``` T_T 

#### october 9th 
Note to future self and developers : when loading huge data into a string inside a for loop, please use stringBuilder or stringBuffer. I was copying the same string literally millions of times with megabytes worth of data.
#### october 10th
Finished setting up the basic renderer. Drawing 55 triangles took 7 seconds, seems kind of slow but won't know until we load actual model.
