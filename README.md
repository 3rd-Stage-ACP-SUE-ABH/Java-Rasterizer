# The Rasterizer
### Current status of Rasterizer :

* all testing done with a .PPM file.
* Renderer has ```setPixel(int x, int y, Color someColor)``` function
* [More functionality will be appended here]

### Development Diary : 

Here will be the rasterizer code.

Converting C++ to Java is hell. Java doesn't let you do anything. I miss ``` const vec2& ``` T_T 

#### october 9th 
Note to future self and developers : when loading huge data into a string inside a for loop, please use stringBuilder or stringBuffer. I was copying the same string literally millions of times with megabytes worth of data.
